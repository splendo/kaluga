package com.splendo.kaluga.alerts

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

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

class AlertDialogPresenter(private val context: Context): AlertPresenter() {

    override fun show(alert: Alert, animated: Boolean, completion: (() -> Unit)?) {

        fun convertActionStyle(style: Alert.Action.Style): Int {
            return when (style) {
                Alert.Action.Style.DEFAULT -> AlertDialog.BUTTON_POSITIVE
                Alert.Action.Style.DESTRUCTIVE -> AlertDialog.BUTTON_NEGATIVE
                Alert.Action.Style.CANCEL -> AlertDialog.BUTTON_NEUTRAL
            }
        }

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(alert.title)
        alertDialog.setMessage(alert.message)
        alert.actions.forEach { action ->
            alertDialog.setButton(convertActionStyle(action.style), action.title) { _, _ ->
                action.handler()
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    override fun dismiss(identifier: Alert.Identifier, animated: Boolean) {
    }
}
