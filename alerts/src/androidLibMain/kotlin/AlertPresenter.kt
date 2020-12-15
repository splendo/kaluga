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
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.getOrPutAndRemoveOnDestroyFromCache
import com.splendo.kaluga.architecture.lifecycle.lifecycleManagerObserver
import com.splendo.kaluga.base.utils.applyIf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

actual class AlertPresenter(
    private val alert: Alert,
    private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    coroutineScope: CoroutineScope
) : BaseAlertPresenter(alert), CoroutineScope by coroutineScope {

    actual class Builder(
        private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver()
    ) : BaseAlertPresenter.Builder(), LifecycleSubscribable by lifecycleManagerObserver {
        actual override fun create(coroutineScope: CoroutineScope) = AlertPresenter(createAlert(), lifecycleManagerObserver, coroutineScope)
    }

    private companion object {
        fun transform(style: Alert.Action.Style): Int = when (style) {
            Alert.Action.Style.DEFAULT, Alert.Action.Style.POSITIVE -> AlertDialog.BUTTON_POSITIVE
            Alert.Action.Style.DESTRUCTIVE, Alert.Action.Style.NEUTRAL -> AlertDialog.BUTTON_NEUTRAL
            Alert.Action.Style.CANCEL, Alert.Action.Style.NEGATIVE -> AlertDialog.BUTTON_NEGATIVE
        }
    }

    private sealed class DialogPresentation {
        data class Showing(val animated: Boolean, val afterHandler: (Alert.Action?) -> Unit, val completion: () -> Unit) : DialogPresentation()
        object Hidden : DialogPresentation()
    }

    private val presentation = MutableStateFlow<DialogPresentation>(DialogPresentation.Hidden)
    private var alertDialog: AlertDialog? = null

    init {
        launch {
            combine(lifecycleManagerObserver.managerState, presentation) { managerState, dialogPresentation ->
                Pair(managerState, dialogPresentation)
            }.collect { contextPresentation ->
                when (val dialogPresentation = contextPresentation.second) {
                    is DialogPresentation.Showing -> contextPresentation.first?.activity?.let { presentDialog(it, dialogPresentation) } ?: run { alertDialog = null }
                    is DialogPresentation.Hidden -> alertDialog?.dismiss()
                }
            }
        }
    }

    override fun dismissAlert(animated: Boolean) {
        presentation.value = DialogPresentation.Hidden
    }

    override fun showAlert(
        animated: Boolean,
        afterHandler: (Alert.Action?) -> Unit,
        completion: () -> Unit
    ) {
        presentation.value = DialogPresentation.Showing(animated, afterHandler, completion)
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
                setOnDismissListener {
                    alertDialog = null
                    this@AlertPresenter.presentation.value = DialogPresentation.Hidden
                }
                setOnCancelListener { presentation.afterHandler(null) }
                show()
            }
        presentation.completion()
    }
}

/**
 * @return The [AlertPresenter.Builder] which can be used to present alerts while this Activity is active
 * Will be created if need but only one instance will exist.
 *
 * Warning: Do not attempt to use this builder outside of the lifespan of the Activity.
 * Instead, for example use a [com.splendo.kaluga.architecture.viewmodel.ViewModel],
 * which can automatically track which Activity is active for it.
 *
 */
fun AppCompatActivity.alertPresenterBuilder(): AlertPresenter.Builder =
    getOrPutAndRemoveOnDestroyFromCache {
        AlertPresenter.Builder(lifecycleManagerObserver())
    }
