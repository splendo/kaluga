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
 * Set of all [MetricSpecificHeatCapacity]
 */
val MetricSpecificHeatCapacityUnits: Set<MetricSpecificHeatCapacity> get() = MetricHeatCapacityUnits.flatMap { heatCapacity ->
    MetricWeightUnits.map { heatCapacity per it }
}.toSet()

/**
 * Set of all [UKImperialSpecificHeatCapacity]
 */
val UKImperialSpecificHeatCapacityUnits: Set<UKImperialSpecificHeatCapacity> get() = UKImperialHeatCapacityUnits.flatMap { heatCapacity ->
    UKImperialWeightUnits.map { heatCapacity per it }
}.toSet()

/**
 * Set of all [USCustomarySpecificHeatCapacity]
 */
val USCustomarySpecificHeatCapacityUnits: Set<USCustomarySpecificHeatCapacity> get() = USCustomaryHeatCapacityUnits.flatMap { heatCapacity ->
    USCustomaryWeightUnits.map { heatCapacity per it }
}.toSet()

/**
 * Set of all [SpecificHeatCapacity]
 */
val SpecificHeatCapacityUnits: Set<SpecificHeatCapacity> get() = MetricSpecificHeatCapacityUnits +
    UKImperialSpecificHeatCapacityUnits +
    USCustomarySpecificHeatCapacityUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.SpecificHeatCapacity]
 * SI unit is `Joule per Kelvin per Kilogram`
 */
@Serializable
sealed class SpecificHeatCapacity : AbstractScientificUnit<PhysicalQuantity.SpecificHeatCapacity>() {

    /**
     * The [HeatCapacity] component
     */
    abstract val heatCapacity: HeatCapacity

    /**
     * The [Weight] component
     */
    abstract val perWeight: Weight
    override val quantity = PhysicalQuantity.SpecificHeatCapacity
    override val symbol: String by lazy { "${heatCapacity.energy.symbol}/${heatCapacity.per.symbol}⋅${perWeight.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = perWeight.toSIUnit(heatCapacity.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = heatCapacity.toSIUnit(perWeight.fromSIUnit(value))
}

/**
 * A [SpecificHeatCapacity] for [MeasurementSystem.Metric]
 * @param heatCapacity the [MetricHeatCapacity] component
 * @param perWeight the [MetricWeight] component
 */
@Serializable
data class MetricSpecificHeatCapacity(override val heatCapacity: MetricHeatCapacity, override val perWeight: MetricWeight) :
    SpecificHeatCapacity(),
    MetricScientificUnit<PhysicalQuantity.SpecificHeatCapacity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [SpecificHeatCapacity] for [MeasurementSystem.UKImperial]
 * @param heatCapacity the [UKImperialHeatCapacity] component
 * @param perWeight the [UKImperialWeight] component
 */
@Serializable
data class UKImperialSpecificHeatCapacity(override val heatCapacity: UKImperialHeatCapacity, override val perWeight: UKImperialWeight) :
    SpecificHeatCapacity(),
    UKImperialScientificUnit<PhysicalQuantity.SpecificHeatCapacity> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [SpecificHeatCapacity] for [MeasurementSystem.USCustomary]
 * @param heatCapacity the [USCustomaryHeatCapacity] component
 * @param perWeight the [USCustomaryWeight] component
 */
@Serializable
data class USCustomarySpecificHeatCapacity(override val heatCapacity: USCustomaryHeatCapacity, override val perWeight: USCustomaryWeight) :
    SpecificHeatCapacity(),
    USCustomaryScientificUnit<PhysicalQuantity.SpecificHeatCapacity> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricSpecificHeatCapacity] from a [MetricAndUKImperialHeatCapacity] and a [MetricWeight]
 * @param weight the [MetricWeight] component
 * @return the [MetricSpecificHeatCapacity] represented by the units
 */
infix fun MetricAndUKImperialHeatCapacity.per(weight: MetricWeight) = MetricSpecificHeatCapacity(metric, weight)

/**
 * Gets a [UKImperialSpecificHeatCapacity] from a [MetricAndUKImperialHeatCapacity] and an [ImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [UKImperialSpecificHeatCapacity] represented by the units
 */
infix fun MetricAndUKImperialHeatCapacity.per(weight: ImperialWeight) = UKImperialSpecificHeatCapacity(ukImperial, weight.ukImperial)

