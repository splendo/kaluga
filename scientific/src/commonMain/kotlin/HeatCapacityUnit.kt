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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricAndUKImperialHeatCapacityUnits: Set<MetricAndUKImperialHeatCapacity> = MetricAndImperialEnergyUnits.flatMap { energy ->
    MetricAndUkImperialTemperatureUnits.map { energy per it }
}.toSet()

val MetricHeatCapacityUnits: Set<MetricHeatCapacity> = MetricEnergyUnits.flatMap { energy ->
    MetricAndUkImperialTemperatureUnits.map { energy per it }
}.toSet()

val UKImperialHeatCapacityUnits: Set<UKImperialHeatCapacity> = ImperialEnergyUnits.flatMap { energy ->
    MetricAndUkImperialTemperatureUnits.map { energy per it }
}.toSet()

val USCustomaryHeatCapacityUnits: Set<USCustomaryHeatCapacity> = ImperialEnergyUnits.flatMap { energy ->
    USCustomaryTemperatureUnits.map { energy per it }
}.toSet()

val HeatCapacityUnits: Set<HeatCapacity> = MetricAndUKImperialHeatCapacityUnits +
    MetricHeatCapacityUnits.filter { it.energy !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    UKImperialHeatCapacityUnits.filter { it.energy !is ImperialMetricAndImperialEnergyWrapper }.toSet() +
    USCustomaryHeatCapacityUnits

@Serializable
sealed class HeatCapacity : AbstractScientificUnit<MeasurementType.HeatCapacity>() {
    abstract val energy: Energy
    abstract val per: Temperature
    override val type = MeasurementType.HeatCapacity
    override val symbol: String by lazy { "${energy.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricAndUKImperialHeatCapacity(override val energy: MetricAndImperialEnergy, override val per: MetricAndUKImperialTemperature) : HeatCapacity(), MetricAndUKImperialScientificUnit<MeasurementType.HeatCapacity> {
    override val system = MeasurementSystem.MetricAndUKImperial
    val metric get() = energy.metric per per
    val ukImperial get() = energy.imperial per per
}
@Serializable
data class MetricHeatCapacity(override val energy: MetricEnergy, override val per: MetricAndUKImperialTemperature) : HeatCapacity(), MetricScientificUnit<MeasurementType.HeatCapacity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class UKImperialHeatCapacity(override val energy: ImperialEnergy, override val per: MetricAndUKImperialTemperature) : HeatCapacity(), UKImperialScientificUnit<MeasurementType.HeatCapacity> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryHeatCapacity(override val energy: ImperialEnergy, override val per: USCustomaryTemperature) : HeatCapacity(), USCustomaryScientificUnit<MeasurementType.HeatCapacity> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricAndImperialEnergy.per(temperature: MetricAndUKImperialTemperature) = MetricAndUKImperialHeatCapacity(this, temperature)
infix fun MetricEnergy.per(temperature: MetricAndUKImperialTemperature) = MetricHeatCapacity(this, temperature)
infix fun ImperialEnergy.per(temperature: MetricAndUKImperialTemperature) = UKImperialHeatCapacity(this, temperature)
infix fun MetricAndImperialEnergy.per(temperature: USCustomaryTemperature) = USCustomaryHeatCapacity(this.imperial, temperature)
infix fun ImperialEnergy.per(temperature: USCustomaryTemperature) = USCustomaryHeatCapacity(this, temperature)

@JvmName("heatCapacityFromEnergyAndTemperature")
fun <
    EnergyUnit : Energy,
    TemperatureUnit : Temperature,
    HeatCapacityUnit : HeatCapacity
    > HeatCapacityUnit.heatCapacity(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>
) = byDividing(energy, temperature)

@JvmName("energyFromHeatCapacityAndTemperature")
fun <
    EnergyUnit : Energy,
    TemperatureUnit : Temperature,
    HeatCapacityUnit : HeatCapacity
    > EnergyUnit.energy(
    heatCapacity: ScientificValue<MeasurementType.HeatCapacity, HeatCapacityUnit>,
    temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>
) = byMultiplying(heatCapacity, temperature)

@JvmName("temperatureFromEnergyAndHeatCapacity")
fun <
    EnergyUnit : Energy,
    TemperatureUnit : Temperature,
    HeatCapacityUnit : HeatCapacity
    > TemperatureUnit.temperature(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    heatCapacity: ScientificValue<MeasurementType.HeatCapacity, HeatCapacityUnit>
) = byDividing(energy, heatCapacity)

@JvmName("metricAndImperialEnergyDivMetricAndUKImperialTemperature")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("metricEnergyDivMetricAndUKImperialTemperature")
infix operator fun <EnergyUnit : MetricEnergy, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("imperialEnergyDivMetricAndUKImperialTemperature")
infix operator fun <EnergyUnit : ImperialEnergy, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("metricAndImperialEnergyDivUSCustomaryTemperature")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("imperialEnergyDivUSCustomaryTemperature")
infix operator fun <EnergyUnit : ImperialEnergy, TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("energyDivTemperature")
infix operator fun <EnergyUnit : Energy, TemperatureUnit : Temperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (Joule per Kelvin).heatCapacity(this, temperature)

@JvmName("metricAndUKImperialHeatCapacityTimesMetricAndUKImperialTemperature")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit.energy).energy(this, temperature)
@JvmName("metricHeatCapacityTimesMetricAndUKImperialTemperature")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.HeatCapacity, MetricHeatCapacity>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit.energy).energy(this, temperature)
@JvmName("ukImperialHeatCapacityTimesMetricAndUKImperialTemperature")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit.energy).energy(this, temperature)
@JvmName("metricAndUKImperialHeatCapacityTimesUSCustomaryTemperature")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit.energy).energy(this, temperature)
@JvmName("usCustomaryHeatCapacityTimesUSCustomaryTemperature")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.HeatCapacity, USCustomaryHeatCapacity>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit.energy).energy(this, temperature)
@JvmName("heatCapacityTimesTemperature")
infix operator fun <HeatCapacityUnit : HeatCapacity, TemperatureUnit : Temperature> ScientificValue<MeasurementType.HeatCapacity, HeatCapacityUnit>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit.energy).energy(this, temperature)

