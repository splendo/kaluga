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

package com.splendo.kaluga.alerts

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribableMarker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

typealias AlertActionHandler = () -> Unit
typealias AlertTextObserver = (String) -> Unit

/**
 * An object that represents an alert with title and/or message and actions (button)
 *
 * @property title The title of the alert
 * @property message The descriptive text that provides more details
 * @property actions The list of action objects that the user can take in response to the alert
 * @property textInputAction The optional action object that sets the alert's input options and text change callback
 */
data class Alert(
    val title: String?,
    val message: String?,
    val actions: List<Action>,
    val textInputAction: TextInputAction? = null,
    val style: Style = Style.ALERT
) {

    /**
     * Alert style
     */
    enum class Style {
        ALERT, ACTION_LIST, TEXT_INPUT
    }

    /**
     * Builder class for creating an [Alert]
     */
    class Builder(private var style: Style = Style.ALERT) {
        private var title: String? = null
        private var message: String? = null
        private var actions: MutableList<Action> = mutableListOf()
        private var textInputAction: TextInputAction? = null

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
            addAction(Action(title, Action.Style.POSITIVE, handler))
        }

        /**
         * Sets button with the id `BUTTON_NEGATIVE` on Android
         * and action with style `UIAlertActionStyleCancel` on iOS
         *
         * @param title The title of the button
         * @param handler The block to execute after user taps a button
         */
        fun setNegativeButton(title: String, handler: AlertActionHandler = {}) = apply {
            addAction(Action(title, Action.Style.NEGATIVE, handler))
        }

        /**
         * Sets button with the id `BUTTON_NEUTRAL` on Android
         * and action with style `UIAlertActionStyleDestructive` on iOS
         *
         * @param title The title of the button
         * @param handler The block to execute after user taps a button
         */
        fun setNeutralButton(title: String, handler: AlertActionHandler = {}) = apply {
            addAction(Action(title, Action.Style.NEUTRAL, handler))
        }

        /**
         * Initializes alert's input field
         *
         * @param text The initial text of the input field
         * @param placeholder The input field hint
         * @param textObserver The callback for text change events of inout field
         */
        fun setTextInput(
            text: String? = null,
            placeholder: String?,
            textObserver: AlertTextObserver
        ) = apply {
            setTextInputAction(TextInputAction(text, placeholder, textObserver))
        }

        /**
         * Adds a list of [actions] to the alert
         *
         * @param actions The list of action objects
         */
        fun addActions(actions: List<Action>) = apply { this.actions.addAll(actions) }

        /**
         * Adds a list of [actions] to the alert
         *
         * @param actions The list of action objects
         */
        fun addActions(vararg actions: Action) = apply { this.actions.addAll(actions) }

        /**
         * Sets a style of the alert
         *
         * @param style The style of an alert
         */
        internal fun setStyle(style: Style) = apply { this.style = style }

        /**
         * Adds an [action] to the alert
         *
         * @param action The action object
         */
        private fun addAction(action: Action) = apply { this.actions.add(action) }

        /**
         * Adds an [Alert.TextInputAction] to the alert
         *
         * @param action The action object
         */
        private fun setTextInputAction(action: TextInputAction) =
            apply { this.textInputAction = action }

        /**
         * Creates an alert based on [title], [message], [actions] and [textInputAction] properties
         *
         * @return The alert object
         * @throws IllegalArgumentException in case missing title and/or message or actions
         */
        fun build(): Alert {
            if (style == Style.ALERT) {
                require(title != null || message != null) { "Please set title and/or message for the Alert" }
            } // Action sheet on iOS can be without title and message

            require(actions.isNotEmpty()) { "Please set at least one Action for the Alert" }

            return Alert(title, message, actions, textInputAction, style)
        }
    }

    /**
     * An action that represents a button in the alert
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

    /**
     * An action that represents an input field in the alert and its initial state
     *
     * @param text The initial text of the input field
     * @param placeholder The hint of the input field
     * @param textObserver The block to execute when the user edits the text in the input field
     */
    data class TextInputAction(
        val text: String?,
        val placeholder: String?,
        val textObserver: AlertTextObserver
    )
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
 * Abstract alert presenter, used to show and dismiss given [Alert]
 * @see [AlertPresenter]
 *
 * @property alert The alert to present (and dismiss if needed)
 */
abstract class BaseAlertPresenter(private val alert: Alert) : AlertActions {

    /**
     * Abstract alert builder class, used to create an [Alert].
     * The resulting Alert that can be shown and dismissed using an [AlertPresenter].
     *
     * @see [AlertPresenter.Builder]
     */
    abstract class Builder : LifecycleSubscribableMarker {

        /**
         * Creates the [BaseAlertPresenter] described by this builder.
         *
         * @param alert The [Alert] to be presented by the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The [BaseAlertPresenter] described by this builder.
         */
        abstract fun create(alert: Alert, coroutineScope: CoroutineScope): BaseAlertPresenter
    }

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

    protected abstract fun dismissAlert(animated: Boolean = true)

    protected abstract fun showAlert(
        animated: Boolean = true,
        afterHandler: (Alert.Action?) -> Unit = {},
        completion: () -> Unit = {}
    )
}

/**
 * Class for presenting an [Alert].
 */
expect class AlertPresenter : BaseAlertPresenter {
    class Builder : BaseAlertPresenter.Builder {
        /**
         * Creates an [AlertPresenter]
         *
         * @param alert The [Alert] to be presented with the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The created [AlertPresenter]
         */
        override fun create(alert: Alert, coroutineScope: CoroutineScope): AlertPresenter
    }
}

/**
 * Builds an alert of type [Alert.Style.ALERT] using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @param initialize The block to construct an Alert
 * @return The built alert interface object
 */
fun BaseAlertPresenter.Builder.buildAlert(
    coroutineScope: CoroutineScope,
    initialize: Alert.Builder.() -> Unit
): BaseAlertPresenter = create(
    Alert.Builder(Alert.Style.ALERT).apply {
        initialize()
    }.build(),
    coroutineScope
)

/**
 * Builds an alert of type [Alert.Style.ACTION_LIST] using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @param initialize The block to construct an Alert
 * @return The built alert interface object
 */
fun BaseAlertPresenter.Builder.buildActionSheet(
    coroutineScope: CoroutineScope,
    initialize: Alert.Builder.() -> Unit
): BaseAlertPresenter = create(
    Alert.Builder(Alert.Style.ACTION_LIST).apply {
        initialize()
    }.build(),
    coroutineScope
)

/**
 * Builds an alert of type [Alert.Style.TEXT_INPUT] using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @param initialize The block to construct an Alert
 * @return The built alert interface object
 */
fun BaseAlertPresenter.Builder.buildAlertWithInput(
    coroutineScope: CoroutineScope,
    initialize: Alert.Builder.() -> Unit
): BaseAlertPresenter = create(
    Alert.Builder(Alert.Style.TEXT_INPUT).apply {
        initialize()
    }.build(),
    coroutineScope
)
