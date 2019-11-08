package com.splendo.kaluga.alerts

import android.app.AlertDialog
import android.content.Context

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

actual class AlertBuilder(private val context: Context) : BaseAlertBuilder() {
    override fun create() = AlertInterface(createAlert(), context)
}

actual class AlertInterface(
    private val alert: Alert,
    private val context: Context
) : BaseAlertPresenter(alert) {

    private inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T = apply {
        if (condition) block(this)
    }

    private var alertDialog: AlertDialog? = null

    private companion object {
        fun transform(style: Alert.Action.Style): Int = when (style) {
            Alert.Action.Style.DEFAULT, Alert.Action.Style.POSITIVE -> AlertDialog.BUTTON_POSITIVE
            Alert.Action.Style.DESTRUCTIVE, Alert.Action.Style.NEUTRAL -> AlertDialog.BUTTON_NEUTRAL
            Alert.Action.Style.CANCEL, Alert.Action.Style.NEGATIVE -> AlertDialog.BUTTON_NEGATIVE
        }
    }

    override fun dismissAlert(animated: Boolean) {
        alertDialog?.dismiss()
    }

    override fun showAlert(
        animated: Boolean,
        afterHandler: (Alert.Action?) -> Unit,
        completion: () -> Unit
    ) {
        alertDialog = AlertDialog.Builder(context)
            .setTitle(alert.title)
            .setMessage(alert.message)
            .applyIf(alert.style == Alert.Style.ACTION_LIST) {
                val titles = alert.actions.map { it.title }.toTypedArray()
                setItems(titles) { _, which ->
                    val action = alert.actions[which].apply { handler() }
                    afterHandler(action)
                }
            }
            .create()
            .applyIf(alert.style == Alert.Style.ALERT) {
                alert.actions.forEach { action ->
                    setButton(transform(action.style), action.title) { _, _ ->
                        action.handler()
                        afterHandler(action)
                    }
                }
            }
            .apply {
                setOnDismissListener { alertDialog = null }
                setOnCancelListener { afterHandler(null) }
                show()
            }
        completion()
    }
}
