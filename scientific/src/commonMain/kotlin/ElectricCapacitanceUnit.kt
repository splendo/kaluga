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
sealed class ElectricCapacitance : AbstractScientificUnit<MeasurementType.ElectricCapacitance>(), MetricAndImperialScientificUnit<MeasurementType.ElectricCapacitance>

@Serializable
object Farad : ElectricCapacitance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance> {
    override val symbol = "F"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricCapacitance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Nano(Farad)
@Serializable
object MicroFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Micro(Farad)
@Serializable
object MilliFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Milli(Farad)
@Serializable
object CentiFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Centi(Farad)
@Serializable
object DeciFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Deci(Farad)
@Serializable
object DecaFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Deca(Farad)
@Serializable
object HectoFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Hecto(Farad)
@Serializable
object KiloFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Kilo(Farad)
@Serializable
object MegaFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Mega(Farad)
@Serializable
object GigaFarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCapacitance, Farad> by Giga(Farad)

@JvmName("capacitanceFromChargeAndVoltage")
fun <
    ChargeUnit : ElectricCharge,
    VoltageUnit : Voltage,
    CapacitanceUnit : ElectricCapacitance
    >
    CapacitanceUnit.capacitance(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
) : ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit> = byDividing(charge, voltage)

@JvmName("capacitanceFromTimeAndResistance")
fun <
    CapacitanceUnit : ElectricCapacitance,
    TimeUnit : Time,
    ResistanceUnit : ElectricResistance
    >
    CapacitanceUnit.capacitance(
    time: ScientificValue<MeasurementType.Time, TimeUnit>,
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>
) : ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit> = byDividing(time, resistance)

@JvmName("timeFromCapacitanceAndResistance")
fun <
    CapacitanceUnit : ElectricCapacitance,
    TimeUnit : Time,
    ResistanceUnit : ElectricResistance
    >
    TimeUnit.duration(
    capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>,
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>
) : ScientificValue<MeasurementType.Time, TimeUnit> = byMultiplying(capacitance, resistance)

@JvmName("capacitanceFromConductanceAndFrequency")
fun <
    CapacitanceUnit : ElectricCapacitance,
    ConductanceUnit : ElectricConductance,
    FrequencyUnit : Frequency
    > CapacitanceUnit.capacitance(
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>,
    frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>
): ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit> = byDividing(conductance, frequency)

@JvmName("chargeDivVoltage")
infix operator fun <ChargeUnit : ElectricCharge, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.div(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Farad.capacitance(this, voltage)
@JvmName("timeDivResistance")
infix operator fun <TimeUnit : Time, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.Time, TimeUnit>.div(resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>) = Farad.capacitance(this, resistance)
@JvmName("conductanceDivFrequency")
infix operator fun <ConductanceUnit : ElectricConductance, FrequencyUnit : Frequency> ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>.div(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>) = Farad.capacitance(this, frequency)
@JvmName("capacitanceTimesResistance")
infix operator fun <CapacitanceUnit : ElectricCapacitance, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>.times(resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>) = Second.duration(this, resistance)
@JvmName("resistanceTimesCapacitance")
infix operator fun <CapacitanceUnit : ElectricCapacitance, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>.times(capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>) = capacitance * this