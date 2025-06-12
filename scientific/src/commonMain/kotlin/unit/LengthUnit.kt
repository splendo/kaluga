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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [MetricLength]
 */
val MetricLengthUnits: Set<MetricLength> get() = setOf(
    Meter,
    Nanometer,
    Micrometer,
    Millimeter,
    Centimeter,
    Decimeter,
    Decameter,
    Hectometer,
    Kilometer,
    Megameter,
    Gigameter,
    NauticalMile,
)

/**
 * Set of all [ImperialLength]
 */
val ImperialLengthUnits: Set<ImperialLength> get() = setOf(
    Inch,
    Foot,
    Yard,
    Mile,
)

/**
 * Set of all [Length]
 */
val LengthUnits: Set<Length> get() = MetricLengthUnits + ImperialLengthUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Length]
 * SI unit is [Meter]
 */
@Serializable
sealed class Length : DefinedScientificUnit<PhysicalQuantity.Length>()

/**
 * A [Length] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricLength :
    Length(),
    MetricScientificUnit<PhysicalQuantity.Length>

/**
 * A [Length] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialLength :
    Length(),
    ImperialScientificUnit<PhysicalQuantity.Length> {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Length
}

// Metric Length
@Serializable
data object Meter : MetricLength(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Length> {
    override val symbol: String = "m"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Length
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class MeterMultiple :
    MetricLength(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter>

@Serializable
data object Nanometer : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Nano(Meter)

@Serializable
data object Micrometer : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Micro(Meter)

@Serializable
data object Millimeter : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Milli(Meter)

@Serializable
data object Centimeter : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Centi(Meter)

@Serializable
data object Decimeter : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Deci(Meter)

@Serializable
data object Decameter : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Deca(Meter)

@Serializable
data object Hectometer : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Hecto(Meter)

@Serializable
data object Kilometer : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Kilo(Meter)

@Serializable
data object Megameter : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Mega(Meter)

@Serializable
data object Gigameter : MeterMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Length, Meter> by Giga(Meter)

@Serializable
data object NauticalMile : MetricLength() {
    private val METER_IN_NAUTICAL_MILE = 1852.toDecimal()
    override val symbol: String = "nmi"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Length
    override fun toSIUnit(value: Decimal): Decimal = value * METER_IN_NAUTICAL_MILE
    override fun fromSIUnit(value: Decimal): Decimal = value / METER_IN_NAUTICAL_MILE
}

// Imperial Length
@Serializable
data object Inch : ImperialLength() {
    private val INCHES_IN_FOOT = 12.toDecimal()
    override val symbol: String = "in"
    override fun toSIUnit(value: Decimal): Decimal = Foot.toSIUnit(value / INCHES_IN_FOOT)
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(value) * INCHES_IN_FOOT
}

@Serializable
data object Foot : ImperialLength() {
    private val METER_IN_FEET = 0.3048.toDecimal()
    override val symbol: String = "ft"
    override fun toSIUnit(value: Decimal): Decimal = value * METER_IN_FEET
    override fun fromSIUnit(value: Decimal): Decimal = value / METER_IN_FEET
}

@Serializable
data object Yard : ImperialLength() {
    private val FOOT_IN_YARD = 3.toDecimal()
    override val symbol: String = "yd"
    override fun toSIUnit(value: Decimal): Decimal = Foot.toSIUnit(value * FOOT_IN_YARD)
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(value) / FOOT_IN_YARD
}

@Serializable
data object Mile : ImperialLength() {
    private val YARDS_IN_MILE = 1760.toDecimal()
    override val symbol: String = "mi"
    override fun toSIUnit(value: Decimal): Decimal = Yard.toSIUnit(value * YARDS_IN_MILE)
    override fun fromSIUnit(value: Decimal): Decimal = Yard.fromSIUnit(value) / YARDS_IN_MILE
}

internal fun SerializersModuleBuilder.setupForLength() {
    polymorphic(Length::class) {
        registerLengthClasses()
    }
    polymorphic(MetricLength::class) {
        registerMetricLengthClasses()
    }
    polymorphic(ImperialLength::class) {
        registerImperialLengthClasses()
    }
}

internal fun PolymorphicModuleBuilder<Length>.registerLengthClasses() {
    registerMetricLengthClasses()
    registerImperialLengthClasses()
}

internal fun PolymorphicModuleBuilder<MetricLength>.registerMetricLengthClasses() {
    subclass(Meter::class, Meter.serializer())
    subclass(Centimeter::class, Centimeter.serializer())
    subclass(Decameter::class, Decameter.serializer())
    subclass(Decimeter::class, Decimeter.serializer())
    subclass(Gigameter::class, Gigameter.serializer())
    subclass(Hectometer::class, Hectometer.serializer())
    subclass(Kilometer::class, Kilometer.serializer())
    subclass(Megameter::class, Megameter.serializer())
    subclass(Micrometer::class, Micrometer.serializer())
    subclass(Millimeter::class, Millimeter.serializer())
    subclass(Nanometer::class, Nanometer.serializer())
    subclass(NauticalMile::class, NauticalMile.serializer())
}

internal fun PolymorphicModuleBuilder<ImperialLength>.registerImperialLengthClasses() {
    subclass(Foot::class, Foot.serializer())
    subclass(Inch::class, Inch.serializer())
    subclass(Mile::class, Mile.serializer())
    subclass(Yard::class, Yard.serializer())
}
