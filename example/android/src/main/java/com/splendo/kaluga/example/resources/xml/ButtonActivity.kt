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

package com.splendo.kaluga.example.resources.xml

import android.os.Bundle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityButtonBinding
import com.splendo.kaluga.example.shared.viewmodel.resources.ButtonViewModel
import com.splendo.kaluga.example.view.ButtonAdapter
import com.splendo.kaluga.example.view.VerticalSpaceItemDecoration
import com.splendo.kaluga.resources.dpToPixel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ButtonActivity : KalugaViewModelActivity<ButtonViewModel>() {

    override val viewModel: ButtonViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityButtonBinding.inflate(layoutInflater, null, false)
        binding.buttonsList.adapter = ButtonAdapter()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        binding.buttonsList.addItemDecoration(VerticalSpaceItemDecoration(10.0f.dpToPixel(this).toInt()))
    }
}
