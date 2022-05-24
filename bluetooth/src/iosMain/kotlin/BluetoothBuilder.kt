package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import kotlinx.coroutines.CoroutineScope
import platform.Foundation.NSBundle
import kotlin.coroutines.CoroutineContext

actual class BluetoothBuilder(
    private val bundle: NSBundle = NSBundle.mainBundle,
    private val permissionsBuilder: (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder(bundle).apply {
                registerBluetoothPermission()
            },
            context
        )
    },
    private val scannerBuilder: Scanner.Builder = Scanner.Builder()
) : Bluetooth.Builder {

    override fun create(connectionSettings: ConnectionSettings, autoRequestPermission: Boolean, autoEnableBluetooth: Boolean, coroutineScope: CoroutineScope): Bluetooth {
        return Bluetooth(permissionsBuilder(coroutineScope.coroutineContext), connectionSettings, autoRequestPermission, autoEnableBluetooth, scannerBuilder, coroutineScope)
    }
}
