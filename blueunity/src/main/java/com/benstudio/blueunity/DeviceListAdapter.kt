package com.benstudio.blueunity

import android.bluetooth.BluetoothDevice

class DeviceListAdapter {
    private val devicesMap: MutableMap<String, BluetoothDevice> = mutableMapOf()

    fun addDevice(device: BluetoothDevice): Boolean {
        if (devicesMap[device.address] != null) {
            return false
        }
        devicesMap[device.address] = device
        return true
    }

    fun getCount(): Int {
        return devicesMap.size
    }

    fun getItem(address: String): BluetoothDevice? {
        return devicesMap[address]
    }

    fun clearAll() {
        devicesMap.clear()
    }
}