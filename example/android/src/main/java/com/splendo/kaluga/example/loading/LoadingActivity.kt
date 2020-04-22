package com.splendo.kaluga.example.loading

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.HudPresenter
import com.splendo.kaluga.hud.HudViewModel
import kotlinx.android.synthetic.main.activity_loading.*

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

@SuppressLint("SetTextI18n")
class LoadingActivity : AppCompatActivity(R.layout.activity_loading) {

    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.subscribe(this)

        btn_show_loading_indicator_system.setOnClickListener {
            viewModel.showSystem()
        }

        btn_show_loading_indicator_custom.setOnClickListener {
            viewModel.showCustom()
        }
    }

    override fun onDestroy() {
        viewModel.unsubscribe()
        super.onDestroy()
    }
}

class ViewModel: HudViewModel() {
    fun showSystem() = HudPresenter(builder).showSystem()
    fun showCustom() = HudPresenter(builder).showCustom()
}
