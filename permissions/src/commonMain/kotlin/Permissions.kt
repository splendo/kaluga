package com.splendo.mpp.permissions

expect class Permissions internal constructor() {

    fun getBluetoothManager(): PermissionManager

    open class Builder private constructor() {
        open fun build(): Permissions
    }
}

