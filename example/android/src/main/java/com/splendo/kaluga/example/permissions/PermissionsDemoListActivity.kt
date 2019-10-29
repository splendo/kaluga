package com.splendo.kaluga.example.permissions

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.example.permissions.bluetooth.BluetoothPermissionsDemoActivity
import kotlinx.android.synthetic.main.activity_permissions_list.*
import com.splendo.kaluga.example.R

class PermissionsDemoListActivity : AppCompatActivity(R.layout.activity_permissions_list) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_permissions_list_bluetooth.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    BluetoothPermissionsDemoActivity::class.java
                )
            )
        }

        btn_permissions_list_location.setOnClickListener {
            Toast.makeText(
                this,
                "Not implemented",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}