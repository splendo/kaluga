package com.splendo.kaluga.example.permissions.bluetooth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionManagerBuilder
import com.splendo.kaluga.permissions.request
import kotlinx.android.synthetic.main.activity_permissions_bluetooth_demo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class BluetoothPermissionsDemoActivity : AppCompatActivity(R.layout.activity_permissions_bluetooth_demo),
    CoroutineScope by MainScope() {

    private lateinit var permissions: Permissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissions = Permissions(BluetoothPermissionManagerBuilder())

        btn_permissions_bluetooth_check_permission.setOnClickListener {
            launch {
                withContext(Dispatchers.IO) {
                    val message = when (permissions[Permission.Bluetooth].first()) {
                        is PermissionState.Allowed -> "Allowed"
                        is PermissionState.Denied.Requestable -> "Denied but Requestable"
                        is PermissionState.Denied.SystemLocked -> "Denied"
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@BluetoothPermissionsDemoActivity,
                            "Permission = $message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }

        btn_permissions_bluetooth_request_permissions.setOnClickListener {
            launch {
                val success = permissions[Permission.Bluetooth].request()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@BluetoothPermissionsDemoActivity,
                        "Request = $success",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


}