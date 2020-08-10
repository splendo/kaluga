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

package com.splendo.kaluga.alerts

import android.app.AlertDialog
import android.content.Context
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.UIContextObserver
import com.splendo.kaluga.utils.applyIf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

actual class AlertInterface(
    private val alert: Alert,
    private val uiContextObserver: UIContextObserver = UIContextObserver()
) : BaseAlertPresenter(alert), CoroutineScope by MainScope()  {

    actual class Builder(private val uiContextObserver: UIContextObserver = UIContextObserver()) : BaseAlertBuilder(), LifecycleSubscribable by uiContextObserver {
        actual fun create() = AlertInterface(createAlert(), uiContextObserver)
    }

    private companion object {
        fun transform(style: Alert.Action.Style): Int = when (style) {
            Alert.Action.Style.DEFAULT, Alert.Action.Style.POSITIVE -> AlertDialog.BUTTON_POSITIVE
            Alert.Action.Style.DESTRUCTIVE, Alert.Action.Style.NEUTRAL -> AlertDialog.BUTTON_NEUTRAL
            Alert.Action.Style.CANCEL, Alert.Action.Style.NEGATIVE -> AlertDialog.BUTTON_NEGATIVE
        }
    }

    private sealed class DialogPresentation {
        data class Showing(val animated: Boolean, val afterHandler: (Alert.Action?) -> Unit, val completion: () -> Unit): DialogPresentation()
        object Hidden : DialogPresentation()
    }

    private val presentation = ConflatedBroadcastChannel<DialogPresentation>(DialogPresentation.Hidden)
    private var alertDialog: AlertDialog? = null

    init {
        launch {
            combine(uiContextObserver.uiContextData, presentation.asFlow()) { uiContextData, dialogPresentation ->
                Pair(uiContextData, dialogPresentation)
            }.collect { contextPresentation ->
                when(val dialogPresentation = contextPresentation.second) {
                    is DialogPresentation.Showing -> contextPresentation.first?.activity?.let { presentDialog(it, dialogPresentation) } ?: run { alertDialog = null }
                    is DialogPresentation.Hidden -> alertDialog?.dismiss()
                }
            }
        }
    }

    override fun dismissAlert(animated: Boolean) {
        launch {
            presentation.send(DialogPresentation.Hidden)
        }
    }

    override fun showAlert(
        animated: Boolean,
        afterHandler: (Alert.Action?) -> Unit,
        completion: () -> Unit
    ) {
        launch {
            presentation.send(DialogPresentation.Showing(animated, afterHandler, completion))
        }
    }

    private fun presentDialog(context: Context, presentation: DialogPresentation.Showing) {
        alertDialog = AlertDialog.Builder(context)
            .setTitle(alert.title)
            .setMessage(alert.message)
            .applyIf(alert.style == Alert.Style.ACTION_LIST) {
                val titles = alert.actions.map { it.title }.toTypedArray()
                setItems(titles) { _, which ->
                    val action = alert.actions[which].apply { handler() }
                    presentation.afterHandler(action)
                }
            }
            .create()
            .applyIf(alert.style == Alert.Style.ALERT) {
                alert.actions.forEach { action ->
                    setButton(transform(action.style), action.title) { _, _ ->
                        action.handler()
                        presentation.afterHandler(action)
                    }
                }
            }
            .apply {
                setOnDismissListener { alertDialog = null }
                setOnCancelListener { presentation.afterHandler(null) }
                show()
            }
        presentation.completion()
    }
}
