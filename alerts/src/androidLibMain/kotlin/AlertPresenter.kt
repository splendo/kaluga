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

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import com.splendo.kaluga.base.utils.applyIf
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.resources.dpToPixel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * A [BaseAlertPresenter] for presenting an [Alert].
 * @param alert The [Alert] being presented.
 * @param lifecycleManagerObserver The [LifecycleManagerObserver] to observe lifecycle changes
 * @param coroutineScope The [CoroutineScope] managing changes to the alert presentation.
 */
actual class AlertPresenter(
    private val alert: Alert,
    private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    private val logger: Logger,
    coroutineScope: CoroutineScope,
) : BaseAlertPresenter(alert), CoroutineScope by coroutineScope {

    /**
     * A [BaseAlertPresenter.Builder] for creating an [AlertPresenter]
     * @param lifecycleManagerObserver The [LifecycleManagerObserver] to observe lifecycle changes
     */
    actual class Builder(
        private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
        private val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    ) : BaseAlertPresenter.Builder(), ActivityLifecycleSubscribable by lifecycleManagerObserver {

        /**
         * Creates an [AlertPresenter]
         *
         * @param alert The [Alert] to be presented with the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The created [AlertPresenter]
         */
        actual override fun create(alert: Alert, coroutineScope: CoroutineScope) =
            AlertPresenter(alert, lifecycleManagerObserver, logger, coroutineScope)
    }

    private companion object {
        fun transform(style: Alert.Action.Style): Int = when (style) {
            Alert.Action.Style.DEFAULT, Alert.Action.Style.POSITIVE -> AlertDialog.BUTTON_POSITIVE
            Alert.Action.Style.DESTRUCTIVE, Alert.Action.Style.NEUTRAL -> AlertDialog.BUTTON_NEUTRAL
            Alert.Action.Style.CANCEL, Alert.Action.Style.NEGATIVE -> AlertDialog.BUTTON_NEGATIVE
        }
    }

    private sealed class DialogPresentation {
        data class Showing(
            val animated: Boolean,
            val afterHandler: (Alert.Action?) -> Unit,
            val completion: () -> Unit,
        ) : DialogPresentation()

        object Hidden : DialogPresentation()
    }

    private val presentation = MutableStateFlow<DialogPresentation>(DialogPresentation.Hidden)
    private var alertDialog: AlertDialog? = null

    init {
        launch {
            combine(
                lifecycleManagerObserver.managerState,
                presentation,
            ) { managerState, dialogPresentation ->
                Pair(managerState, dialogPresentation)
            }.collect { contextPresentation ->
                when (val dialogPresentation = contextPresentation.second) {
                    is DialogPresentation.Showing -> contextPresentation.first?.activity?.let {
                        logger.info(TAG, "Displaying alert dialog with title: ${alert.title}")
                        presentDialog(it, dialogPresentation)
                    } ?: run { alertDialog = null }

                    is DialogPresentation.Hidden -> {
                        logger.info(TAG, "Dismissing alert dialog with title: ${alert.title}")
                        alertDialog?.dismiss()
                    }
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
        completion: () -> Unit,
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
                    val action = alert.actions[which].apply {
                        logger.info(TAG, "Action ${this.title} was called on dialog with title: ${alert.title}")
                        handler()
                    }
                    presentation.afterHandler(action)
                }
            }
            .create()
            .applyIf(alert.style == Alert.Style.TEXT_INPUT) {
                alert.textInputAction?.let { textInputAction ->
                    val editText = createEditTextView(
                        context,
                        textInputAction,
                    )
                    setView(editText)
                }
                alert.actions.forEach { action ->
                    setButton(transform(action.style), action.title) { _, _ ->
                        logger.info(TAG, "Action ${action.title} was called on dialog with title: ${alert.title}")
                        action.handler()
                        presentation.afterHandler(action)
                    }
                }
            }
            .applyIf(alert.style == Alert.Style.ALERT) {
                alert.actions.forEach { action ->
                    setButton(transform(action.style), action.title) { _, _ ->
                        logger.info(TAG, "Action ${action.title} was called on dialog with title: ${alert.title}")
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
                setOnCancelListener {
                    logger.info(TAG, "Canceling alert dialog with title: ${alert.title}")
                    presentation.afterHandler(null)
                }
                show()
            }
        presentation.completion()
    }

    /**
     * Creates an [EditText] wrapped in a [LinearLayout] for dialogs of type [Alert.Style.TEXT_INPUT]
     *
     * @param context The Android context used to create view
     * @param textInputAction The [Alert.TextInputAction] used for initializing the [EditText]
     */
    private fun createEditTextView(
        context: Context,
        textInputAction: Alert.TextInputAction,
    ): LinearLayout {
        val linearLayout = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        val editText = EditText(context)
        editText.layoutParams = layoutParams
        linearLayout.addView(editText)
        val padding =
            context.resources.getDimension(R.dimen.dialog_text_input_padding).dpToPixel(context).toInt()
        linearLayout.setPaddingRelative(padding, 0, padding, 0)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
                // Do nothing
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                textInputAction.textObserver(s.toString())
            }
        })
        textInputAction.placeholder?.let { editText.hint = it }
        textInputAction.text?.let { editText.setText(it) }
        return linearLayout
    }
}
