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

package com.splendo.kaluga.test.base.mock

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds

interface TimedMockableTestMethod {
    fun methodWithoutParamsAndReturnType()
}

class TimedMockableTestMethodImpl : TimedMockableTestMethod {
    override fun methodWithoutParamsAndReturnType() {
        TODO("Not implemented")
    }
}

class TimedMockMethodTest {

    private val mock = TimedMockableTestMethodImpl()

    @Test
    fun testVerifyWithin() = runBlocking {
        val mock = mock::methodWithoutParamsAndReturnType.mock()
        launch(Dispatchers.Default) {
            delay(100)
            mock.call()
        }
        mock.verifyBefore(200.milliseconds)
    }

    @Test
    fun testVerifyWithinSeveral() = runBlocking {
        val mock = mock::methodWithoutParamsAndReturnType.mock()
        launch(Dispatchers.Default) {
            delay(50)
            mock.call()
            delay(10)
            mock.call()
            delay(10)
            mock.call()
        }
        mock.verifyBefore(200.milliseconds, times = 3)
    }


    @Test
    fun testVerifyNotWithin() = runBlocking {
        val mock = mock::methodWithoutParamsAndReturnType.mock()
        launch(Dispatchers.Default) {
            delay(200)
            mock.call()
        }
        mock.verifyBefore(100.milliseconds, times = 0)
    }

}