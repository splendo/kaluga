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
sealed class ElectricConductance : AbstractScientificUnit<MeasurementType.ElectricConductance>(), MetricAndImperialScientificUnit<MeasurementType.ElectricConductance>

@Serializable
object Siemens : ElectricConductance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance> {
    override val symbol = "S"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricConductance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Nano(Siemens)
@Serializable
object MicroSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Micro(Siemens)
@Serializable
object MilliSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Milli(Siemens)
@Serializable
object CentiSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Centi(Siemens)
@Serializable
object DeciSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Deci(Siemens)
@Serializable
object DecaSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Deca(Siemens)
@Serializable
object HectoSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Hecto(Siemens)
@Serializable
object KiloSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Kilo(Siemens)
@Serializable
object MegaSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Mega(Siemens)
@Serializable
object GigaSiemens : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricConductance, Siemens> by Giga(Siemens)

fun <
    ResistanceUnit : ElectricResistance,
    ConductanceUnit : ElectricConductance
    > ConductanceUnit.conductance(resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>): ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit> = byInverting(resistance)

@JvmName("conductanceFromCurrentAndVoltage")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ConductanceUnit : ElectricConductance
    > ConductanceUnit.conductance(
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
): ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit> = byDividing(current, voltage)

@JvmName("conductanceFromCapacitanceAndFrequency")
fun <
    CapacitanceUnit : ElectricCapacitance,
    ConductanceUnit : ElectricConductance,
    FrequencyUnit : Frequency
    > ConductanceUnit.conductance(
    capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>,
    frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>
): ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit> = byMultiplying(capacitance, frequency)

fun <ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>.conductance() = Siemens.conductance(this)
@JvmName("currentDivVoltage")
infix operator fun <CurrentUnit : ElectricCurrent, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.div(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Siemens.conductance(this, voltage)
@JvmName("capacitanceTimesFrequency")
infix operator fun <CapacitanceUnit : ElectricCapacitance, FrequencyUnit : Frequency> ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>.times(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>) = Siemens.conductance(this, frequency)
@JvmName("frequencyTimesCapacitance")
infix operator fun <CapacitanceUnit : ElectricCapacitance, FrequencyUnit : Frequency> ScientificValue<MeasurementType.Frequency, FrequencyUnit>.times(capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>) = capacitance * this
