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

import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.mock.voidParametersMock
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleUIThreadViewModelTestTest : SimpleUIThreadViewModelTest<SimpleUIThreadViewModelTestTest.ViewModel>() {

    companion object {
        val onClearedMock = voidParametersMock<Unit>().apply {
            on().doReturn(Unit)
        }
    }

    class ViewModel : BaseLifecycleViewModel() {
        var v = ""

        override fun onCleared() = onClearedMock.call()
    }

    override fun createViewModel() = ViewModel()

    @Test
    fun test() = testOnUIThread {
        assertEquals("", viewModel.v)
        viewModel.v = "foo" // should not crash on native
    }

    @AfterTest
    fun testCleared() {
        onClearedMock.verify()
        onClearedMock.resetCalls()
    }
}
