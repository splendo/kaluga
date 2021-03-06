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

package com.splendo.kaluga.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

// we use this surrogate main thread because Swing/JavaFX testing on CI is unreliable
actual open class BaseTest {

    private val mainDispatcher: ExecutorCoroutineDispatcher = newSingleThreadContext("UI thread")

    @BeforeTest
    actual open fun beforeTest() {
        Dispatchers.setMain(mainDispatcher)
    }

    @AfterTest
    actual open fun afterTest() {
        // we want to avoid triggering Swing or FX
        // Dispatchers.resetMain()
        // mainDispatcher.close()
        // mainDispatcher.cancelChildren()
    }
}