@JvmName("metricAndUKImperialTemperatureTimesMetricAndUKImperialHeatCapacity")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>) = heatCapacity * this
@JvmName("metricAndUKImperialTemperatureTimesMetricHeatCapacity")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, MetricHeatCapacity>) = heatCapacity * this
@JvmName("metricAndUKImperialTemperatureTimesUKImperialHeatCapacity")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>) = heatCapacity * this
@JvmName("usCustomaryTemperatureTimesMetricAndUKImperialHeatCapacity")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>) = heatCapacity * this
@JvmName("usCustomaryTemperatureTimesUSCustomaryHeatCapacity")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, USCustomaryHeatCapacity>) = heatCapacity * this
@JvmName("temperatureTimesHeatCapacity")
infix operator fun <HeatCapacityUnit : HeatCapacity, TemperatureUnit : Temperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, HeatCapacityUnit>) = heatCapacity * this

@JvmName("metricAndImperialEnergyDivMetricAndUKImperialHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("metricAndImperialEnergyDivMetricHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, MetricHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("metricAndImperialEnergyDivUKImperialHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("metricEnergyDivMetricHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, MetricEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, MetricHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("ukImperialEnergyDivUKImperialHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("usCustomaryEnergyDivUSCustomaryHeatCapacity")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, USCustomaryHeatCapacity>) = heatCapacity.unit.per.temperature(this, heatCapacity)
@JvmName("energyDivHeatCapacity")
infix operator fun <EnergyUnit : Energy, HeatCapacityUnit : HeatCapacity> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(heatCapacity: ScientificValue<MeasurementType.HeatCapacity, HeatCapacityUnit>) = heatCapacity.unit.per.temperature(this, heatCapacity)
