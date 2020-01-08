package com.splendo.kaluga.example.permissions.bluetooth

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.log.error
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.Permit
import com.splendo.kaluga.permissions.Support
import kotlinx.android.synthetic.main.activity_permissions_bluetooth_demo.*
import kotlinx.coroutines.*

class BluetoothPermissionsDemoActivity : AppCompatActivity(R.layout.activity_permissions_bluetooth_demo),
    CoroutineScope by MainScope() {

    private lateinit var permissions: Permissions
    private lateinit var bluetoothManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissions = Permissions.Builder(this).build()
        bluetoothManager = permissions.getBluetoothManager()

        btn_permissions_bluetooth_check_manifest_declaration.setOnClickListener {
            Permissions.checkPermissionsDeclaration(
                this,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        }

        btn_permissions_bluetooth_check_support.setOnClickListener {
            launch {
                var support: Support = Support.NOT_SUPPORTED
                withContext(Dispatchers.IO) {
                    try {
                        support = bluetoothManager.checkSupport()
                    } catch (error: Exception) {
                        error("BluetoothDemo", error)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@BluetoothPermissionsDemoActivity,
                        "Support = {${support.name}}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

        btn_permissions_bluetooth_check_permission.setOnClickListener {
            launch {
                var permit: Permit = Permit.UNDEFINED
                withContext(Dispatchers.IO) {
                    try {
                        permit = bluetoothManager.checkPermit()
                    } catch (error: Exception) {
                        error("BluetoothDemo", error)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@BluetoothPermissionsDemoActivity,
                        "Permit = {${permit.name}}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        btn_permissions_bluetooth_open_settings.setOnClickListener {
            launch {
                bluetoothManager.openSettings()
            }
        }

        btn_permissions_bluetooth_request_permissions.setOnClickListener {
            launch {
                Permissions.requestPermissions(this@BluetoothPermissionsDemoActivity, Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

    }


}