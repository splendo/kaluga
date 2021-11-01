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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName
import kotlin.math.ln

val RadioactivityUnits: Set<Radioactivity> = setOf(
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

@Serializable
sealed class Radioactivity : AbstractScientificUnit<MeasurementType.Radioactivity>(), MetricAndImperialScientificUnit<MeasurementType.Radioactivity>

@Serializable
object Becquerel : Radioactivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity> {
    override val symbol = "Bq"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanobecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Nano(Becquerel)
@Serializable
object Microbecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Micro(Becquerel)
@Serializable
object Millibecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Milli(Becquerel)
@Serializable
object Centibecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Centi(Becquerel)
@Serializable
object Decibecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Deci(Becquerel)
@Serializable
object Decabecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Deca(Becquerel)
@Serializable
object Hectobecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Hecto(Becquerel)
@Serializable
object Kilobecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Kilo(Becquerel)
@Serializable
object Megabecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Mega(Becquerel)
@Serializable
object Gigabecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Giga(Becquerel)

@Serializable
object Rutherford : Radioactivity() {
    private const val RUTHERFORD_IN_BECQUEREL = 0.000001
    override val symbol = "Rd"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value * RUTHERFORD_IN_BECQUEREL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / RUTHERFORD_IN_BECQUEREL.toDecimal()
}

@Serializable
object Curie : Radioactivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity> {
    private const val BECQUEREL_IN_CURIE = 3.7e10
    override val symbol = "Ci"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value / BECQUEREL_IN_CURIE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * BECQUEREL_IN_CURIE.toDecimal()
}

@Serializable
object Nanocurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Nano(Curie)
@Serializable
object Microcurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Micro(Curie)
@Serializable
object Millicurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Milli(Curie)
@Serializable
object Centicurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Centi(Curie)
@Serializable
object Decicurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Deci(Curie)
@Serializable
object Decacurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Deca(Curie)
@Serializable
object Hectocurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Hecto(Curie)
@Serializable
object Kilocurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Kilo(Curie)
@Serializable
object Megacurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Mega(Curie)
@Serializable
object Gigacurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Giga(Curie)
