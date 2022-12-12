package com.splendo.kaluga.bluetooth

import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.DefaultScanner
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlin.coroutines.CoroutineContext

actual class BluetoothBuilder(
    private val applicationContext: Context = ApplicationHolder.applicationContext,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder(PermissionContext(applicationContext)).apply {
                registerBluetoothPermissionIfNotRegistered()
                registerLocationPermissionIfNotRegistered()
            },
            coroutineContext = context
        )
    },
    private val scannerBuilder: BaseScanner.Builder = DefaultScanner.Builder(applicationContext = applicationContext)
) : Bluetooth.Builder {

    override fun create(
        scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings,
        connectionSettings: ConnectionSettings,
        coroutineContext: CoroutineContext
    ): Bluetooth = Bluetooth(
        { scannerContext ->
            scannerSettingsBuilder(permissionsBuilder(scannerContext))
        },
        connectionSettings,
        scannerBuilder,
        coroutineContext
    )
}
