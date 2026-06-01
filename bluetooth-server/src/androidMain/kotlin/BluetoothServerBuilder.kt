package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothManager
import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.server.AndroidServerState
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.BluetoothServerDSL
import com.splendo.kaluga.bluetooth.server.KalugaBluetoothGattServerCallback
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.bluetooth.server.ServerState
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseBluetoothServerBuilder]
 * @param applicationContext the [Context] in which Bluetooth should run
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
 */
actual class BluetoothServerBuilder(
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
) : BaseBluetoothServerBuilder {

    actual override suspend fun createServer(
        settingsBuilder: (Permissions) -> ServerSettings,
        coroutineContext: CoroutineContext,
        specs: BluetoothServerDSL.() -> Unit,
    ): BluetoothServer {
        val settings = settingsBuilder(permissionsBuilder(coroutineContext))
        val manager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        val initialState = manager?.let {
            AndroidServerState.AwaitingPermissions(
                it,
                settings.permissions[BluetoothPermission(BluetoothPermission.Type.Server)] as BluetoothPermissionStateRepo,
                KalugaBluetoothGattServerCallback(settings.logger, coroutineContext),
                applicationContext,
                settings.logger,
            )
        } ?: ServerState.NotSupported

        return BluetoothServer.DSL(settings, initialState, coroutineContext).apply(specs).build()
    }
}
