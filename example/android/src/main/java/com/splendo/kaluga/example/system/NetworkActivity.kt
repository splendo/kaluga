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

package com.splendo.kaluga.example.system

import android.os.Bundle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.databinding.ActivityNetworkBinding
import com.splendo.kaluga.example.shared.viewmodel.system.network.NetworkViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetworkActivity : KalugaViewModelActivity<NetworkViewModel>(R.layout.activity_network) {

    override val viewModel: NetworkViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNetworkBinding.inflate(layoutInflater, null, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

}