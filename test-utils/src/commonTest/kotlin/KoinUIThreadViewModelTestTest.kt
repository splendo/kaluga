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

package com.splendo.kaluga.test.koin

import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.test.architecture.koin.KoinUIThreadViewModelTest
import com.splendo.kaluga.test.mock.alerts.MockAlertPresenter
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.logger.Level
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KoinUIThreadViewModelTestTest :
    KoinUIThreadViewModelTest<KoinUIThreadViewModelTestTest.MyKoinViewModelTestContext, KoinUIThreadViewModelTestTest.KoinViewModel>() {

    class KoinViewModel : BaseViewModel(), KoinComponent {
        val s: String by inject() // test injecting into ViewModel
    }

    inner class MyKoinViewModelTestContext :
        KoinUIThreadViewModelTest.KoinViewModelTestContext<KoinViewModel>(
            {
                printLogger(Level.DEBUG) // not the default
            },
            module {
                single { "S" }
                single<BaseAlertPresenter.Builder> { MockAlertPresenter.Builder() }
                single { KoinViewModel() }
            }
        ) {

        // if you're using this as example and don't want inject your viewmodel you can instead use `by lazy`
        override val viewModel: KoinViewModel by inject()

        val builder: BaseAlertPresenter.Builder by inject() // test injecting into context
    }

    override fun CoroutineScope.createTestContext(): MyKoinViewModelTestContext =
        MyKoinViewModelTestContext()

    @Test
    fun testKoinViewModelTestContext() = testOnUIThread {
        assertEquals("S", viewModel.s)
        assertTrue(builder is MockAlertPresenter.Builder)
        assertEquals(
            Level.DEBUG,
            viewModel.getKoin().logger.level,
            "KoinApplicationDeclaration should have changed the Logger"
        )
    }
}
