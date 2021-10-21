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
import com.splendo.kaluga.base.utils.times
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

@Serializable
sealed class Energy : AbstractScientificUnit<MeasurementType.Energy>()

@Serializable
sealed class MetricEnergy : Energy(), MetricScientificUnit<MeasurementType.Energy>

@Serializable
object Joule : MetricEnergy(), BaseMetricUnit<MeasurementType.Energy, MeasurementSystem.Metric> {
    override val symbol: String = "J"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Energy
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@JvmName("energyFromForceAndDistance")
fun <
    EnergyUnit : Energy,
    ForceUnit : Force,
    LengthUnit : Length
    >
    EnergyUnit.energy(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    distance: ScientificValue<MeasurementType.Length, LengthUnit>
): ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(force, distance)

@JvmName("energyFromPressureAndVolume")
fun <
    EnergyUnit : Energy,
    PressureUnit : Pressure,
    VolumeUnit : Volume
    >
    EnergyUnit.energy(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>,
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
): ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(pressure, volume)

@JvmName("energyFromChargeAndVoltage")
fun <
    EnergyUnit : Energy,
    ChargeUnit : ElectricCharge,
    VoltageUnit : Voltage
    >
    EnergyUnit.energy(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
): ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(charge, voltage)

@JvmName("energyFromPowerAndTime")
fun <
    EnergyUnit : Energy,
    TimeUnit : Time,
    PowerUnit : Power
    > EnergyUnit.energy(
    power: ScientificValue<MeasurementType.Power, PowerUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
): ScientificValue<MeasurementType.Energy, EnergyUnit> = byMultiplying(power, time)

@JvmName("forceTimesLength")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<MeasurementType.Force, ForceUnit>.times(distance: ScientificValue<MeasurementType.Length, LengthUnit>) = Joule.energy(this, distance)
@JvmName("lengthTimesForce")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("pressureTimesVolume")
infix operator fun <PressureUnit : Pressure, VolumeUnit : Volume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = Joule.energy(this, volume)
@JvmName("volumeTimesPressure")
infix operator fun <PressureUnit : Pressure, VolumeUnit : Volume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = pressure * this
@JvmName("chargeTimesVoltage")
infix operator fun <ChargeUnit : ElectricCharge, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.times(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Joule.energy(this, voltage)
@JvmName("voltageTimesCharge")
infix operator fun <ChargeUnit : ElectricCharge, VoltageUnit : Voltage> ScientificValue<MeasurementType.Voltage, VoltageUnit>.times(charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>) = charge * this
@JvmName("powerTimesTime")
infix operator fun <PowerUnit : Power, TimeUnit : Time> ScientificValue<MeasurementType.Power, PowerUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Joule.energy(this, time)
@JvmName("timeTimesPower")
infix operator fun <PowerUnit : Power, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, PowerUnit>) = power * this
