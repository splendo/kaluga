package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.BluetoothServerDSL
import com.splendo.kaluga.bluetooth.server.IOSServerState
import com.splendo.kaluga.bluetooth.server.KalugaCBPeripheralManagerDelegate
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import platform.Foundation.NSBundle
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseBluetoothServerBuilder]
 * @param bundle the [NSBundle] in which Bluetooth should run
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
 */
actual class BluetoothServerBuilder(
    private val bundle: NSBundle = NSBundle.mainBundle,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder(bundle).apply {
                registerBluetoothPermissionIfNotRegistered()
            },
            context,
        )
    },
) : BaseBluetoothServerBuilder {

    actual override suspend fun createServer(
        settingsBuilder: (Permissions) -> ServerSettings,
        coroutineContext: CoroutineContext,
        specs: BluetoothServerDSL.() -> Unit,
    ): BluetoothServer {
        val settings = settingsBuilder(permissionsBuilder(coroutineContext))
        val initialState = IOSServerState.AwaitingPermissions(
            settings.permissions[BluetoothPermission(BluetoothPermission.Type.Server)] as BluetoothPermissionStateRepo,
            KalugaCBPeripheralManagerDelegate(settings.logger, coroutineContext),
            settings.logger,
        )
        return BluetoothServer.DSL(settings, initialState, coroutineContext).apply(specs).build()
    }
}
