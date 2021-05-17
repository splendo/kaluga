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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.splendo.kaluga.architecture.navigation.toNavigationBundle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionNavigationBundleSpec
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionNavigationBundleSpecRow
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionView
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionViewModel
import com.splendo.kaluga.permissions.Permission
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PermissionsDemoActivity : KalugaViewModelActivity<PermissionViewModel>(R.layout.activity_permissions_demo) {

    override val viewModel: PermissionViewModel by viewModel {
        val permissionNavSpec = PermissionNavigationBundleSpec()
        intent.extras?.toNavigationBundle(permissionNavSpec)?.let { bundle ->
            val permission = when (bundle.get(PermissionNavigationBundleSpecRow)) {
                PermissionView.Bluetooth -> Permission.Bluetooth
                PermissionView.Calendar -> Permission.Calendar(allowWrite = true)
                PermissionView.Camera -> Permission.Camera
                PermissionView.Contacts -> Permission.Contacts(allowWrite = true)
                PermissionView.Location -> Permission.Location(background = true, precise = true)
                PermissionView.Microphone -> Permission.Microphone
                PermissionView.Notifications -> Permission.Notifications()
                PermissionView.Storage -> Permission.Storage(allowWrite = true)
            }
            parametersOf(permission)
        } ?: parametersOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionNavSpec = PermissionNavigationBundleSpec()
        intent.extras?.toNavigationBundle(permissionNavSpec)?.let { bundle ->
            supportActionBar?.title = bundle.get(PermissionNavigationBundleSpecRow).title
        }

        viewModel.permissionStateMessage.observe {
            findViewById<TextView>(R.id.permissions_message).text = it
        }

        viewModel.showPermissionButton.observe {
            findViewById<AppCompatButton>(R.id.btn_permissions_bluetooth_request_permissions).visibility = if (it == true) View.VISIBLE else View.GONE
        }

        findViewById<AppCompatButton>(R.id.btn_permissions_bluetooth_request_permissions).setOnClickListener { viewModel.requestPermission() }

        viewModel.requestMessage.observeInitialized { message ->
            if (message != null) Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
