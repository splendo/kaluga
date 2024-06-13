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

import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedSubject
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.mock.voidParametersMock
import com.splendo.kaluga.test.base.yieldMultiple
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class LazyUIThreadViewModelTestTest : UIThreadViewModelTest<LazyUIThreadViewModelTestTest.CustomLazyViewModelTestContext, LazyUIThreadViewModelTestTest.ViewModel>() {

    companion object {
        private val isDisposed = atomic(false)
        val onClearedMock = voidParametersMock<Unit>().apply {
            on().doReturn(Unit)
        }
    }

    class ViewModel : BaseLifecycleViewModel() {
        var v: String = ""

        override fun onCleared() = onClearedMock.call()
    }

    class CustomLazyViewModelTestContext(coroutineScope: CoroutineScope) :
        LazyViewModelTestContext<ViewModel>(coroutineScope, { ViewModel() }) {

        override fun dispose() {
            super.dispose()
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
        onClearedMock.verify()
        onClearedMock.resetCalls()
    }
}

class CustomUIThreadViewModelTestTest : UIThreadViewModelTest<CustomUIThreadViewModelTestTest.CustomViewModelTestContext, CustomUIThreadViewModelTestTest.MyViewModel>() {

    companion object {
        val onClearedMock = voidParametersMock<Unit>().apply {
            on().doReturn(Unit)
        }
    }

    class MyViewModel(testFlow: MutableStateFlow<Int>) : BaseLifecycleViewModel() {
        val testObservable = testFlow.map { it.toString() }.toInitializedObservable("", coroutineScope)
        val testSubject = testFlow.toInitializedSubject(coroutineScope)

        override fun onCleared() = onClearedMock.call()
    }

    class CustomViewModelTestContext : ViewModelTestContext<MyViewModel> {
        val mockFlow = MutableStateFlow(1)
        override val viewModel: MyViewModel = MyViewModel(mockFlow)
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> CustomViewModelTestContext = { CustomViewModelTestContext() }

    @Test
    fun testCustomUIThreadViewModelTest() = testOnUIThread {
        val observableDisposable = viewModel.testObservable.observe { }
        val subjectDisposable = viewModel.testSubject.observe { }
        yieldMultiple(2)

        assertEquals("1", viewModel.testObservable.current)
        assertEquals(1, viewModel.testSubject.current)
        viewModel.testSubject.stateFlow.value = 2
        yieldMultiple(2)
        assertEquals("2", viewModel.testObservable.current)
        assertEquals(2, viewModel.testSubject.current)
        mockFlow.value = 3
        yieldMultiple(2)
        assertEquals("3", viewModel.testObservable.current)
        assertEquals(3, viewModel.testSubject.current)

        observableDisposable.dispose()
        subjectDisposable.dispose()
    }

    @AfterTest
    fun testCleared() {
        onClearedMock.verify()
        onClearedMock.resetCalls()
    }
}
