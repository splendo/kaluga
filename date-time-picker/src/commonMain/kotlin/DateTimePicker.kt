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
import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.utils.Locale.Companion.defaultLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

data class DateTimePicker(
    val message: String?,
    val cancelButtonTitle: String,
    val confirmButtonTitle: String,
    val type: Type,
    val locale: Locale,
    val selectedDate: KalugaDate
) {

    /**
     * Date Type to select
     */
    sealed class Type {
        /**
         * Selects a Date
         * A range can be provided to limit the dates selectable
         */
        class DateType(
            val earliestDate: KalugaDate? = null,
            val latestDate: KalugaDate? = null
        ) : Type()

        /**
         * Selects a Time
         */
        object TimeType : Type()
    }

    class Builder(private var type: Type = Type.TimeType) {
        private var message: String? = null
        private var cancelButtonTitle: String = ""
        private var confirmButtonTitle: String = ""
        private var locale: Locale = defaultLocale
        private var selectedDate: KalugaDate = DefaultKalugaDate.epoch()

        /**
         * Sets the [message] displayed in the DateTimePicker
         *
         * @param message The message of the alert
         */
        fun setMessage(message: String?) = apply { this.message = message }

        /**
         * Sets this [cancelButtonTitle] shown in the DateTimePicker
         */
        fun setCancelButtonTitle(cancelButtonTitle: String) = apply { this.cancelButtonTitle = cancelButtonTitle }

        /**
         * Sets this [cancelButtonTitle] shown in the DateTimePicker
         */
        fun setConfirmButtonTitle(confirmButtonTitle: String) = apply { this.confirmButtonTitle = confirmButtonTitle }

        /**
         * Sets the Locale for which a Date is selected
         */
        fun setLocale(locale: Locale) = apply { this.locale = locale }

        fun setSelectedDate(date: KalugaDate) = apply { this.selectedDate = date }

        /**
         * Sets a style of the alert
         *
         * @param type The style of an alert
         */
        internal fun setType(type: Type) = apply { this.type = type }

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
 * Interface that defines actions that can be applied to the alert.
 */
interface DateTimePickerActions {
    /**
     * Presents an DateTimePicker
     *
     * @param animated Pass `true` to animate the presentation
     * @param completion The callback invoked when a Date is selected or the dialog is cancelled
     */
    fun showAsync(animated: Boolean = true, completion: (KalugaDate?) -> Unit = {})

    /**
     * Presents an DateTimePicker and suspends
     *
     * @param animated
     * @return The [KalugaDate] that was selected or `null` if the DateTimePicker was cancelled
     */
    suspend fun show(animated: Boolean = true): KalugaDate?

    /**
     * Dismisses the DateTimePicker, which was presented previously
     *
     * @param animated Pass `true` to animate the transition
     */
    fun dismiss(animated: Boolean)
}

/**
 * Abstract DateTimePicker presenter, used to show and dismiss given [DateTimePicker]
 * @see [DateTimePickerPresenter]
 *
 * @param dateTimePicker The alert to present (and dismiss if needed)
 */
abstract class BaseDateTimePickerPresenter(private val dateTimePicker: DateTimePicker) : DateTimePickerActions {

    /**
     * Abstract alert builder class, used to create an [DateTimePicker].
     * The resulting DateTimePicker that can be shown and dismissed using an [DateTimePickerPresenter].
     *
     * @see [DateTimePickerPresenter.Builder]
     */
    abstract class Builder : LifecycleSubscribable {

        /**
         * Creates the [BaseDateTimePickerPresenter] described by this builder.
         *
         * @param dateTimePicker The [DateTimePicker] to be presented with the built presenter
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The [BaseDateTimePickerPresenter] described by this builder.
         */
        abstract fun create(dateTimePicker: DateTimePicker, coroutineScope: CoroutineScope): BaseDateTimePickerPresenter
    }

    override fun showAsync(animated: Boolean, completion: (KalugaDate?) -> Unit) {
        showDateTimePicker(animated, completion = completion)
    }

    override suspend fun show(animated: Boolean): KalugaDate? =
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                dismissDateTimePicker(animated)
                continuation.resume(null)
            }
            showDateTimePicker(animated) { continuation.resume(it) }
        }

    override fun dismiss(animated: Boolean) {
        dismissDateTimePicker(animated)
    }

    protected abstract fun dismissDateTimePicker(animated: Boolean = true)

    protected abstract fun showDateTimePicker(
        animated: Boolean = true,
        completion: (KalugaDate?) -> Unit = {}
    )
}

/**
 * Class for presenting an [DateTimePicker].
 */
expect class DateTimePickerPresenter : BaseDateTimePickerPresenter {
    class Builder : BaseDateTimePickerPresenter.Builder {
        /**
         * Creates DateTimePickerPresenter object
         *
         * @param dateTimePicker The [DateTimePicker] to be presented with the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The DateTimePickerPresenter object
         */
        override fun create(dateTimePicker: DateTimePicker, coroutineScope: CoroutineScope): DateTimePickerPresenter
    }
}

/**
 * Builds an alert using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @param initialize The block to construct an [DateTimePicker]
 * @return The built alert interface object
 */
fun BaseDateTimePickerPresenter.Builder.buildDatePicker(
    coroutineScope: CoroutineScope,
    earliestDate: KalugaDate? = null,
    latestDate: KalugaDate? = null,
    initialize: DateTimePicker.Builder.() -> Unit
): BaseDateTimePickerPresenter = create(
    DateTimePicker.Builder(DateTimePicker.Type.DateType(earliestDate, latestDate)).apply {
        initialize()
    }.build(),
    coroutineScope
)

/**
 * Builds an alert using DSL syntax (thread safe)
 *
 * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
 * @param initialize The block to construct an Alert
 * @return The built alert interface object
 */
fun BaseDateTimePickerPresenter.Builder.buildTimePicker(
    coroutineScope: CoroutineScope,
    initialize: DateTimePicker.Builder.() -> Unit
): BaseDateTimePickerPresenter = create(
    DateTimePicker.Builder(DateTimePicker.Type.TimeType).apply {
        initialize()
    }.build(),
    coroutineScope
)
