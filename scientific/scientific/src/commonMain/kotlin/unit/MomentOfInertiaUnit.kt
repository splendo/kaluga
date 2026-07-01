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
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [MetricMomentOfInertia]
 */
val MetricMomentOfInertiaUnits: Set<MetricMomentOfInertia> get() = MetricWeightUnits.flatMap { weight ->
    MetricAreaUnits.map { weight x it }
}.toSet()

/**
 * Set of all [ImperialMomentOfInertia]
 */
val ImperialMomentOfInertiaUnits: Set<ImperialMomentOfInertia> get() = ImperialWeightUnits.flatMap { weight ->
    ImperialAreaUnits.map { weight x it }
}.toSet()

/**
 * Set of all [UKImperialMomentOfInertia]
 */
val UKImperialMomentOfInertiaUnits: Set<UKImperialMomentOfInertia> get() = UKImperialWeightUnits.flatMap { weight ->
    ImperialAreaUnits.map { weight x it }
}.toSet()

/**
 * Set of all [USCustomaryMomentOfInertia]
 */
val USCustomaryMomentOfInertiaUnits: Set<USCustomaryMomentOfInertia> get() = USCustomaryWeightUnits.flatMap { weight ->
    ImperialAreaUnits.map { weight x it }
}.toSet()

/**
 * Set of all [MomentOfInertia]
 */
val MomentOfInertiaUnits: Set<MomentOfInertia> get() = MetricMomentOfInertiaUnits +
    ImperialMomentOfInertiaUnits +
    UKImperialMomentOfInertiaUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMomentOfInertiaUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.MomentOfInertia]
 * SI unit is `Kilogram x SquareMeter`
 */
@Serializable
sealed class MomentOfInertia : DefinedScientificUnit<PhysicalQuantity.MomentOfInertia>() {

    /**
     * The [Weight] component
     */
    abstract val weight: Weight

    /**
     * The [Area] component
     */
    abstract val area: Area
    override val quantity = PhysicalQuantity.MomentOfInertia
    override val symbol: String by lazy { "${weight.symbol}⋅${area.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = area.fromSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(area.toSIUnit(value))
}

/**
 * A [MomentOfInertia] for [MeasurementSystem.Metric]
 * @param weight the [MetricWeight] component
 * @param area the [MetricArea] component
 */
@Serializable
data class MetricMomentOfInertia(override val weight: MetricWeight, override val area: MetricArea) :
    MomentOfInertia(),
    MetricScientificUnit<PhysicalQuantity.MomentOfInertia> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [MomentOfInertia] for [MeasurementSystem.Imperial]
 * @param weight the [ImperialWeight] component
 * @param area the [ImperialArea] component
 */
@Serializable
data class ImperialMomentOfInertia(override val weight: ImperialWeight, override val area: ImperialArea) :
    MomentOfInertia(),
    ImperialScientificUnit<PhysicalQuantity.MomentOfInertia> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialMomentOfInertia] equivalent to this [ImperialMomentOfInertia]
     */
    val ukImperial get() = UKImperialMomentOfInertia(weight.ukImperial, area)

    /**
     * The [USCustomaryMomentOfInertia] equivalent to this [ImperialMomentOfInertia]
     */
    val usCustomary get() = USCustomaryMomentOfInertia(weight.usCustomary, area)
}

/**
 * A [MomentOfInertia] for [MeasurementSystem.UKImperial]
 * @param weight the [UKImperialWeight] component
 * @param area the [ImperialArea] component
 */
@Serializable
data class UKImperialMomentOfInertia(override val weight: UKImperialWeight, override val area: ImperialArea) :
    MomentOfInertia(),
    UKImperialScientificUnit<PhysicalQuantity.MomentOfInertia> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [MomentOfInertia] for [MeasurementSystem.USCustomary]
 * @param weight the [USCustomaryWeight] component
 * @param area the [ImperialArea] component
 */
@Serializable
data class USCustomaryMomentOfInertia(override val weight: USCustomaryWeight, override val area: ImperialArea) :
    MomentOfInertia(),
    USCustomaryScientificUnit<PhysicalQuantity.MomentOfInertia> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricMomentOfInertia] from a [MetricWeight] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricMomentOfInertia] represented by the units
 */
infix fun MetricWeight.x(area: MetricArea) = MetricMomentOfInertia(this, area)

/**
 * Gets an [ImperialMomentOfInertia] from an [ImperialWeight] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialMomentOfInertia] represented by the units
 */
infix fun ImperialWeight.x(area: ImperialArea) = ImperialMomentOfInertia(this, area)

/**
 * Gets a [UKImperialMomentOfInertia] from a [UKImperialWeight] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [UKImperialMomentOfInertia] represented by the units
 */
infix fun UKImperialWeight.x(area: ImperialArea) = UKImperialMomentOfInertia(this, area)

/**
 * Gets a [USCustomaryMomentOfInertia] from a [USCustomaryWeight] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [USCustomaryMomentOfInertia] represented by the units
 */
infix fun USCustomaryWeight.x(area: ImperialArea) = USCustomaryMomentOfInertia(this, area)

internal fun SerializersModuleBuilder.setupForMomentOfInertia() {
    polymorphic(MomentOfInertia::class) {
        registerMomentOfInertiaClasses()
    }
}

internal fun PolymorphicModuleBuilder<MomentOfInertia>.registerMomentOfInertiaClasses() {
    subclass(ImperialMomentOfInertia::class, ImperialMomentOfInertia.serializer())
    subclass(MetricMomentOfInertia::class, MetricMomentOfInertia.serializer())
    subclass(UKImperialMomentOfInertia::class, UKImperialMomentOfInertia.serializer())
    subclass(USCustomaryMomentOfInertia::class, USCustomaryMomentOfInertia.serializer())
}
