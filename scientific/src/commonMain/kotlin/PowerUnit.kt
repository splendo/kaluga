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

@Serializable
sealed class Power : AbstractScientificUnit<MeasurementType.Power>()

@Serializable
sealed class MetricPower : Power(), MetricScientificUnit<MeasurementType.Power>

@Serializable
object Watt : MetricPower(), BaseMetricUnit<MeasurementType.Power, MeasurementSystem.Metric> {
    override val symbol: String = "W"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Power
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

fun <
    EnergyUnit : Energy,
    TimeUnit : Time,
    PowerUnit : Power
    > PowerUnit.power(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
): ScientificValue<MeasurementType.Power, PowerUnit> = byDividing(energy, time)

fun <
    EnergyUnit : Energy,
    TimeUnit : Time,
    PowerUnit : Power
    > TimeUnit.time(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    power: ScientificValue<MeasurementType.Power, PowerUnit>
): ScientificValue<MeasurementType.Time, TimeUnit> = byDividing(energy, power)

fun <
    VoltageUnit : Voltage,
    ElectricCurrentUnit : ElectricCurrent,
    PowerUnit : Power
    > PowerUnit.power(
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, ElectricCurrentUnit>
): ScientificValue<MeasurementType.Power, PowerUnit> = byMultiplying(voltage, current)

infix operator fun <VoltageUnit : Voltage, ElectricCurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.Voltage, VoltageUnit>.times(current: ScientificValue<MeasurementType.ElectricCurrent, ElectricCurrentUnit>) = Watt.power(this, current)
infix operator fun <VoltageUnit : Voltage, ElectricCurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.ElectricCurrent, ElectricCurrentUnit>.times(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = voltage * this
infix operator fun <EnergyUnit : Energy, TimeUnit : Time> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Watt.power(this, time)
infix operator fun <EnergyUnit : Energy, PowerUnit : Power> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(power: ScientificValue<MeasurementType.Power, PowerUnit>) = Second.time(this, power)
