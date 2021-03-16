/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.keyboard

import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class KeyboardManagerActivity : KalugaViewModelActivity<KeyboardViewModel>(R.layout.activity_keyboard_manager) {

    override val viewModel: KeyboardViewModel by viewModel { parametersOf(R.id.edit_field) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<AppCompatButton>(R.id.btn_show_keyboard).setOnClickListener { viewModel.onShowPressed() }
        findViewById<AppCompatButton>(R.id.btn_hide_keyboard).setOnClickListener { viewModel.onHidePressed() }
    }
}
