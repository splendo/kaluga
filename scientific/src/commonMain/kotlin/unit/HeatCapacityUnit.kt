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

val MetricAndUKImperialHeatCapacityUnits: Set<MetricAndUKImperialHeatCapacity> get() = MetricAndImperialEnergyUnits.flatMap { energy ->
    MetricAndUkImperialTemperatureUnits.map { energy per it }
}.toSet()

val MetricHeatCapacityUnits: Set<MetricHeatCapacity> get() = MetricEnergyUnits.flatMap { energy ->
    MetricAndUkImperialTemperatureUnits.map { energy per it }
}.toSet()

val UKImperialHeatCapacityUnits: Set<UKImperialHeatCapacity> get() = ImperialEnergyUnits.flatMap { energy ->
    MetricAndUkImperialTemperatureUnits.map { energy per it }
}.toSet()

val USCustomaryHeatCapacityUnits: Set<USCustomaryHeatCapacity> get() = ImperialEnergyUnits.flatMap { energy ->
    USCustomaryTemperatureUnits.map { energy per it }
}.toSet()

val HeatCapacityUnits: Set<HeatCapacity> get() = MetricAndUKImperialHeatCapacityUnits +
    MetricHeatCapacityUnits.filter { it.energy !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    UKImperialHeatCapacityUnits.filter { it.energy !is ImperialMetricAndImperialEnergyWrapper }.toSet() +
    USCustomaryHeatCapacityUnits

@Serializable
sealed class HeatCapacity : AbstractScientificUnit<PhysicalQuantity.HeatCapacity>() {
    abstract val energy: Energy
    abstract val per: Temperature
    override val quantity = PhysicalQuantity.HeatCapacity
    override val symbol: String by lazy { "${energy.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.deltaToSIUnitDelta(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.deltaFromSIUnitDelta(value))
}

@Serializable
data class MetricAndUKImperialHeatCapacity(override val energy: MetricAndImperialEnergy, override val per: MetricAndUKImperialTemperature) : HeatCapacity(), MetricAndUKImperialScientificUnit<PhysicalQuantity.HeatCapacity> {
    override val system = MeasurementSystem.MetricAndUKImperial
    val metric get() = energy.metric per per
    val ukImperial get() = energy.imperial per per
}
@Serializable
data class MetricHeatCapacity(override val energy: MetricEnergy, override val per: MetricAndUKImperialTemperature) : HeatCapacity(), MetricScientificUnit<PhysicalQuantity.HeatCapacity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class UKImperialHeatCapacity(override val energy: ImperialEnergy, override val per: MetricAndUKImperialTemperature) : HeatCapacity(), UKImperialScientificUnit<PhysicalQuantity.HeatCapacity> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryHeatCapacity(override val energy: ImperialEnergy, override val per: USCustomaryTemperature) : HeatCapacity(), USCustomaryScientificUnit<PhysicalQuantity.HeatCapacity> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricAndImperialEnergy.per(temperature: MetricAndUKImperialTemperature) = MetricAndUKImperialHeatCapacity(this, temperature)
infix fun MetricEnergy.per(temperature: MetricAndUKImperialTemperature) = MetricHeatCapacity(this, temperature)
infix fun ImperialEnergy.per(temperature: MetricAndUKImperialTemperature) = UKImperialHeatCapacity(this, temperature)
infix fun MetricAndImperialEnergy.per(temperature: USCustomaryTemperature) = USCustomaryHeatCapacity(this.imperial, temperature)
infix fun ImperialEnergy.per(temperature: USCustomaryTemperature) = USCustomaryHeatCapacity(this, temperature)
