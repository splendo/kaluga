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

import com.splendo.kaluga.alerts.Alert.Action.Style
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine

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
    val style: Style = Style.ALERT,
) {

    /**
     * Alert style
     */
    enum class Style {
        /**
         * Alert that shows only a title/message and positive/neutral/negative actions.
         */
        ALERT,

        /**
         * Alert that shows a list of [Action]
         */
        ACTION_LIST,

        /**
         * Alert that shows some Text Inout
         */
        TEXT_INPUT,
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
         * @return The modified [Alert.Builder]
         */
        fun setTitle(title: String?) = apply { this.title = title }

        /**
         * Sets the [message] displayed in the alert
         *
         * @param message The message of the alert
         * @return The modified [Alert.Builder]
         */
        fun setMessage(message: String?) = apply { this.message = message }

        /**
         * Sets an [Action] with [Action.Style.POSITIVE]. If one already exists it will be replaced.
         *
         * @param title The title of the button
         * @param handler The block to execute after user taps a button
         * @return The modified [Alert.Builder]
         */
        fun setPositiveButton(title: String, handler: AlertActionHandler = {}) = apply {
            this.actions.removeAll { it.style == Action.Style.POSITIVE || it.style == Action.Style.DEFAULT }
            addAction(Action(title, Action.Style.POSITIVE, handler))
        }

        /**
         * Sets an [Action] with [Action.Style.NEGATIVE]. If one already exists it will be replaced.
         *
         * @param title The title of the button
         * @param handler The block to execute after user taps a button
         * @return The modified [Alert.Builder]
         */
        fun setNegativeButton(title: String, handler: AlertActionHandler = {}) = apply {
            this.actions.removeAll { it.style == Action.Style.NEGATIVE || it.style == Action.Style.CANCEL }
            addAction(Action(title, Action.Style.NEGATIVE, handler))
        }

        /**
         * Sets the [Action] with [Action.Style.NEUTRAL]. If one already exists it will be replaced.
         *
         * @param title The title of the button
         * @param handler The block to execute after user taps a button
         * @return The modified [Alert.Builder]
         */
        fun setNeutralButton(title: String, handler: AlertActionHandler = {}) = apply {
            this.actions.removeAll { it.style == Action.Style.NEUTRAL || it.style == Action.Style.DESTRUCTIVE }
            addAction(Action(title, Action.Style.NEUTRAL, handler))
        }

        /**
         * Initializes alert's input field
         *
         * @param text The initial text of the input field
         * @param placeholder The input field hint
         * @param textObserver The callback for text change events of inout field
         * @return The modified [Alert.Builder]
         */
        fun setTextInput(text: String? = null, placeholder: String?, textObserver: AlertTextObserver) = apply {
            setTextInputAction(TextInputAction(text, placeholder, textObserver))
        }

        /**
         * Adds a list of [Action] to the alert
         *
         * @param actions The list of [Action] objects
         * @return The modified [Alert.Builder]
         */
        fun addActions(actions: List<Action>) = apply { this.actions.addAll(actions) }

        /**
         * Adds a list of [Action] to the alert
         *
         * @param actions The list of [Action] objects
         * @return The modified [Alert.Builder]
         */
        fun addActions(vararg actions: Action) = apply { this.actions.addAll(actions) }

        /**
         * Sets a [Style] of the alert
         *
         * @param style The [Style] of an alert
         * @return The modified [Alert.Builder]
         */
        internal fun setStyle(style: Style) = apply { this.style = style }

        /**
         * Adds an [Action] to the alert
         *
         * @param action The action object
         * @return The modified [Alert.Builder]
         */
        private fun addAction(action: Action) = apply { this.actions.add(action) }

        /**
         * Adds an [Alert.TextInputAction] to the alert
         *
         * @param action The action object
         * @return The modified [Alert.Builder]
         */
        private fun setTextInputAction(action: TextInputAction) = apply { this.textInputAction = action }

        /**
         * Creates an [Alert] based on [title], [message], [actions] and [textInputAction] properties
         *
         * @return The [Alert] object
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
     * @property style The [Style] that is applied to the action's button
     * @property handler The block to execute when the user taps a button
     */
    data class Action(
        val title: String,
        val style: Style = Style.DEFAULT,
        val handler: AlertActionHandler = {},
    ) {
        /**
         * The style of an action. This determines the look of the button when displayed in the alert.
         */
        enum class Style(val value: Int) {
            /**
             * Default button. Synonymous with [POSITIVE]
             */
            DEFAULT(0),

            /**
             * Positive button. Synonymous with [DEFAULT]
             */
            POSITIVE(DEFAULT.value),

            /**
             * Destructive button. Synonymous with [NEUTRAL]
             */
            DESTRUCTIVE(1),

            /**
             * Neutral button. Synonymous with [DESTRUCTIVE]
             */
            NEUTRAL(DESTRUCTIVE.value),

            /**
             * Cancel button. Synonymous with [NEGATIVE]
             */
            CANCEL(2),

            /**
             * Negative button. Synonymous with [CANCEL]
             */
            NEGATIVE(CANCEL.value),
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
        val textObserver: AlertTextObserver,
    )
}

/**
 * Interface that defines actions that can be used for presenting an [Alert].
 */
interface AlertActions {
    /**
     * Presents an [Alert]
     *
     * @param animated Pass `true` to animate the presentation
     * @param completion The block to execute after the presentation finishes
     */
    fun showAsync(animated: Boolean = true, completion: () -> Unit = {})

    /**
     * Presents an [Alert] and suspends until completion
     *
     * @param animated Pass `true` to animate the presentation
     * @return The [Alert.Action] that was performed by button click or `null` if the alert was cancelled by the user.
     * Note that some platforms, such as iOS, may not allow the user to cancel the alert.
     */
    suspend fun show(animated: Boolean = true): Alert.Action?

    /**
     * Dismisses the currently presented [Alert]
     *
     * @param animated Pass `true` to animate the transition
     */
    fun dismiss(animated: Boolean = true)
}

/**
 * Abstract alert presenter, used to show and dismiss given [Alert]
 * @see [AlertPresenter]
 *
 * @param alert The [Alert] to present (and dismiss if needed)
 * @param logger The [Logger] to log alert actions to
 */
abstract class BaseAlertPresenter(val alert: Alert, private val logger: Logger) : AlertActions {

    companion object {
        const val TAG = "AlertDialog"
    }

    /**
     * Abstract alert builder class, used to create a [BaseAlertPresenter].
     *
     * @see [AlertPresenter.Builder]
     */
    abstract class Builder : LifecycleSubscribable {

        /**
         * Creates the [BaseAlertPresenter] described by this builder.
         *
         * @param alert The [Alert] to be presented by the built presenter.
         * @param logger The [Logger] that logs the logs of the presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The [BaseAlertPresenter] described by this builder.
         */
        abstract fun create(alert: Alert, logger: Logger = RestrictedLogger(RestrictedLogLevel.None), coroutineScope: CoroutineScope): BaseAlertPresenter
    }

    override fun showAsync(animated: Boolean, completion: () -> Unit) {
        logger.info(TAG, "Displaying alert dialog with title: ${alert.title}")
        showAlert(animated, completion = completion)
    }

    override suspend fun show(animated: Boolean): Alert.Action? = suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            dismissAlert(animated)
            continuation.tryResume(null)?.let {
                continuation.completeResume(it)
            }
        }
        logger.info(TAG, "Displaying alert dialog with title: ${alert.title}")
        showAlert(
            animated,
            afterHandler = { action ->
                // Null action is passed on cancel for both platforms
                action?.let {
                    logger.info(TAG, "Action ${it.title} was called on dialog with title: ${alert.title}")
                } ?: logger.info(TAG, "Action Cancel was called on dialog with title: ${alert.title}")
                continuation.tryResume(action)?.let {
                    continuation.completeResume(it)
                }
            },
        )
    }

    override fun dismiss(animated: Boolean) {
        dismissAlert(animated)
    }

    protected fun dismissAlertWithLog(animated: Boolean = true) {
        logger.info(TAG, "Dismissing alert dialog with title: ${alert.title}")
        dismissAlert(animated)
    }

    protected abstract fun dismissAlert(animated: Boolean = true)

    protected abstract fun showAlert(animated: Boolean = true, afterHandler: (Alert.Action?) -> Unit = {}, completion: () -> Unit = {})
}

