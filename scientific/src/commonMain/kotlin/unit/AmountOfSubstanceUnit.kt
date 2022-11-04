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
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

val AmountOfSubstanceUnits: Set<AmountOfSubstance> get() = setOf(
    Mole,
    Nanomole,
    Micromole,
    Millimole,
    Centimole,
    Decimole,
    Decamole,
    Hectomole,
    Kilomole,
    Megamole,
    Gigamole
)

@Serializable
sealed class AmountOfSubstance : AbstractScientificUnit<PhysicalQuantity.AmountOfSubstance>(), MetricAndImperialScientificUnit<PhysicalQuantity.AmountOfSubstance>

@Serializable
object Mole : AmountOfSubstance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance> {
    override val symbol = "mol"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.AmountOfSubstance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanomole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Nano(Mole)
@Serializable
object Micromole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Micro(Mole)
@Serializable
object Millimole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Milli(Mole)
@Serializable
object Centimole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Centi(Mole)
@Serializable
object Decimole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Deci(Mole)
@Serializable
object Decamole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Deca(Mole)
@Serializable
object Hectomole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Hecto(Mole)
@Serializable
object Kilomole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Kilo(Mole)
@Serializable
object Megamole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Mega(Mole)
@Serializable
object Gigamole : AmountOfSubstance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Giga(Mole)

val AvogadroConstant = 6.02214076e23.toDecimal()
