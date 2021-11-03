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

val ElectricInductanceUnits: Set<ElectricInductance> get() = setOf(
    Henry,
    Nanohenry,
    Microhenry,
    Millihenry,
    Centihenry,
    Decihenry,
    Decahenry,
    Hectohenry,
    Kilohenry,
    Megahenry,
    Gigahenry,
    Abhenry
)

@Serializable
sealed class ElectricInductance : AbstractScientificUnit<MeasurementType.ElectricInductance>(), MetricAndImperialScientificUnit<MeasurementType.ElectricInductance>

@Serializable
object Henry : ElectricInductance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance> {
    override val symbol = "H"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricInductance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanohenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Nano(Henry)
@Serializable
object Abhenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Nano(Henry) {
    override val symbol: String = "abH"
}
@Serializable
object Microhenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Micro(Henry)
@Serializable
object Millihenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Milli(Henry)
@Serializable
object Centihenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Centi(Henry)
@Serializable
object Decihenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Deci(Henry)
@Serializable
object Decahenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Deca(Henry)
@Serializable
object Hectohenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Hecto(Henry)
@Serializable
object Kilohenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Kilo(Henry)
@Serializable
object Megahenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Mega(Henry)
@Serializable
object Gigahenry : ElectricInductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricInductance, Henry> by Giga(Henry)
