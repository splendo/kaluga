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

package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.test.architecture.KoinUIThreadViewModelTest
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest
import com.splendo.kaluga.test.mock.alerts.MockAlertPresenter
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KoinUIThreadViewModelTestTest :
    UIThreadViewModelTest<KoinUIThreadViewModelTestTest.TestContext, KoinUIThreadViewModelTestTest.KoinViewModel>() {

    class KoinViewModel : BaseViewModel(), KoinComponent {
        val s: String by inject() // test injecting into ViewModel
    }

    inner class TestContext :
        KoinUIThreadViewModelTest.KoinViewModelTestContext<KoinViewModel>(
            {
                logger(PrintLogger()) // not the default
            },
            module {
                single { "S" }
                single<BaseAlertPresenter.Builder> { MockAlertPresenter.Builder() }
            }
        ),
        KoinComponent {
        override fun createViewModel(): KoinViewModel = KoinViewModel()

        val builder: BaseAlertPresenter.Builder by inject() // test injecting into context
    }

    override fun createViewModelContext(): TestContext = TestContext()

    @Test
    fun testKoinViewModelTestContext() = testWithViewModel {
        assertEquals("S", viewModel.s)
        assertTrue(builder is MockAlertPresenter.Builder)
        assertTrue(
            viewModel.getKoin()._logger is PrintLogger,
            "KoinApplicationDeclaration should have changed the Logger"
        )
    }
}
