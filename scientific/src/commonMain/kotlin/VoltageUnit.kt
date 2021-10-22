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

@Serializable
sealed class Voltage : AbstractScientificUnit<MeasurementType.Voltage>(), MetricAndImperialScientificUnit<MeasurementType.Voltage>

@Serializable
object Volt : Voltage(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage> {
    override val symbol = "V"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Voltage
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Nano(Volt)
@Serializable
object MicroVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Micro(Volt)
@Serializable
object MilliVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Milli(Volt)
@Serializable
object CentiVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Centi(Volt)
@Serializable
object DeciVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Deci(Volt)
@Serializable
object DecaVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Deca(Volt)
@Serializable
object HectoVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Hecto(Volt)
@Serializable
object KiloVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Kilo(Volt)
@Serializable
object MegaVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Mega(Volt)
@Serializable
object GigaVolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Giga(Volt)

@JvmName("voltageFromCurrentAndResistance")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ResistanceUnit : ElectricResistance
    > VoltageUnit.voltage(
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>,
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>
): ScientificValue<MeasurementType.Voltage, VoltageUnit> = byMultiplying(current, resistance)

@JvmName("voltageFromCurrentAndConductance")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ConductanceUnit : ElectricConductance
    > VoltageUnit.voltage(
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>,
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>
): ScientificValue<MeasurementType.Voltage, VoltageUnit> = byDividing(current, conductance)

@JvmName("voltageFromEnergyAndCharge")
fun <
    VoltageUnit : Voltage,
    EnergyUnit : Energy,
    ChargeUnit : ElectricCharge
    > VoltageUnit.voltage(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>
): ScientificValue<MeasurementType.Voltage, VoltageUnit> = byDividing(energy, charge)

@JvmName("voltageFromPowerAndCurrent")
fun <
    VoltageUnit : Voltage,
    ElectricCurrentUnit : ElectricCurrent,
    PowerUnit : Power
    > VoltageUnit.voltage(
    power: ScientificValue<MeasurementType.Power, PowerUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, ElectricCurrentUnit>
): ScientificValue<MeasurementType.Voltage, VoltageUnit> = byDividing(power, current)

@JvmName("voltageFromChargeAndCapacitance")
fun <
    ChargeUnit : ElectricCharge,
    VoltageUnit : Voltage,
    CapacitanceUnit : ElectricCapacitance
    >
    VoltageUnit.voltage(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>
) : ScientificValue<MeasurementType.Voltage, VoltageUnit> = byDividing(charge, capacitance)

@JvmName("voltageFromFluxAndTime")
fun <
    VoltageUnit : Voltage,
    TimeUnit : Time,
    FluxUnit : MagneticFlux
    >
    VoltageUnit.voltage(
    flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) : ScientificValue<MeasurementType.Voltage, VoltageUnit> = byDividing(flux, time)

@JvmName("currentTimesResistance")
infix operator fun <CurrentUnit : ElectricCurrent, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.times(resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>) = Volt.voltage(this, resistance)
@JvmName("resistanceTimesCurrent")
infix operator fun <CurrentUnit : ElectricCurrent, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>.times(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = current * this
@JvmName("currentDivConductance")
infix operator fun <CurrentUnit : ElectricCurrent, ConductanceUnit : ElectricConductance> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.div(conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>) = Volt.voltage(this, conductance)
@JvmName("energyDivCharge")
infix operator fun <EnergyUnit : Energy, ChargeUnit : ElectricCharge> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>) = Volt.voltage(this, charge)
@JvmName("powerDivCurrent")
infix operator fun <PowerUnit : Power, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.Power, PowerUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Volt.voltage(this, current)
@JvmName("chargeDivCapacitance")
infix operator fun <ChargeUnit : ElectricCharge, CapacitanceUnit : ElectricCapacitance> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.div(capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>) = Volt.voltage(this, capacitance)
@JvmName("fluxDivTime")
infix operator fun <FluxUnit : MagneticFlux, TimeUnit : Time> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Volt.voltage(this, time)
