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

package com.splendo.kaluga.test.alerts

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest
import com.splendo.kaluga.test.base.mock.matcher.AnyCaptor
import com.splendo.kaluga.test.base.mock.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class MockAlertPresenterTest : UIThreadViewModelTest<MockAlertPresenterTest.TestContext, MockAlertPresenterTest.ViewModel>() {

    class ViewModel(val alertBuilder: BaseAlertPresenter.Builder) : BaseLifecycleViewModel(alertBuilder)

    class TestContext : ViewModelTestContext<ViewModel> {
        val mockAlertBuilder = MockAlertPresenter.Builder()
        override val viewModel: ViewModel = ViewModel(mockAlertBuilder)
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> TestContext = { TestContext() }

    @Test
    fun testMockAlertPresenter() = testOnUIThread {
        val done = EmptyCompletableDeferred()
        withTimeout(2.seconds) {
            // we can use alertBuilder from our viewModel
            viewModel.alertBuilder.buildAlert(this) {
                setTitle("foo")
                setPositiveButton("OK")
            }.showAsync {
                done.complete()
            }

            val captor = AnyCaptor<Alert>()
            mockAlertBuilder.createMock.verify(captor)
            assertEquals(Alert.Style.ALERT, captor.lastCaptured?.style)
            assertEquals("foo", captor.lastCaptured?.title)
            assertEquals(1, captor.lastCaptured?.actions?.size)
            assertEquals("OK", captor.lastCaptured?.actions?.first()?.title)
            assertEquals(Alert.Action.Style.POSITIVE, captor.lastCaptured?.actions?.first()?.style)
            // **mock**AlertBuilder is available also from our context
            mockAlertBuilder.builtAlerts.first().apply {
                assertEquals(alert, captor.lastCaptured)
                closeWithAction(alert.actions.first())
            }

            done.await()
        }
    }
}
