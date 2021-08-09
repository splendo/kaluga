package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import kotlinx.coroutines.CoroutineScope
import platform.Foundation.NSBundle

actual class BluetoothBuilder(
    private val bundle: NSBundle = NSBundle.mainBundle,
    private val permissions: Permissions = Permissions(
        PermissionsBuilder(bundle).apply {
            registerBluetoothPermission()
        }
    )
) : Bluetooth.Builder {

    override fun create(connectionSettings: ConnectionSettings, autoRequestPermission: Boolean, autoEnableBluetooth: Boolean, coroutineScope: CoroutineScope): Bluetooth {
        return Bluetooth(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, Scanner.Builder(), coroutineScope)
    }
}
