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

package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.bytes.medfloat.toByteArray
import com.splendo.kaluga.bytes.medfloat.MedFloat32
import com.splendo.kaluga.bytes.annotations.MedFloat
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.test.Test

/**
 * Tests that depend on open, [SerializersModule]-registered polymorphic dispatch over an abstract class
 * hierarchy that isn't sealed - the set of subtypes is only known at [SerializersModule] registration time, not
 * from the type declaration. A KSP-generated view backend processes one closed, annotated interface at a time
 * and has no equivalent of an externally-extensible hierarchy, so it will never implement [Capability.OPEN_POLYMORPHISM]
 * and can never run this class. That is asserted explicitly below rather than left for the suite to skip silently.
 */
class BytesFormatKotlinxOnlyTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    init {
        check(Capability.OPEN_POLYMORPHISM in createBackend().capabilities) {
            "BytesFormatKotlinxOnlyTest requires a backend with open polymorphism support"
        }
    }

    private object EncodePolymorphicFixtures {
        @Serializable
        abstract class Base

        @Serializable
        @SerializedByteValue(0x01)
        data class A(val a: Int) : Base()

        @Serializable
        @SerializedByteValue(0x02)
        data class B(@MedFloat val b: Double) : Base()

        val module = SerializersModule {
            polymorphic(Base::class) {
                subclass(A::class)
                subclass(B::class)
            }
        }

        @Serializable
        data class Container(val base: Base)
    }

    @Test
    fun encodePolymorphic() {
        val backend = createBackend(BackendOptions(serializersModule = EncodePolymorphicFixtures.module))
        validateEncoding(
            EncodePolymorphicFixtures.Container(EncodePolymorphicFixtures.A(4)),
            EncodePolymorphicFixtures.Container.serializer(),
            byteArrayOf(0x01, 0x04, 0x00, 0x00, 0x00),
            backend,
        )
        validateEncoding(
            EncodePolymorphicFixtures.Container(EncodePolymorphicFixtures.B(600.0)),
            EncodePolymorphicFixtures.Container.serializer(),
            byteArrayOf(0x02) + MedFloat32(600.0).toByteArray(),
            backend,
        )
    }
}
