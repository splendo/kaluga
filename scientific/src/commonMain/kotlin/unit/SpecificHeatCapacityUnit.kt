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

val MetricSpecificHeatCapacityUnits: Set<MetricSpecificHeatCapacity> get() = MetricHeatCapacityUnits.flatMap { heatCapacity ->
    MetricWeightUnits.map { heatCapacity per it }
}.toSet()

val UKImperialSpecificHeatCapacityUnits: Set<UKImperialSpecificHeatCapacity> get() = UKImperialHeatCapacityUnits.flatMap { heatCapacity ->
    UKImperialWeightUnits.map { heatCapacity per it }
}.toSet()

val USCustomarySpecificHeatCapacityUnits: Set<USCustomarySpecificHeatCapacity> get() = USCustomaryHeatCapacityUnits.flatMap { heatCapacity ->
    USCustomaryWeightUnits.map { heatCapacity per it }
}.toSet()

val SpecificHeatCapacityUnits: Set<SpecificHeatCapacity> get() = MetricSpecificHeatCapacityUnits +
    UKImperialSpecificHeatCapacityUnits +
    USCustomarySpecificHeatCapacityUnits

@Serializable
sealed class SpecificHeatCapacity : AbstractScientificUnit<PhysicalQuantity.SpecificHeatCapacity>() {
    abstract val heatCapacity: HeatCapacity
    abstract val perWeight: Weight
    override val quantity = PhysicalQuantity.SpecificHeatCapacity
    override val symbol: String by lazy { "${heatCapacity.energy.symbol} / (${heatCapacity.per.symbol}⋅${perWeight.symbol})" }
    override fun fromSIUnit(value: Decimal): Decimal = perWeight.toSIUnit(heatCapacity.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = heatCapacity.toSIUnit(perWeight.fromSIUnit(value))
}

@Serializable
data class MetricSpecificHeatCapacity(override val heatCapacity: MetricHeatCapacity, override val perWeight: MetricWeight) : SpecificHeatCapacity(), MetricScientificUnit<PhysicalQuantity.SpecificHeatCapacity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class UKImperialSpecificHeatCapacity(override val heatCapacity: UKImperialHeatCapacity, override val perWeight: UKImperialWeight) : SpecificHeatCapacity(), UKImperialScientificUnit<PhysicalQuantity.SpecificHeatCapacity> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomarySpecificHeatCapacity(override val heatCapacity: USCustomaryHeatCapacity, override val perWeight: USCustomaryWeight) : SpecificHeatCapacity(), USCustomaryScientificUnit<PhysicalQuantity.SpecificHeatCapacity> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricAndUKImperialHeatCapacity.per(weight: MetricWeight) = MetricSpecificHeatCapacity(metric, weight)
infix fun MetricAndUKImperialHeatCapacity.per(weight: ImperialWeight) = UKImperialSpecificHeatCapacity(ukImperial, weight.ukImperial)
infix fun MetricAndUKImperialHeatCapacity.per(weight: UKImperialWeight) = UKImperialSpecificHeatCapacity(ukImperial, weight)
infix fun MetricHeatCapacity.per(weight: MetricWeight) = MetricSpecificHeatCapacity(this, weight)
infix fun UKImperialHeatCapacity.per(weight: ImperialWeight) = UKImperialSpecificHeatCapacity(this, weight.ukImperial)
infix fun UKImperialHeatCapacity.per(weight: UKImperialWeight) = UKImperialSpecificHeatCapacity(this, weight)
infix fun USCustomaryHeatCapacity.per(weight: ImperialWeight) = USCustomarySpecificHeatCapacity(this, weight.usCustomary)
infix fun USCustomaryHeatCapacity.per(weight: USCustomaryWeight) = USCustomarySpecificHeatCapacity(this, weight)

infix fun MetricSpecificEnergy.per(temperature: MetricAndUKImperialTemperature) = MetricSpecificHeatCapacity(energy per temperature, per)
infix fun ImperialSpecificEnergy.per(temperature: MetricAndUKImperialTemperature) = UKImperialSpecificHeatCapacity(energy per temperature, per.ukImperial)
infix fun ImperialSpecificEnergy.per(temperature: USCustomaryTemperature) = USCustomarySpecificHeatCapacity(energy per temperature, per.usCustomary)
infix fun UKImperialSpecificEnergy.per(temperature: MetricAndUKImperialTemperature) = UKImperialSpecificHeatCapacity(energy per temperature, per)
infix fun USCustomarySpecificEnergy.per(temperature: USCustomaryTemperature) = USCustomarySpecificHeatCapacity(energy per temperature, per)
