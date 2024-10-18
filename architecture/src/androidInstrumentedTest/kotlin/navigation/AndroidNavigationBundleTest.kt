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

import android.os.Bundle
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.milliseconds

class AndroidNavigationBundleTest {

    @Test
    fun testConvertBundle() {
        val booleanValue = false
        val serializableValue = MockSerializable("Value")
        val nestedString = "NestedString"
        val nestedSpec = NestedSpec()
        val nestedBundle: NavigationBundle<NestedSpecRow<*>> = nestedSpec.toBundle { entry ->
            when (entry) {
                is NestedSpecRow.StringSpecRow -> entry.associatedType.convertValue(nestedString)
            }
        }
        val optionalString: String = "Some String"
        val optionalFloat: Float? = null
        val dateValue = DefaultKalugaDate.epoch(offset = 1606204800000.milliseconds)
        val dateArray = listOf(
            DefaultKalugaDate.epoch(offset = 1606204800001.milliseconds),
            DefaultKalugaDate.epoch(offset = 1606204800002.milliseconds),
        )

        val mockSpec = MockSpec()
        val navigationBundle = mockSpec.toBundle { entry ->
            when (entry) {
                is MockSpecRow.BooleanSpecRow -> entry.associatedType.convertValue(booleanValue)
                is MockSpecRow.SerializableSpecRow -> entry.associatedType.convertValue(serializableValue)
                is MockSpecRow.NestedBundleSpecRow -> entry.associatedType.convertValue(nestedBundle)
                is MockSpecRow.OptionalString -> entry.associatedType.convertValue(optionalString)
                is MockSpecRow.OptionalFloat -> entry.associatedType.convertValue(optionalFloat)
                is MockSpecRow.DateSpecRow -> entry.associatedType.convertValue(dateValue)
                is MockSpecRow.DateArraySpecRow -> entry.associatedType.convertValue(dateArray)
            }
        }

        val bundle = navigationBundle.toBundle().toNavigationBundle(mockSpec)

        val booleanResult = bundle.get(MockSpecRow.BooleanSpecRow)
        val serializableResult = bundle.get(MockSpecRow.SerializableSpecRow)
        val bundleResult = bundle.get(MockSpecRow.NestedBundleSpecRow)
        val nestedStringResult = bundleResult.get(NestedSpecRow.StringSpecRow)
        val optionalStringResult = bundle.get(MockSpecRow.OptionalString)
        val optionalFloatResult = bundle.get(MockSpecRow.OptionalFloat)
        val dateResult = bundle.get(MockSpecRow.DateSpecRow)
        val dateArrayResult = bundle.get(MockSpecRow.DateArraySpecRow)

        assertEquals(booleanValue, booleanResult)
        assertEquals(serializableValue, serializableResult)
        assertEquals(nestedString, nestedStringResult)
        assertEquals(optionalString, optionalStringResult)
        assertEquals(optionalFloat, optionalFloatResult)
        assertEquals(dateValue, dateResult)
        assertEquals(dateArray, dateArrayResult)
    }

    @Test
    fun testConvertSingleValueBundle() {
        val serializable = MockSerializable("value")
        val specType = NavigationBundleSpecType.SerializedType(MockSerializable.serializer())
        val row = SingleValueNavigationAction(serializable, NavigationBundleSpecType.SerializedType(MockSerializable.serializer()))
        val bundleValue = row.bundle!!.toBundle().apply {
            putBoolean(MockSpecRow.BooleanSpecRow.key, false)
        }.toTypedProperty(specType)
        assertEquals(serializable, bundleValue)
    }

    @Test
    fun testFailToConvertBundle() {
        assertFailsWith(BundleConversionError::class) { Bundle().toNavigationBundle(MockSpec()) }
    }

    @Test
    fun testFailToDeserializeBundle() {
        val specType = NavigationBundleSpecType.SerializedType(MockSerializable.serializer())
        val bundle = Bundle().apply {
            putString(SingleValueNavigationSpec.Row(specType).key, "invalid json")
        }
        assertFailsWith(BundleConversionError::class) { bundle.toTypedProperty(specType) }
    }
}
