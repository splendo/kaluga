/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.bluetooth

import android.os.Bundle
import com.splendo.kaluga.architecture.navigation.parseTypeOf
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.bluetooth.device.SerializableIdentifier
import com.splendo.kaluga.example.databinding.ActivityBluetoothMoreBinding
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothDeviceDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BluetoothMoreActivity : KalugaViewModelActivity<BluetoothDeviceDetailViewModel>() {

    override val viewModel: BluetoothDeviceDetailViewModel by viewModel {
        parametersOf(
            parseTypeOf(SerializableIdentifier.serializer()).identifier,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityBluetoothMoreBinding.inflate(layoutInflater, null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.serviceList.adapter = BluetoothServiceAdapter(this)
    }
}
