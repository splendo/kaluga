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

actual class AlertBuilder(private val context: Context): BaseAlertBuilder() {
    override fun create(): AlertInterface {
        return AlertInterface(createAlert(), context)
    }
}

actual class AlertInterface(
    private val alert: Alert,
    private val context: Context
): BaseAlertPresenter(alert) {

    private var latestDialog: AlertDialog? = null

    override fun show(animated: Boolean, completion: (() -> Unit)?) {

        fun convertActionStyle(style: Alert.Action.Style): Int {
            return when (style) {
                Alert.Action.Style.DEFAULT -> AlertDialog.BUTTON_POSITIVE
                Alert.Action.Style.DESTRUCTIVE -> AlertDialog.BUTTON_NEGATIVE
                Alert.Action.Style.CANCEL -> AlertDialog.BUTTON_NEUTRAL
            }
        }

        val alertDialog = AlertDialog.Builder(context)
            .setTitle(alert.title)
            .setMessage(alert.message)
            .create()

        alert.actions.forEach { action ->
            alertDialog.setButton(convertActionStyle(action.style), action.title) { dialog, _ ->
                action.handler()
                dialog.dismiss()
                latestDialog = null
            }
        }
        alertDialog.show()
        latestDialog = alertDialog
        completion?.invoke()
    }

    override fun dismiss(animated: Boolean) {
        latestDialog?.dismiss()
        latestDialog = null
    }
}
