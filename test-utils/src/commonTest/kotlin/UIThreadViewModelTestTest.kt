import UIThreadViewModelTestTest.CustomViewModelTestContext
import UIThreadViewModelTestTest.ViewModel
import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

class UIThreadViewModelTestTest : UIThreadViewModelTest<CustomViewModelTestContext, ViewModel>() {
    private val isDisposed = AtomicBoolean(false)
    class ViewModel : BaseViewModel() {
        var v: String = ""
    }

    class CustomViewModelTestContext(private val isDisposed: AtomicBoolean) :
        ViewModelTestContext<ViewModel>() {
        override fun dispose() {
            isDisposed.value = true
        }

        override fun createViewModel() = ViewModel()
    }

    override fun createViewModelContext(): CustomViewModelTestContext =
        CustomViewModelTestContext(isDisposed)

    @Test
    fun testMainThreadViewModelTest() = testWithViewModel {
        assertEquals("", viewModel.v)
        viewModel.v = "foo" // should not crash on native since we are on the right thread
    }

    @Test
    fun testMainThreadViewModelTestException() {
        try {
            testWithViewModel {
                error("Expected error for testing")
            }
            fail("An exception should have been thrown")
        } catch (t: Throwable) {
            assertEquals("Expected error for testing", t.message)
        }
    }

    @AfterTest
    fun testDisposed() {
        assertTrue(isDisposed.value)
    }
}
