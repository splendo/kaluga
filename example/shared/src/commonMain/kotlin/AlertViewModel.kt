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

package com.splendo.kaluga.example.shared

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertInterface
import com.splendo.kaluga.alerts.buildActionSheet
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlertViewModel(val builder: AlertInterface.Builder) : BaseViewModel() {

    fun showAlert() = MainScope().launch(MainQueueDispatcher) {
        val okAction = Alert.Action("OK", Alert.Action.Style.POSITIVE)
        val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
        val alert = builder.buildAlert {
            setTitle("Hello, Kaluga 🐟")
            setMessage("This is sample message")
            addActions(okAction, cancelAction)
        }
        when (alert.show()) {
            okAction -> debug("OK pressed")
            cancelAction -> debug("Cancel pressed")
        }
    }

    fun showAndDismissAfter(timeSecs: Long) = MainScope().launch(MainQueueDispatcher) {
        val coroutine = MainScope().launch(MainQueueDispatcher) {
            builder.buildAlert {
                setTitle("Wait for $timeSecs sec...")
                setPositiveButton("OK")
            }.show()
        }
        MainScope().launch(MainQueueDispatcher) {
            delay(timeSecs * 1_000)
            coroutine.cancel()
        }
    }

    fun showList() = MainScope().launch(MainQueueDispatcher) {
        builder.buildActionSheet {
            setTitle("Select an option")
            addActions(
                Alert.Action("Option 1") { debug("Option 1") },
                Alert.Action("Option 2") { debug("Option 2") },
                Alert.Action("Option 3") { debug("Option 3") },
                Alert.Action("Option 4") { debug("Option 4") }
            )
        }.show()
    }
}
