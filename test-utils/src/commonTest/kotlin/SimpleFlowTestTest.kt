/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleFlowTestTest: SimpleFlowTest<Int>() {

    override val flow = { MutableStateFlow(1) }

    @Test
    fun testFlowActionFirst() = testWithFlow { flow ->
        action {
            (flow as MutableStateFlow).emit(2)
        }
        test {
            assertEquals(2, it)
        }
    }

    @Test
    fun testFlowTestFirst() = testWithFlow { flow ->
        test {
            assertEquals(1, it)
        }
        action {
            (flow as MutableStateFlow).emit(2)
        }
        test {
            assertEquals(2, it)
        }


    }

}