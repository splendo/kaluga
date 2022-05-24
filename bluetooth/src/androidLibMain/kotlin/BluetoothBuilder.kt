package com.splendo.kaluga.bluetooth

import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import com.splendo.kaluga.permissions.location.registerLocationPermission
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

actual class BluetoothBuilder(
    private val applicationContext: Context = ApplicationHolder.applicationContext,
    private val permissionsBuilder: (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder(PermissionContext(applicationContext)).apply {
                registerBluetoothPermission()
                registerLocationPermission()
            },
            coroutineContext = context
        )
    },
    private val scannerBuilder: Scanner.Builder = Scanner.Builder(applicationContext = applicationContext)
) : Bluetooth.Builder {

    override fun create(connectionSettings: ConnectionSettings, autoRequestPermission: Boolean, autoEnableBluetooth: Boolean, coroutineScope: CoroutineScope): Bluetooth {
        return Bluetooth(permissionsBuilder(coroutineScope.coroutineContext), connectionSettings, autoRequestPermission, autoEnableBluetooth, scannerBuilder, coroutineScope)
    }
}
