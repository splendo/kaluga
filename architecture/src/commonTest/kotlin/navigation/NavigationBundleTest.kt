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

package com.splendo.kaluga.architecture.navigation

import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.test.base.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NavigationBundleTest : BaseTest() {

    @Test
    fun testCreateBundle() {
        val booleanValue = false
        val serializableValue = MockSerializable("Value")
        val nestedString = "NestedString"
        val nestedSpec = NestedSpec()
        val nestedBundle: NavigationBundle<NestedSpecRow<*>> = nestedSpec.toBundle { entry ->
            when (entry) {
                is NestedSpecRow.StringSpecRow -> entry.convertValue(nestedString)
            }
        }
        val optionalString: String? = "Some String"
        val optionalFloat: Float? = null
        val dateValue = DefaultKalugaDate.epoch(offsetInMilliseconds = 1606204800000)
        val dateArray = listOf(
            DefaultKalugaDate.epoch(offsetInMilliseconds = 1606204800001),
            DefaultKalugaDate.epoch(offsetInMilliseconds = 1606204800002)
        )

        val mockSpec = MockSpec()
        val bundle = mockSpec.toBundle { entry ->
            when (entry) {
                is MockSpecRow.BooleanSpecRow -> entry.convertValue(booleanValue)
                is MockSpecRow.SerializableSpecRow -> entry.convertValue(serializableValue)
                is MockSpecRow.NestedBundleSpecRow -> entry.convertValue(nestedBundle)
                is MockSpecRow.OptionalString -> entry.convertValue(optionalString)
                is MockSpecRow.OptionalFloat -> entry.convertValue(optionalFloat)
                is MockSpecRow.DateSpecRow -> entry.convertValue(dateValue)
                is MockSpecRow.DateArraySpecRow -> entry.convertValue(dateArray)
            }
        }

        val booleanResult = bundle.get(MockSpecRow.BooleanSpecRow)
        val serializableResult = bundle.get(MockSpecRow.SerializableSpecRow)
        val bundleResult = bundle.get(MockSpecRow.NestedBundleSpecRow)
        val optionalStringResult = bundle.get(MockSpecRow.OptionalString)
        val optionalFloatResult = bundle.get(MockSpecRow.OptionalFloat)
        val dateResult = bundle.get(MockSpecRow.DateSpecRow)
        val dateArrayResult = bundle.get(MockSpecRow.DateArraySpecRow)

        assertEquals(booleanValue, booleanResult)
        assertEquals(serializableValue, serializableResult)
        assertEquals(nestedBundle, bundleResult)
        assertEquals(optionalString, optionalStringResult)
        assertEquals(optionalFloat, optionalFloatResult)
        assertEquals(dateValue, dateResult)
        assertEquals(dateArray, dateArrayResult)

        val nestedStringResult = bundleResult.get(NestedSpecRow.StringSpecRow)
        assertEquals(nestedString, nestedStringResult)

        assertFailsWith(NavigationBundleGetError::class) { bundle.get(NestedSpecRow.StringSpecRow) }
    }
}
