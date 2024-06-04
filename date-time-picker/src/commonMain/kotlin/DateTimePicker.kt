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

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.KalugaLocale.Companion.defaultLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * An object that represents a date-time picker view
 *
 * @property message (optional) message to show in the date-time picker
 * @property cancelButtonTitle the text to show in the button to cancel picking a date
 * @property confirmButtonTitle the text to show in the button to confirm picking a date
 * @property type the [Type] of date time picker
 * @property locale the [KalugaLocale] used for representing time in the date-time picker
 * @property selectedDate the [KalugaDate] that the date-time picker will use as a base for picking the date.
 * This will determine the initial time/day to be selected in the picker, depending on the [type].
 * Upon completion, a copy of this date modified with the selected date or time will be provided, keeping timezone and components not selected the same.
 */
data class DateTimePicker(
    val message: String?,
    val cancelButtonTitle: String,
    val confirmButtonTitle: String,
    val type: Type,
    val locale: KalugaLocale,
    val selectedDate: KalugaDate,
) {

    /**
     * Date Type to select
     */
    sealed class Type {
        /**
         * Selects a Date
         * A range can be provided to limit the dates selectable
         *
         * @property earliestDate if provided, no date can be picked that is before this [KalugaDate]
         * @property latestDate if provided, no date can be picked that is after this [KalugaDate]
         */
        data class DateType(
            val earliestDate: KalugaDate? = null,
            val latestDate: KalugaDate? = null,
        ) : Type() {

            internal fun adjustDate(date: KalugaDate) = when {
                earliestDate != null && earliestDate > date -> earliestDate
                latestDate != null && latestDate < date -> latestDate
                else -> date
            }
        }

        /**
         * Selects a Time
         */
        data object TimeType : Type()
    }

    /**
     * Builder class for creating a [DateTimePicker]
     * @param type the [Type] of [DateTimePicker] to build.
     */
    class Builder(private val type: Type = Type.TimeType) {
        private var message: String? = null
        private var cancelButtonTitle: String = ""
        private var confirmButtonTitle: String = ""
        private var locale: KalugaLocale = defaultLocale
        private var selectedDate: KalugaDate = when (type) {
            is Type.DateType -> type.adjustDate(
                DefaultKalugaDate.now().apply {
                    hour = 0
                    minute = 0
                    second = 0
                    millisecond = 0
                },
            )
            is Type.TimeType -> DefaultKalugaDate.epoch().apply {
                val now = DefaultKalugaDate.now()
                hour = now.hour
                minute = now.minute
                second = 0
                millisecond = 0
            }
        }

        /**
         * Sets the [message] displayed in the DateTimePicker
         *
         * @param message The message of the DateTimePicker
         */
        fun setMessage(message: String?) = apply { this.message = message }

        /**
         * Sets the text to show in the DateTimePicker button to cancel picking a date
         *
         * @param cancelButtonTitle the text to show in the cancel button
         */
        fun setCancelButtonTitle(cancelButtonTitle: String) = apply { this.cancelButtonTitle = cancelButtonTitle }

        /**
         * Sets the text to show in the DateTimePicker button to confirm picking a date
         *
         * @param confirmButtonTitle the text to show in the confirm button
         */
        fun setConfirmButtonTitle(confirmButtonTitle: String) = apply { this.confirmButtonTitle = confirmButtonTitle }

        /**
         * Sets the [KalugaLocale] for which a Date is selected
         *
         * @param locale the [KalugaLocale] for which the date is selected
         */
        fun setLocale(locale: KalugaLocale) = apply { this.locale = locale }

        /**
         * Sets the [KalugaDate] that the date-time picker will use as a base for picking the date.
         * This will determine the initial time/day to be selected in the picker, depending on the [type].
         * Upon completion, a copy of this date modified with the selected date or time will be provided, keeping timezone and components not selected the same.
         *
         * @param date the date to use as a basis for selection.
         */
        fun setSelectedDate(date: KalugaDate) = apply {
            this.selectedDate = when (type) {
                is Type.DateType -> type.adjustDate(date)
                is Type.TimeType -> date
            }
        }

        /**
         * Creates a [DateTimePicker]
         *
         * @return The [DateTimePicker] object
         * @throws IllegalArgumentException in case missing cancel or confirm titles
         */
        fun build(): DateTimePicker {
            require(cancelButtonTitle.isNotEmpty() && confirmButtonTitle.isNotEmpty()) { "Please set Cancel and Confirm Titles" }

            return DateTimePicker(message, cancelButtonTitle, confirmButtonTitle, type, locale, selectedDate)
        }
    }
}

/**
 * Interface that defines actions that can used to present a [DateTimePicker].
 */
