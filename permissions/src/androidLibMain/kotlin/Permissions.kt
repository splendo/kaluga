package com.splendo.mpp.permissions

import android.content.Context

actual class Permissions {

    lateinit var context: Context

    actual fun getBluetoothManager(): PermissionManager {
        return BluetoothPermissionManager(context)
    }

    actual companion object {
        actual fun builder(): Builder {
            return AndroidPermissionBuilder()
        }
    }

}

class AndroidPermissionBuilder : Builder() {

    lateinit var context: Context

    override fun build(): Permissions {
        val permissions = Permissions()
        permissions.context = context
        return permissions
    }

}