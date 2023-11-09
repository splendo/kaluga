package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.DefaultScanner
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseBluetoothBuilder]
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
 * Needs to have [com.splendo.kaluga.permissions.bluetooth.BluetoothPermission] registered.
 */
actual class BluetoothBuilder(
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder().apply {
                registerBluetoothPermissionIfNotRegistered()
            },
            context,
        )
    },
) : BaseBluetoothBuilder {

    override fun create(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): Bluetooth = Bluetooth(
        { scannerContext ->
            scannerSettingsBuilder(permissionsBuilder(scannerContext))
        },
        DefaultScanner.Builder(),
        coroutineContext,
    )
}
