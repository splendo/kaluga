package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.DefaultScanner
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.BluetoothServerDSL
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import kotlinx.coroutines.flow.first
import platform.CoreBluetooth.CBCentralManager
import platform.Foundation.NSBundle
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseBluetoothBuilder]
 * @param bundle the [NSBundle] in which Bluetooth should run
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
 * Needs to have [com.splendo.kaluga.permissions.bluetooth.BluetoothPermission] registered.
 * @param scannerBuilder the [BaseScanner.Builder] for creating the [BaseScanner] to handle scanning
 */
actual class BluetoothBuilder(
    private val bundle: NSBundle = NSBundle.mainBundle,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder(bundle).apply {
                registerBluetoothPermissionIfNotRegistered()
            },
            context,
        )
    },
    private val scannerBuilder: DefaultScanner.Builder = DefaultScanner.Builder(),
) : BaseBluetoothBuilder {

    actual override fun createClient(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): Bluetooth = Bluetooth(
        { scannerContext ->
            scannerSettingsBuilder(permissionsBuilder(scannerContext))
        },
        scannerBuilder,
        coroutineContext,
    )

    actual override suspend fun createServer(coroutineContext: CoroutineContext, logger: Logger, specs: BluetoothServerDSL.() -> Unit): BluetoothServer {
        require(permissionsBuilder(coroutineContext).request(BluetoothPermission.Server)) { "Server could not be started. Missing permission" }
        val enabledManager = DefaultBluetoothMonitor { CBCentralManager() }
        try {
            enabledManager.startMonitoring()
            enabledManager.isEnabled.first { it }
        } finally {
            enabledManager.stopMonitoring()
        }

        return BluetoothServer.DSL(logger, coroutineContext).apply(specs).build()
    }
}
