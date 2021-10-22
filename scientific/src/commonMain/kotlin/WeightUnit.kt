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
object Gram : MetricWeight(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Weight> {
    override val symbol: String = "g"
    private const val GRAMS_IN_KILOGRAM = 1000.0
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Weight
    override fun toSIUnit(value: Decimal): Decimal = value / GRAMS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * GRAMS_IN_KILOGRAM.toDecimal()
}

@Serializable
object Nanogram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Nano(Gram)

@Serializable
object Microgram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Micro(Gram)

@Serializable
object Milligram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Milli(Gram)

@Serializable
object Centigram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Centi(Gram)

@Serializable
object Decigram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Deci(Gram)

@Serializable
object Decagram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Deca(Gram)

@Serializable
object Hectogram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Hecto(Gram)

@Serializable
object Kilogram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Kilo(Gram)

@Serializable
object Tonne : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Mega(Gram) {
    override val symbol: String = "t"
}

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

fun <
    MassUnit : ScientificUnit<MeasurementType.Weight>,
    AccelerationUnit : ScientificUnit<MeasurementType.Acceleration>,
    ForceUnit : ScientificUnit<MeasurementType.Force>
    > MassUnit.mass(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>
) : ScientificValue<MeasurementType.Weight, MassUnit> = byDividing(force, acceleration)

@JvmName("newtonDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Newton>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("newtonMultipleDivAcceleration")
operator fun <M : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton>> ScientificValue<MeasurementType.Force, M>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("dyneDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Dyne>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("dyneMultipleDivAcceleration")
operator fun <M : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne>> ScientificValue<MeasurementType.Force, M>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("poundalDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Poundal>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("poundForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("ounceForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, OunceForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Ounce.mass(this, acceleration)
@JvmName("grainForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, GrainForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Grain.mass(this, acceleration)
@JvmName("kipDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Kip>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("usTonForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = UsTon.mass(this, acceleration)
@JvmName("imperialTonForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = ImperialTon.mass(this, acceleration)
