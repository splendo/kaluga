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
import android.view.LayoutInflater
import android.widget.Toast
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.toTypedProperty
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityPermissionBinding
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionView
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PermissionActivity : KalugaViewModelActivity<PermissionViewModel>() {

    override val viewModel: PermissionViewModel by viewModel {
        intent.extras?.toTypedProperty(NavigationBundleSpecType.SerializedType(PermissionView.serializer()))?.let { permissionView ->
            parametersOf(permissionView.permission)
        } ?: parametersOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPermissionBinding.inflate(LayoutInflater.from(this), null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        supportActionBar?.title = viewModel.title

        viewModel.requestMessage.observeInitialized { message ->
            if (message != null) Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
