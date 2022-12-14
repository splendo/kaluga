package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.scanner.DefaultScanner
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
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
) : Bluetooth.Builder() {
    override fun create(): Bluetooth = Bluetooth(
        { scannerContext ->
            scannerSettingsFactory(permissionsBuilder(scannerContext))
        },
        defaultConnectionSettings,
        DefaultScanner.Builder(),
        coroutineContext
    )

    // override fun create(
    //     scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings,
    //     connectionSettings: ConnectionSettings,
    //     coroutineContext: CoroutineContext
    // ): Bluetooth = Bluetooth(
    //     { scannerContext ->
    //         scannerSettingsBuilder(permissionsBuilder(scannerContext))
    //     },
    //     connectionSettings,
    //     DefaultScanner.Builder(),
    //     coroutineContext
    // )
}
