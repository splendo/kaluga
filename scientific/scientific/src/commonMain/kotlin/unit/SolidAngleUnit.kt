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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.base.decimal.div
import com.splendo.kaluga.base.decimal.times
import com.splendo.kaluga.base.decimal.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [SolidAngle]
 */
val SolidAngleUnits: Set<SolidAngle> get() = setOf(
    Steradian,
    Nanosteradian,
    Microsteradian,
    Millisteradian,
    Centisteradian,
    Decisteradian,
    Spat,
    SquareDegree,
)

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.SolidAngle]
 * SI unit is [Steradian]
 */
@Serializable
sealed class SolidAngle :
    DefinedScientificUnit<PhysicalQuantity.SolidAngle>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.SolidAngle>

@Serializable
data object Steradian : SolidAngle(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle> {
    override val symbol = "sr"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.SolidAngle
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class SteradianMultiple :
    SolidAngle(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian>

@Serializable
data object Nanosteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Nano(Steradian)

@Serializable
data object Microsteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Micro(Steradian)

@Serializable
data object Millisteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Milli(Steradian)

@Serializable
data object Centisteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Centi(Steradian)

@Serializable
data object Decisteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Deci(Steradian)

@Serializable
data object Spat : SolidAngle(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle> {
    private val STERADIAN_IN_SPAT = Decimal.PI * 4.toDecimal()
    override val symbol = "sp"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.SolidAngle
    override fun fromSIUnit(value: Decimal): Decimal = value / STERADIAN_IN_SPAT
    override fun toSIUnit(value: Decimal): Decimal = value * STERADIAN_IN_SPAT
}

@Serializable
data object SquareDegree : SolidAngle() {
    override val symbol = "°2"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.SolidAngle
    override fun fromSIUnit(value: Decimal): Decimal = Degree.fromSIUnit(Degree.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Degree.toSIUnit(Degree.toSIUnit(value))
}

internal fun SerializersModuleBuilder.setupForSolidAngle() {
    polymorphic(SolidAngle::class) {
        registerSolidAngleClasses()
    }
}

internal fun PolymorphicModuleBuilder<SolidAngle>.registerSolidAngleClasses() {
    subclass(Spat::class, Spat.serializer())
    subclass(SquareDegree::class, SquareDegree.serializer())
    subclass(Steradian::class, Steradian.serializer())
    subclass(Centisteradian::class, Centisteradian.serializer())
    subclass(Decisteradian::class, Decisteradian.serializer())
    subclass(Microsteradian::class, Microsteradian.serializer())
    subclass(Millisteradian::class, Millisteradian.serializer())
    subclass(Nanosteradian::class, Nanosteradian.serializer())
}
