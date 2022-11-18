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
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.toTypedProperty
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionView
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PermissionsDemoActivity : KalugaViewModelActivity<PermissionViewModel>(R.layout.activity_permissions_demo) {

    override val viewModel: PermissionViewModel by viewModel {
        intent.extras?.toTypedProperty(NavigationBundleSpecType.SerializedType(PermissionView.serializer()))?.let { permissionView ->
            parametersOf(permissionView.permission)
        } ?: parametersOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = intent.extras?.toTypedProperty(NavigationBundleSpecType.SerializedType(PermissionView.serializer()))?.title

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
