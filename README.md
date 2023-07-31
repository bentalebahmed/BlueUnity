# BlueUnity
BlueUnity is a plugin for using Bluetooth Low Energy (BLE) with Unity3d on Android.

## Installation

Import `UnityAndroidBluetooth.unitypackage` (V1 or V2) into a Unity project. In the Scenes folder, you will find a scene "SampleScene" to test the Bluetooth plugin.

## Usage

1) Build the game using SampleScene or use the one I already built (see in 'Use this build to test').

2) Pair your Bluetooth device with a baud rate of 9600 to your Android phone.

3) Open the app.
   
4) For V2, you can click search to display a list of paired devices in the Log Viewer.

5) Enter the name of the Bluetooth device, and click Start.

6) Enter data in the input field named "Enter Data to send" and click Send.

7) To see incoming data, draw a circle in the screen with your finger to see the Log Viewer (Unity-Logs-Viewer from assets store)

See [this video](https://www.youtube.com/watch?v=H3llkatHt1E) for a full walkthrough and how to create your own plugin.

> **Note:** The plugin parses incoming data with `inputBuffer.readLine()`, which reads until it reaches a new line character ('\n').

## Bluetooth Compatibility

BlueUnity was tested on HC-05, HC-06, and BM78 Bluetooth modules, also with ESP32, using the standard Bluetooth library (BluetoothSerial.h).

## Adapting to Your Project

If you want to edit the plugin and adapt it to your needs, check the "Android source code" folder, which contains the code of the plugin.

Notes:

* In the folder Assets\Plugins you will find the Android plugin and AndroidManifest (for adding Bluetooth permission), with a C# script (BluetoothService)for handling the plugin.

* In the Assets folder, you will find another C# script (BluetoothTest) containing functions assigned to buttons to Search, Start, Stop, and Send data to Bluetooth.

## Contributing or Questions

Contact: bentalebahmed98@gmail.com

## License
[MIT](https://choosealicense.com/licenses/mit/)
