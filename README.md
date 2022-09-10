# BlueUnity
BlueUnity is a plugin for using Bluetooth with Unity3d on Android.

## Installation

Import `UnityAndroidBluetooth.unitypackage` into a Unity project. In the Scenes folder you will find a scene "SampleScene" to test the Bluetooth plugin.

## Usage

1) Build the game using SampleScene or use the one I already built (see in 'Use this build to test').

2) Pair your Bluetooth device with baud rate of 9600 to your android phone.

3) Open the app.

4) Enter the name of the Bluetooth device, click Start.

5) Enter data in the input filed named "Enter Data to send" and click send.

6) To see incoming data, draw a circle in the screen with your finger to see a the Log Viewer (Unity-Logs-Viewer from assets store)

See [this video](https://www.youtube.com/watch?v=H3llkatHt1E) for a walkthrough (sorry for the low volume).

> **Note:** The plugin parses incoming data with `inputBuffer.readLine()`, which reads until it reaches a new line character ('\n').

## Bluetooth Compatibility

BlueUnity was tested on HC-05, HC-06 and BM78 Bluetooth modules.

## Adapting to Your Project

If you want to edit the plugin and adapt it on your needs, check the "Android Studio src" file that contains the code of the plugin, and see [this video](https://www.youtube.com/watch?v=bmNMugkOQBI&list=WL&index=2&t=171s) by CWGTech on Creating an Android plugin for Unity3D.

Notes:

* BluetoothPlugin is the Android Project if you want to check the code and make changes on the Jar file.

* In folder Assets\Plugins you will find the Android plugin named classes, AndroidManifest (for adding Bluetooth permission) and C# script for handling the plugin.

* In Assets folder you will find a script (BluetoothTest) that contains function assigned to buttons to Start, Stop and send data to Bluetooth.

<!--
## Donation Button

If this project helped you in your path, you can give me a cup of coffee :)

[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=USTC757GQDJ8E)

-->
## Contributing or Questions

Contact: bentalebahmed98@gmail.com

## License
[MIT](https://choosealicense.com/licenses/mit/)
