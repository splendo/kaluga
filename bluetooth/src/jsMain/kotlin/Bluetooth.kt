package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import kotlin.coroutines.CoroutineContext

actual class BluetoothBuilder(private val permissions: Permissions = Permissions(PermissionsBuilder())
) : Bluetooth.Builder {

    override fun create(connectionSettings: ConnectionSettings, autoRequestPermission: Boolean, autoEnableBluetooth: Boolean): Bluetooth {
        return Bluetooth(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, Scanner.Builder())
    }
}