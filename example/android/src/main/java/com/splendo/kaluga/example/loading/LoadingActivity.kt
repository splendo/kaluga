package com.splendo.kaluga.example.loading

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.HudPresenter
import com.splendo.kaluga.hud.AndroidHUD
import com.splendo.kaluga.hud.HUD
import com.splendo.kaluga.hud.UiContextTrackingBuilder
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.coroutines.launch

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

private val uiContextTrackingBuilder = UiContextTrackingBuilder()
private val builder = AndroidHUD.Builder(uiContextTrackingBuilder)

@SuppressLint("SetTextI18n")
class LoadingActivity : AppCompatActivity(R.layout.activity_loading) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiContextTrackingBuilder.uiContextData = UiContextTrackingBuilder.UiContextData(
            this@LoadingActivity,
            this@LoadingActivity.supportFragmentManager
        )

        btn_show_loading_indicator_system.setOnClickListener {
            HudPresenter(builder).showSystem()
        }

        btn_show_loading_indicator_custom.setOnClickListener {
            HudPresenter(builder).showCustom()
        }
    }

    override fun onDestroy() {
        uiContextTrackingBuilder.uiContextData = null
        super.onDestroy()
    }
}
