/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.permissions

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.PermissionsPrinter
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import kotlinx.android.synthetic.main.activity_permissions_demo.*
import kotlinx.coroutines.*

abstract class PermissionsDemoActivity<P:Permission> : AppCompatActivity(R.layout.activity_permissions_demo),
    CoroutineScope by MainScope() {

    private lateinit var permissions: Permissions
    protected abstract val permission: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissions = Permissions(PermissionsBuilder())
        val printer = PermissionsPrinter(permissions, permission)

        btn_permissions_bluetooth_check_permission.setOnClickListener {
            GlobalScope.launch {
                printer.printPermission { text ->
                    Toast.makeText(
                        this@PermissionsDemoActivity,
                        text,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btn_permissions_bluetooth_request_permissions.setOnClickListener {
            GlobalScope.launch {
                printer.printRequest { text ->
                    Toast.makeText(
                        this@PermissionsDemoActivity,
                        text,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


}