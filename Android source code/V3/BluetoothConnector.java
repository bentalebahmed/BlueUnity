// aar file to import into unity after build
// C:\Users\ user\AndroidStudioProjects\ unity3dbluetoothplugin\build\outputs\aar

package com.example.unity3dbluetoothplugin;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.widget.Toast;


import com.unity3d.player.UnityPlayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnector {

    private static BluetoothConnector mInstance = null;

    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static BluetoothManager mBluetoothManager = null;
    private static BluetoothAdapter mBluetoothAdapter = null;
    private static BluetoothLeScanner mBluetoothLeScanner = null;
    private static DeviceListAdapter mDeviceListAdapter;
    private static boolean scanning = false;
    private static final Handler handler = new Handler();
    private static final long SCAN_PERIOD = 10000;
    private static BluetoothSocket skt;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static BufferedReader reader;

    @SuppressLint("MissingPermission")
    public static BluetoothConnector getInstance() {
        if (mInstance == null)
            mInstance = new BluetoothConnector();
        return mInstance;
    }

    @SuppressLint("MissingPermission")
    public BluetoothConnector() {
//        checkPermissions(UnityPlayer.currentActivity.getApplicationContext(), UnityPlayer.currentActivity);
        mBluetoothManager = UnityPlayer.currentActivity.getApplication().getApplicationContext().getSystemService(BluetoothManager.class);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            UnityPlayer.currentActivity.startActivityForResult(enableBtIntent, 1);
        }

        if (!mBluetoothAdapter.isEnabled())
            mBluetoothAdapter.enable();

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        mDeviceListAdapter = new DeviceListAdapter();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (mDeviceListAdapter.AddDevice(device)){
                        String deviceName = device.getName()==null? "null": device.getName();
                        String deviceAddress = device.getAddress();
                        UnityPlayer.UnitySendMessage("BluetoothManager", "NewDeviceFound", deviceAddress + "+" + deviceName);
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    UnityPlayer.UnitySendMessage("BluetoothManager", "ScanStatus", "started");
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    UnityPlayer.UnitySendMessage("BluetoothManager", "ScanStatus", "stopped");
                }
            }
        };
        UnityPlayer.currentActivity.getApplication().getApplicationContext().registerReceiver(receiver, filter);
    }

    @SuppressLint("MissingPermission")
    private static void StartScanDevices() {
        // Check if discovery is already in progress, and cancel if so
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mDeviceListAdapter.clearAll();
        mBluetoothAdapter.startDiscovery();
    }

    @SuppressLint("MissingPermission")
    private static void StopScanDevices() {
        // Check if discovery is already in progress, and cancel if so
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            UnityPlayer.UnitySendMessage("BluetoothManager", "ScanStatus", "stopped");
        }
    }

    @SuppressLint("MissingPermission")
    public static String[] GetPairedDevices() {
        try {
            ArrayList<String> paired_devices = new ArrayList<>();

            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
            for(BluetoothDevice device:devices)
            {
                paired_devices.add(device.getAddress()+"+"+device.getName());
            }
            return paired_devices.toArray(new String[0]);
        }
        catch (Exception ex){
            UnityPlayer.UnitySendMessage("BluetoothManager", "ReadLog",  ex.toString());
            return null;
        }
    }

    @SuppressLint("MissingPermission")
    public static void StartConnection(String DeviceAdd){
        UnityPlayer.UnitySendMessage("BluetoothManager", "ConnectionStatus", "connecting");
        try{
            if(!BluetoothAdapter.checkBluetoothAddress(DeviceAdd))
                UnityPlayer.UnitySendMessage("BluetoothManager", "ConnectionStatus", "Device not found");

            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(DeviceAdd);
//            skt = device.createInsecureRfcommSocketToServiceRecord(mUUID);
            skt = device.createRfcommSocketToServiceRecord(mUUID);
            mBluetoothAdapter.cancelDiscovery();
            skt.connect();
            inputStream = skt.getInputStream();
            outputStream = skt.getOutputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            ReadDatathread = new Thread(ReadData);
            ReadDatathread.start();

            UnityPlayer.UnitySendMessage("BluetoothManager", "ConnectionStatus", "connected");
        }
        catch (Exception ex)
        {
            UnityPlayer.UnitySendMessage("BluetoothManager", "ConnectionStatus", "unable to connect");
            UnityPlayer.UnitySendMessage("BluetoothManager", "ReadLog", "StartConnection Error: "+ex);
        }
    }
    public static void StopConnection(){
        try {
            ReadDatathread.interrupt();
            if(inputStream != null) inputStream.close();
            if(outputStream != null) outputStream.close();
            if(skt != null) skt.close();
        } catch (IOException e) {
            UnityPlayer.UnitySendMessage("BluetoothManager", "ReadLog", "StopConnection Error: "+e);
        }
    }

    private static final Runnable ReadData = new Runnable() {
        public void run() {
            while (skt.isConnected() && !ReadDatathread.isInterrupted()) {
                try {
                    if (inputStream.available() > 0) {

                        UnityPlayer.UnitySendMessage("BluetoothManager", "ReadData", reader.readLine());
                    }
                } catch (IOException e) {
                    UnityPlayer.UnitySendMessage("BluetoothManager", "ReadLog", "inputStream Error: " + e);
                }
            }
            UnityPlayer.UnitySendMessage("BluetoothManager", "ConnectionStatus", "disconnected");
        }
    };

    private static Thread ReadDatathread = null;

    public static void WriteData(String data) {
        try {
            if (skt.isConnected()) {
                outputStream.write(data.getBytes());
            }
        } catch (IOException e) {
            UnityPlayer.UnitySendMessage("BluetoothManager", "ReadLog", "WriteData Error: "+e);
        }
    }

    public static void Toast(String info){
        Toast.makeText(UnityPlayer.currentActivity.getApplicationContext(), info,Toast.LENGTH_SHORT).show();
    }

}




