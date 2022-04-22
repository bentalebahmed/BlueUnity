# BlueUnity
Plugin for using Bluetooth (teseted on HC-05, HC-06 and BM78 bluetooth modules) with Unity3d on Android

## How it Works

Build the game using SampleScene or use the one i already built ( see in 'Use this build to test').

Pair your bluetooth device (tested on HC-05 and HC-06) with baud rate of 9600 to your android phone.

Open the app.

Enter the name of the bluetooth device, click Start.

Enter data in the input filed named "Enter Data to send" and click send.

To see incoming data, draw a circle in the screen with your finger to see a the Log Viewer (Unity-Logs-Viewer from assets store)

Also see this video: https://www.youtube.com/watch?v=H3llkatHt1E (the sound is low sorry for that)

If you want to edit the plugin and adapt it on your needs, check the "Android Studio src" file that contains the code of the plugin, check this video by CWGTech on Creating an Android plugin for Unity3D (https://www.youtube.com/watch?v=bmNMugkOQBI&list=WL&index=2&t=171s)

## Installation

BluetoothPlugin is the Android Project if you want to check the code and make changes on the Jar file.

Add unityPackage to Unity, in folder Scenes you will find a scene "SampleScene" to test the buetooth plugin (teseted on HC-06, HC-05).

In folder Assets\Plugins you will find the Android plugin named classes, AndroidManifest (for adding Bluetooth permission) and C# script for handling the plugin for you.

In Assets folder you will find a script (BluetoothTest) that contains functoin assigned to buttons to Start, Stop and send data to Bluetooth.

For the incoming data the plugin reads till new line appears ('\n') , (inputBuffer.readLine()) 
<!--
## Donation Button

If this project helped you in your path, you can give me a cup of coffee :)

[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=USTC757GQDJ8E)

-->
## Contributing or Questions

Contact : bentalebahmed98@gmail.com

## License
[MIT](https://choosealicense.com/licenses/mit/)
