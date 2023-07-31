
package com.example.unity3dbluetoothplugin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
//import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnector {


    private static final BluetoothConnector ourInstance = new BluetoothConnector();
    public static BluetoothConnector getInstance() {
        return ourInstance;
    }
    public final String TAG = "MAIN";
    public ArrayList<BluetoothDevice> devices_true = new ArrayList<>();
    public ArrayList<String>device_names = new ArrayList<>();
    public final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothAdapter adapter;
    public BluetoothSocket skt;
    public BluetoothDevice cur_device;
    public BluetoothDevice con_device;
    public Set<BluetoothDevice> devices;
    public InputStream inputStream;
    public OutputStream outputStream;


    private BluetoothConnector() {}

    public String[] GetBluetoothDevices(){
        try{
            devices_true.clear();
            device_names.clear();
            adapter = BluetoothAdapter.getDefaultAdapter();
            devices = adapter.getBondedDevices();
            for(BluetoothDevice device:devices)
            {
                devices_true.add(device);
                device_names.add(device.getName());
            }
            return device_names.toArray(new String[0]);
        }
        catch (Exception ex){
            return null;
        }
    }

    public String StartBluetoothConnection(String DeviceName){
        try{
            devices_true.clear();
            device_names.clear();
            adapter = BluetoothAdapter.getDefaultAdapter();
            devices = adapter.getBondedDevices();
            for(BluetoothDevice device:devices)
            {
                devices_true.add(device);
                device_names.add(device.getName());
            }

            cur_device = devices_true.get(device_names.indexOf(DeviceName));

            if(cur_device == null)
            {
                return "Device not found";
            }

            con_device = adapter.getRemoteDevice(cur_device.getAddress());

            skt = con_device.createInsecureRfcommSocketToServiceRecord(mUUID);
            adapter.cancelDiscovery();
            skt.connect();

            inputStream = skt.getInputStream();
            outputStream = skt.getOutputStream();

            return "Connected";
        }
        catch (Exception ex)
        {
            return "Error";
        }
    }
    public String ReadData(){

        try {
            if(inputStream.available() > 0){

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                return reader.readLine();
            }
            else return "";
        } catch (IOException e) {
            return "";
        }
    }

    public void WriteData(String data){
        try {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public void StopBluetoothConnection(){
        try {
            if(inputStream != null) inputStream.close();
            if(outputStream != null) outputStream.close();
            if(skt != null) skt.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public void PrintOnScreen(Context context, String info){
        Toast.makeText(context, info,Toast.LENGTH_SHORT).show();
    }
}




