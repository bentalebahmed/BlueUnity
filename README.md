# BlueUnity
Plugin for using Bluetooth (teseted on HC-05, HC-06) with unity3d on Android

## How it Works

Pair your bluetooth device to your android phone.

Build the game using SampleScene.

Enter the name of the bluetooth device, click Start.

Enter data in the input filed named "Enter Data to send" and click send.

To see incoming data, draw a circle in the screen with your finger to see a the Log Viewer (Unity-Logs-Viewer from assets store)


## Installation

Add unityPackage to Unity, in folder Scenes you will find a scene "SampleScene" to test the buetooth plugin (teseted on HC-06, HC-05).

In folder Assets\Plugins you will find the Android plugin named classes, AndroidManifest (for adding Bluetooth permission) and C# script for handling the plugin for you.

In Assets folder you will find a script (BluetoothTest) that contains functoin assigned to buttons to Start, Stop and send data to Bluetooth.

For the incoming data the plugin reads till new line appears ('\n') , (inputBuffer.readLine()) 


## Contributing or Asking

Contact : bentalebahmed98@gmail.com

## License
[MIT](https://choosealicense.com/licenses/mit/)
