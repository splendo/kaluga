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
 * Set of all [ElectricInductance]
 */
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
    Abhenry,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.ElectricInductance]
 * SI unit is [Henry]
 */
@Serializable
sealed class ElectricInductance :
    AbstractScientificUnit<PhysicalQuantity.ElectricInductance>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.ElectricInductance>

@Serializable
data object Henry : ElectricInductance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance> {
    override val symbol = "H"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.ElectricInductance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class HenryMultiple :
    ElectricInductance(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry>

@Serializable
data object Nanohenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Nano(Henry)

@Serializable
data object Abhenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Nano(Henry) {
    override val symbol: String = "abH"
}

@Serializable
data object Microhenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Micro(Henry)

@Serializable
data object Millihenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Milli(Henry)

@Serializable
data object Centihenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Centi(Henry)

@Serializable
data object Decihenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Deci(Henry)

@Serializable
data object Decahenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Deca(Henry)

@Serializable
data object Hectohenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Hecto(Henry)

@Serializable
data object Kilohenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Kilo(Henry)

@Serializable
data object Megahenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Mega(Henry)

@Serializable
data object Gigahenry : HenryMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricInductance, Henry> by Giga(Henry)
