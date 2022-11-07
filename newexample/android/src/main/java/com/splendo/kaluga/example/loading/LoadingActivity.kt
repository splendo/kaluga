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

package com.splendo.kaluga.example.loading

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.hud.HudViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("SetTextI18n")
class LoadingActivity : KalugaViewModelActivity<HudViewModel>(R.layout.activity_loading) {

    override val viewModel: HudViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<AppCompatButton>(R.id.btn_show_loading_indicator_system).setOnClickListener {
            viewModel.onShowSystemPressed()
        }

        findViewById<AppCompatButton>(R.id.btn_show_loading_indicator_custom).setOnClickListener {
            viewModel.onShowCustomPressed()
        }
    }
}
