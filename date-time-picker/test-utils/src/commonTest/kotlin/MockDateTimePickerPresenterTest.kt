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

package com.splendo.kaluga.keyboard.test

import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.enUsPosix
import com.splendo.kaluga.datetimepicker.DateTimePicker
import com.splendo.kaluga.datetimepicker.buildDatePicker
import com.splendo.kaluga.base.test.UIThreadTest
import com.splendo.kaluga.base.test.mock.matcher.AnyCaptor
import com.splendo.kaluga.base.test.mock.verify
import com.splendo.kaluga.datetimepicker.test.MockDateTimePickerPresenter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class MockDateTimePickerPresenterTest : UIThreadTest<MockDateTimePickerPresenterTest.TestContext>() {

    class TestContext : UIThreadTest.TestContext {
        val mockDateTimePickerPresenterBuilder = MockDateTimePickerPresenter.Builder()
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> TestContext = { TestContext() }

    @Test
    fun testMockDateTimePickerPresenter() = testOnUIThread {
        val earliest = DefaultKalugaDate.epoch()
        val latest = DefaultKalugaDate.now()
        val selected = DefaultKalugaDate.epoch(568623600000.milliseconds)

        val done = CompletableDeferred<KalugaDate?>()
        withTimeout(2.seconds) {
            // we can use date time picker from our viewModel
            mockDateTimePickerPresenterBuilder.buildDatePicker(this, earliest, latest) {
                setMessage("Hello world")
                setSelectedDate(selected)
                setConfirmButtonTitle("OK")
                setCancelButtonTitle("Cancel")
                setLocale(KalugaLocale.enUsPosix)
            }.showAsync {
                done.complete(it)
            }

            val captor = AnyCaptor<DateTimePicker>()
            mockDateTimePickerPresenterBuilder.createMock.verify(captor)
            assertEquals(DateTimePicker.Type.DateType(earliest, latest), captor.lastCaptured?.type)
            assertEquals(selected, captor.lastCaptured?.selectedDate)
            assertEquals("Hello world", captor.lastCaptured?.message)
            assertEquals("OK", captor.lastCaptured?.confirmButtonTitle)
            assertEquals("Cancel", captor.lastCaptured?.cancelButtonTitle)
            assertEquals(KalugaLocale.enUsPosix, captor.lastCaptured?.locale)

            // **mock**AlertBuilder is available also from our context
            mockDateTimePickerPresenterBuilder.builtDateTimePickerPresenters.first().apply {
                assertEquals(dateTimePicker, captor.lastCaptured)
                closeWithDate(latest)
            }

            assertEquals(latest, done.await())
        }
    }
}
