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
 * Set of all [MetricMomentum]
 */
val MetricMomentumUnits: Set<MetricMomentum> get() = MetricWeightUnits.flatMap { weight ->
    MetricSpeedUnits.map {
        MetricMomentum(weight, it)
    }
}.toSet()

/**
 * Set of all [ImperialMomentum]
 */
val ImperialMomentumUnits: Set<ImperialMomentum> get() = ImperialWeightUnits.flatMap { weight ->
    ImperialSpeedUnits.map {
        ImperialMomentum(weight, it)
    }
}.toSet()

/**
 * Set of all [UKImperialMomentum]
 */
val UKImperialMomentumUnits: Set<UKImperialMomentum> get() = UKImperialWeightUnits.flatMap { weight ->
    ImperialSpeedUnits.map {
        UKImperialMomentum(weight, it)
    }
}.toSet()

/**
 * Set of all [USCustomaryMomentum]
 */
val USCustomaryMomentumUnits: Set<USCustomaryMomentum> get() = USCustomaryWeightUnits.flatMap { weight ->
    ImperialSpeedUnits.map {
        USCustomaryMomentum(weight, it)
    }
}.toSet()

/**
 * Set of all [Momentum]
 */
val MomentumUnits: Set<Momentum> get() = MetricMomentumUnits +
    ImperialMomentumUnits +
    UKImperialMomentumUnits.filter { it.mass !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMomentumUnits.filter { it.mass !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Momentum]
 * SI unit is `Kilogram x Meter per Second`
 */
@Serializable
sealed class Momentum : AbstractScientificUnit<PhysicalQuantity.Momentum>() {

    /**
     * The [Weight] component
     */
    abstract val mass: Weight

    /**
     * The [Speed] component
     */
    abstract val speed: Speed
    override val quantity = PhysicalQuantity.Momentum
    override val symbol: String by lazy { "${mass.symbol}⋅${speed.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = speed.fromSIUnit(mass.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = mass.toSIUnit(speed.toSIUnit(value))
}

/**
 * A [Momentum] for [MeasurementSystem.Metric]
 * @param mass the [MetricWeight] component
 * @param speed the [MetricSpeed] component
 */
@Serializable
data class MetricMomentum(override val mass: MetricWeight, override val speed: MetricSpeed) :
    Momentum(),
    MetricScientificUnit<PhysicalQuantity.Momentum> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Momentum] for [MeasurementSystem.Imperial]
 * @param mass the [ImperialWeight] component
 * @param speed the [ImperialSpeed] component
 */
@Serializable
data class ImperialMomentum(override val mass: ImperialWeight, override val speed: ImperialSpeed) :
    Momentum(),
    ImperialScientificUnit<PhysicalQuantity.Momentum> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialMomentum] equivalent to this [ImperialMomentum]
     */
    val ukImperial get() = UKImperialMomentum(mass.ukImperial, speed)

    /**
     * The [USCustomaryMomentum] equivalent to this [ImperialMomentum]
     */
    val usCustomary get() = USCustomaryMomentum(mass.usCustomary, speed)
}

/**
 * A [Momentum] for [MeasurementSystem.UKImperial]
 * @param mass the [UKImperialWeight] component
 * @param speed the [ImperialSpeed] component
 */
@Serializable
data class UKImperialMomentum(override val mass: UKImperialWeight, override val speed: ImperialSpeed) :
    Momentum(),
    UKImperialScientificUnit<PhysicalQuantity.Momentum> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [Momentum] for [MeasurementSystem.USCustomary]
 * @param mass the [USCustomaryWeight] component
 * @param speed the [ImperialSpeed] component
 */
@Serializable
data class USCustomaryMomentum(override val mass: USCustomaryWeight, override val speed: ImperialSpeed) :
    Momentum(),
    USCustomaryScientificUnit<PhysicalQuantity.Momentum> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricMomentum] from a [MetricWeight] and a [MetricSpeed]
 * @param speed the [MetricSpeed] component
 * @return the [MetricMomentum] represented by the units
 */
infix fun MetricWeight.x(speed: MetricSpeed) = MetricMomentum(this, speed)

/**
 * Gets an [ImperialMomentum] from an [ImperialWeight] and an [ImperialSpeed]
 * @param speed the [ImperialSpeed] component
 * @return the [ImperialMomentum] represented by the units
 */
infix fun ImperialWeight.x(speed: ImperialSpeed) = ImperialMomentum(this, speed)

/**
 * Gets a [UKImperialMomentum] from a [UKImperialWeight] and an [ImperialSpeed]
 * @param speed the [ImperialSpeed] component
 * @return the [UKImperialMomentum] represented by the units
 */
infix fun UKImperialWeight.x(speed: ImperialSpeed) = UKImperialMomentum(this, speed)

/**
 * Gets a [USCustomaryMomentum] from a [USCustomaryWeight] and an [ImperialSpeed]
 * @param speed the [ImperialSpeed] component
 * @return the [USCustomaryMomentum] represented by the units
 */
infix fun USCustomaryWeight.x(speed: ImperialSpeed) = USCustomaryMomentum(this, speed)
