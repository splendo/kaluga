package com.splendo.kaluga.alerts

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

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

typealias AlertActionHandler = () -> Unit

data class Alert(
    val title: String?,
    val message: String?,
    val actions: List<Action>
) {

    data class Action(
        val title: String,
        val style: Style = Style.DEFAULT,
        val handler: AlertActionHandler = {}
    ) {
        enum class Style(val value: Int) {
            DEFAULT(0),
            POSITIVE(DEFAULT.value),
            DESTRUCTIVE(1),
            NEUTRAL(DESTRUCTIVE.value),
            CANCEL(2),
            NEGATIVE(CANCEL.value)
        }
    }
}

interface AlertActions {
    fun show(animated: Boolean = true, completion: () -> Unit = {})
    suspend fun show(animated: Boolean = true): Alert.Action?
    fun dismiss(animated: Boolean = true)
}

abstract class BaseAlertPresenter(private val alert: Alert) : AlertActions {

    override fun show(animated: Boolean, completion: () -> Unit) {
        showAlert(animated, completion = completion)
    }

    override suspend fun show(animated: Boolean): Alert.Action? =
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                dismissAlert(animated)
                continuation.resume(null)
            }
            showAlert(animated, afterHandler = { continuation.resume(it) })
        }

    override fun dismiss(animated: Boolean) {
        dismissAlert(animated)
    }

    protected abstract fun dismissAlert(animated: Boolean = true)

    protected abstract fun showAlert(
        animated: Boolean = true,
        afterHandler: (Alert.Action?) -> Unit = {},
        completion: () -> Unit = {}
    )
}

expect class AlertInterface : BaseAlertPresenter

interface AlertBuilderActions {
    fun create(): AlertInterface
}

abstract class BaseAlertBuilder : AlertBuilderActions {

    private var title: String? = null
    private var message: String? = null
    private var actions: MutableList<Alert.Action> = mutableListOf()

    fun setTitle(title: String?) = apply { this.title = title }

    fun setMessage(message: String?) = apply { this.message = message }

    fun setPositiveButton(title: String, handler: AlertActionHandler) = apply {
        addAction(Alert.Action(title, Alert.Action.Style.POSITIVE, handler))
    }

    fun setNegativeButton(title: String, handler: AlertActionHandler) = apply {
        addAction(Alert.Action(title, Alert.Action.Style.NEGATIVE, handler))
    }

    fun setNeutralButton(title: String, handler: AlertActionHandler) = apply {
        addAction(Alert.Action(title, Alert.Action.Style.NEUTRAL, handler))
    }

    fun addActions(actions: List<Alert.Action>) = apply { this.actions.addAll(actions) }

    private fun addAction(action: Alert.Action) = apply { this.actions.add(action) }

    internal fun createAlert(): Alert {
        require(title != null || message != null) { "Please set title and/or message for the Alert" }
        require(actions.isNotEmpty()) { "Please set at least one Action for the Alert" }

        return Alert(title, message, actions)
    }
}

expect class AlertBuilder : BaseAlertBuilder
