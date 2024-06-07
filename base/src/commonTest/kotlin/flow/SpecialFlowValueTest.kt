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

package com.splendo.kaluga.base.flow

import com.splendo.kaluga.base.flow.SpecialFlowValueTest.Special.Last
import com.splendo.kaluga.base.flow.SpecialFlowValueTest.Special.More
import com.splendo.kaluga.base.flow.SpecialFlowValueTest.Special.Normal
import com.splendo.kaluga.base.flow.SpecialFlowValueTest.Special.NotImportant
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class SpecialFlowValueTest : BaseTest() {

    sealed class Special {
        data object Last : SpecialFlowValue.Last, Special()
        data object Normal : Special()
        data object NotImportant : SpecialFlowValue.NotImportant, Special()
        data object More : Special()
    }

    private fun flow() = flowOf(Normal, NotImportant, Last, More)

    @Test
    fun testSpecialValues() = runBlocking {
        assertEquals(
            expected = listOf(Normal, NotImportant, Last, More),
            actual = flow().toList(),
        )

        assertEquals(
            expected = listOf(Normal, Last, More),
            actual = flow().filterOnlyImportant().toList(),
        )

        val list = mutableListOf<Special>()
        flow().collectImportantUntilLast {
            list.add(it)
        }
        assertEquals(
            expected = listOf(Normal, Last),
            actual = list,
        )

        list.clear()
        flow().collectUntilLast {
            list.add(it)
        }
        assertEquals(
            expected = listOf(Normal, NotImportant, Last),
            actual = list,
        )

        list.clear()

        launch {
            flow().collectImportant {
                list.add(it)
                if (it is More) {
                    assertEquals(
                        expected = listOf(Normal, Last, More),
                        actual = list,
                    )
                }
                cancel()
            }
        }.join()
    }
}
