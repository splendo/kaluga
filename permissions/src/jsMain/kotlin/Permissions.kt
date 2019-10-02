package com.splendo.mpp.permissions

actual class Permissions {
    actual fun getBluetoothManager(): PermissionManager {
        return BluetoothPermissionManager()
    }

    actual open class Builder private actual constructor() {
        actual open fun build(): Permissions {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

}