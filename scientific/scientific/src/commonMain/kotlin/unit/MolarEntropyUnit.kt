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
 * Set of all [MetricAndUKImperialMolarEntropy]
 */
val MetricAndUKImperialMolarEntropyUnits: Set<MetricAndUKImperialMolarEntropy> get() = MetricAndUKImperialHeatCapacityUnits.flatMap { heatCapacity ->
    AmountOfSubstanceUnits.map { heatCapacity per it }
}.toSet()

/**
 * Set of all [MetricMolarEntropy]
 */
val MetricMolarEntropyUnits: Set<MetricMolarEntropy> get() = MetricHeatCapacityUnits.flatMap { heatCapacity ->
    AmountOfSubstanceUnits.map { heatCapacity per it }
}.toSet()

/**
 * Set of all [UKImperialMolarEntropy]
 */
val UKImperialMolarEntropyUnits: Set<UKImperialMolarEntropy> get() = UKImperialHeatCapacityUnits.flatMap { heatCapacity ->
    AmountOfSubstanceUnits.map { heatCapacity per it }
}.toSet()

/**
 * Set of all [USCustomaryMolarEntropy]
 */
val USCustomaryMolarEntropyUnits: Set<USCustomaryMolarEntropy> get() = USCustomaryHeatCapacityUnits.flatMap { heatCapacity ->
    AmountOfSubstanceUnits.map { heatCapacity per it }
}.toSet()

/**
 * Set of all [MolarEntropy]
 */
val MolarEntropyUnits: Set<MolarEntropy> get() = MetricAndUKImperialMolarEntropyUnits +
    MetricMolarEntropyUnits.filter { it.heatCapacity.energy !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    UKImperialMolarEntropyUnits.filter { it.heatCapacity.energy !is ImperialMetricAndImperialEnergyWrapper }.toSet() +
    USCustomaryMolarEntropyUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.MolarEntropy]
 * SI unit is `Joule per Kelvin per Mole`
 */
@Serializable
sealed class MolarEntropy : DefinedScientificUnit<PhysicalQuantity.MolarEntropy>() {

    /**
     * The [HeatCapacity] component
     */
    abstract val heatCapacity: HeatCapacity

    /**
     * The [AmountOfSubstance] component
     */
    abstract val perAmountOfSubstance: AmountOfSubstance
    override val quantity = PhysicalQuantity.MolarEntropy
    override val symbol: String by lazy { "${heatCapacity.energy.symbol}/${heatCapacity.per.symbol}⋅${perAmountOfSubstance.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = perAmountOfSubstance.toSIUnit(heatCapacity.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = heatCapacity.toSIUnit(perAmountOfSubstance.fromSIUnit(value))
}

/**
 * A [MolarEntropy] for [MeasurementSystem.MetricAndUKImperial]
 * @param heatCapacity the [MetricAndUKImperialHeatCapacity] component
 * @param perAmountOfSubstance the [AmountOfSubstance] component
 */
@Serializable
data class MetricAndUKImperialMolarEntropy(override val heatCapacity: MetricAndUKImperialHeatCapacity, override val perAmountOfSubstance: AmountOfSubstance) :
    MolarEntropy(),
    MetricAndUKImperialScientificUnit<PhysicalQuantity.MolarEntropy> {
    override val system = MeasurementSystem.MetricAndUKImperial

    /**
     * The [MetricMolarEntropy] equivalent to this [MetricAndUKImperialMolarEntropy]
     */
    val metric get() = heatCapacity.metric per perAmountOfSubstance

    /**
     * The [UKImperialMolarEntropy] equivalent to this [MetricAndUKImperialMolarEntropy]
     */
    val ukImperial get() = heatCapacity.ukImperial per perAmountOfSubstance
}

/**
 * A [MolarEntropy] for [MeasurementSystem.Metric]
 * @param heatCapacity the [MetricHeatCapacity] component
 * @param perAmountOfSubstance the [AmountOfSubstance] component
 */
@Serializable
data class MetricMolarEntropy(override val heatCapacity: MetricHeatCapacity, override val perAmountOfSubstance: AmountOfSubstance) :
    MolarEntropy(),
    MetricScientificUnit<PhysicalQuantity.MolarEntropy> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [MolarEntropy] for [MeasurementSystem.UKImperial]
 * @param heatCapacity the [UKImperialHeatCapacity] component
 * @param perAmountOfSubstance the [AmountOfSubstance] component
 */
@Serializable
data class UKImperialMolarEntropy(override val heatCapacity: UKImperialHeatCapacity, override val perAmountOfSubstance: AmountOfSubstance) :
    MolarEntropy(),
    UKImperialScientificUnit<PhysicalQuantity.MolarEntropy> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [MolarEntropy] for [MeasurementSystem.USCustomary]
 * @param heatCapacity the [USCustomaryHeatCapacity] component
 * @param perAmountOfSubstance the [AmountOfSubstance] component
 */
@Serializable
data class USCustomaryMolarEntropy(override val heatCapacity: USCustomaryHeatCapacity, override val perAmountOfSubstance: AmountOfSubstance) :
    MolarEntropy(),
    USCustomaryScientificUnit<PhysicalQuantity.MolarEntropy> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricAndUKImperialMolarEntropy] from a [MetricAndUKImperialHeatCapacity] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [MetricAndUKImperialMolarEntropy] represented by the units
 */
infix fun MetricAndUKImperialHeatCapacity.per(amountOfSubstance: AmountOfSubstance) = MetricAndUKImperialMolarEntropy(this, amountOfSubstance)

/**
 * Gets a [MetricMolarEntropy] from a [MetricHeatCapacity] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [MetricMolarEntropy] represented by the units
 */
infix fun MetricHeatCapacity.per(amountOfSubstance: AmountOfSubstance) = MetricMolarEntropy(this, amountOfSubstance)

/**
 * Gets a [UKImperialMolarEntropy] from a [UKImperialHeatCapacity] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [UKImperialMolarEntropy] represented by the units
 */
infix fun UKImperialHeatCapacity.per(amountOfSubstance: AmountOfSubstance) = UKImperialMolarEntropy(this, amountOfSubstance)

/**
 * Gets a [USCustomaryMolarEntropy] from a [USCustomaryHeatCapacity] and an [AmountOfSubstance]
 * @param amountOfSubstance the [AmountOfSubstance] component
 * @return the [USCustomaryMolarEntropy] represented by the units
 */
infix fun USCustomaryHeatCapacity.per(amountOfSubstance: AmountOfSubstance) = USCustomaryMolarEntropy(this, amountOfSubstance)

internal fun SerializersModuleBuilder.setupForMolarEntropy() {
    polymorphic(MolarEntropy::class) {
        registerMolarEntropyClasses()
    }
}

internal fun PolymorphicModuleBuilder<MolarEntropy>.registerMolarEntropyClasses() {
    subclass(MetricAndUKImperialMolarEntropy::class, MetricAndUKImperialMolarEntropy.serializer())
    subclass(MetricMolarEntropy::class, MetricMolarEntropy.serializer())
    subclass(UKImperialMolarEntropy::class, UKImperialMolarEntropy.serializer())
    subclass(USCustomaryMolarEntropy::class, USCustomaryMolarEntropy.serializer())
}