/**
 * Class for presenting an [Alert]. Implementation of [BaseAlertPresenter]
 */
expect class AlertPresenter : BaseAlertPresenter {

    /**
     * A [BaseAlertPresenter.Builder] for creating an [AlertPresenter]
     */
    class Builder : BaseAlertPresenter.Builder {
        /**
         * Creates an [AlertPresenter]
         *
         * @param alert The [Alert] to be presented with the built presenter.
         * @param logger The [Logger] that logs the logs of the presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The created [AlertPresenter]
         */
        override fun create(alert: Alert, logger: Logger, coroutineScope: CoroutineScope): AlertPresenter
    }

    override fun showAlert(animated: Boolean, afterHandler: (Alert.Action?) -> Unit, completion: () -> Unit)
    override fun dismissAlert(animated: Boolean)
}

/**
 * Builds a [BaseAlertPresenter] of type [Alert.Style.ALERT] using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @param logger The [Logger] that logs the logs of the presenter.
 * @param initialize The block to construct an [Alert]
 * @return The built [BaseAlertPresenter]
 */
fun BaseAlertPresenter.Builder.buildAlert(
    coroutineScope: CoroutineScope,
    logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    initialize: Alert.Builder.() -> Unit,
): BaseAlertPresenter = create(
    Alert.Builder(Alert.Style.ALERT).apply {
        initialize()
    }.build(),
    logger,
    coroutineScope,
)

/**
 * Builds a [BaseAlertPresenter] of type [Alert.Style.ACTION_LIST] using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @param logger The [Logger] that logs the logs of the presenter.
 * @param initialize The block to construct an [Alert]
 * @return The built [BaseAlertPresenter]
 */
fun BaseAlertPresenter.Builder.buildActionSheet(
    coroutineScope: CoroutineScope,
    logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    initialize: Alert.Builder.() -> Unit,
): BaseAlertPresenter = create(
    Alert.Builder(Alert.Style.ACTION_LIST).apply {
        initialize()
    }.build(),
    logger,
    coroutineScope,
)

/**
 * Builds a [BaseAlertPresenter] of type [Alert.Style.TEXT_INPUT] using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @param logger The [Logger] that logs the logs of the presenter.
 * @param initialize The block to construct an [Alert]
 * @return The built [BaseAlertPresenter]
 */
fun BaseAlertPresenter.Builder.buildAlertWithInput(
    coroutineScope: CoroutineScope,
    logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    initialize: Alert.Builder.() -> Unit,
): BaseAlertPresenter = create(
    Alert.Builder(Alert.Style.TEXT_INPUT).apply {
        initialize()
    }.build(),
    logger,
    coroutineScope,
)
