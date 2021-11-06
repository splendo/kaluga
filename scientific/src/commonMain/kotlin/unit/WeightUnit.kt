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

val MetricWeightUnits: Set<MetricWeight> get() = setOf(
    Kilogram,
    Nanogram,
    Microgram,
    Milligram,
    Centigram,
    Decigram,
    Decagram,
    Hectogram,
    Megagram,
    Gigagram,
    Tonne,
    Dalton,
    Nanodalton,
    Microdalton,
    Millidalton,
    Centidalton,
    Decidalton,
    Decadalton,
    HectoDalton,
    Kilodalton,
    Megadalton,
    Gigadalton
)

val ImperialWeightUnits: Set<ImperialWeight> get() = setOf(
    Grain,
    Ounce,
    Pound,
    Stone,
    Slug
)

val UKImperialWeightUnits: Set<UKImperialWeight> get() = ImperialWeightUnits.map { it.ukImperial }.toSet() + setOf(ImperialTon)
val USCustomaryWeightUnits: Set<USCustomaryWeight> get() = ImperialWeightUnits.map { it.usCustomary }.toSet() + setOf(UsTon)

val WeightUnits: Set<Weight> get() = MetricWeightUnits +
    ImperialWeightUnits +
    UKImperialWeightUnits.filter { it !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryWeightUnits.filter { it !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class Weight : AbstractScientificUnit<PhysicalQuantity.Weight>()

@Serializable
sealed class MetricWeight : Weight(), MetricScientificUnit<PhysicalQuantity.Weight>

@Serializable
sealed class ImperialWeight(override val symbol: String) : Weight(), ImperialScientificUnit<PhysicalQuantity.Weight> {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Weight
}

@Serializable
sealed class USCustomaryWeight(override val symbol: String) : Weight(), USCustomaryScientificUnit<PhysicalQuantity.Weight> {
    override val system = MeasurementSystem.USCustomary
    override val quantity = PhysicalQuantity.Weight
}

@Serializable
sealed class UKImperialWeight(override val symbol: String) : Weight(), UKImperialScientificUnit<PhysicalQuantity.Weight> {
    override val system = MeasurementSystem.UKImperial
    override val quantity = PhysicalQuantity.Weight
}

// Metric Weight

@Serializable
object Gram : MetricWeight(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight> {
    override val symbol: String = "g"
    private const val GRAMS_IN_KILOGRAM = 1000.0
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Weight
    override fun toSIUnit(value: Decimal): Decimal = value / GRAMS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * GRAMS_IN_KILOGRAM.toDecimal()
}

@Serializable
object Nanogram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Nano(Gram)
@Serializable
object Microgram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Micro(Gram)
@Serializable
object Milligram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Milli(Gram)
@Serializable
object Centigram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Centi(Gram)
@Serializable
object Decigram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Deci(Gram)
@Serializable
object Decagram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Deca(Gram)
@Serializable
object Hectogram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Hecto(Gram)
@Serializable
object Kilogram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Kilo(Gram)
@Serializable
object Megagram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Mega(Gram)
@Serializable
object Gigagram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Giga(Gram)

@Serializable
object Tonne : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Mega(Gram) {
    override val symbol: String = "t"
}

@Serializable
object Dalton : MetricWeight(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight> {
    override val symbol: String = "Da"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Weight
    override fun toSIUnit(value: Decimal): Decimal = Gram.toSIUnit(value) / AvogadroConstant
    override fun fromSIUnit(value: Decimal): Decimal = Gram.fromSIUnit(value) * AvogadroConstant
}

@Serializable
object Nanodalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Nano(Dalton)
@Serializable
object Microdalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Micro(Dalton)
@Serializable
object Millidalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Milli(Dalton)
@Serializable
object Centidalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Centi(Dalton)
@Serializable
object Decidalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Deci(Dalton)
@Serializable
object Decadalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Deca(Dalton)
@Serializable
object HectoDalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Hecto(Dalton)
@Serializable
object Kilodalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Kilo(Dalton)
@Serializable
object Megadalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Mega(Dalton)
@Serializable
object Gigadalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Giga(Dalton)

// Imperial Weight
@Serializable
object Grain : ImperialWeight("gr") {
    private const val GRAIN_IN_POUND = 7000
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / GRAIN_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * GRAIN_IN_POUND.toDecimal()
}

@Serializable
object Dram : ImperialWeight("dr") {
    private const val DRAMS_IN_POUND = 256
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / DRAMS_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * DRAMS_IN_POUND.toDecimal()
}

@Serializable
object Ounce : ImperialWeight("oz") {
    private const val OUNCES_IN_POUND = 16
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / OUNCES_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * OUNCES_IN_POUND.toDecimal()
}

@Serializable
object Pound : ImperialWeight("lb") {
    private const val KILOGRAM_IN_POUND = 0.45359237
    override fun toSIUnit(value: Decimal): Decimal = value * KILOGRAM_IN_POUND.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value / KILOGRAM_IN_POUND.toDecimal()
}

@Serializable
object Stone : ImperialWeight("st") {
    private const val STONES_IN_POUND = 14
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * STONES_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / STONES_IN_POUND.toDecimal()
}

@Serializable
object Slug : ImperialWeight("slug") {
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit((Foot per Second).fromSIUnit(value))
    override fun fromSIUnit(value: Decimal): Decimal = (Foot per Second).toSIUnit(PoundForce.fromSIUnit(value))
}

// also long ton
@Serializable
object ImperialTon : UKImperialWeight("ton") {
    private const val POUND_IN_LONG_TONES = 2240
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * POUND_IN_LONG_TONES.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / POUND_IN_LONG_TONES.toDecimal()
}

@Serializable
data class UKImperialImperialWeightWrapper(val imperial: ImperialWeight) : UKImperialWeight(imperial.symbol) {
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

val <WeightUnit : ImperialWeight> WeightUnit.ukImperial get() = UKImperialImperialWeightWrapper(this)

// also short ton
@Serializable
object UsTon : USCustomaryWeight("ton") {
    private const val POUND_IN_SHORT_TONES = 2000
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * POUND_IN_SHORT_TONES.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / POUND_IN_SHORT_TONES.toDecimal()
}

@Serializable
data class USCustomaryImperialWeightWrapper(val imperial: ImperialWeight) : USCustomaryWeight(imperial.symbol) {
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

val <WeightUnit : ImperialWeight> WeightUnit.usCustomary get() = USCustomaryImperialWeightWrapper(this)
