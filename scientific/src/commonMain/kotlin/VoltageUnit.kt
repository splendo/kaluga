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
import com.splendo.kaluga.base.utils.div
import kotlinx.serialization.Serializable

@Serializable
sealed class Voltage : AbstractScientificUnit<MeasurementType.Voltage>(), SIScientificUnit<MeasurementType.Voltage>

@Serializable
object Volt : Voltage(), BaseMetricUnit<MeasurementType.Voltage, MeasurementSystem.SI> {
    override val symbol = "V"
    override val system = MeasurementSystem.SI
    override val type = MeasurementType.Voltage
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Nano(Volt)
@Serializable
object MicroVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Micro(Volt)
@Serializable
object MilliVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Milli(Volt)
@Serializable
object CentiVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Centi(Volt)
@Serializable
object DeciVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Deci(Volt)
@Serializable
object DecaVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Deca(Volt)
@Serializable
object HectoVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Hecto(Volt)
@Serializable
object KiloVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Kilo(Volt)
@Serializable
object MegaVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Mega(Volt)
@Serializable
object GigaVolt : Voltage(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.Voltage> by Giga(Volt)

fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ConductanceUnit : ElectricConductance
    > VoltageUnit.voltage(
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>,
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>
): ScientificValue<MeasurementType.Voltage, VoltageUnit> {
    return ScientificValue(current.convertValue(Ampere) / conductance.convertValue(Siemens), Volt).convert(this)
}

infix operator fun <CurrentUnit : ElectricCurrent, ConductanceUnit : ElectricConductance> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.div(conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>) = Volt.voltage(this, conductance)
