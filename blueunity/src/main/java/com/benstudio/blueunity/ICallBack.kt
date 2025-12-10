package com.benstudio.blueunity

interface ICallBack {

    fun onDiscoveryStarted()
    fun onDiscoveryDeviceFound(name: String, address: String)
    fun onDiscoveryFinished()

    fun onConnecting(address: String)
    fun onConnectDeviceNotFound(address: String)
    fun onConnected(address: String)
    fun onDisconnected(address: String)

    fun onDataReceived(data: ByteArray)
    fun onDataNotSent(data: ByteArray)

    fun onError(error: String)
}
