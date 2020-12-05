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

package com.splendo.kaluga.datetimepicker

import com.splendo.kaluga.base.utils.Date
import kotlinx.coroutines.CoroutineScope

actual class DateTimePickerPresenter(
    dateTimePicker: DateTimePicker
) : BaseDateTimePickerPresenter(dateTimePicker) {

    actual class Builder : BaseDateTimePickerPresenter.Builder() {

        actual override fun create(coroutineScope: CoroutineScope): DateTimePickerPresenter {
            return DateTimePickerPresenter(createDateTimePicker())
        }
    }

    override fun showAsync(animated: Boolean, completion: (Date?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun show(animated: Boolean): Date? {
        TODO("not implemented")
    }

    override fun dismiss(animated: Boolean) {
        TODO("not implemented")
    }

    override fun dismissDateTimePicker(animated: Boolean) {
        TODO("not implemented")
    }

    override fun showDateTimePicker(animated: Boolean, completion: (Date?) -> Unit) {
        TODO("not implemented")
    }
}
