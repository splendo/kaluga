package com.splendo.mpp.permissions

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBPeripheralManager

actual class Permissions {
    actual fun getBluetoothManager(): PermissionManager {
        return BluetoothPermissionManager(CBCentralManager(), CBPeripheralManager())
    }

    actual open class Builder {
        actual open fun build(): Permissions {
            return Permissions()
        }
    }

}