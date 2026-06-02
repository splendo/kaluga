package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.DefaultScanner
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.BluetoothServerBuilder
import com.splendo.kaluga.bluetooth.server.BluetoothServerDSL
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import platform.Foundation.NSBundle
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseBluetoothBuilder] that delegates the client side to [BluetoothClientBuilder] and the server side to [BluetoothServerBuilder].
 * @param bundle the [NSBundle] in which Bluetooth should run
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

    private val clientBuilder = BluetoothClientBuilder(bundle, permissionsBuilder, scannerBuilder)
    private val serverBuilder = BluetoothServerBuilder(bundle, permissionsBuilder)

    actual override fun createClient(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): BluetoothClient =
        clientBuilder.createClient(scannerSettingsBuilder, coroutineContext)

    actual override suspend fun createServer(
        settingsBuilder: (Permissions) -> ServerSettings,
        coroutineContext: CoroutineContext,
        specs: BluetoothServerDSL.() -> Unit,
    ): BluetoothServer = serverBuilder.createServer(settingsBuilder, coroutineContext, specs)
}
