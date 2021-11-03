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
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlinx.serialization.Serializable

val VoltageUnits: Set<Voltage> get() = setOf(
    Volt,
    Nanovolt,
    Microvolt,
    Millivolt,
    Centivolt,
    Decivolt,
    Decavolt,
    Hectovolt,
    Kilovolt,
    Megavolt,
    Gigavolt,
    Abvolt
)

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
object Nanovolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Nano(Volt)
@Serializable
object Abvolt : Voltage() {
    private const val ABVOLT_IN_VOLT = 100000000.0
    override val symbol = "abV"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Voltage
    override fun fromSIUnit(value: Decimal): Decimal = value * ABVOLT_IN_VOLT.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / ABVOLT_IN_VOLT.toDecimal()
}
@Serializable
object Microvolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Micro(Volt)
@Serializable
object Millivolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Milli(Volt)
@Serializable
object Centivolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Centi(Volt)
@Serializable
object Decivolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Deci(Volt)
@Serializable
object Decavolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Deca(Volt)
@Serializable
object Hectovolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Hecto(Volt)
@Serializable
object Kilovolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Kilo(Volt)
@Serializable
object Megavolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Mega(Volt)
@Serializable
object Gigavolt : Voltage(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Voltage, Volt> by Giga(Volt)
