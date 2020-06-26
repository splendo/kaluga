package com.splendo.kaluga.hud

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import kotlin.test.assertFails
import kotlin.test.assertNull
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

class UiContextTests {

    private val lifecycleOwner: LifecycleOwner
        get() = mock(LifecycleOwner::class.java).apply {
            val lifecycle = LifecycleRegistry(this)
            `when`(this.lifecycle).thenReturn(lifecycle)
        }

    private val fragmentManager: FragmentManager
        get() = mock(FragmentManager::class.java)

    @Test
    fun testUiContextObserverHandlerCalled() {

        val observer = UiContextObserver()
        val data: UiContextObserver.UiContextData? = UiContextObserver.UiContextData(
            lifecycleOwner, fragmentManager
        )

        observer.onUiContextDataWillChange = { newValue, oldValue ->
            assertNull(oldValue)
            assertThat(newValue, equalTo(data))
        }
        observer.uiContextData = data

        observer.onUiContextDataWillChange = { newValue, oldValue ->
            assertThat(oldValue, equalTo(data))
            assertNull(newValue)
        }
        observer.uiContextData = null
    }

    @Test
    fun testUiContextObserverExceptionOnSetNullTwice() {
        assertFails {
            UiContextObserver().uiContextData = null
        }
    }

    @Test
    fun testUiContextObserverExceptionOnDataSetTwice() {

        val observer = UiContextObserver()
        val data: UiContextObserver.UiContextData? = UiContextObserver.UiContextData(
            lifecycleOwner, fragmentManager
        )

        observer.onUiContextDataWillChange = { newValue, oldValue ->
            assertNull(oldValue)
            assertThat(newValue, equalTo(data))
        }
        observer.uiContextData = data

        assertFails {
            observer.uiContextData = data
        }
    }
}
