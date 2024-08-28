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

package com.splendo.kaluga.datetimepicker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.uses24HourClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * A [BaseDateTimePickerPresenter] for presenting a [DateTimePicker].
 * @param dateTimePicker The [DateTimePicker] being presented.
 * @param themeResourceId the resource ID of the theme to apply to the date-time picker dialog
 * @param lifecycleManagerObserver The [LifecycleManagerObserver] to observe lifecycle changes
 * @param coroutineScope The [CoroutineScope] managing changes to the alert presentation.
 */
actual class DateTimePickerPresenter(
    dateTimePicker: DateTimePicker,
    private val themeResourceId: Int,
    private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    coroutineScope: CoroutineScope,
) : BaseDateTimePickerPresenter(dateTimePicker), CoroutineScope by coroutineScope {

    /**
     * A [BaseDateTimePickerPresenter.Builder] for creating a [DateTimePickerPresenter]
     * @param themeResourceId the resource ID of the theme to apply to the date-time picker dialog
     * @param lifecycleManagerObserver The [LifecycleManagerObserver] to observe lifecycle changes
     */
    actual class Builder(
        private val themeResourceId: Int = 0,
        private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    ) : BaseDateTimePickerPresenter.Builder(), ActivityLifecycleSubscribable by lifecycleManagerObserver {

        /**
         * Creates a [DateTimePickerPresenter]
         *
         * @param dateTimePicker The [DateTimePicker] to be presented with the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The created [DateTimePickerPresenter]
         */
        actual override fun create(dateTimePicker: DateTimePicker, coroutineScope: CoroutineScope) =
            DateTimePickerPresenter(dateTimePicker, themeResourceId, lifecycleManagerObserver, coroutineScope)
    }

    private sealed class DialogPresentation {
        data class Showing(val animated: Boolean, val completion: (KalugaDate?) -> Unit) : DialogPresentation()
        data object Hidden : DialogPresentation()
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

    actual override fun dismissDateTimePicker(animated: Boolean) {
        presentation.value = DialogPresentation.Hidden
    }

    actual override fun showDateTimePicker(animated: Boolean, completion: (KalugaDate?) -> Unit) {
        presentation.value = DialogPresentation.Showing(animated, completion)
    }

    private fun presentDialog(context: Context, presentation: DialogPresentation.Showing) {
        val alertDialog: AlertDialog = when (val type = dateTimePicker.type) {
            is DateTimePicker.Type.TimeType -> TimePickerDialog(
                context,
                themeResourceId,
                { _, hour, minute ->
                    presentation.completion(
                        dateTimePicker.selectedDate.copy().apply {
                            this.hour = hour
                            this.minute = minute
                        },
                    )
                },
                dateTimePicker.selectedDate.hour,
                dateTimePicker.selectedDate.minute,
                dateTimePicker.locale.uses24HourClock,
            )
            is DateTimePicker.Type.DateType -> {
                DatePickerDialog(
                    context,
                    themeResourceId,
                    { _, year, month, dayOfMonth ->
                        presentation.completion(
                            dateTimePicker.selectedDate.copy().apply {
                                this.year = year
                                this.month = month + 1
                                this.day = dayOfMonth
                            },
                        )
                    },
                    dateTimePicker.selectedDate.year,
                    dateTimePicker.selectedDate.month - 1,
                    dateTimePicker.selectedDate.day,
                ).apply {
                    type.earliestDate?.let {
                        datePicker.minDate = it.durationSinceEpoch.inWholeMilliseconds
                    }
                    type.latestDate?.let {
                        datePicker.maxDate = it.durationSinceEpoch.inWholeMilliseconds
                    }
                }
            }
        }
        alertDialog.apply {
            dateTimePicker.message?.let {
                setMessage(it)
            }
            setButton(
                DialogInterface.BUTTON_POSITIVE,
                dateTimePicker.confirmButtonTitle,
                this as DialogInterface.OnClickListener,
            )
            setButton(
                DialogInterface.BUTTON_NEGATIVE,
                dateTimePicker.cancelButtonTitle,
                this as DialogInterface.OnClickListener,
            )
            setOnCancelListener { presentation.completion(null) }
            setOnDismissListener {
                this@DateTimePickerPresenter.alertDialog = null
                this@DateTimePickerPresenter.presentation.value = DialogPresentation.Hidden
            }
        }
        this.alertDialog = alertDialog
        alertDialog.show()
    }
}