/**
 * Gets a [UKImperialSpecificHeatCapacity] from a [MetricAndUKImperialHeatCapacity] and a [UKImperialWeight]
 * @param weight the [UKImperialWeight] component
 * @return the [UKImperialSpecificHeatCapacity] represented by the units
 */
infix fun MetricAndUKImperialHeatCapacity.per(weight: UKImperialWeight) = UKImperialSpecificHeatCapacity(ukImperial, weight)

/**
 * Gets a [MetricSpecificHeatCapacity] from a [MetricHeatCapacity] and a [MetricWeight]
 * @param weight the [MetricWeight] component
 * @return the [MetricSpecificHeatCapacity] represented by the units
 */
infix fun MetricHeatCapacity.per(weight: MetricWeight) = MetricSpecificHeatCapacity(this, weight)

/**
 * Gets a [UKImperialSpecificHeatCapacity] from a [UKImperialHeatCapacity] and an [ImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [UKImperialSpecificHeatCapacity] represented by the units
 */
infix fun UKImperialHeatCapacity.per(weight: ImperialWeight) = UKImperialSpecificHeatCapacity(this, weight.ukImperial)

/**
 * Gets a [UKImperialSpecificHeatCapacity] from a [UKImperialHeatCapacity] and a [UKImperialWeight]
 * @param weight the [UKImperialWeight] component
 * @return the [UKImperialSpecificHeatCapacity] represented by the units
 */
infix fun UKImperialHeatCapacity.per(weight: UKImperialWeight) = UKImperialSpecificHeatCapacity(this, weight)

/**
 * Gets a [USCustomarySpecificHeatCapacity] from a [USCustomaryHeatCapacity] and a [UKImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [USCustomarySpecificHeatCapacity] represented by the units
 */
infix fun USCustomaryHeatCapacity.per(weight: ImperialWeight) = USCustomarySpecificHeatCapacity(this, weight.usCustomary)

/**
 * Gets a [USCustomarySpecificHeatCapacity] from a [USCustomaryHeatCapacity] and a [UKImperialWeight]
 * @param weight the [USCustomaryWeight] component
 * @return the [USCustomarySpecificHeatCapacity] represented by the units
 */
infix fun USCustomaryHeatCapacity.per(weight: USCustomaryWeight) = USCustomarySpecificHeatCapacity(this, weight)

/**
 * Gets a [MetricSpecificHeatCapacity] from a [MetricSpecificEnergy] and a [MetricAndUKImperialTemperature]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @return the [MetricSpecificHeatCapacity] represented by the units
 */
infix fun MetricSpecificEnergy.per(temperature: MetricAndUKImperialTemperature) = MetricSpecificHeatCapacity(energy per temperature, per)

/**
 * Gets a [UKImperialSpecificHeatCapacity] from an [ImperialSpecificEnergy] and a [MetricAndUKImperialTemperature]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @return the [UKImperialSpecificHeatCapacity] represented by the units
 */
infix fun ImperialSpecificEnergy.per(temperature: MetricAndUKImperialTemperature) = UKImperialSpecificHeatCapacity(energy per temperature, per.ukImperial)

/**
 * Gets a [USCustomarySpecificHeatCapacity] from an [ImperialSpecificEnergy] and a [USCustomaryTemperature]
 * @param temperature the [USCustomaryTemperature] component
 * @return the [USCustomarySpecificHeatCapacity] represented by the units
 */
infix fun ImperialSpecificEnergy.per(temperature: USCustomaryTemperature) = USCustomarySpecificHeatCapacity(energy per temperature, per.usCustomary)

/**
 * Gets a [UKImperialSpecificHeatCapacity] from a [UKImperialSpecificEnergy] and a [MetricAndUKImperialTemperature]
 * @param temperature the [MetricAndUKImperialTemperature] component
 * @return the [UKImperialSpecificHeatCapacity] represented by the units
 */
infix fun UKImperialSpecificEnergy.per(temperature: MetricAndUKImperialTemperature) = UKImperialSpecificHeatCapacity(energy per temperature, per)

/**
 * Gets a [USCustomarySpecificHeatCapacity] from a [USCustomarySpecificEnergy] and a [USCustomaryTemperature]
 * @param temperature the [USCustomaryTemperature] component
 * @return the [USCustomarySpecificHeatCapacity] represented by the units
 */
infix fun USCustomarySpecificEnergy.per(temperature: USCustomaryTemperature) = USCustomarySpecificHeatCapacity(energy per temperature, per)
