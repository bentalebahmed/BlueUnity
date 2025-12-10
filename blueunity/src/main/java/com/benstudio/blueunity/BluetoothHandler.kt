package com.benstudio.blueunity

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.google.gson.Gson
import com.unity3d.player.UnityPlayer
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID



@SuppressLint("MissingPermission")
class BluetoothHandler private constructor() {

    private var mBluetoothManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mDeviceListAdapter: DeviceListAdapter? = null
    private var callback: ICallBack? = null;
    private var serverThread: ServerThread? = null
    private var dataTransferThread: DataTransferThread? = null
    private val mUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var pairing: Boolean = false;

    companion object {
        private var mInstance: BluetoothHandler? = null

        @JvmStatic
        fun getInstance(): BluetoothHandler {
            return mInstance ?: BluetoothHandler().also { mInstance = it }
        }
    }
    
    private fun getUnityActivity(): Activity? {
        return UnityPlayer.currentActivity
    }

    init {
        val activity = getUnityActivity()
        val context: Context? = activity?.applicationContext
        mBluetoothManager = context?.getSystemService(BluetoothManager::class.java)
        mBluetoothAdapter = mBluetoothManager?.adapter

        if (mBluetoothAdapter?.isEnabled == false) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity?.startActivity(enableIntent)
        }

        mDeviceListAdapter = DeviceListAdapter()

        SetPairing(true)
    }

    fun isEnabled(): Boolean {
        return mBluetoothAdapter?.isEnabled ?: false
    }

    fun registerCallbackEvents(cb: ICallBack){

        callback = cb

        val activity = getUnityActivity() ?: run {
            callback?.onError("No Unity Activity, could not register scanForDevicesReceiver")
            return
        }

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        activity.registerReceiver(scanForDevicesReceiver, filter)
    }

    private val scanForDevicesReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String? = intent?.action
            when (action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    callback?.onDiscoveryStarted()
                }

                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                            intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE,
                                BluetoothDevice::class.java
                            )
                        }

                        else -> {
                            @Suppress("DEPRECATION")
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }
                    }

                    device?.let {
                        if (mDeviceListAdapter?.addDevice(it) == true) {
                            callback?.onDiscoveryDeviceFound(it.name ?: "Unknown", it.address)
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    callback?.onDiscoveryFinished()
                }
            }
        }
    }

    fun setDeviceName(name: String){
        if (name.isEmpty()) return
        mBluetoothAdapter?.setName(name)
    }

    fun scanForDevices() {
        stopScanForDevices()
        mDeviceListAdapter?.clearAll()
        mBluetoothAdapter?.startDiscovery()
    }

    fun SetPairing(isOn: Boolean){
        pairing = isOn
    }

    fun stopScanForDevices() {
        mBluetoothAdapter?.cancelDiscovery()
        callback?.onDiscoveryFinished()
    }

    fun getPairedDevices(): String {
        val pairedDevices = mBluetoothAdapter?.bondedDevices?.map { device ->
            BluetoothDeviceInfo(
                name = device.name.orEmpty().ifEmpty { "Unknown" },
                address = device.address
            )

        } ?: emptyList()

        val listWrapper = BluetoothDeviceList(pairedDevices)
        return Gson().toJson(listWrapper)
    }

    fun startDiscoverable(duration: Int) {
        val activity = getUnityActivity() ?: return
        val discoverableIntent: Intent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        activity.startActivity(discoverableIntent)
    }

    fun startServer() {
        closeServer()
        serverThread = ServerThread(pairing)
        serverThread?.start()
    }

    fun closeServer() {
        serverThread?.cancel()
    }

    fun connectAsClient(address: String) {
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            callback?.onConnectDeviceNotFound(address)
            return
        }

        val device: BluetoothDevice? = mBluetoothAdapter?.getRemoteDevice(address)

        if (device == null) {
            callback?.onConnectDeviceNotFound(address)
            return
        }

        val connThread = ConnectThread(device, pairing)
        connThread.start()
    }

    private inner class ServerThread(withPairing: Boolean) : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            if (withPairing) mBluetoothAdapter?.listenUsingRfcommWithServiceRecord("BlueUnity", mUUID)
            else mBluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord("BlueUnity", mUUID)
        }

        override fun run() {
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    shouldLoop = false
                    callback?.onError(e.message.toString())
                    null
                }
                socket?.also {
                    dataTransferThread?.cancel()
                    dataTransferThread = DataTransferThread(it)
                    dataTransferThread?.start()
                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }
        }

        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                callback?.onError(e.message.toString())
            }
            callback?.onDisconnected("");
        }
    }

    private inner class ConnectThread(device: BluetoothDevice, withPairing: Boolean) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            if(withPairing) device.createRfcommSocketToServiceRecord(mUUID)
            else device.createInsecureRfcommSocketToServiceRecord(mUUID)
        }

        override fun run() {
            mmSocket?.remoteDevice?.address?.let {
                callback?.onConnecting(it)
            }

            try {
                mBluetoothAdapter?.cancelDiscovery()

                mmSocket?.let { socket ->
                    socket.connect()
                    dataTransferThread?.cancel()
                    dataTransferThread = DataTransferThread(socket)
                    dataTransferThread?.start()
                }
            } catch (e: IOException) {

                callback?.onError(e.message.toString())
                dataTransferThread?.cancel()
                dataTransferThread = null
                cancel()
                mmSocket?.remoteDevice?.address?.let {
                    callback?.onDisconnected(it)
                }
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                callback?.onError(e.message.toString())
            }
            mmSocket?.remoteDevice?.address?.let {
                callback?.onDisconnected(it)
            }
        }
    }

    private inner class DataTransferThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024)

        override fun run() {
            var numBytes: Int // bytes returned from read()

            callback?.onConnected(mmSocket.remoteDevice.address)
            while (true) {
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    mmInStream.close()
                    mmOutStream.close()
                    mmSocket.close()
                    callback?.onError(e.message.toString())
                    callback?.onDisconnected(mmSocket.remoteDevice.address)
                    break
                }
                callback?.onDataReceived(mmBuffer.copyOf(numBytes))
            }
        }

        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                callback?.onDataNotSent(bytes)
                callback?.onError(e.message.toString())

            }
        }

        fun cancel() {
            try {
                mmInStream.close()
                mmOutStream.close()
                mmSocket.close()
            } catch (e: IOException) {
                callback?.onError(e.message.toString())
            }
            callback?.onDisconnected(mmSocket.remoteDevice.address)
        }
    }

    fun write(data: ByteArray) {
        dataTransferThread?.write(data)
    }

    fun disconnect() {
        serverThread?.cancel()
        dataTransferThread?.cancel()

    }

    fun cleanup() {
        callback = null
        dataTransferThread = null
        serverThread = null
        mDeviceListAdapter?.clearAll()

        val activity = getUnityActivity() ?: return
        activity.unregisterReceiver(scanForDevicesReceiver)
    }

    data class BluetoothDeviceInfo(
        val name: String, val address: String
    )

    data class BluetoothDeviceList(
        val devices: List<BluetoothDeviceInfo>
    )
}

