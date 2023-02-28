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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [Radioactivity]
 */
val RadioactivityUnits: Set<Radioactivity> get() = setOf(
    Becquerel,
    Nanobecquerel,
    Microbecquerel,
    Millibecquerel,
    Centibecquerel,
    Decibecquerel,
    Decabecquerel,
    Hectobecquerel,
    Kilobecquerel,
    Megabecquerel,
    Gigabecquerel,
    Rutherford,
    Curie,
    Nanocurie,
    Microcurie,
    Millicurie,
    Centicurie,
    Decicurie,
    Decacurie,
    Hectocurie,
    Kilocurie,
    Megacurie,
    Gigacurie
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Radioactivity]
 * SI unit is [Becquerel]
 */
@Serializable
sealed class Radioactivity : AbstractScientificUnit<PhysicalQuantity.Radioactivity>(), MetricAndImperialScientificUnit<PhysicalQuantity.Radioactivity>

@Serializable
object Becquerel : Radioactivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity> {
    override val symbol = "Bq"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class BecquerelMultiple : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel>

@Serializable
object Nanobecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Nano(Becquerel)
@Serializable
object Microbecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Micro(Becquerel)
@Serializable
object Millibecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Milli(Becquerel)
@Serializable
object Centibecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Centi(Becquerel)
@Serializable
object Decibecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Deci(Becquerel)
@Serializable
object Decabecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Deca(Becquerel)
@Serializable
object Hectobecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Hecto(Becquerel)
@Serializable
object Kilobecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Kilo(Becquerel)
@Serializable
object Megabecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Mega(Becquerel)
@Serializable
object Gigabecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Giga(Becquerel)

@Serializable
object Rutherford : Radioactivity() {
    private const val RUTHERFORD_IN_BECQUEREL = 0.000001
    override val symbol = "Rd"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value * RUTHERFORD_IN_BECQUEREL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / RUTHERFORD_IN_BECQUEREL.toDecimal()
}

@Serializable
object Curie : Radioactivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity> {
    private const val BECQUEREL_IN_CURIE = 3.7e10
    override val symbol = "Ci"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value / BECQUEREL_IN_CURIE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * BECQUEREL_IN_CURIE.toDecimal()
}

@Serializable
sealed class CurieMultiple : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie>

@Serializable
object Nanocurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Nano(Curie)
@Serializable
object Microcurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Micro(Curie)
@Serializable
object Millicurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Milli(Curie)
@Serializable
object Centicurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Centi(Curie)
@Serializable
object Decicurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Deci(Curie)
@Serializable
object Decacurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Deca(Curie)
@Serializable
object Hectocurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Hecto(Curie)
@Serializable
object Kilocurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Kilo(Curie)
@Serializable
object Megacurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Mega(Curie)
@Serializable
object Gigacurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Giga(Curie)