interface DateTimePickerActions {
    /**
     * Presents a [DateTimePicker]
     *
     * @param animated Pass `true` to animate the presentation
     * @param completion The callback invoked when a [KalugaDate] is selected or the dialog is cancelled
     */
    fun showAsync(animated: Boolean = true, completion: (KalugaDate?) -> Unit = {})

    /**
     * Presents a [DateTimePicker] and suspends until completion
     *
     * @param animated Pass `true` to animate the presentation
     * @return The [KalugaDate] that was selected or `null` if the DateTimePicker was cancelled
     */
    suspend fun show(animated: Boolean = true): KalugaDate?

    /**
     * Dismisses the currently presented [DateTimePicker].
     *
     * @param animated Pass `true` to animate the transition
     */
    fun dismiss(animated: Boolean)
}

/**
 * Abstract DateTimePicker presenter, used to show and dismiss given [DateTimePicker]
 * @see [DateTimePickerPresenter]
 *
 * @param dateTimePicker The [DateTimePicker] to present (and dismiss if needed)
 */
abstract class BaseDateTimePickerPresenter(protected open val dateTimePicker: DateTimePicker) : DateTimePickerActions {

    /**
     * Abstract alert builder class, used to create a [BaseDateTimePickerPresenter].
     *
     * @see [DateTimePickerPresenter.Builder]
     */
    abstract class Builder : LifecycleSubscribable {

        /**
         * Creates the [BaseDateTimePickerPresenter] described by this builder.
         *
         * @param dateTimePicker The [DateTimePicker] to be presented with the built presenter
         * @param coroutineScope The [CoroutineScope] managing the date-time picker lifecycle.
         * @return The [BaseDateTimePickerPresenter] described by this builder.
         */
        abstract fun create(dateTimePicker: DateTimePicker, coroutineScope: CoroutineScope): BaseDateTimePickerPresenter
    }

    override fun showAsync(animated: Boolean, completion: (KalugaDate?) -> Unit) {
        showDateTimePicker(animated, completion = completion)
    }

    override suspend fun show(animated: Boolean): KalugaDate? = suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            dismissDateTimePicker(animated)
            continuation.tryResume(null)?.let {
                continuation.completeResume(it)
            }
        }
        showDateTimePicker(animated) { pickedDate ->
            continuation.tryResume(pickedDate)?.let {
                continuation.completeResume(it)
            }
        }
    }

    override fun dismiss(animated: Boolean) {
        dismissDateTimePicker(animated)
    }

    protected abstract fun dismissDateTimePicker(animated: Boolean = true)

    protected abstract fun showDateTimePicker(animated: Boolean = true, completion: (KalugaDate?) -> Unit = {})
}

/**
 * Class for presenting a [DateTimePicker]. Implementation of [BaseDateTimePickerPresenter]
 */
expect class DateTimePickerPresenter : BaseDateTimePickerPresenter {

    /**
     * A [BaseDateTimePickerPresenter.Builder] for creating a [DateTimePickerPresenter]
     */
    class Builder : BaseDateTimePickerPresenter.Builder {
        /**
         * Creates a [DateTimePickerPresenter]
         *
         * @param dateTimePicker The [DateTimePicker] to be presented with the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The created [DateTimePickerPresenter]
         */
        override fun create(dateTimePicker: DateTimePicker, coroutineScope: CoroutineScope): DateTimePickerPresenter
    }

    override fun showDateTimePicker(animated: Boolean, completion: (KalugaDate?) -> Unit)
    override fun dismissDateTimePicker(animated: Boolean)
}

/**
 * Builds a date picker using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @property earliestDate if provided, no date can be picked that is before this [KalugaDate]
 * @property latestDate if provided, no date can be picked that is after this [KalugaDate]
 * @param initialize The block to construct a [DateTimePicker] with type [DateTimePicker.Type.DateType]
 * @return The built alert interface object
 */
fun BaseDateTimePickerPresenter.Builder.buildDatePicker(
    coroutineScope: CoroutineScope,
    earliestDate: KalugaDate? = null,
    latestDate: KalugaDate? = null,
    initialize: DateTimePicker.Builder.() -> Unit,
): BaseDateTimePickerPresenter = create(
    DateTimePicker.Builder(DateTimePicker.Type.DateType(earliestDate, latestDate)).apply {
        initialize()
    }.build(),
    coroutineScope,
)

/**
 * Builds a time picker using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @param initialize The block to construct a [DateTimePicker] with type [DateTimePicker.Type.TimeType]
 * @return The built alert interface object
 */
fun BaseDateTimePickerPresenter.Builder.buildTimePicker(coroutineScope: CoroutineScope, initialize: DateTimePicker.Builder.() -> Unit): BaseDateTimePickerPresenter = create(
    DateTimePicker.Builder(DateTimePicker.Type.TimeType).apply {
        initialize()
    }.build(),
    coroutineScope,
)
