package com.splendo.kaluga.bluetooth

import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.DefaultScanner
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseBluetoothBuilder]
 * @param applicationContext the [Context] in which Bluetooth should run
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
 * Needs to have [com.splendo.kaluga.permissions.bluetooth.BluetoothPermission] and [com.splendo.kaluga.permissions.location.LocationPermission] registered.
 * @param scannerBuilder the [BaseScanner.Builder] for creating the [BaseScanner] to handle scanning
 */
actual class BluetoothBuilder(
    private val applicationContext: Context = ApplicationHolder.applicationContext,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder(PermissionContext(applicationContext)).apply {
                registerBluetoothPermissionIfNotRegistered()
                registerLocationPermissionIfNotRegistered()
            },
            coroutineContext = context,
        )
    },
    private val scannerBuilder: BaseScanner.Builder = DefaultScanner.Builder(applicationContext = applicationContext),
) : BaseBluetoothBuilder {

    actual override fun create(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): Bluetooth = Bluetooth(
        { scannerContext ->
            scannerSettingsBuilder(permissionsBuilder(scannerContext))
        },
        scannerBuilder,
        coroutineContext,
    )
}
