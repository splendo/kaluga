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

package com.splendo.kaluga.example.keyboard.xml

import android.os.Bundle
import android.view.LayoutInflater
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.databinding.ActivityKeyboardManagerBinding
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import com.splendo.kaluga.keyboard.ViewFocusHandler
import com.splendo.kaluga.keyboard.ViewKeyboardManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class XMLKeyboardActivity : KalugaViewModelActivity<KeyboardViewModel<ViewFocusHandler>>() {

    companion object {
        const val VIEW_MODEL_NAME = "ViewKeyboardViewModel"
    }

    override val viewModel: KeyboardViewModel<ViewFocusHandler> by viewModel(named(VIEW_MODEL_NAME)) {
        parametersOf(
            ViewKeyboardManager.Builder(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityKeyboardManagerBinding.inflate(LayoutInflater.from(this), null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        viewModel.editFieldFocusHandler.post(ViewFocusHandler(R.id.edit_field))
    }
}
