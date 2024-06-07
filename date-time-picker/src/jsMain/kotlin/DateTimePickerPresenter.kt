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

import com.splendo.kaluga.base.utils.KalugaDate
import kotlinx.coroutines.CoroutineScope

/**
 * A [DateTimePickerPresenter] for presenting a [DateTimePicker].
 *
 * This is not yet fully implemented on JVM
 *
 * @param dateTimePicker The [DateTimePicker] being presented.
 */
actual class DateTimePickerPresenter(
    dateTimePicker: DateTimePicker,
) : BaseDateTimePickerPresenter(dateTimePicker) {

    /**
     * A [BaseDateTimePickerPresenter.Builder] for creating a [DateTimePickerPresenter]
     */
    actual class Builder : BaseDateTimePickerPresenter.Builder() {

        /**
         * Creates a [DateTimePickerPresenter]
         *
         * @param dateTimePicker The [DateTimePicker] to be presented with the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The created [DateTimePickerPresenter]
         */
        actual override fun create(dateTimePicker: DateTimePicker, coroutineScope: CoroutineScope): DateTimePickerPresenter {
            return DateTimePickerPresenter(dateTimePicker)
        }
    }

    override fun showAsync(animated: Boolean, completion: (KalugaDate?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun show(animated: Boolean): KalugaDate? {
        TODO("not implemented")
    }

    override fun dismiss(animated: Boolean) {
        TODO("not implemented")
    }

    actual override fun dismissDateTimePicker(animated: Boolean) {
        TODO("not implemented")
    }

    actual override fun showDateTimePicker(animated: Boolean, completion: (KalugaDate?) -> Unit) {
        TODO("not implemented")
    }
}
