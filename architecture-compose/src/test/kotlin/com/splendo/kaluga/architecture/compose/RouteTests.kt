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

package com.splendo.kaluga.architecture.compose

import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.text.iso8601Pattern
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.base.utils.utc
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.nullable
import org.junit.Test
import kotlin.test.assertEquals

@Serializable
data class MockSerializable(val value: String)

object MockSpec : NavigationBundleSpec<MockSpecRow<*>>(
    setOf(
        MockSpecRow.StringSpecRow,
        MockSpecRow.BooleanSpecRow,
        MockSpecRow.FloatSpecRow,
        MockSpecRow.SerializableSpecRow,
        MockSpecRow.OptionalString,
        MockSpecRow.OptionalFloat,
        MockSpecRow.OptionalMockSerializable,
        MockSpecRow.DateSpecRow,
    ),
)

sealed class MockSpecRow<V>(associatedType: NavigationBundleSpecType<V>) :
    NavigationBundleSpecRow<V>(associatedType) {
    data object StringSpecRow : MockSpecRow<String>(NavigationBundleSpecType.StringType)
    data object BooleanSpecRow : MockSpecRow<Boolean>(NavigationBundleSpecType.BooleanType)
    data object FloatSpecRow : MockSpecRow<Float>(NavigationBundleSpecType.FloatType)
    data object SerializableSpecRow : MockSpecRow<MockSerializable>(
        NavigationBundleSpecType.SerializedType(MockSerializable.serializer()),
    )
    data object OptionalString : MockSpecRow<String?>(
        NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.StringType),
    )
    data object OptionalFloat : MockSpecRow<Float?>(
        NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.FloatType),
    )
    data object OptionalMockSerializable : MockSpecRow<MockSerializable?>(
        NavigationBundleSpecType.SerializedType(MockSerializable.serializer().nullable),
    )
    data object DateSpecRow : MockSpecRow<KalugaDate>(NavigationBundleSpecType.DateType)
}

class NestedSpec : NavigationBundleSpec<NestedSpecRow<*>>(setOf(NestedSpecRow.StringSpecRow))

sealed class NestedSpecRow<V>(associatedType: NavigationBundleSpecType<V>) :
    NavigationBundleSpecRow<V>(associatedType) {

    data object StringSpecRow : NestedSpecRow<String>(NavigationBundleSpecType.StringType)
}

class TestNavigationAction(bundle: NavigationBundle<MockSpecRow<*>>) :
    NavigationAction<MockSpecRow<*>>(bundle)

class RouteTests {

    @Test
    fun testRoute() {
        val time = DefaultKalugaDate.epoch(timeZone = KalugaTimeZone.utc)
        val bundle = MockSpec.toBundle { row ->
            when (row) {
                is MockSpecRow.StringSpecRow -> row.convertValue("string")
                is MockSpecRow.BooleanSpecRow -> row.convertValue(true)
                is MockSpecRow.FloatSpecRow -> row.convertValue(0.5f)
                is MockSpecRow.SerializableSpecRow -> row.convertValue(
                    MockSerializable("Mock"),
                )
                is MockSpecRow.OptionalString -> row.convertValue("optional")
                is MockSpecRow.OptionalFloat -> row.convertValue(null)
                is MockSpecRow.OptionalMockSerializable -> row.convertValue(
                    MockSerializable("OptionalMock"),
                )
                is MockSpecRow.DateSpecRow -> row.convertValue(time)
            }
        }
        val action = TestNavigationAction(bundle)
        assertEquals(
            "TestNavigationAction/true/0.5/{" +
                "\"value\":\"Mock\"}/{\"value\":\"OptionalMock\"" +
                "}/${KalugaDateFormatter.iso8601Pattern().format(time)}?" +
                "StringSpecRow=string&OptionalString=optional",
            action.route(),
        )
    }
}
