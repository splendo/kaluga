package com.splendo.kaluga.example.permissions.bluetooth

import com.splendo.kaluga.example.permissions.PermissionsDemoActivity
import com.splendo.kaluga.permissions.Permission

class BluetoothPermissionsDemoActivity : PermissionsDemoActivity<Permission.Bluetooth>() {

    override val permission = Permission.Bluetooth
    
}