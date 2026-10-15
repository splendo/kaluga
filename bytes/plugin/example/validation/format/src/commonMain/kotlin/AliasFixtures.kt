/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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
package com.splendo.kaluga.bytes.example.format

import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.Size
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

typealias MaybeInt = Int?
typealias AliasedInner = AliasFixtures.Inner
typealias Items<T> = List<T>

// Type aliases and a value class wrapping a layout, which LayoutParityTest checks against the runtime descriptors.
object AliasFixtures {
    @BytesLayout
    interface Inner {
        val value: Byte
    }

    @BytesLayout
    interface Aliased {
        @Size(Length.`8_BIT`)
        val maybe: MaybeInt
        val inner: AliasedInner
        val inners: Items<AliasedInner>
        val numbers: Items<Short>
    }

    @Serializable
    @JvmInline
    value class InnerWrapper(@Serializable(with = AliasFixturesInnerData.InterfaceSerializer::class) val inner: Inner)

    @BytesLayout
    interface Wrapping {
        val wrapper: InnerWrapper
    }
}
