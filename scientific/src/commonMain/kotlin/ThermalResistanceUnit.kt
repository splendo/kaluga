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

val MetricAndUKImperialThermalResistanceUnits: Set<MetricAndUKImperialThermalResistance> get() = MetricAndUkImperialTemperatureUnits.flatMap { temperature ->
    MetricAndImperialPowerUnits.map { temperature per it }
}.toSet()

val MetricThermalResistanceUnits: Set<MetricThermalResistance> get() = MetricAndUkImperialTemperatureUnits.flatMap { temperature ->
    MetricPowerUnits.map { temperature per it }
}.toSet()

val UKImperialThermalResistanceUnits: Set<UKImperialThermalResistance> get() = MetricAndUkImperialTemperatureUnits.flatMap { temperature ->
    ImperialPowerUnits.map { temperature per it }
}.toSet()

val USCustomaryThermalResistanceUnits: Set<USCustomaryThermalResistance> get() = USCustomaryTemperatureUnits.flatMap { temperature ->
    ImperialPowerUnits.map { temperature per it }
}.toSet()

val ThermalResistanceUnits: Set<ThermalResistance> get() = MetricAndUKImperialThermalResistanceUnits +
    MetricThermalResistanceUnits.filter { it.per !is MetricMetricAndImperialPowerWrapper }.toSet() +
    UKImperialThermalResistanceUnits.filter { it.per !is ImperialMetricAndImperialPowerWrapper }.toSet() +
    USCustomaryThermalResistanceUnits

@Serializable
sealed class ThermalResistance : AbstractScientificUnit<MeasurementType.ThermalResistance>() {
    abstract val temperature: Temperature
    abstract val per: Power
    override val type = MeasurementType.ThermalResistance
    override val symbol: String by lazy { "${temperature.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(temperature.deltaFromSIUnitDelta(value))
    override fun toSIUnit(value: Decimal): Decimal = temperature.deltaToSIUnitDelta(per.fromSIUnit(value))
}

@Serializable
data class MetricAndUKImperialThermalResistance(override val temperature: MetricAndUKImperialTemperature, override val per: MetricAndImperialPower) : ThermalResistance(), MetricAndUKImperialScientificUnit<MeasurementType.ThermalResistance> {
    override val system = MeasurementSystem.MetricAndUKImperial
    val metric get() = temperature per per.metric
    val ukImperial get() = temperature per per.imperial
}
@Serializable
data class MetricThermalResistance(override val temperature: MetricAndUKImperialTemperature, override val per: MetricPower) : ThermalResistance(), MetricScientificUnit<MeasurementType.ThermalResistance> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class UKImperialThermalResistance(override val temperature: MetricAndUKImperialTemperature, override val per: ImperialPower) : ThermalResistance(), UKImperialScientificUnit<MeasurementType.ThermalResistance> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryThermalResistance(override val temperature: USCustomaryTemperature, override val per: ImperialPower) : ThermalResistance(), USCustomaryScientificUnit<MeasurementType.ThermalResistance> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricAndUKImperialTemperature.per(power: MetricAndImperialPower) = MetricAndUKImperialThermalResistance(this, power)
infix fun MetricAndUKImperialTemperature.per(power: MetricPower) = MetricThermalResistance(this, power)
infix fun MetricAndUKImperialTemperature.per(power: ImperialPower) = UKImperialThermalResistance(this, power)
infix fun USCustomaryTemperature.per(power: MetricAndImperialPower) = USCustomaryThermalResistance(this, power.imperial)
infix fun USCustomaryTemperature.per(power: ImperialPower) = USCustomaryThermalResistance(this, power)
