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

@Serializable
sealed class Weight : AbstractScientificUnit<MeasurementType.Weight>()

@Serializable
sealed class MetricWeight : Weight(), MetricScientificUnit<MeasurementType.Weight>

@Serializable
sealed class ImperialWeight(override val symbol: String) : Weight(), ImperialScientificUnit<MeasurementType.Weight> {
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Weight
}

@Serializable
sealed class USCustomaryWeight(override val symbol: String) : Weight(), USCustomaryScientificUnit<MeasurementType.Weight> {
    override val system = MeasurementSystem.USCustomary
    override val type = MeasurementType.Weight
}

@Serializable
sealed class UKImperialWeight(override val symbol: String) : Weight(), UKImperialScientificUnit<MeasurementType.Weight> {
    override val system = MeasurementSystem.UKImperial
    override val type = MeasurementType.Weight
}

// Metric Weight

@Serializable
object Gram : MetricWeight(), BaseMetricUnit<MeasurementType.Weight, MeasurementSystem.Metric> {
    override val symbol: String = "g"
    const val GRAMS_IN_KILOGRAM = 1000.0
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Weight
    override fun toSIUnit(value: Decimal): Decimal = value / GRAMS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * GRAMS_IN_KILOGRAM.toDecimal()
}

@Serializable
object Nanogram : MetricWeight(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Weight> by Nano(Gram)

@Serializable
object Microgram : MetricWeight(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Weight> by Micro(Gram)

@Serializable
object Milligram : MetricWeight(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Weight> by Milli(Gram)

@Serializable
object Centigram : MetricWeight(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Weight> by Centi(Gram)

@Serializable
object Decigram : MetricWeight(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Weight> by Deci(Gram)

@Serializable
object Decagram : MetricWeight(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Weight> by Deca(Gram)

@Serializable
object Hectogram : MetricWeight(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Weight> by Hecto(Gram)

@Serializable
object Kilogram : MetricWeight(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Weight> by Kilo(Gram)

@Serializable
object Tonne : MetricWeight(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Weight> by Mega(Gram) {
    override val symbol: String = "t"
}

// Imperial Weight
@Serializable
object Grain : ImperialWeight("gr") {
    const val GRAIN_IN_POUND = 7000
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / GRAIN_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * GRAIN_IN_POUND.toDecimal()
}

@Serializable
object Dram : ImperialWeight("dr") {
    const val DRAMS_IN_POUND = 256
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / DRAMS_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * DRAMS_IN_POUND.toDecimal()
}

@Serializable
object Ounce : ImperialWeight("oz") {
    const val OUNCES_IN_POUND = 16
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / OUNCES_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * OUNCES_IN_POUND.toDecimal()
}

@Serializable
object Pound : ImperialWeight("lb") {
    const val KILOGRAM_IN_POUND = 0.45359237
    override fun toSIUnit(value: Decimal): Decimal = value * KILOGRAM_IN_POUND.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value / KILOGRAM_IN_POUND.toDecimal()
}

@Serializable
object Stone : ImperialWeight("st") {
    const val STONES_IN_POUND = 14
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * STONES_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / STONES_IN_POUND.toDecimal()
}

// also long ton
@Serializable
object ImperialTon : UKImperialWeight("ton") {
    const val POUND_IN_LONG_TONES = 2240
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * POUND_IN_LONG_TONES.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / POUND_IN_LONG_TONES.toDecimal()
}

@Serializable
data class UKImperialImperialWeightWrapper(val imperial: ImperialWeight) : UKImperialWeight(imperial.symbol) {
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

// also short ton
@Serializable
object UsTon : USCustomaryWeight("ton") {
    const val POUND_IN_SHORT_TONES = 2000
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * POUND_IN_SHORT_TONES.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / POUND_IN_SHORT_TONES.toDecimal()
}

@Serializable
data class USCustomaryImperialWeightWrapper(val imperial: ImperialWeight) : USCustomaryWeight(imperial.symbol) {
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}
