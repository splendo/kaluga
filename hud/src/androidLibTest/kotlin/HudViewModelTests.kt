package com.splendo.kaluga.hud

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.assertFails

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

class HudViewModelTests {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun testViewModelSubscribeUnsubscribe() = runBlockingTest {
        val activity = mock(AppCompatActivity::class.java)
        val fragmentManager = mock(FragmentManager::class.java)
        `when`(activity.supportFragmentManager).thenReturn(fragmentManager)

        val viewModel = HudViewModel()

        MainScope().launch(Dispatchers.Main) {
            viewModel.subscribe(activity)
            viewModel.builder.build()
            viewModel.unsubscribe()
        }
    }

    @Test
    fun testExceptionIfBuildWithoutSubscribe() {
        val activity = mock(AppCompatActivity::class.java)
        val fragmentManager = mock(FragmentManager::class.java)
        `when`(activity.supportFragmentManager).thenReturn(fragmentManager)

        val viewModel = HudViewModel()
        assertFails {
            viewModel.builder.build()
        }
    }
}
