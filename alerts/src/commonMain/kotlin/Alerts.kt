package com.splendo.kaluga.alerts

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

/**
 * An object that represents an alert with title and/or message and actions (button)
 *
 * @property title The title of the alert
 * @property message The descriptive text that provides more details
 * @property actions The list of action objects that the user can take in response to the alert
 */
data class Alert(
    val title: String?,
    val message: String?,
    val actions: List<Action>,
    val style: Style = Style.ALERT
) {

    /**
     * Alert style
     */
    enum class Style {
        ALERT, ACTION_LIST
    }

    /**
     * An action than represents a button in the alert
     *
     * @property title The title of the action's button
     * @property style The style that is applied to the action's button
     * @property handler The block to execute when the user taps a button
     */
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

/**
 * Interface that defines actions that can be applied to the alert.
 */
interface AlertActions {
    /**
     * Presents an alert
     *
     * @param animated Pass `true` to animate the presentation
     * @param completion The block to execute after the presentation finishes
     */
    fun showAsync(animated: Boolean = true, completion: () -> Unit = {})

    /**
     * Presents an alert and suspends
     *
     * @param animated
     * @return The action that was performed by button click
     *         or `null` if the alert was cancelled by pressing back (on Android)
     */
    suspend fun show(animated: Boolean = true): Alert.Action?

    /**
     * Dismisses the alert, which was presented previously
     *
     * @param animated Pass `true` to animate the transition
     */
    fun dismiss(animated: Boolean = true)
}

/**
 * Base alert presenter class, which used to show and dismiss given [alert]
 * Abstract methods should be implemented on platform-specific side
 *
 * @property alert The alert to present (and dismiss if needed)
 */
abstract class BaseAlertPresenter(private val alert: Alert) : AlertActions {

    override fun showAsync(animated: Boolean, completion: () -> Unit) {
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

    internal abstract fun dismissAlert(animated: Boolean = true)

    internal abstract fun showAlert(
        animated: Boolean = true,
        afterHandler: (Alert.Action?) -> Unit = {},
        completion: () -> Unit = {}
    )
}

expect class AlertInterface : BaseAlertPresenter

/**
 * Base alert builder class, which used to create an alert, which can be shown and dismissed
 * later on using AlertInterface object
 *
 * @see AlertInterface
 */
abstract class BaseAlertBuilder {

    private var title: String? = null
    private var message: String? = null
    private var actions: MutableList<Alert.Action> = mutableListOf()
    private var style: Alert.Style = Alert.Style.ALERT
    private val mutex = Mutex()

    /**
     * Sets the [title] displayed in the alert
     *
     * @param title The title of the alert
     */
    fun setTitle(title: String?) = apply { this.title = title }

    /**
     * Sets the [message] displayed in the alert
     *
     * @param message The message of the alert
     */
    fun setMessage(message: String?) = apply { this.message = message }

    /**
     * Sets button with the id `BUTTON_POSITIVE` on Android
     * and action with style `UIAlertActionStyleDefault` on iOS
     *
     * @param title The title of the button
     * @param handler The block to execute after user taps a button
     */
    fun setPositiveButton(title: String, handler: AlertActionHandler = {}) = apply {
        addAction(Alert.Action(title, Alert.Action.Style.POSITIVE, handler))
    }

    /**
     * Sets button with the id `BUTTON_NEGATIVE` on Android
     * and action with style `UIAlertActionStyleCancel` on iOS
     *
     * @param title The title of the button
     * @param handler The block to execute after user taps a button
     */
    fun setNegativeButton(title: String, handler: AlertActionHandler = {}) = apply {
        addAction(Alert.Action(title, Alert.Action.Style.NEGATIVE, handler))
    }

    /**
     * Sets button with the id `BUTTON_NEUTRAL` on Android
     * and action with style `UIAlertActionStyleDestructive` on iOS
     *
     * @param title The title of the button
     * @param handler The block to execute after user taps a button
     */
    fun setNeutralButton(title: String, handler: AlertActionHandler = {}) = apply {
        addAction(Alert.Action(title, Alert.Action.Style.NEUTRAL, handler))
    }

    /**
     * Adds a list of [actions] to the alert
     *
     * @param actions The list of action objects
     */
    fun addActions(actions: List<Alert.Action>) = apply { this.actions.addAll(actions) }

    /**
     * Adds a list of [actions] to the alert
     *
     * @param actions The list of action objects
     */
    fun addActions(vararg actions: Alert.Action) = apply { this.actions.addAll(actions) }

    /**
     * Sets a style of the alert
     *
     * @param style The style of an alert
     */
    private fun setStyle(style: Alert.Style) = apply { this.style = style }

    /**
     * Builds an alert using DSL syntax (thread safe)
     *
     * @param initialize The block to construct an Alert
     * @return The built alert interface object
     */
    suspend fun buildAlert(initialize: BaseAlertBuilder.() -> Unit): AlertInterface = mutex.withLock(this) {
        buildAlertUnsafe(initialize)
    }

    /**
     * Builds an alert using DSL syntax (not thread safe)
     *
     * @param initialize The block to construct an Alert
     * @return The built alert interface object
     */
    fun buildAlertUnsafe(initialize: BaseAlertBuilder.() -> Unit): AlertInterface {
        reset()
        setStyle(Alert.Style.ALERT)
        initialize()
        return create()
    }

    /**
     * Builds an alert using DSL syntax (thread safe)
     *
     * @param initialize The block to construct an Alert
     * @return The built alert interface object
     */
    suspend fun buildActionSheet(initialize: BaseAlertBuilder.() -> Unit): AlertInterface = mutex.withLock(this) {
        buildActionSheetUnsafe(initialize)
    }

    /**
     * Builds an alert using DSL syntax (not thread safe)
     *
     * @param initialize The block to construct an Alert
     * @return The built alert interface object
     */
    fun buildActionSheetUnsafe(initialize: BaseAlertBuilder.() -> Unit): AlertInterface {
        reset()
        setStyle(Alert.Style.ACTION_LIST)
        initialize()
        return create()
    }

    /**
     * Adds an [action] to the alert
     *
     * @param action The action object
     */
    private fun addAction(action: Alert.Action) = apply { this.actions.add(action) }

    /**
     * Reset builder into initial state
     */
    private fun reset() = apply {
        this.title = null
        this.message = null
        this.actions.clear()
        this.style = Alert.Style.ALERT
    }

    /**
     * Creates an alert based on [title], [message] and [actions] properties
     *
     * @return The alert object
     * @throws IllegalArgumentException in case missing title and/or message or actions
     */
    internal fun createAlert(): Alert {
        if (style == Alert.Style.ALERT) {
            require(title != null || message != null) { "Please set title and/or message for the Alert" }
        } // Action sheet on iOS can be without title and message

        require(actions.isNotEmpty()) { "Please set at least one Action for the Alert" }

        return Alert(title, message, actions, style)
    }

    /**
     * Creates AlertInterface object
     *
     * @return The AlertInterface object
     */
    internal abstract fun create(): AlertInterface
}

expect class AlertBuilder : BaseAlertBuilder
