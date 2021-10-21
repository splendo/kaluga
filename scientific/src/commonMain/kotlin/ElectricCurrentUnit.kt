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
sealed class ElectricCurrent : AbstractScientificUnit<MeasurementType.ElectricCurrent>(), MetricAndImperialScientificUnit<MeasurementType.ElectricCurrent>

@Serializable
object Ampere : ElectricCurrent(), BaseMetricUnit<MeasurementType.ElectricCurrent, MeasurementSystem.MetricAndImperial> {
    override val symbol = "A"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricCurrent
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Nano(Ampere)
@Serializable
object MicroAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Micro(Ampere)
@Serializable
object MilliAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Milli(Ampere)
@Serializable
object CentiAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Centi(Ampere)
@Serializable
object DeciAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Deci(Ampere)
@Serializable
object DecaAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Deca(Ampere)
@Serializable
object AbAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Deca(Ampere) {
    override val symbol: String = "abA"
}
@Serializable
object Biot : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Deca(Ampere) {
    override val symbol: String = "B"
}
@Serializable
object HectoAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Hecto(Ampere)
@Serializable
object KiloAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Kilo(Ampere)
@Serializable
object MegaAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Mega(Ampere)
@Serializable
object GigaAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> by Giga(Ampere)

@JvmName("currentFromCHargeAndTime")
fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge
    >
    CurrentUnit.current(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    per: ScientificValue<MeasurementType.Time, TimeUnit>
) : ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit> = byDividing(charge, per)

@JvmName("currentFromVoltageAndConductance")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ConductanceUnit : ElectricConductance
    > CurrentUnit.current(
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
): ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit> = byMultiplying(conductance, voltage)

@JvmName("currentFromVoltageAndResistance")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ResistanceUnit : ElectricResistance
    > CurrentUnit.current(
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>,
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>
): ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit> = byDividing(voltage, resistance)

@JvmName("currentFromPowerAndVoltage")
fun <
    VoltageUnit : Voltage,
    ElectricCurrentUnit : ElectricCurrent,
    PowerUnit : Power
    > ElectricCurrentUnit.current(
    power: ScientificValue<MeasurementType.Power, PowerUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
): ScientificValue<MeasurementType.ElectricCurrent, ElectricCurrentUnit> = byDividing(power, voltage)

@JvmName("conductanceTimesVoltage")
infix operator fun <ConductanceUnit : ElectricConductance, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>.times(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Ampere.current(this, voltage)
@JvmName("voltageTimesConductance")
infix operator fun <ConductanceUnit : ElectricConductance, VoltageUnit : Voltage> ScientificValue<MeasurementType.Voltage, VoltageUnit>.times(conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>) = conductance * this
@JvmName("chargeDivTime")
infix operator fun <ChargeUnit : ElectricCharge, TimeUnit : Time> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Ampere.current(this, time)
@JvmName("voltageDivResistance")
infix operator fun <VoltageUnit : Voltage, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.Voltage, VoltageUnit>.div(resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>) = Ampere.current(this, resistance)
@JvmName("powerDivVoltage")
infix operator fun <PowerUnit : Power, VoltageUnit : Voltage> ScientificValue<MeasurementType.Power, PowerUnit>.div(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Ampere.current(this, voltage)
