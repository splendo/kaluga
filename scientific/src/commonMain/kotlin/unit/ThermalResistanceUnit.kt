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
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [MetricAndUKImperialThermalResistance]
 */
val MetricAndUKImperialThermalResistanceUnits: Set<MetricAndUKImperialThermalResistance> get() = MetricAndUkImperialTemperatureUnits.flatMap { temperature ->
    MetricAndImperialPowerUnits.map { temperature per it }
}.toSet()

/**
 * Set of all [MetricThermalResistance]
 */
val MetricThermalResistanceUnits: Set<MetricThermalResistance> get() = MetricAndUkImperialTemperatureUnits.flatMap { temperature ->
    MetricPowerUnits.map { temperature per it }
}.toSet()

/**
 * Set of all [UKImperialThermalResistance]
 */
val UKImperialThermalResistanceUnits: Set<UKImperialThermalResistance> get() = MetricAndUkImperialTemperatureUnits.flatMap { temperature ->
    ImperialPowerUnits.map { temperature per it }
}.toSet()

/**
 * Set of all [USCustomaryThermalResistance]
 */
val USCustomaryThermalResistanceUnits: Set<USCustomaryThermalResistance> get() = USCustomaryTemperatureUnits.flatMap { temperature ->
    ImperialPowerUnits.map { temperature per it }
}.toSet()

/**
 * Set of all [ThermalResistance]
 */
val ThermalResistanceUnits: Set<ThermalResistance> get() = MetricAndUKImperialThermalResistanceUnits +
    MetricThermalResistanceUnits.filter { it.per !is MetricMetricAndImperialPowerWrapper }.toSet() +
    UKImperialThermalResistanceUnits.filter { it.per !is ImperialMetricAndImperialPowerWrapper }.toSet() +
    USCustomaryThermalResistanceUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.ThermalResistance]
 * SI unit is `Kelvin per Watt`
 */
@Serializable
sealed class ThermalResistance : AbstractScientificUnit<PhysicalQuantity.ThermalResistance>() {

    /**
     * The [Temperature] component
     */
    abstract val temperature: Temperature

    /**
     * The [Power] component
     */
    abstract val per: Power
    override val quantity = PhysicalQuantity.ThermalResistance
    override val symbol: String by lazy { "${temperature.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(temperature.deltaFromSIUnitDelta(value))
    override fun toSIUnit(value: Decimal): Decimal = temperature.deltaToSIUnitDelta(per.fromSIUnit(value))
}

/**
 * A [ThermalResistance] for [MeasurementSystem.MetricAndUKImperial]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @param per the [MetricAndImperialPower] component
 */
@Serializable
data class MetricAndUKImperialThermalResistance(override val temperature: MetricAndUKImperialTemperature, override val per: MetricAndImperialPower) :
    ThermalResistance(),
    MetricAndUKImperialScientificUnit<PhysicalQuantity.ThermalResistance> {
    override val system = MeasurementSystem.MetricAndUKImperial

    /**
     * The [MetricThermalResistance] equivalent to this [MetricAndUKImperialThermalResistance]
     */
    val metric get() = temperature per per.metric

    /**
     * The [UKImperialThermalResistance] equivalent to this [MetricAndUKImperialThermalResistance]
     */
    val ukImperial get() = temperature per per.imperial
}

/**
 * A [ThermalResistance] for [MeasurementSystem.Metric]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @param per the [MetricPower] component
 */
@Serializable
data class MetricThermalResistance(override val temperature: MetricAndUKImperialTemperature, override val per: MetricPower) :
    ThermalResistance(),
    MetricScientificUnit<PhysicalQuantity.ThermalResistance> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [ThermalResistance] for [MeasurementSystem.UKImperial]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @param per the [ImperialPower] component
 */
@Serializable
data class UKImperialThermalResistance(override val temperature: MetricAndUKImperialTemperature, override val per: ImperialPower) :
    ThermalResistance(),
    UKImperialScientificUnit<PhysicalQuantity.ThermalResistance> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [ThermalResistance] for [MeasurementSystem.USCustomary]
 * @param temperature the [USCustomaryTemperature] component
 * @param per the [ImperialPower] component
 */
@Serializable
data class USCustomaryThermalResistance(override val temperature: USCustomaryTemperature, override val per: ImperialPower) :
    ThermalResistance(),
    USCustomaryScientificUnit<PhysicalQuantity.ThermalResistance> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricAndUKImperialThermalResistance] from a [MetricAndUKImperialTemperature] and a [MetricAndImperialPower]
 * @param power the [MetricAndImperialPower] component
 * @return the [MetricAndUKImperialThermalResistance] represented by the units
 */
infix fun MetricAndUKImperialTemperature.per(power: MetricAndImperialPower) = MetricAndUKImperialThermalResistance(this, power)

/**
 * Gets a [MetricThermalResistance] from a [MetricAndUKImperialTemperature] and a [MetricPower]
 * @param power the [MetricPower] component
 * @return the [MetricThermalResistance] represented by the units
 */
infix fun MetricAndUKImperialTemperature.per(power: MetricPower) = MetricThermalResistance(this, power)

/**
 * Gets a [UKImperialThermalResistance] from a [MetricAndUKImperialTemperature] and an [ImperialPower]
 * @param power the [ImperialPower] component
 * @return the [UKImperialThermalResistance] represented by the units
 */
infix fun MetricAndUKImperialTemperature.per(power: ImperialPower) = UKImperialThermalResistance(this, power)

/**
 * Gets a [USCustomaryThermalResistance] from a [USCustomaryTemperature] and a [MetricAndImperialPower]
 * @param power the [MetricAndImperialPower] component
 * @return the [USCustomaryThermalResistance] represented by the units
 */
infix fun USCustomaryTemperature.per(power: MetricAndImperialPower) = USCustomaryThermalResistance(this, power.imperial)

/**
 * Gets a [USCustomaryThermalResistance] from a [USCustomaryTemperature] and an [ImperialPower]
 * @param power the [ImperialPower] component
 * @return the [USCustomaryThermalResistance] represented by the units
 */
infix fun USCustomaryTemperature.per(power: ImperialPower) = USCustomaryThermalResistance(this, power)
