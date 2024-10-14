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
 * Set of all [MetricAndUKImperialHeatCapacity]
 */
val MetricAndUKImperialHeatCapacityUnits: Set<MetricAndUKImperialHeatCapacity> get() = MetricAndImperialEnergyUnits.flatMap { energy ->
    MetricAndUkImperialTemperatureUnits.map { energy per it }
}.toSet()

/**
 * Set of all [MetricHeatCapacity]
 */
val MetricHeatCapacityUnits: Set<MetricHeatCapacity> get() = MetricEnergyUnits.flatMap { energy ->
    MetricAndUkImperialTemperatureUnits.map { energy per it }
}.toSet()

/**
 * Set of all [UKImperialHeatCapacity]
 */
val UKImperialHeatCapacityUnits: Set<UKImperialHeatCapacity> get() = ImperialEnergyUnits.flatMap { energy ->
    MetricAndUkImperialTemperatureUnits.map { energy per it }
}.toSet()

/**
 * Set of all [USCustomaryHeatCapacity]
 */
val USCustomaryHeatCapacityUnits: Set<USCustomaryHeatCapacity> get() = ImperialEnergyUnits.flatMap { energy ->
    USCustomaryTemperatureUnits.map { energy per it }
}.toSet()

/**
 * Set of all [HeatCapacity]
 */
val HeatCapacityUnits: Set<HeatCapacity> get() = MetricAndUKImperialHeatCapacityUnits +
    MetricHeatCapacityUnits.filter { it.energy !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    UKImperialHeatCapacityUnits.filter { it.energy !is ImperialMetricAndImperialEnergyWrapper }.toSet() +
    USCustomaryHeatCapacityUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.HeatCapacity]
 * SI unit is `Joule per Kelvin`
 */
@Serializable
sealed class HeatCapacity : AbstractScientificUnit<PhysicalQuantity.HeatCapacity>() {

    /**
     * The [Energy] component
     */
    abstract val energy: Energy

    /**
     * The [Temperature] component
     */
    abstract val per: Temperature
    override val quantity = PhysicalQuantity.HeatCapacity
    override val symbol: String by lazy { "${energy.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.deltaToSIUnitDelta(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.deltaFromSIUnitDelta(value))
}

/**
 * A [HeatCapacity] for [MeasurementSystem.MetricAndImperial]
 * @param energy the [MetricAndImperialEnergy] component
 * @param per the [MetricAndUKImperialTemperature] component
 */
@Serializable
data class MetricAndUKImperialHeatCapacity(override val energy: MetricAndImperialEnergy, override val per: MetricAndUKImperialTemperature) :
    HeatCapacity(),
    MetricAndUKImperialScientificUnit<PhysicalQuantity.HeatCapacity> {
    override val system = MeasurementSystem.MetricAndUKImperial

    /**
     * The [MetricHeatCapacity] equivalent to this [MetricAndUKImperialHeatCapacity]
     */
    val metric get() = energy.metric per per

    /**
     * The [UKImperialHeatCapacity] equivalent to this [MetricAndUKImperialHeatCapacity]
     */
    val ukImperial get() = energy.imperial per per
}

/**
 * A [HeatCapacity] for [MeasurementSystem.Metric]
 * @param energy the [MetricEnergy] component
 * @param per the [MetricAndUKImperialTemperature] component
 */
@Serializable
data class MetricHeatCapacity(override val energy: MetricEnergy, override val per: MetricAndUKImperialTemperature) :
    HeatCapacity(),
    MetricScientificUnit<PhysicalQuantity.HeatCapacity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [HeatCapacity] for [MeasurementSystem.UKImperial]
 * @param energy the [ImperialEnergy] component
 * @param per the [MetricAndUKImperialTemperature] component
 */
@Serializable
data class UKImperialHeatCapacity(override val energy: ImperialEnergy, override val per: MetricAndUKImperialTemperature) :
    HeatCapacity(),
    UKImperialScientificUnit<PhysicalQuantity.HeatCapacity> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [HeatCapacity] for [MeasurementSystem.USCustomary]
 * @param energy the [ImperialEnergy] component
 * @param per the [USCustomaryTemperature] component
 */
@Serializable
data class USCustomaryHeatCapacity(override val energy: ImperialEnergy, override val per: USCustomaryTemperature) :
    HeatCapacity(),
    USCustomaryScientificUnit<PhysicalQuantity.HeatCapacity> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricAndUKImperialHeatCapacity] from a [MetricAndImperialEnergy] and a [MetricAndUKImperialTemperature]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @return the [MetricAndUKImperialHeatCapacity] represented by the units
 */
infix fun MetricAndImperialEnergy.per(temperature: MetricAndUKImperialTemperature) = MetricAndUKImperialHeatCapacity(this, temperature)

/**
 * Gets a [MetricHeatCapacity] from a [MetricEnergy] and a [MetricAndUKImperialTemperature]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @return the [MetricHeatCapacity] represented by the units
 */
infix fun MetricEnergy.per(temperature: MetricAndUKImperialTemperature) = MetricHeatCapacity(this, temperature)

/**
 * Gets a [UKImperialHeatCapacity] from an [ImperialEnergy] and a [MetricAndUKImperialTemperature]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @return the [UKImperialHeatCapacity] represented by the units
 */
infix fun ImperialEnergy.per(temperature: MetricAndUKImperialTemperature) = UKImperialHeatCapacity(this, temperature)

/**
 * Gets a [USCustomaryHeatCapacity] from a [MetricAndImperialEnergy] and a [USCustomaryTemperature]
 * @param temperature the [USCustomaryTemperature] component
 * @return the [USCustomaryHeatCapacity] represented by the units
 */
infix fun MetricAndImperialEnergy.per(temperature: USCustomaryTemperature) = USCustomaryHeatCapacity(this.imperial, temperature)

/**
 * Gets a [USCustomaryHeatCapacity] from an [ImperialEnergy] and a [USCustomaryTemperature]
 * @param temperature the [USCustomaryTemperature] component
 * @return the [USCustomaryHeatCapacity] represented by the units
 */
infix fun ImperialEnergy.per(temperature: USCustomaryTemperature) = USCustomaryHeatCapacity(this, temperature)
