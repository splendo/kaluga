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

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.base.decimal.div
import com.splendo.kaluga.base.decimal.times
import com.splendo.kaluga.base.decimal.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

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
    Gigacurie,
)

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Radioactivity]
 * SI unit is [Becquerel]
 */
@Serializable
sealed class Radioactivity :
    DefinedScientificUnit<PhysicalQuantity.Radioactivity>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Radioactivity>

@Serializable
data object Becquerel : Radioactivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity> {
    override val symbol = "Bq"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class BecquerelMultiple :
    Radioactivity(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel>

@Serializable
data object Nanobecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Nano(Becquerel)

@Serializable
data object Microbecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Micro(Becquerel)

@Serializable
data object Millibecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Milli(Becquerel)

@Serializable
data object Centibecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Centi(Becquerel)

@Serializable
data object Decibecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Deci(Becquerel)

@Serializable
data object Decabecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Deca(Becquerel)

@Serializable
data object Hectobecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Hecto(Becquerel)

@Serializable
data object Kilobecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Kilo(Becquerel)

@Serializable
data object Megabecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Mega(Becquerel)

@Serializable
data object Gigabecquerel : BecquerelMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Becquerel> by Giga(Becquerel)

@Serializable
data object Rutherford : Radioactivity() {
    private val RUTHERFORD_IN_BECQUEREL = "0.000001".toDecimal()
    override val symbol = "Rd"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value * RUTHERFORD_IN_BECQUEREL
    override fun toSIUnit(value: Decimal): Decimal = value / RUTHERFORD_IN_BECQUEREL
}

@Serializable
data object Curie : Radioactivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity> {
    private val BECQUEREL_IN_CURIE = 3.7e10.toDecimal()
    override val symbol = "Ci"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value / BECQUEREL_IN_CURIE
    override fun toSIUnit(value: Decimal): Decimal = value * BECQUEREL_IN_CURIE
}

@Serializable
sealed class CurieMultiple :
    Radioactivity(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie>

@Serializable
data object Nanocurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Nano(Curie)

@Serializable
data object Microcurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Micro(Curie)

@Serializable
data object Millicurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Milli(Curie)

@Serializable
data object Centicurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Centi(Curie)

@Serializable
data object Decicurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Deci(Curie)

@Serializable
data object Decacurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Deca(Curie)

@Serializable
data object Hectocurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Hecto(Curie)

@Serializable
data object Kilocurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Kilo(Curie)

@Serializable
data object Megacurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Mega(Curie)

@Serializable
data object Gigacurie : CurieMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Radioactivity, Curie> by Giga(Curie)

internal fun SerializersModuleBuilder.setupForRadioactivity() {
    polymorphic(Radioactivity::class) {
        registerRadioactivityClasses()
    }
}

internal fun PolymorphicModuleBuilder<Radioactivity>.registerRadioactivityClasses() {
    subclass(Becquerel::class, Becquerel.serializer())
    subclass(Centibecquerel::class, Centibecquerel.serializer())
    subclass(Decabecquerel::class, Decabecquerel.serializer())
    subclass(Decibecquerel::class, Decibecquerel.serializer())
    subclass(Gigabecquerel::class, Gigabecquerel.serializer())
    subclass(Hectobecquerel::class, Hectobecquerel.serializer())
    subclass(Kilobecquerel::class, Kilobecquerel.serializer())
    subclass(Megabecquerel::class, Megabecquerel.serializer())
    subclass(Microbecquerel::class, Microbecquerel.serializer())
    subclass(Millibecquerel::class, Millibecquerel.serializer())
    subclass(Nanobecquerel::class, Nanobecquerel.serializer())
    subclass(Curie::class, Curie.serializer())
    subclass(Centicurie::class, Centicurie.serializer())
    subclass(Decacurie::class, Decacurie.serializer())
    subclass(Decicurie::class, Decicurie.serializer())
    subclass(Gigacurie::class, Gigacurie.serializer())
    subclass(Hectocurie::class, Hectocurie.serializer())
    subclass(Kilocurie::class, Kilocurie.serializer())
    subclass(Megacurie::class, Megacurie.serializer())
    subclass(Microcurie::class, Microcurie.serializer())
    subclass(Millicurie::class, Millicurie.serializer())
    subclass(Nanocurie::class, Nanocurie.serializer())
    subclass(Rutherford::class, Rutherford.serializer())
}
