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
sealed class MagneticFlux : AbstractScientificUnit<MeasurementType.MagneticFlux>(), MetricAndImperialScientificUnit<MeasurementType.MagneticFlux>

@Serializable
object Weber : MagneticFlux(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux> {
    override val symbol = "Wb"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.MagneticFlux
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Nano(Weber)
@Serializable
object MicroWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Micro(Weber)
@Serializable
object MilliWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Milli(Weber)
@Serializable
object CentiWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Centi(Weber)
@Serializable
object DeciWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Deci(Weber)
@Serializable
object DecaWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Deca(Weber)
@Serializable
object HectoWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Hecto(Weber)
@Serializable
object KiloWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Kilo(Weber)
@Serializable
object MegaWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Mega(Weber)
@Serializable
object GigaWeber : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.MagneticFlux, Weber> by Giga(Weber)

@JvmName("fluxFromResistanceAndCharge")
fun <
    ResistanceUnit : ElectricResistance,
    ChargeUnit : ElectricCharge,
    FluxUnit : MagneticFlux
    >
    FluxUnit.flux(
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>,
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>
) : ScientificValue<MeasurementType.MagneticFlux, FluxUnit> = byMultiplying(resistance, charge)

@JvmName("fluxFromVoltageAndTime")
fun <
    VoltageUnit : Voltage,
    TimeUnit : Time,
    FluxUnit : MagneticFlux
    >
    FluxUnit.flux(
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) : ScientificValue<MeasurementType.MagneticFlux, FluxUnit> = byMultiplying(voltage, time)

@JvmName("timeFromFluxAndVoltage")
fun <
    VoltageUnit : Voltage,
    TimeUnit : Time,
    FluxUnit : MagneticFlux
    >
    TimeUnit.time(
    flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
) : ScientificValue<MeasurementType.Time, TimeUnit> = byDividing(flux, voltage)


@JvmName("fluxFromEnergyAndCurrent")
fun <
    EnergyUnit : Energy,
    CurrentUnit : ElectricCurrent,
    FluxUnit : MagneticFlux
    >
    FluxUnit.flux(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
) : ScientificValue<MeasurementType.MagneticFlux, FluxUnit> = byDividing(energy, current)

@JvmName("fluxFromInductionAndArea")
fun <
    FluxUnit : MagneticFlux,
    AreaUnit : Area,
    InductionUnit : MagneticInduction
    >
    FluxUnit.flux(
    induction: ScientificValue<MeasurementType.MagneticInduction, InductionUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) : ScientificValue<MeasurementType.MagneticFlux, FluxUnit> = byMultiplying(induction, area)

@JvmName("fluxFromInductanceAndCurrent")
fun <
    FluxUnit : MagneticFlux,
    CurrentUnit : ElectricCurrent,
    InductanceUnit : ElectricInductance
    >
    FluxUnit.flux(
    inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
) : ScientificValue<MeasurementType.MagneticFlux, FluxUnit> = byMultiplying(inductance, current)

@JvmName("resistanceTimesCharge")
infix operator fun <ResistanceUnit : ElectricResistance, ChargeUnit : ElectricCharge> ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>.times(charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>) = Weber.flux(this, charge)
@JvmName("chargeTimesResistance")
infix operator fun <ResistanceUnit : ElectricResistance, ChargeUnit : ElectricCharge> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.times(resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>) = resistance * this
@JvmName("voltageTimesTime")
infix operator fun <VoltageUnit : Voltage, TimeUnit : Time> ScientificValue<MeasurementType.Voltage, VoltageUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Weber.flux(this, time)
@JvmName("timeTimesVoltage")
infix operator fun <VoltageUnit : Voltage, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = voltage * this
@JvmName("fluxDivVoltage")
infix operator fun <FluxUnit : MagneticFlux, VoltageUnit : Voltage> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.div(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Second.time(this, voltage)
@JvmName("energyDivCurrent")
infix operator fun <EnergyUnit : Energy, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Weber.flux(this, current)
@JvmName("inductionTimesArea")
infix operator fun <InductionUnit : MagneticInduction, AreaUnit : Area> ScientificValue<MeasurementType.MagneticInduction, InductionUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Weber.flux(this, area)
@JvmName("areaTimesInduction")
infix operator fun <InductionUnit : MagneticInduction, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(induction: ScientificValue<MeasurementType.MagneticInduction, InductionUnit>) = induction * this
@JvmName("inductanceTimesCurrent")
infix operator fun <InductanceUnit : ElectricInductance, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>.times(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Weber.flux(this, current)
@JvmName("currentTimesInductance")
infix operator fun <InductanceUnit : ElectricInductance, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.times(inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>) = inductance * this
