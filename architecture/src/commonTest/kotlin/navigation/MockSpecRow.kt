/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.utils.Date
import kotlinx.serialization.Serializable

@Serializable
data class MockSerializable(val value: String)

class MockSpec : NavigationBundleSpec<MockSpecRow<*>>(
    setOf(
        MockSpecRow.BooleanSpecRow,
        MockSpecRow.SerializableSpecRow,
        MockSpecRow.NestedBundleSpecRow,
        MockSpecRow.OptionalString,
        MockSpecRow.OptionalFloat,
        MockSpecRow.DateSpecRow
    )
)

sealed class MockSpecRow<V>(associatedType: NavigationBundleSpecType<V>) : NavigationBundleSpecRow<V>(associatedType) {
    object BooleanSpecRow : MockSpecRow<Boolean>(NavigationBundleSpecType.BooleanType)
    object SerializableSpecRow : MockSpecRow<MockSerializable>(NavigationBundleSpecType.SerializedType(MockSerializable.serializer()))
    object NestedBundleSpecRow : MockSpecRow<NavigationBundle<NestedSpecRow<*>>>(NavigationBundleSpecType.BundleType(NestedSpec()))
    object OptionalString : MockSpecRow<String?>(NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.StringType))
    object OptionalFloat : MockSpecRow<Float?>(NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.FloatType))
    object DateSpecRow : MockSpecRow<Date>(NavigationBundleSpecType.DateType)
}

class NestedSpec : NavigationBundleSpec<NestedSpecRow<*>>(setOf(NestedSpecRow.StringSpecRow))

sealed class NestedSpecRow<V>(associatedType: NavigationBundleSpecType<V>) : NavigationBundleSpecRow<V>(associatedType) {

    object StringSpecRow : NestedSpecRow<String>(NavigationBundleSpecType.StringType)
}
