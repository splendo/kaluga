package com.splendo.mpp.permissions

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBPeripheralManager
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.darwin.NSObject

class BluetoothPermissionManager : PermissionManager() {
    override suspend fun openSettings() {
        UIApplication.sharedApplication.openURL(NSURL(string = "App-Prefs:root=General"))
    }

    override suspend fun checkSupport(): Support {
        val cbCentralManagerDelegate = CBCentralManagerDelegate({ cbCentaralManager -> println("@@@@ state:${cbCentaralManager.state}") })
        val cbCentralManager = CBCentralManager(
            cbCentralManagerDelegate,
            null
        )

        return when (cbCentralManager.state.toLong()) {
            STATE_UNKNOWN -> Support.NOT_SUPPORTED
            STATE_RESETTING -> Support.RESETTING
            STATE_UNSUPPORTED -> Support.NOT_SUPPORTED
            STATE_UNAUTHORIZED -> Support.UNAUTHORIZED
            STATE_POWERED_OFF -> Support.POWER_OFF
            STATE_POWERED_ON -> Support.POWER_ON
            else -> Support.NOT_SUPPORTED
        }
    }

    override suspend fun checkPermit(): Permit {
        //TODO: Add support for iOS 13. `authorizationStatus` is being deprecated
        return when (CBPeripheralManager.authorizationStatus().toLong()) {
            AUTHORIZATION_STATUS_NOT_DETERMINED -> Permit.UNDEFINED
            AUTHORIZATION_STATUS_RESTRICTED -> Permit.RESTRICTED
            AUTHORIZATION_STATUS_DENIED -> Permit.DENIED
            AUTHORIZATION_STATUS_AUTHORIZED -> Permit.ALLOWED
            else -> Permit.UNDEFINED
        }
    }

    companion object {
        const val STATE_UNKNOWN = 0L
        const val STATE_RESETTING = 1L
        const val STATE_UNSUPPORTED = 2L
        const val STATE_UNAUTHORIZED = 3L
        const val STATE_POWERED_OFF = 4L
        const val STATE_POWERED_ON = 5L

        const val AUTHORIZATION_STATUS_NOT_DETERMINED = 0L
        const val AUTHORIZATION_STATUS_RESTRICTED = 1L
        const val AUTHORIZATION_STATUS_DENIED = 2L
        const val AUTHORIZATION_STATUS_AUTHORIZED = 3L
    }

}

private class CBCentralManagerDelegate(val onStateUpdate: (central: CBCentralManager) -> Unit) : NSObject(), CBCentralManagerDelegateProtocol {
    override fun centralManagerDidUpdateState(central: CBCentralManager) {
        onStateUpdate(central)
    }
}