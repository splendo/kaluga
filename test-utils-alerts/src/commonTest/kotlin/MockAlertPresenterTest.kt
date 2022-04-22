import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.test.alerts.MockAlertPresenter
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test

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

class MockAlertPresenterTest : UIThreadViewModelTest<MockAlertPresenterTest.CustomViewModelTestContext, MockAlertPresenterTest.MyViewModel>() {

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