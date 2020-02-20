package com.splendo.kaluga.example.permissions.bluetooth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.PermissionsPrinter
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
        val printer = PermissionsPrinter(permissions)

        btn_permissions_bluetooth_check_permission.setOnClickListener {
            printer.printPermission {text ->
                Toast.makeText(
                this@BluetoothPermissionsDemoActivity,
                text,
                Toast.LENGTH_SHORT).show()
            }
        }

        btn_permissions_bluetooth_request_permissions.setOnClickListener {
            printer.printRequest {text ->
                Toast.makeText(
                    this@BluetoothPermissionsDemoActivity,
                    text,
                    Toast.LENGTH_SHORT).show()
            }
        }

    }


}