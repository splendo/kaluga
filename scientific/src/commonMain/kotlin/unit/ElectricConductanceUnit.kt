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
 * Set of all [ElectricConductance]
 */
val ElectricConductanceUnits: Set<ElectricConductance> get() = setOf(
    Siemens,
    Nanosiemens,
    Microsiemens,
    Millisiemens,
    Centisiemens,
    Decisiemens,
    Decasiemens,
    Hectosiemens,
    Kilosiemens,
    Megasiemens,
    Gigasiemens,
    Absiemens,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.ElectricConductance]
 * SI unit is [Siemens]
 */
@Serializable
sealed class ElectricConductance : AbstractScientificUnit<PhysicalQuantity.ElectricConductance>(), MetricAndImperialScientificUnit<PhysicalQuantity.ElectricConductance>

@Serializable
data object Siemens : ElectricConductance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance> {
    override val symbol = "S"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.ElectricConductance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class SiemensMultiple : ElectricConductance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens>

@Serializable
data object Nanosiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Nano(Siemens)

@Serializable
data object Microsiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Micro(Siemens)

@Serializable
data object Millisiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Milli(Siemens)

@Serializable
data object Centisiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Centi(Siemens)

@Serializable
data object Decisiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Deci(Siemens)

@Serializable
data object Decasiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Deca(Siemens)

@Serializable
data object Hectosiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Hecto(Siemens)

@Serializable
data object Kilosiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Kilo(Siemens)

@Serializable
data object Megasiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Mega(Siemens)

@Serializable
data object Gigasiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Giga(Siemens)

@Serializable
data object Absiemens : SiemensMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricConductance, Siemens> by Giga(Siemens) {
    override val symbol: String = "abS"
}
