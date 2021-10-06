/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.monitor

import android.os.Bundle
import android.view.View
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityMonitorBinding
import com.splendo.kaluga.example.shared.viewmodel.monitor.ServiceMonitorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceMonitorActivity : KalugaViewModelActivity<ServiceMonitorViewModel>() {
    override val viewModel: ServiceMonitorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setUpBindings())
    }

    private fun setUpBindings(): View {
        val binding = ActivityMonitorBinding.inflate(layoutInflater, null, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}
