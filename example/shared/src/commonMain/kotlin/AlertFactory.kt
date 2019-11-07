package com.splendo.kaluga.example.shared

import com.splendo.kaluga.MainQueueDispatcher
import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.log.LogLevel
import com.splendo.kaluga.log.log
import kotlinx.coroutines.*

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

class AlertFactory(private val builder: AlertBuilder) {

    private suspend fun build(initialize: AlertBuilder.() -> Unit) =
        builder.alert { builder.initialize() }

    fun showAlert() = GlobalScope.launch(MainQueueDispatcher) {
        val okAction = Alert.Action("OK", Alert.Action.Style.POSITIVE)
        val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
        val alert = build {
            setTitle("Hello, Kaluga 🐟")
            setMessage("This is sample message")
            addActions(listOf(okAction, cancelAction))
        }
        when (alert.show()) {
            okAction -> log(LogLevel.DEBUG, "OK pressed")
            cancelAction -> log(LogLevel.DEBUG, "Cancel pressed")
        }
    }

    fun showAndDismissAfter(timeSecs: Long) = GlobalScope.launch(MainQueueDispatcher) {
        val coroutine = GlobalScope.launch(MainQueueDispatcher) {
            build {
                setTitle("Wait for $timeSecs sec...")
                setPositiveButton("OK")
            }.show()
        }
        GlobalScope.launch(MainQueueDispatcher) {
            delay(timeSecs * 1_000)
            coroutine.cancel()
        }
    }
}
