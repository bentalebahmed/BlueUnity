# BlueUnity (Kotlin Version)

BlueUnity is a Unity plugin that provides [Android Bluetooth Classic](https://developer.android.com/develop/connectivity/bluetooth/setup) connectivity.
This version is a Kotlin-based implementation that enables device scanning, pairing, server/client connections, and byte-stream communication directly from Unity.

---


### Permissions & Bluetooth Control
- Automatic Bluetooth permission handling *(No location permission needed)*
- Check Bluetooth enabled state and request enabling
- Set device discoverability (server mode)
- Rename the device during discoverable mode

### Device Management
- Retrieve paired devices
- Search for nearby devices
- Connect/disconnect using MAC address
- Device-to-device (mobile, tablet, headset, etc.) communication (one server, one clients)
- Device-to-module (ESP32, HC-05/06, BM78, etc.) communication (Device as client and the module always as a server)

### Communication
- Read and write byte streams
- Callback system using JavaProxy (no `Unity SendMessage`)

#### Available Callbacks
  - onDiscoveryStarted()
  - onDiscoveryDeviceFound(name: String, address: String)
  - onDiscoveryFinished()
  - onConnecting(address: String)
  - onConnected(address: String)
  - onDisconnected(address: String)
  - onDataReceived(data: ByteArray)
  - onDataNotSent(data: ByteArray)
  - onError(error: String)

## Installation

1. **Set Minimum API Level → 27**  
   Project Settings → Player → Other Settings → Minimum API Level

2. **Enable Custom Main Manifest**  
   Project Settings → Player → Publishing Settings → Custom Main Manifest

3. **Unity 6 only:**  
   Set **Application Entry Point** to **Activity**  
   Project Settings → Player → Other Settings

4. [**Import BlueUnity UnityPackage**](https://github.com/bentalebahmed/BlueUnity/releases)   

5. Use the **BlueUnity** top-menu in Unity to:
   - Validate plugin files  
   - Copy required Android manifest entries

---

## Example Scene with Unity 6000.3.0f1

  Build the app with the example scene:  
   `Assets/BlueUnity/Scenes/BluetoothManagerExample`

   Or install the demo [APK](https://github.com/bentalebahmed/BlueUnity/tree/Kotlin-version/UnityBuild)


### Example Scene Features
- Scan for nearby devices  
- Enable device discoverability (shows as **BlueUnity**)  
- Tap to connect to discovered devices  
- Display paired devices  
- Toggle connect with/without pairing  
- Send and receive data  

---

## Support

ben.studio33@gmail.com

---

## License

[MIT](https://choosealicense.com/licenses/mit/)