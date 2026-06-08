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

package com.splendo.kaluga.test.koin

import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.mock.voidParametersMock
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KoinUIThreadViewModelTestTest : KoinUIThreadViewModelTest<KoinUIThreadViewModelTestTest.MyKoinViewModelTestContext, KoinUIThreadViewModelTestTest.KoinViewModel>() {

    companion object {
        val onClearedMock = voidParametersMock<Unit>().apply {
            on().doReturn(Unit)
        }
    }

    class KoinViewModel :
        BaseLifecycleViewModel(),
        KoinComponent {
        val s: String by inject() // test injecting into ViewModel
        override fun onCleared() = onClearedMock.call()
    }

    class MyKoinViewModelTestContext :
        KoinViewModelTestContext<KoinViewModel>(
            {
                printLogger(Level.DEBUG) // not the default
            },
            module {
                single { "S" }
                single { KoinViewModel() }
            },
        ) {

        // if you're using this as example and don't want inject your viewmodel you can instead use `by lazy`
        override val viewModel: KoinViewModel by inject()
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> MyKoinViewModelTestContext =
        { MyKoinViewModelTestContext() }

    @OptIn(KoinInternalApi::class)
    @Test
    fun testKoinViewModelTestContext() = testOnUIThread {
        assertNotNull(KoinPlatformTools.defaultContext().getOrNull())
        assertEquals("S", viewModel.s)
        assertEquals(
            Level.DEBUG,
            viewModel.getKoin().logger.level,
            "KoinApplicationDeclaration should have changed the Logger",
        )
    }

    @AfterTest
    fun testCleared() {
        assertNull(KoinPlatformTools.defaultContext().getOrNull())
        onClearedMock.verify()
        onClearedMock.resetCalls()
    }
}
