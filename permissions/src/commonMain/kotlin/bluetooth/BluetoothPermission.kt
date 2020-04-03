package com.splendo.kaluga.permissions.bluetooth

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo
import kotlinx.coroutines.CoroutineScope

expect class BluetoothPermissionManager : PermissionManager<Permission.Bluetooth>

interface BaseBluetoothPermissionManagerBuilder {
    fun create(repo: BluetoothPermissionStateRepo,
        coroutineScope: CoroutineScope
    ): PermissionManager<Permission.Bluetooth>
}

expect class BluetoothPermissionManagerBuilder :BaseBluetoothPermissionManagerBuilder

class BluetoothPermissionStateRepo(builder: BaseBluetoothPermissionManagerBuilder,
    coroutineScope: CoroutineScope) : PermissionStateRepo<Permission.Bluetooth>(coroutineScope = coroutineScope) {

    override val permissionManager: PermissionManager<Permission.Bluetooth> = builder.create(this, coroutineScope)

}