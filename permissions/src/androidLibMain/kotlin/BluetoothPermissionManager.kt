package com.splendo.kaluga.permissions

/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager


actual open class BluetoothPermissionManager(
    private val context: Context,
    private val bluetoothAdapterProvider: BluetoothAdapterWrapper = BluetoothAdapterWrapper()
) : PermissionManager {

    override fun requestPermissions() {
        Permissions.requestPermissions(context, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)
    }

    override fun openSettings() {
        val intent = Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    override fun checkSupport(): Support {
        return when {
            !bluetoothAdapterProvider.isAvailable() -> Support.NOT_SUPPORTED
            bluetoothAdapterProvider.isEnabled() -> Support.POWER_ON
            else -> Support.POWER_OFF
        }
    }

    override fun checkPermit(): Permit {
        //this is non-dangerous permission so it should be always available
        return when (context.checkSelfPermission(Manifest.permission.BLUETOOTH)) {
            PackageManager.PERMISSION_DENIED -> Permit.DENIED
            PackageManager.PERMISSION_GRANTED -> Permit.ALLOWED
            else -> Permit.DENIED
        }
    }

    /**
     * This class is used to wrap final class BluetoothAdapter.
     * @see android.bluetooth.BluetoothAdapter
     */
    open class BluetoothAdapterWrapper {
        private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        open fun isAvailable(): Boolean {
            return bluetoothAdapter != null
        }

        open fun isEnabled(): Boolean {
            return bluetoothAdapter.isEnabled
        }
    }

}

