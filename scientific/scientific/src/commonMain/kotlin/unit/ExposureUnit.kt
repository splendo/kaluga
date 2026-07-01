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
 * Set of all [MetricExposure]
 */
val MetricExposureUnits: Set<MetricExposure> get() = ElectricChargeUnits.flatMap { charge ->
    MetricWeightUnits.map { charge per it }
}.toSet()

/**
 * Set of all [ImperialExposure]
 */
val ImperialExposureUnits: Set<ImperialExposure> get() = ElectricChargeUnits.flatMap { charge ->
    ImperialWeightUnits.map { charge per it }
}.toSet()

/**
 * Set of all [UKImperialExposure]
 */
val UKImperialExposureUnits: Set<UKImperialExposure> get() = ElectricChargeUnits.flatMap { charge ->
    UKImperialWeightUnits.map { charge per it }
}.toSet()

/**
 * Set of all [USCustomaryExposure]
 */
val USCustomaryExposureUnits: Set<USCustomaryExposure> get() = ElectricChargeUnits.flatMap { charge ->
    USCustomaryWeightUnits.map { charge per it }
}.toSet()

/**
 * Set of all [Exposure]
 */
val ExposureUnits: Set<Exposure> get() = MetricExposureUnits +
    ImperialExposureUnits +
    UKImperialExposureUnits.filter { it.per !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryExposureUnits.filter { it.per !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Exposure]
 * SI unit is `Coulomb per Kilogram`
 */
@Serializable
sealed class Exposure : DefinedScientificUnit<PhysicalQuantity.Exposure>() {

    /**
     * The [ElectricCharge] component
     */
    abstract val charge: ElectricCharge

    /**
     * The [Weight] component
     */
    abstract val per: Weight
    override val symbol: String by lazy { "${charge.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.Exposure
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(charge.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = charge.toSIUnit(per.fromSIUnit(value))
}

/**
 * An [Exposure] for [MeasurementSystem.Metric]
 * @param charge the [ElectricCharge] component
 * @param per the [MetricWeight] component
 */
@Serializable
data class MetricExposure(override val charge: ElectricCharge, override val per: MetricWeight) :
    Exposure(),
    MetricScientificUnit<PhysicalQuantity.Exposure> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [Exposure] for [MeasurementSystem.Imperial]
 * @param charge the [ElectricCharge] component
 * @param per the [ImperialWeight] component
 */
@Serializable
data class ImperialExposure(override val charge: ElectricCharge, override val per: ImperialWeight) :
    Exposure(),
    ImperialScientificUnit<PhysicalQuantity.Exposure> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialExposure] equivalent to this [ImperialExposure]
     */
    val ukImperial get() = charge per per.ukImperial

    /**
     * The [USCustomaryExposure] equivalent to this [ImperialExposure]
     */
    val usCustomary get() = charge per per.usCustomary
}

/**
 * An [Exposure] for [MeasurementSystem.USCustomary]
 * @param charge the [ElectricCharge] component
 * @param per the [USCustomaryWeight] component
 */
@Serializable
data class USCustomaryExposure(override val charge: ElectricCharge, override val per: USCustomaryWeight) :
    Exposure(),
    USCustomaryScientificUnit<PhysicalQuantity.Exposure> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * An [Exposure] for [MeasurementSystem.UKImperial]
 * @param charge the [ElectricCharge] component
 * @param per the [UKImperialWeight] component
 */
@Serializable
data class UKImperialExposure(override val charge: ElectricCharge, override val per: UKImperialWeight) :
    Exposure(),
    UKImperialScientificUnit<PhysicalQuantity.Exposure> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricExposure] from an [ElectricCharge] and a [MetricWeight]
 * @param weight the [MetricWeight] component
 * @return the [MetricExposure] represented by the units
 */
infix fun ElectricCharge.per(weight: MetricWeight) = MetricExposure(this, weight)

/**
 * Gets an [ImperialExposure] from an [ElectricCharge] and an [ImperialWeight]
 * @param weight the [ImperialWeight] component
 * @return the [ImperialExposure] represented by the units
 */
infix fun ElectricCharge.per(weight: ImperialWeight) = ImperialExposure(this, weight)

/**
 * Gets a [UKImperialExposure] from an [ElectricCharge] and a [UKImperialWeight]
 * @param weight the [UKImperialWeight] component
 * @return the [UKImperialExposure] represented by the units
 */
infix fun ElectricCharge.per(weight: UKImperialWeight) = UKImperialExposure(this, weight)

/**
 * Gets a [USCustomaryExposure] from an [ElectricCharge] and a [USCustomaryWeight]
 * @param weight the [USCustomaryWeight] component
 * @return the [USCustomaryExposure] represented by the units
 */
infix fun ElectricCharge.per(weight: USCustomaryWeight) = USCustomaryExposure(this, weight)

internal fun SerializersModuleBuilder.setupForExposure() {
    polymorphic(Exposure::class) {
        registerExposureClasses()
    }
}

internal fun PolymorphicModuleBuilder<Exposure>.registerExposureClasses() {
    subclass(ImperialExposure::class, ImperialExposure.serializer())
    subclass(MetricExposure::class, MetricExposure.serializer())
    subclass(UKImperialExposure::class, UKImperialExposure.serializer())
    subclass(USCustomaryExposure::class, USCustomaryExposure.serializer())
}
