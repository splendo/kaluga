package com.splendo.mpp.permissions

expect class Permissions internal constructor() {

    fun getBluetoothManager(): PermissionManager

    companion object {
        fun builder(): Builder
    }

}

open class Builder() {
    open fun build(): Permissions {
        return Permissions()
    }
}