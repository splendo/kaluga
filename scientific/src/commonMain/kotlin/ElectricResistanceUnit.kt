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
sealed class ElectricResistance : AbstractScientificUnit<MeasurementType.ElectricResistance>(), MetricAndImperialScientificUnit<MeasurementType.ElectricResistance>

@Serializable
object Ohm : ElectricResistance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> {
    override val symbol = "Ω"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricResistance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Nano(Ohm)
@Serializable
object MicroOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Micro(Ohm)
@Serializable
object MilliOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Milli(Ohm)
@Serializable
object CentiOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Centi(Ohm)
@Serializable
object DeciOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Deci(Ohm)
@Serializable
object DecaOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Deca(Ohm)
@Serializable
object HectoOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Hecto(Ohm)
@Serializable
object KiloOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Kilo(Ohm)
@Serializable
object MegaOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Mega(Ohm)
@Serializable
object GigaOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Giga(Ohm)

fun <
    ResistanceUnit : ElectricResistance,
    ConductanceUnit : ElectricConductance
    > ResistanceUnit.resistance(conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>): ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit> = byInverting(conductance)

@JvmName("resistanceFromVoltageAndCurrent")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ResistanceUnit : ElectricResistance
    > ResistanceUnit.resistance(
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
): ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit> = byDividing(voltage, current)

@JvmName("resistanceFromTimeAndCapacitance")
fun <
    CapacitanceUnit : ElectricCapacitance,
    TimeUnit : Time,
    ResistanceUnit : ElectricResistance
    >
    ResistanceUnit.resistance(
    time: ScientificValue<MeasurementType.Time, TimeUnit>,
    capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>
) : ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit> = byDividing(time, capacitance)

@JvmName("resistanceFromFluxAndCharge")
fun <
    ResistanceUnit : ElectricResistance,
    ChargeUnit : ElectricCharge,
    FluxUnit : MagneticFlux
    >
    ResistanceUnit.resistance(
    flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>,
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>
) : ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit> = byDividing(flux, charge)

@JvmName("resistanceFromInductanceAndFrequency")
fun <
    ResistanceUnit : ElectricResistance,
    FrequencyUnit : Frequency,
    InductanceUnit : ElectricInductance
    >
    ResistanceUnit.resistance(
    inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>,
    frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>
) : ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit> = byMultiplying(inductance, frequency)

@JvmName("resistanceFromInductanceAndTime")
fun <
    ResistanceUnit : ElectricResistance,
    TimeUnit : Time,
    InductanceUnit : ElectricInductance
    >
    ResistanceUnit.resistance(
    inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) : ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit> = byDividing(inductance, time)

fun <ConductanceUnit : ElectricConductance> ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>.resistance() = Ohm.resistance(this)
@JvmName("voltageDivCurrent")
infix operator fun <CurrentUnit : ElectricCurrent, VoltageUnit : Voltage> ScientificValue<MeasurementType.Voltage, VoltageUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Ohm.resistance(this, current)
@JvmName("timeDivCapacitance")
infix operator fun <TimeUnit : Time, CapacitanceUnit : ElectricCapacitance> ScientificValue<MeasurementType.Time, TimeUnit>.div(capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>) = Ohm.resistance(this, capacitance)
@JvmName("fluxDivCharge")
infix operator fun <FluxUnit : MagneticFlux, ChargeUnit : ElectricCharge> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.div(charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>) = Ohm.resistance(this, charge)
@JvmName("inductanceTimesFrequency")
infix operator fun <InductanceUnit : ElectricInductance, FrequencyUnit : Frequency> ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>.times(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>) = Ohm.resistance(this, frequency)
@JvmName("frequencyTimesInductance")
infix operator fun <InductanceUnit : ElectricInductance, FrequencyUnit : Frequency> ScientificValue<MeasurementType.Frequency, FrequencyUnit>.times(inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>) = inductance * this
@JvmName("inductanceDivTime")
infix operator fun <InductanceUnit : ElectricInductance, TimeUnit : Time> ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Ohm.resistance(this, time)
