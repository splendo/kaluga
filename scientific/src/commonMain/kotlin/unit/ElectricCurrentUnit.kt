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
 * Set of all [ElectricCurrent]
 */
val ElectricCurrentUnits: Set<ElectricCurrent> get() = setOf(
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
    Abampere,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.ElectricCurrent]
 * SI unit is [Ampere]
 */
@Serializable
sealed class ElectricCurrent :
    AbstractScientificUnit<PhysicalQuantity.ElectricCurrent>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.ElectricCurrent>

@Serializable
data object Ampere : ElectricCurrent(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent> {
    override val symbol = "A"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.ElectricCurrent
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class AmpereMultiple :
    ElectricCurrent(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere>

@Serializable
data object Nanoampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Nano(Ampere)

@Serializable
data object Microampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Micro(Ampere)

@Serializable
data object Milliampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Milli(Ampere)

@Serializable
data object Centiampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Centi(Ampere)

@Serializable
data object Deciampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Deci(Ampere)

@Serializable
data object Decaampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Deca(Ampere)

@Serializable
data object Abampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Deca(Ampere) {
    override val symbol: String = "abA"
}

@Serializable
data object Biot : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Deca(Ampere) {
    override val symbol: String = "B"
}

@Serializable
data object Hectoampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Hecto(Ampere)

@Serializable
data object Kiloampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Kilo(Ampere)

@Serializable
data object Megaampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Mega(Ampere)

@Serializable
data object Gigaampere : AmpereMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricCurrent, Ampere> by Giga(Ampere)
