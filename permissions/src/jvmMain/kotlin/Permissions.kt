package com.splendo.mpp.permissions

actual class Permissions {
    actual fun getBluetoothManager(): PermissionManager {
        return BluetoothPermissionManager()
    }

    actual open class Builder {
        actual open fun build(): Permissions {
            return Permissions()
        }
    }

}