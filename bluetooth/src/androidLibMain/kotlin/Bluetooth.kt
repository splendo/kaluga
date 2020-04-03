package com.splendo.kaluga.bluetooth

import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import kotlinx.coroutines.CoroutineScope

actual class BluetoothBuilder(private val context: Context = ApplicationHolder.applicationContext,
                              private val permissions: Permissions = Permissions(PermissionsBuilder(context)),
                              private val scannerBuilder: Scanner.Builder = Scanner.Builder(context=context)
) : Bluetooth.Builder {

    override fun create(connectionSettings: ConnectionSettings, autoRequestPermission: Boolean, autoEnableBluetooth: Boolean, coroutineScope: CoroutineScope): Bluetooth {
        return Bluetooth(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scannerBuilder, coroutineScope)
    }

}