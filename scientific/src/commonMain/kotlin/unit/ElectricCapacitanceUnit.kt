/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

/**
 * Set of all [ElectricCapacitance]
 */
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
    Gigafarad,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.ElectricCapacitance]
 * SI unit is [Farad]
 */
@Serializable
sealed class ElectricCapacitance :
    AbstractScientificUnit<PhysicalQuantity.ElectricCapacitance>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.ElectricCapacitance>

@Serializable
data object Farad : ElectricCapacitance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance> {
    override val symbol = "F"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.ElectricCapacitance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class FaradMultiple :
    ElectricCapacitance(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad>

@Serializable
data object Nanofarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Nano(Farad)

@Serializable
data object Microfarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Micro(Farad)

@Serializable
data object Millifarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Milli(Farad)

@Serializable
data object Centifarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Centi(Farad)

@Serializable
data object Decifarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Deci(Farad)

@Serializable
data object Decafarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Deca(Farad)

@Serializable
data object Hectofarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Hecto(Farad)

@Serializable
data object Kilofarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Kilo(Farad)

@Serializable
data object Megafarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Mega(Farad)

@Serializable
data object Gigafarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Giga(Farad)

@Serializable
data object Abfarad : FaradMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCapacitance, Farad> by Giga(Farad) {
    override val symbol: String = "abF"
}
