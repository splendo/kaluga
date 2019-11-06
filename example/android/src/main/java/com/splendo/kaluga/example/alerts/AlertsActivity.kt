package com.splendo.kaluga.example.alerts

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.example.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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

    private lateinit var alertBuilder: AlertBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertBuilder = AlertBuilder(this)

        btn_simple_alert.setOnClickListener {
            MainScope().launch {
                showAlert()
            }
        }

        btn_dismissible_alert.setOnClickListener {
            MainScope().launch {
                showDismissibleAlert()
            }
        }
    }

    private suspend fun showAlert() {
        val okAction = Alert.Action("OK", Alert.Action.Style.POSITIVE)
        val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
        val alert = alertBuilder.alert {
            setTitle("Hello, Kaluga")
            addActions(listOf(okAction, cancelAction))
        }
        when (alert.show()) {
            okAction -> println("OK pressed")
            cancelAction -> println("Cancel pressed")
        }
    }

    private fun showDismissibleAlert() {
        val coroutine = MainScope().launch {
            alertBuilder.alert {
                setTitle("Hello")
                setMessage("Wait for 3 sec...")
                setPositiveButton("OK") { println("OK pressed") }
            }.show()
        }

        Handler().postDelayed({
            coroutine.cancel()
        }, 3_000)
    }
}
