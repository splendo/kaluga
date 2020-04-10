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

import kotlinx.serialization.Serializable

@Serializable
data class MockSerializable(val value: String)

class MockSpec : NavigationBundleSpec<MockSpecRow<*>>(setOf(MockSpecRow.BooleanSpecRow, MockSpecRow.SerializableSpecRow, MockSpecRow.NestedBundleSpecRow, MockSpecRow.OptionalString, MockSpecRow.OptionalFloat))

sealed class MockSpecRow<V> : NavigationBundleSpecRow<V> {
    object BooleanSpecRow : MockSpecRow<Boolean>() {
        override val key: String = "BooleanSpec"
        override val associatedType = NavigationBundleSpecType.BooleanType
    }
    object SerializableSpecRow : MockSpecRow<MockSerializable>() {
        override val key: String = "SerializableSpec"
        override val associatedType = NavigationBundleSpecType.SerializedType(MockSerializable.serializer())
    }
    object NestedBundleSpecRow : MockSpecRow<NavigationBundle<NestedSpecRow<*>>>() {
        override val key: String = "NestedSpec"
        override val associatedType = NavigationBundleSpecType.BundleType(NestedSpec())
    }
    object OptionalString : MockSpecRow<String?>() {
        override val key: String = "OptionalStringSpec"
        override val associatedType = NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.StringType)
    }
    object OptionalFloat : MockSpecRow<Float?>() {
        override val key: String = "OptionalFloatSpec"
        override val associatedType = NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.FloatType)
    }
}

class NestedSpec : NavigationBundleSpec<NestedSpecRow<*>>(setOf(NestedSpecRow.StringSpecRow))

sealed class NestedSpecRow<V> : NavigationBundleSpecRow<V> {

    object StringSpecRow : NestedSpecRow<String>() {
        override val key: String = "StringSpec"
        override val associatedType: NavigationBundleSpecType<String> = NavigationBundleSpecType.StringType
    }

}

