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

/**
 * Set of all [MetricAndImperialPower]
 */
val MetricAndImperialPowerUnits: Set<MetricAndImperialPower> get() = setOf(
    Watt,
    Nanowatt,
    Microwatt,
    Milliwatt,
    Centiwatt,
    Deciwatt,
    Decawatt,
    Hectowatt,
    Kilowatt,
    Megawatt,
    Gigawatt,
)

/**
 * Set of all [MetricPower]
 */
val MetricPowerUnits: Set<MetricPower> get() = MetricAndImperialPowerUnits.map { it.metric }.toSet() +
    setOf(
        ErgPerSecond,
        MetricHorsepower,
    )

/**
 * Set of all [ImperialPower]
 */
val ImperialPowerUnits: Set<ImperialPower> get() = MetricAndImperialPowerUnits.map { it.imperial }.toSet() +
    setOf(
        FootPoundForcePerSecond,
        FootPoundForcePerMinute,
        InchPoundForcePerSecond,
        InchPoundForcePerMinute,
        Horsepower,
        BritishThermalUnitPerSecond,
        BritishThermalUnitPerMinute,
        BritishThermalUnitPerHour,
    )

/**
 * Set of all [Power]
 */
val PowerUnits: Set<Power> get() = MetricAndImperialPowerUnits +
    MetricPowerUnits.filter { it !is MetricMetricAndImperialPowerWrapper }.toSet() +
    ImperialPowerUnits.filter { it !is ImperialMetricAndImperialPowerWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Power]
 * SI unit is [Watt]
 */
@Serializable
sealed class Power : AbstractScientificUnit<PhysicalQuantity.Power>()

/**
 * A [Power] for [MeasurementSystem.MetricAndImperial]
 */
@Serializable
sealed class MetricAndImperialPower : Power(), MetricAndImperialScientificUnit<PhysicalQuantity.Power>

/**
 * A [Power] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricPower : Power(), MetricScientificUnit<PhysicalQuantity.Power>

/**
 * A [Power] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialPower : Power(), ImperialScientificUnit<PhysicalQuantity.Power>

@Serializable
data object Watt : MetricAndImperialPower(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power> {
    override val symbol: String = "W"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class WattMultiple : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt>

@Serializable
data object Nanowatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Nano(Watt)

@Serializable
data object Microwatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Micro(Watt)

@Serializable
data object Milliwatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Milli(Watt)

@Serializable
data object Centiwatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Centi(Watt)

@Serializable
data object Deciwatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Deci(Watt)

@Serializable
data object Decawatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Deca(Watt)

@Serializable
data object Hectowatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Hecto(Watt)

@Serializable
data object Kilowatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Kilo(Watt)

@Serializable
data object Megawatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Mega(Watt)

@Serializable
data object Gigawatt : WattMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Power, Watt> by Giga(Watt)

@Serializable
data object ErgPerSecond : MetricPower(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Power> {
    override val symbol: String = "erg/s"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = Erg.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = Erg.toSIUnit(value)
}

@Serializable
data object MetricHorsepower : MetricPower() {
    private const val KILOGRAM_FORCE_METER_SECOND_TO_WATT = 75.0
    override val symbol: String = "PS"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = KilogramForce.fromSIUnit(value) / KILOGRAM_FORCE_METER_SECOND_TO_WATT.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = KilogramForce.toSIUnit(value * KILOGRAM_FORCE_METER_SECOND_TO_WATT.toDecimal())
}

@Serializable
data object Horsepower : ImperialPower() {
    private const val FOOTPOUND_PER_MINUTE = 33000
    override val symbol: String = "hp"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = FootPoundForce.fromSIUnit(Minute.toSIUnit(value)) / FOOTPOUND_PER_MINUTE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Minute.fromSIUnit(FootPoundForce.toSIUnit(value * FOOTPOUND_PER_MINUTE.toDecimal()))
}

@Serializable
data object FootPoundForcePerSecond : ImperialPower() {
    override val symbol: String = "${FootPoundForce.symbol} / ${Second.symbol}"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = FootPoundForce.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = FootPoundForce.toSIUnit(value)
}

@Serializable
data object FootPoundForcePerMinute : ImperialPower() {
    override val symbol: String = "${FootPoundForce.symbol} / ${Minute.symbol}"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = Minute.toSIUnit(FootPoundForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = FootPoundForce.toSIUnit(Minute.fromSIUnit(value))
}

@Serializable
data object InchPoundForcePerSecond : ImperialPower() {
    override val symbol: String = "${FootPoundForce.symbol} / ${Second.symbol}"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = InchPoundForce.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = InchPoundForce.toSIUnit(value)
}

@Serializable
data object InchPoundForcePerMinute : ImperialPower() {
    override val symbol: String = "${FootPoundForce.symbol} / ${Minute.symbol}"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = Minute.toSIUnit(InchPoundForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = InchPoundForce.toSIUnit(Minute.fromSIUnit(value))
}

@Serializable
data object BritishThermalUnitPerSecond : ImperialPower() {
    override val symbol: String = "${BritishThermalUnit.symbol} / ${Second.symbol}"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = BritishThermalUnit.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = BritishThermalUnit.toSIUnit(value)
}

@Serializable
data object BritishThermalUnitPerMinute : ImperialPower() {
    override val symbol: String = "${BritishThermalUnit.symbol} / ${Minute.symbol}"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = Minute.toSIUnit(BritishThermalUnit.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = BritishThermalUnit.toSIUnit(Minute.fromSIUnit(value))
}

@Serializable
data object BritishThermalUnitPerHour : ImperialPower() {
    override val symbol: String = "${BritishThermalUnit.symbol} / ${Hour.symbol}"
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override fun fromSIUnit(value: Decimal): Decimal = Hour.toSIUnit(BritishThermalUnit.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = BritishThermalUnit.toSIUnit(Hour.fromSIUnit(value))
}

/**
 * Wraps a [MetricAndImperialPower] unit to a [MetricPower] unit
 * @param metricAndImperialPower the [MetricAndImperialPower] to wrap
 */
@Serializable
data class MetricMetricAndImperialPowerWrapper(val metricAndImperialPower: MetricAndImperialPower) : MetricPower() {
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Power
    override val symbol: String = metricAndImperialPower.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialPower.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialPower.toSIUnit(value)
}

/**
 * Converts a [MetricAndImperialPower] unit to a [MetricMetricAndImperialPowerWrapper] unit
 * @param PowerUnit the type of [MetricAndImperialPower] to convert
 */
val <PowerUnit : MetricAndImperialPower> PowerUnit.metric get() = MetricMetricAndImperialPowerWrapper(this)

/**
 * Wraps a [MetricAndImperialPower] unit to an [ImperialPower] unit
 * @param metricAndImperialPower the [MetricAndImperialPower] to wrap
 */
@Serializable
data class ImperialMetricAndImperialPowerWrapper(val metricAndImperialPower: MetricAndImperialPower) : ImperialPower() {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Power
    override val symbol: String = metricAndImperialPower.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialPower.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialPower.toSIUnit(value)
}

/**
 * Converts a [MetricAndImperialPower] unit to an [ImperialMetricAndImperialPowerWrapper] unit
 * @param PowerUnit the type of [MetricAndImperialPower] to convert
 */
val <PowerUnit : MetricAndImperialPower> PowerUnit.imperial get() = ImperialMetricAndImperialPowerWrapper(this)
