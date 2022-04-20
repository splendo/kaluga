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

package com.splendo.kaluga.test.architecture

import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.CustomUIThreadViewModelTestTest.CustomViewModelTestContext
import com.splendo.kaluga.test.CustomUIThreadViewModelTestTest.MyViewModel
import com.splendo.kaluga.test.LazyUIThreadViewModelTestTest.CustomLazyViewModelTestContext
import com.splendo.kaluga.test.LazyUIThreadViewModelTestTest.ViewModel
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest
import com.splendo.kaluga.test.mock.alerts.MockAlertPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withTimeout
import kotlin.test.*
import kotlin.time.seconds

class LazyUIThreadViewModelTestTest : UIThreadViewModelTest<CustomLazyViewModelTestContext, ViewModel>() {

    companion object {
        val isDisposed = AtomicBoolean(false)
    }

    class ViewModel : BaseViewModel() {
        var v: String = ""
    }

    class CustomLazyViewModelTestContext(coroutineScope: CoroutineScope) :
        LazyViewModelTestContext<ViewModel>(coroutineScope, { ViewModel() }) {

        override fun dispose() {
            isDisposed.value = true
        }
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> CustomLazyViewModelTestContext =
        { CustomLazyViewModelTestContext(it) }

    @Test
    fun testMainThreadViewModelTest() = testOnUIThread {
        assertEquals("", viewModel.v)
        viewModel.v = "foo" // should not crash on native since we are on the right thread
    }

    @Test
    fun testMainThreadViewModelTestException() {
        try {
            testOnUIThread {
                error("Expected error for testing")
            }
            fail("An exception should have been thrown")
        } catch (t: Throwable) {
            assertEquals("Expected error for testing", t.message)
        }
    }

    @BeforeTest
    fun resetDisposed() {
        isDisposed.value = false
    }

    @AfterTest
    fun testDisposed() {
        assertTrue(isDisposed.value)
    }
}

class CustomUIThreadViewModelTestTest : UIThreadViewModelTest<CustomViewModelTestContext, MyViewModel>() {

    class MyViewModel(val alertBuilder: BaseAlertPresenter.Builder) : BaseViewModel()

    class CustomViewModelTestContext : ViewModelTestContext<MyViewModel> {
        val mockAlertBuilder = MockAlertPresenter.Builder()
        override val viewModel: MyViewModel = MyViewModel(mockAlertBuilder)
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> CustomViewModelTestContext = { CustomViewModelTestContext() }

    @Test
    fun testCustomUIThreadViewModelTest() = testOnUIThread {

        val done = EmptyCompletableDeferred()
        withTimeout(2.seconds) {
            // we can use alertBuilder from our viewModel
            viewModel.alertBuilder.buildAlert(this) {
                setTitle("foo")
                setPositiveButton("OK")
            }.showAsync {
                done.complete()
            }

            // **mock**AlertBuilder is available also from our context
            mockAlertBuilder.builtAlerts.first().apply {
                closeWithAction(alert.actions.first())
            }

            done.await()
        }
    }
}