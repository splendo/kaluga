package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

actual class BluetoothBuilder(
    private val permissionsBuilder: (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder().apply {
                registerBluetoothPermission()
            },
            context
        )
    }
) : Bluetooth.Builder {

    override fun create(connectionSettings: ConnectionSettings, autoRequestPermission: Boolean, autoEnableBluetooth: Boolean, coroutineScope: CoroutineScope): Bluetooth {
        return Bluetooth(permissionsBuilder(coroutineScope.coroutineContext), connectionSettings, autoRequestPermission, autoEnableBluetooth, Scanner.Builder(), coroutineScope)
    }
}
