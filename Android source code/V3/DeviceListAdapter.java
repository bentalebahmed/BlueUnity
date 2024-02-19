package com.example.unity3dbluetoothplugin;

import android.bluetooth.BluetoothDevice;

import java.util.HashMap;
import java.util.Map;

public class DeviceListAdapter {
    public Map<String, BluetoothDevice> mDevicesMap = new HashMap<String, BluetoothDevice>();

    public boolean AddDevice(BluetoothDevice device) {
        if (this.mDevicesMap.get(device.getAddress()) != null) {
            return false;
        }

        this.mDevicesMap.put(device.getAddress(), device);
        return true;
    }

    public int getCount() {
        return this.mDevicesMap.size();
    }

    public BluetoothDevice getItem(String i) {
        return this.mDevicesMap.get(i);
    }

    public void clearAll(){
        this.mDevicesMap.clear();
    }
}
