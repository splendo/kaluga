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

val ElectricCurrentUnits = setOf(
    Ampere,
    Nanoampere,
    Microampere,
    Milliampere,
    Centiampere,
    Deciampere,
    Decaampere,
    Hectoampere,
    Kiloampere,
    Megaampere,
    Gigaampere,
    Biot,
    Abampere
)

@Serializable
sealed class ElectricCurrent : AbstractScientificUnit<MeasurementType.ElectricCurrent>(), MetricAndImperialScientificUnit<MeasurementType.ElectricCurrent>

@Serializable
object Ampere : ElectricCurrent(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent> {
    override val symbol = "A"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricCurrent
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanoampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Nano(Ampere)
@Serializable
object Microampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Micro(Ampere)
@Serializable
object Milliampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Milli(Ampere)
@Serializable
object Centiampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Centi(Ampere)
@Serializable
object Deciampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Deci(Ampere)
@Serializable
object Decaampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Deca(Ampere)
@Serializable
object Abampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Deca(Ampere) {
    override val symbol: String = "abA"
}
@Serializable
object Biot : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Deca(Ampere) {
    override val symbol: String = "B"
}
@Serializable
object Hectoampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Hecto(Ampere)
@Serializable
object Kiloampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Kilo(Ampere)
@Serializable
object Megaampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Mega(Ampere)
@Serializable
object Gigaampere : ElectricCurrent(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricCurrent, Ampere> by Giga(Ampere)
