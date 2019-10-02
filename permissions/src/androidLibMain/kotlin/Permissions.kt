package com.splendo.mpp.permissions

import android.content.Context

actual class Permissions {

    internal lateinit var context: Context

    actual fun getBluetoothManager(): PermissionManager {
        return BluetoothPermissionManager(context)
    }

    actual open class Builder {
        private lateinit var context: Context

        fun context(context: Context) = apply { this.context = context }

        actual open fun build(): Permissions {
            val permissions = Permissions()

            with(permissions) {
                this.context = this@Builder.context
            }

            return permissions
        }
    }

}