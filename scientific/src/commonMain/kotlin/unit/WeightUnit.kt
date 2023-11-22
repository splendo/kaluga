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
 * Set of all [MetricWeight]
 */
val MetricWeightUnits: Set<MetricWeight> get() = setOf(
    Kilogram,
    Nanogram,
    Microgram,
    Milligram,
    Centigram,
    Decigram,
    Gram,
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
    Gigadalton,
)

/**
 * Set of all [ImperialWeight]
 */
val ImperialWeightUnits: Set<ImperialWeight> get() = setOf(
    Grain,
    Ounce,
    Pound,
    Stone,
    Slug,
)

/**
 * Set of all [UKImperialWeight]
 */
val UKImperialWeightUnits: Set<UKImperialWeight> get() = ImperialWeightUnits.map { it.ukImperial }.toSet() + setOf(ImperialTon)

/**
 * Set of all [USCustomaryWeight]
 */
val USCustomaryWeightUnits: Set<USCustomaryWeight> get() = ImperialWeightUnits.map { it.usCustomary }.toSet() + setOf(UsTon)

/**
 * Set of all [Weight]
 */
val WeightUnits: Set<Weight> get() = MetricWeightUnits +
    ImperialWeightUnits +
    UKImperialWeightUnits.filter { it !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryWeightUnits.filter { it !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Weight]
 * SI unit is [Kilogram]
 */
@Serializable
sealed class Weight : AbstractScientificUnit<PhysicalQuantity.Weight>()

/**
 * A [Weight] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricWeight : Weight(), MetricScientificUnit<PhysicalQuantity.Weight>

/**
 * A [Weight] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialWeight : Weight(), ImperialScientificUnit<PhysicalQuantity.Weight> {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Weight
}

/**
 * A [Weight] for [MeasurementSystem.USCustomary]
 */
@Serializable
sealed class USCustomaryWeight : Weight(), USCustomaryScientificUnit<PhysicalQuantity.Weight> {
    override val system = MeasurementSystem.USCustomary
    override val quantity = PhysicalQuantity.Weight
}

/**
 * A [Weight] for [MeasurementSystem.UKImperial]
 */
@Serializable
sealed class UKImperialWeight : Weight(), UKImperialScientificUnit<PhysicalQuantity.Weight> {
    override val system = MeasurementSystem.UKImperial
    override val quantity = PhysicalQuantity.Weight
}

// Metric Weight

@Serializable
data object Gram : MetricWeight(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight> {
    override val symbol: String = "g"
    private const val GRAMS_IN_KILOGRAM = 1000.0
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Weight
    override fun toSIUnit(value: Decimal): Decimal = value / GRAMS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * GRAMS_IN_KILOGRAM.toDecimal()
}

@Serializable
sealed class GramMultiple : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram>

@Serializable
data object Nanogram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Nano(Gram)

@Serializable
data object Microgram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Micro(Gram)

@Serializable
data object Milligram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Milli(Gram)

@Serializable
data object Centigram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Centi(Gram)

@Serializable
data object Decigram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Deci(Gram)

@Serializable
data object Decagram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Deca(Gram)

@Serializable
data object Hectogram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Hecto(Gram)

@Serializable
data object Kilogram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Kilo(Gram)

@Serializable
data object Megagram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Mega(Gram)

@Serializable
data object Gigagram : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Giga(Gram)

@Serializable
data object Tonne : GramMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Gram> by Mega(Gram) {
    override val symbol: String = "t"
}

@Serializable
data object Dalton : MetricWeight(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight> {
    override val symbol: String = "Da"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Weight
    override fun toSIUnit(value: Decimal): Decimal = Gram.toSIUnit(value) / AvogadroConstant
    override fun fromSIUnit(value: Decimal): Decimal = Gram.fromSIUnit(value) * AvogadroConstant
}

@Serializable
sealed class DaltonMultiple : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton>

@Serializable
data object Nanodalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Nano(Dalton)

@Serializable
data object Microdalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Micro(Dalton)

@Serializable
data object Millidalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Milli(Dalton)

@Serializable
data object Centidalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Centi(Dalton)

@Serializable
data object Decidalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Deci(Dalton)

@Serializable
data object Decadalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Deca(Dalton)

@Serializable
data object HectoDalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Hecto(Dalton)

@Serializable
data object Kilodalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Kilo(Dalton)

@Serializable
data object Megadalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Mega(Dalton)

@Serializable
data object Gigadalton : DaltonMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Weight, Dalton> by Giga(Dalton)

// Imperial Weight
@Serializable
data object Grain : ImperialWeight() {
    private const val GRAIN_IN_POUND = 7000
    override val symbol: String = "gr"
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / GRAIN_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * GRAIN_IN_POUND.toDecimal()
}

@Serializable
data object Dram : ImperialWeight() {
    private const val DRAMS_IN_POUND = 256
    override val symbol: String = "dr"
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / DRAMS_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * DRAMS_IN_POUND.toDecimal()
}

@Serializable
data object Ounce : ImperialWeight() {
    private const val OUNCES_IN_POUND = 16
    override val symbol: String = "oz"
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / OUNCES_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * OUNCES_IN_POUND.toDecimal()
}

@Serializable
data object Pound : ImperialWeight() {
    private const val KILOGRAM_IN_POUND = 0.45359237
    override val symbol: String = "lb"
    override fun toSIUnit(value: Decimal): Decimal = value * KILOGRAM_IN_POUND.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value / KILOGRAM_IN_POUND.toDecimal()
}

@Serializable
data object Stone : ImperialWeight() {
    private const val STONES_IN_POUND = 14
    override val symbol: String = "st"
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * STONES_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / STONES_IN_POUND.toDecimal()
}

@Serializable
data object Slug : ImperialWeight() {
    override val symbol: String = "slug"
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit((Foot per Second).fromSIUnit(value))
    override fun fromSIUnit(value: Decimal): Decimal = (Foot per Second).toSIUnit(PoundForce.fromSIUnit(value))
}

// also long ton
@Serializable
data object ImperialTon : UKImperialWeight() {
    private const val POUND_IN_LONG_TONES = 2240
    override val symbol: String = "ton"
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * POUND_IN_LONG_TONES.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / POUND_IN_LONG_TONES.toDecimal()
}

/**
 * Wraps an [ImperialWeight] unit to a [UKImperialWeight] unit
 * @param imperial the [ImperialWeight] to wrap
 */
@Serializable
data class UKImperialImperialWeightWrapper(val imperial: ImperialWeight) : UKImperialWeight() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

/**
 * Converts an [ImperialWeight] unit to a [UKImperialImperialWeightWrapper] unit
 * @param WeightUnit the type of [ImperialWeight] to convert
 */
val <WeightUnit : ImperialWeight> WeightUnit.ukImperial get() = UKImperialImperialWeightWrapper(this)

// also short ton
@Serializable
data object UsTon : USCustomaryWeight() {
    private const val POUND_IN_SHORT_TONES = 2000
    override val symbol: String = "ton"
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * POUND_IN_SHORT_TONES.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / POUND_IN_SHORT_TONES.toDecimal()
}

/**
 * Wraps an [ImperialWeight] unit to a [USCustomaryWeight] unit
 * @param imperial the [ImperialWeight] to wrap
 */
@Serializable
data class USCustomaryImperialWeightWrapper(val imperial: ImperialWeight) : USCustomaryWeight() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

/**
 * Converts an [ImperialWeight] unit to a [USCustomaryImperialWeightWrapper] unit
 * @param WeightUnit the type of [ImperialWeight] to convert
 */
val <WeightUnit : ImperialWeight> WeightUnit.usCustomary get() = USCustomaryImperialWeightWrapper(this)
