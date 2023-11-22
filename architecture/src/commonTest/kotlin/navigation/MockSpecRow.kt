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

import com.splendo.kaluga.base.utils.KalugaDate
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
        MockSpecRow.DateSpecRow,
        MockSpecRow.DateArraySpecRow,
    ),
)

sealed class MockSpecRow<V>(associatedType: NavigationBundleSpecType<V>) : NavigationBundleSpecRow<V>(associatedType) {
    data object BooleanSpecRow : MockSpecRow<Boolean>(NavigationBundleSpecType.BooleanType)
    data object SerializableSpecRow : MockSpecRow<MockSerializable>(NavigationBundleSpecType.SerializedType(MockSerializable.serializer()))
    data object NestedBundleSpecRow : MockSpecRow<NavigationBundle<NestedSpecRow<*>>>(NavigationBundleSpecType.BundleType(NestedSpec()))
    data object OptionalString : MockSpecRow<String?>(NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.StringType))
    data object OptionalFloat : MockSpecRow<Float?>(NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.FloatType))
    data object DateSpecRow : MockSpecRow<KalugaDate>(NavigationBundleSpecType.DateType)
    data object DateArraySpecRow : MockSpecRow<List<KalugaDate>>(NavigationBundleSpecType.DateArrayType)
}

class NestedSpec : NavigationBundleSpec<NestedSpecRow<*>>(setOf(NestedSpecRow.StringSpecRow))

sealed class NestedSpecRow<V>(associatedType: NavigationBundleSpecType<V>) : NavigationBundleSpecRow<V>(associatedType) {
    data object StringSpecRow : NestedSpecRow<String>(NavigationBundleSpecType.StringType)
}
