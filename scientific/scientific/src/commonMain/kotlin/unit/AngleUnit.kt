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
 * Set of all [Angle]
 */
val AngleUnits: Set<Angle> get() = setOf(
    Radian,
    Nanoradian,
    Microradian,
    Milliradian,
    Centiradian,
    Deciradian,
    Turn,
    Nanoturn,
    Microturn,
    Milliturn,
    Centiturn,
    Deciturn,
    Degree,
    Gradian,
    ArcMinute,
    ArcSecond,
)

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Angle]
 * SI unit is [Radian]
 */
@Serializable
sealed class Angle :
    DefinedScientificUnit<PhysicalQuantity.Angle>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Angle>

@Serializable
data object Radian : Angle(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle> {
    override val symbol = "rad"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Angle
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class RadianMultiple :
    Angle(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Radian>

@Serializable
data object Nanoradian : RadianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Radian> by Nano(Radian)

@Serializable
data object Microradian : RadianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Radian> by Micro(Radian)

@Serializable
data object Milliradian : RadianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Radian> by Milli(Radian)

@Serializable
data object Centiradian : RadianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Radian> by Centi(Radian)

@Serializable
data object Deciradian : RadianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Radian> by Deci(Radian)

@Serializable
data object Turn : Angle(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle> {
    private val RADIAN_IN_TURN = Decimal.PI * 2.toDecimal()
    override val symbol = "tr"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Angle
    override fun fromSIUnit(value: Decimal): Decimal = value / RADIAN_IN_TURN
    override fun toSIUnit(value: Decimal): Decimal = value * RADIAN_IN_TURN
}

@Serializable
sealed class TurnMultiple :
    Angle(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Turn>

@Serializable
data object Nanoturn : TurnMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Turn> by Nano(Turn)

@Serializable
data object Microturn : TurnMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Turn> by Micro(Turn)

@Serializable
data object Milliturn : TurnMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Turn> by Milli(Turn)

@Serializable
data object Centiturn : TurnMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Turn> by Centi(Turn)

@Serializable
data object Deciturn : TurnMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Angle, Turn> by Deci(Turn)

@Serializable
data object Degree : Angle() {
    private val DEGREE_IN_TURN = 360.toDecimal()
    override val symbol = "°"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Angle
    override fun fromSIUnit(value: Decimal): Decimal = Turn.fromSIUnit(value) * DEGREE_IN_TURN
    override fun toSIUnit(value: Decimal): Decimal = Turn.toSIUnit(value / DEGREE_IN_TURN)
}

@Serializable
data object Gradian : Angle() {
    private val GRADIAN_IN_TURN = 400.toDecimal()
    override val symbol = "gon"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Angle
    override fun fromSIUnit(value: Decimal): Decimal = Turn.fromSIUnit(value) * GRADIAN_IN_TURN
    override fun toSIUnit(value: Decimal): Decimal = Turn.toSIUnit(value / GRADIAN_IN_TURN)
}

@Serializable
data object ArcMinute : Angle() {
    private val ARCMINUTE_IN_TURN = 21600.toDecimal()
    override val symbol = "′"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Angle
    override fun fromSIUnit(value: Decimal): Decimal = Turn.fromSIUnit(value) * ARCMINUTE_IN_TURN
    override fun toSIUnit(value: Decimal): Decimal = Turn.toSIUnit(value / ARCMINUTE_IN_TURN)
}

@Serializable
data object ArcSecond : Angle() {
    private val ARCSECOND_IN_TURN = 1296000.toDecimal()
    override val symbol = "″"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Angle
    override fun fromSIUnit(value: Decimal): Decimal = Turn.fromSIUnit(value) * ARCSECOND_IN_TURN
    override fun toSIUnit(value: Decimal): Decimal = Turn.toSIUnit(value / ARCSECOND_IN_TURN)
}

internal fun SerializersModuleBuilder.setupForAngle() {
    polymorphic(Angle::class) {
        registerAngleClasses()
    }
}

internal fun PolymorphicModuleBuilder<Angle>.registerAngleClasses() {
    subclass(ArcMinute::class, ArcMinute.serializer())
    subclass(ArcSecond::class, ArcSecond.serializer())
    subclass(Degree::class, Degree.serializer())
    subclass(Gradian::class, Gradian.serializer())
    subclass(Radian::class, Radian.serializer())
    subclass(Centiradian::class, Centiradian.serializer())
    subclass(Deciradian::class, Deciradian.serializer())
    subclass(Microradian::class, Microradian.serializer())
    subclass(Milliradian::class, Milliradian.serializer())
    subclass(Nanoradian::class, Nanoradian.serializer())
    subclass(Turn::class, Turn.serializer())
    subclass(Centiturn::class, Centiturn.serializer())
    subclass(Deciturn::class, Deciturn.serializer())
    subclass(Microturn::class, Microturn.serializer())
    subclass(Milliturn::class, Milliturn.serializer())
    subclass(Nanoturn::class, Nanoturn.serializer())
}
