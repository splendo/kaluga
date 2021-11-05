/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.scientific.MeasurementType
import kotlinx.serialization.Serializable

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
    ErgPerSecond
)

val MetricPowerUnits: Set<MetricPower> get() = MetricAndImperialPowerUnits.map { it.metric }.toSet() + setOf(MetricHorsepower)
val ImperialPowerUnits: Set<ImperialPower> get() = MetricAndImperialPowerUnits.map { it.imperial }.toSet() +
    setOf(
    FootPoundForcePerSecond,
    FootPoundForcePerMinute,
    Horsepower,
    BritishThermalUnitPerSecond,
    BritishThermalUnitPerMinute,
    BritishThermalUnitPerHour
)

val PowerUnits: Set<Power> get() = MetricAndImperialPowerUnits +
    MetricPowerUnits.filter { it !is MetricMetricAndImperialPowerWrapper }.toSet() +
    ImperialPowerUnits.filter { it !is ImperialMetricAndImperialPowerWrapper }.toSet()

@Serializable
sealed class Power : AbstractScientificUnit<MeasurementType.Power>()

@Serializable
sealed class MetricAndImperialPower : Power(), MetricAndImperialScientificUnit<MeasurementType.Power>
@Serializable
sealed class MetricPower : Power(), MetricScientificUnit<MeasurementType.Power>
@Serializable
sealed class ImperialPower : Power(), ImperialScientificUnit<MeasurementType.Power>

@Serializable
object Watt : MetricAndImperialPower(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power> {
    override val symbol: String = "W"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}
@Serializable
object Nanowatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Nano(Watt)
@Serializable
object Microwatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Micro(Watt)
@Serializable
object Milliwatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Milli(Watt)
@Serializable
object Centiwatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Centi(Watt)
@Serializable
object Deciwatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Deci(Watt)
@Serializable
object Decawatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Deca(Watt)
@Serializable
object Hectowatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Hecto(Watt)
@Serializable
object Kilowatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Kilo(Watt)
@Serializable
object Megawatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Mega(Watt)
@Serializable
object Gigawatt : MetricAndImperialPower(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power, Watt> by Giga(Watt)

@Serializable
object ErgPerSecond : MetricAndImperialPower(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Power> {
    override val symbol: String = "erg/s"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = Erg.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = Erg.toSIUnit(value)
}

@Serializable
object MetricHorsepower : MetricPower() {
    private const val KILOGRAM_FORCE_METER_SECOND_TO_WATT = 75.0
    override val symbol: String = "PS"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = KilogramForce.fromSIUnit(value) / KILOGRAM_FORCE_METER_SECOND_TO_WATT.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = KilogramForce.toSIUnit(value * KILOGRAM_FORCE_METER_SECOND_TO_WATT.toDecimal())
}

@Serializable
object Horsepower : ImperialPower() {
    private const val FOOTPOUND_PER_MINUTE = 33000
    override val symbol: String = "hp"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = FootPoundForce.fromSIUnit(Minute.toSIUnit(value)) / FOOTPOUND_PER_MINUTE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Minute.toSIUnit(FootPoundForce.fromSIUnit(value * FOOTPOUND_PER_MINUTE.toDecimal()))
}

@Serializable
object FootPoundForcePerSecond : ImperialPower() {
    override val symbol: String = "${FootPoundForce.symbol} / ${Second.symbol}"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = FootPoundForce.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = FootPoundForce.toSIUnit(value)
}

@Serializable
object FootPoundForcePerMinute : ImperialPower() {
    override val symbol: String = "${FootPoundForce.symbol} / ${Minute.symbol}"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = Minute.toSIUnit(FootPoundForce.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = FootPoundForce.toSIUnit(Minute.fromSIUnit(value))
}

@Serializable
object BritishThermalUnitPerSecond : ImperialPower() {
    override val symbol: String = "${BritishThermalUnit.symbol} / ${Second.symbol}"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = BritishThermalUnit.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = BritishThermalUnit.toSIUnit(value)
}

@Serializable
object BritishThermalUnitPerMinute : ImperialPower() {
    override val symbol: String = "${BritishThermalUnit.symbol} / ${Minute.symbol}"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = Minute.toSIUnit(BritishThermalUnit.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = BritishThermalUnit.toSIUnit(Minute.fromSIUnit(value))
}

@Serializable
object BritishThermalUnitPerHour : ImperialPower() {
    override val symbol: String = "${BritishThermalUnit.symbol} / ${Hour.symbol}"
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = Hour.toSIUnit(BritishThermalUnit.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = BritishThermalUnit.toSIUnit(Hour.fromSIUnit(value))
}
@Serializable
data class MetricMetricAndImperialPowerWrapper(val metricAndImperialPower: MetricAndImperialPower) : MetricPower() {
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Power
    override val symbol: String = metricAndImperialPower.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialPower.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialPower.toSIUnit(value)
}

val <PowerUnit : MetricAndImperialPower> PowerUnit.metric get() = MetricMetricAndImperialPowerWrapper(this)

@Serializable
data class ImperialMetricAndImperialPowerWrapper(val metricAndImperialPower: MetricAndImperialPower) : ImperialPower() {
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Power
    override val symbol: String = metricAndImperialPower.symbol
    override fun fromSIUnit(value: Decimal): Decimal = metricAndImperialPower.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = metricAndImperialPower.toSIUnit(value)
}

val <PowerUnit : MetricAndImperialPower> PowerUnit.imperial get() = ImperialMetricAndImperialPowerWrapper(this)
