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

package com.splendo.kaluga.test

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.datetimepicker.DateTimePickerPresenter
import com.splendo.kaluga.datetimepicker.buildTimePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

abstract class DateTimePickerPresenterTests {

    abstract val builder: DateTimePickerPresenter.Builder

    @Test
    fun testDateTimePickerBuilderExceptionNoActions() = runBlocking {
        assertFailsWith<IllegalArgumentException> {
            builder.buildTimePicker(this) {
            }
        }
        Unit
    }

    @Test
    fun testDateTimePickerBuilderExceptionNoCancelTitle() = runBlocking {
        assertFailsWith<IllegalArgumentException> {
            builder.buildTimePicker(this) {
                setConfirmButtonTitle("OK")
            }
        }
        Unit
    }

    @Test
    fun testDateTimePickerFlowCancel() = runBlocking {
        val coroutine = CoroutineScope(Dispatchers.Main).launch {
            val presenter = builder.buildTimePicker(this) {
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("Cancel")
            }

            val result = coroutineContext.run { presenter.show() }
            assertNull(result)
        }
        // On cancel call, we expect the dialog to be dismissed
        coroutine.cancel()
    }
}
