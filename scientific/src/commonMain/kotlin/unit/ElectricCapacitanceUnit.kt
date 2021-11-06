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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

val ElectricCapacitanceUnits: Set<ElectricCapacitance> get() = setOf(
    Farad,
    Nanofarad,
    Microfarad,
    Millifarad,
    Centifarad,
    Decifarad,
    Decafarad,
    Hectofarad,
    Kilofarad,
    Megafarad,
    Gigafarad
)

@Serializable
sealed class ElectricCapacitance : AbstractScientificUnit<PhysicalQuantity.ElectricCapacitance>(), MetricAndImperialScientificUnit<PhysicalQuantity.ElectricCapacitance>

@Serializable
object Farad : ElectricCapacitance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance> {
    override val symbol = "F"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.ElectricCapacitance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanofarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Nano(Farad)
@Serializable
object Microfarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Micro(Farad)
@Serializable
object Millifarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Milli(Farad)
@Serializable
object Centifarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Centi(Farad)
@Serializable
object Decifarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Deci(Farad)
@Serializable
object Decafarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Deca(Farad)
@Serializable
object Hectofarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Hecto(Farad)
@Serializable
object Kilofarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Kilo(Farad)
@Serializable
object Megafarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Mega(Farad)
@Serializable
object Gigafarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Giga(Farad)
@Serializable
object Abfarad : ElectricCapacitance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Giga(Farad) {
    override val symbol: String = "abF"
}
