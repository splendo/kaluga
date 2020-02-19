package com.splendo.kaluga.permissions.bluetooth

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

expect class BluetoothPermissionManager : PermissionManager<Permission.Bluetooth>

interface BaseBluetoothPermissionManagerBuilder {
    fun create(repo: BluetoothPermissionStateRepo): BluetoothPermissionManager
}

expect class BluetoothPermissionManagerBuilder :BaseBluetoothPermissionManagerBuilder

class BluetoothPermissionStateRepo(builder: BluetoothPermissionManagerBuilder) : PermissionStateRepo<Permission.Bluetooth>() {

    override val permissionManager: PermissionManager<Permission.Bluetooth> = builder.create(this)

}