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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

val MagneticInductionUnits: Set<MagneticInduction> get() = setOf(
    Tesla,
    Nanotesla,
    Microtesla,
    Millitesla,
    Centitesla,
    Decitesla,
    Decatesla,
    Hectotesla,
    Kilotesla,
    Megatesla,
    Gigatesla,
    Gauss
)

@Serializable
sealed class MagneticInduction : AbstractScientificUnit<PhysicalQuantity.MagneticInduction>(), MetricAndImperialScientificUnit<PhysicalQuantity.MagneticInduction>

@Serializable
object Tesla : MagneticInduction(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction> {
    override val symbol = "T"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.MagneticInduction
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanotesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Nano(Tesla)
@Serializable
object Microtesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Micro(Tesla)
@Serializable
object Millitesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Milli(Tesla)
@Serializable
object Centitesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Centi(Tesla)
@Serializable
object Decitesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Deci(Tesla)
@Serializable
object Decatesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Deca(Tesla)
@Serializable
object Hectotesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Hecto(Tesla)
@Serializable
object Kilotesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Kilo(Tesla)
@Serializable
object Megatesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Mega(Tesla)
@Serializable
object Gigatesla : MagneticInduction(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Giga(Tesla)
@Serializable
object Gauss : MagneticInduction() {
    private const val GAUSS_IN_TESLA = 10000.0
    override val symbol = "G"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.MagneticInduction
    override fun fromSIUnit(value: Decimal): Decimal = value * GAUSS_IN_TESLA.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / GAUSS_IN_TESLA.toDecimal()
}
