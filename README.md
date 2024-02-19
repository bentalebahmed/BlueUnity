# BlueUnity
BlueUnity is a plugin for using Bluetooth with Unity3d on Android.

## Installation

Import `UnityAndroidBluetooth.unitypackage` (V3) into a Unity project. In the Scenes folder, you will find a scene "SampleScene" to test the Bluetooth plugin.
Make sure the set the minimum android API level to API level 27 from Player Settings.

## Usage

1) Build the game using SampleScene or use the one I already built (see in 'Use this build to test/ V3').

2) Enable bluetooth and location.

3) Open the app.

4) Enter the MAC address of the Bluetooth device, and click Start button to connect to BT.

5) Enter data in the input field named "Enter Data to send" and click Send.
   
6) The incoming data is displayed directly on the screen, using ReadData(string data) which is called from Java class using UnityPlayer.UnitySendMessage().

8) To see Log messages, draw a circle in the screen with your finger to see the Log Viewer (Unity-Logs-Viewer from assets store).
   
## Supported functionality
- Check and Request BT and location permissions (for Search Devices).
- Search Devices.
- Search Status.
- Get Paired Devices.
- Connect BT with MAC address.
- Disconnect BT.
- Connection Status.
- Read Stream.
- Write Stream.

I have Enhanced the code by using Unity3D UnityPlayer java class, this is done by adding the following line in build.gradle.kts of the Module, Here i m using Unity 2020.3.28f1, change that to your version:

```
dependencies {
    ...
    compileOnly(files("C:/Program Files/Unity/Hub/Editor/2020.3.28f1/Editor/Data/PlaybackEngines/AndroidPlayer/Variations/mono/Release/Classes/classes.jar"))
    }

```    
In the Java class you can now import UnityPlayer java class as
```
import com.unity3d.player.UnityPlayer;
```
and use 
```
UnityPlayer.UnitySendMessage("GameObjectName", "FunctionToBeCalled", "ArgumentAsString");
```


See [this video](https://www.youtube.com/watch?v=n9F6J5m7BJI) for a full walkthrough and how to create your own plugin (for V2). 
V3 demo and code explanation coming soon, for now i hope the code is self explained.

> **Note:** The plugin parses incoming data with `inputBuffer.readLine()`, which reads until it reaches a new line character ('\n').

## Bluetooth Compatibility

BlueUnity was tested on HC-05, HC-06, and BM78 Bluetooth modules, also with ESP32, using the standard Bluetooth library (BluetoothSerial.h).

## Adapting to Your Project

If you want to edit the plugin and adapt it to your needs, check the "Android source code" folder, which contains the code of the plugin.

## Contributing or Questions

Contact: bentalebahmed98@gmail.com

## License
[MIT](https://choosealicense.com/licenses/mit/)
