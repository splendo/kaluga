package com.splendo.kaluga.example.alerts

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.AlertFactory
import kotlinx.android.synthetic.main.activity_alerts.*

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
class AlertsActivity : AppCompatActivity(R.layout.activity_alerts) {

    private val alertFactory = AlertFactory(AlertBuilder(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_simple_alert.setOnClickListener { alertFactory.showAlert() }
        btn_dismissible_alert.setOnClickListener { alertFactory.showAndDismissAfter() }
    }
}
