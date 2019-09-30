package com.splendo.mpp.permissions

actual class Permissions {
    actual fun getBluetoothManager(): PermissionManager {
        return BluetoothPermissionManager()
    }

    actual companion object {
        actual fun builder(): Builder {
            return Builder()
        }
    }

}