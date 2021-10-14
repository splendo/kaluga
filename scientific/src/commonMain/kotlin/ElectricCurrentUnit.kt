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

@Serializable
sealed class ElectricCurrent : AbstractScientificUnit<MeasurementType.ElectricCurrent>(), SIScientificUnit<MeasurementType.ElectricCurrent>

@Serializable
object Ampere : ElectricCurrent(), BaseMetricUnit<MeasurementType.ElectricCurrent, MeasurementSystem.SI> {
    override val symbol = "A"
    override val system = MeasurementSystem.SI
    override val type = MeasurementType.ElectricCurrent
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Nano(Ampere)
@Serializable
object MicroAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Micro(Ampere)
@Serializable
object MilliAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Milli(Ampere)
@Serializable
object CentiAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Centi(Ampere)
@Serializable
object DeciAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Deci(Ampere)
@Serializable
object DecaAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Deca(Ampere)
@Serializable
object AbAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Deca(Ampere) {
    override val symbol: String = "abA"
}
@Serializable
object Biot : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Deca(Ampere) {
    override val symbol: String = "B"
}
@Serializable
object HectoAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Hecto(Ampere)
@Serializable
object KiloAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Kilo(Ampere)
@Serializable
object MegaAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Mega(Ampere)
@Serializable
object GigaAmpere : ElectricCurrent(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCurrent> by Giga(Ampere)

fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ConductanceUnit : ElectricConductance
    > CurrentUnit.current(
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
): ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit> {
    return ScientificValue(conductance.convertValue(Siemens) * voltage.convertValue(Volt), Ampere).convert(this)
}

infix operator fun <ConductanceUnit : ElectricConductance, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>.times(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Ampere.current(this, voltage)
infix operator fun <ConductanceUnit : ElectricConductance, VoltageUnit : Voltage> ScientificValue<MeasurementType.Voltage, VoltageUnit>.times(conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>) = Ampere.current(conductance, this)
