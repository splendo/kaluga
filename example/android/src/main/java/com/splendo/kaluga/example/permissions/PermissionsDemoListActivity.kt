package com.splendo.kaluga.example.permissions

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.example.permissions.bluetooth.BluetoothPermissionsDemoActivity
import kotlinx.android.synthetic.main.activity_permissions_list.*
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.permissions.calendar.CalendarPermissionsDemoActivity
import com.splendo.kaluga.example.permissions.camera.CameraPermissionsDemoActivity
import com.splendo.kaluga.example.permissions.contacts.ContactsPermissionsDemoActivity
import com.splendo.kaluga.example.permissions.location.LocationPermissionsDemoActivity
import com.splendo.kaluga.example.permissions.microphone.MicrophonePermissionsDemoActivity
import com.splendo.kaluga.example.permissions.storage.StoragePermissionsDemoActivity

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

        btn_permissions_list_calendar.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CalendarPermissionsDemoActivity::class.java
                )
            )
        }

        btn_permissions_list_camera.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CameraPermissionsDemoActivity::class.java
                )
            )
        }

        btn_permissions_list_contacts.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ContactsPermissionsDemoActivity::class.java
                )
            )
        }

        btn_permissions_list_location.setOnClickListener {
            Intent(
                this,
                LocationPermissionsDemoActivity::class.java
            )
        }

        btn_permissions_list_microphone.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MicrophonePermissionsDemoActivity::class.java
                )
            )
        }

        btn_permissions_list_storage.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    StoragePermissionsDemoActivity::class.java
                )
            )
        }
    }
}