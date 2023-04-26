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

package com.splendo.kaluga.example.shared.viewmodel.alert

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.alerts.buildActionSheet
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.alerts.buildAlertWithInput
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AlertViewModel(private val builder: BaseAlertPresenter.Builder) : BaseLifecycleViewModel(builder) {

    companion object {
        private val dismissTime: Duration = 3.seconds
    }

    val showAlertButton = KalugaButton.Plain("show_alert".localized(), ButtonStyles.default) {
        coroutineScope.launch {
            val okAction = Alert.Action("OK", Alert.Action.Style.POSITIVE)
            val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
            val alert = builder.buildAlert(this) {
                setTitle("Hello, Kaluga 🐟")
                setMessage("This is sample message")
                addActions(okAction, cancelAction)
            }
            when (alert.show()) {
                okAction -> debug("OK pressed")
                cancelAction -> debug("Cancel pressed")
            }
        }
    }

    val showAndDismissAfter3SecondsButton = KalugaButton.Plain("dismissible_alert".localized(), ButtonStyles.default) {
        coroutineScope.launch {
            val job = launch {
                builder.buildAlert(coroutineScope) {
                    setTitle("Wait for ${dismissTime.inWholeSeconds} sec...")
                    setPositiveButton("OK")
                }.show()
            }
            launch {
                delay(dismissTime)
                job.cancel()
            }
        }
    }

    val showAlertWithInputButton = KalugaButton.Plain("alert_input".localized(), ButtonStyles.default) {
        coroutineScope.launch {
            val okAction = Alert.Action("OK", Alert.Action.Style.POSITIVE)
            val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
            val alert = builder.buildAlertWithInput(this) {
                setTitle("Hello, Kaluga 🐟")
                setMessage("Type something!")
                addActions(okAction, cancelAction)
                setTextInput(placeholder = "This is a sample hint..") {
                    debug("Input value changed to: $it")
                }
            }
            when (alert.show()) {
                okAction -> debug("OK pressed")
                cancelAction -> debug("Cancel pressed")
            }
        }
    }

    val showAlertWithListButton = KalugaButton.Plain("alert_list".localized(), ButtonStyles.default) {
        coroutineScope.launch {
            builder.buildActionSheet(this) {
                setTitle("Select an option")
                addActions(
                    Alert.Action("Option 1") { debug("Option 1") },
                    Alert.Action("Option 2") { debug("Option 2") },
                    Alert.Action("Option 3") { debug("Option 3") },
                    Alert.Action("Option 4") { debug("Option 4") },
                )
            }.show()
        }
    }
}
