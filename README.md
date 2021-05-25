# BlueUnity
Plugin for using Bluetooth (teseted on HC-05, HC-06) with unity3d on Android

## How it Works

Build the game using SampleScene or use the one i already built ( see in 'Use this build to test').

Pair your bluetooth device (tested on HC-05 and HC-06) to your android phone.

Open the app.

Enter the name of the bluetooth device, click Start.

Enter data in the input filed named "Enter Data to send" and click send.

To see incoming data, draw a circle in the screen with your finger to see a the Log Viewer (Unity-Logs-Viewer from assets store)

Also see this video: https://www.youtube.com/watch?v=H3llkatHt1E (the sound is low sorry for that)


## Installation

BluetoothPlugin is the Android Project if you want to check the code and make changes on the Jar file.

Add unityPackage to Unity, in folder Scenes you will find a scene "SampleScene" to test the buetooth plugin (teseted on HC-06, HC-05).

In folder Assets\Plugins you will find the Android plugin named classes, AndroidManifest (for adding Bluetooth permission) and C# script for handling the plugin for you.

In Assets folder you will find a script (BluetoothTest) that contains functoin assigned to buttons to Start, Stop and send data to Bluetooth.

For the incoming data the plugin reads till new line appears ('\n') , (inputBuffer.readLine()) 


## Contributing or Asking

Contact : bentalebahmed98@gmail.com

## License
[MIT](https://choosealicense.com/licenses/mit/)
