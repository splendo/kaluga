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
 * Set of all [MetricLinearChargeDensity]
 */
val MetricLinearChargeDensityUnits: Set<MetricLinearChargeDensity> get() = ElectricChargeUnits.flatMap { charge ->
    MetricLengthUnits.map { charge per it }
}.toSet()

/**
 * Set of all [ImperialLinearChargeDensity]
 */
val ImperialLinearChargeDensityUnits: Set<ImperialLinearChargeDensity> get() = ElectricChargeUnits.flatMap { charge ->
    ImperialLengthUnits.map { charge per it }
}.toSet()

/**
 * Set of all [LinearChargeDensity]
 */
val LinearChargeDensityUnits: Set<LinearChargeDensity> get() = MetricLinearChargeDensityUnits +
    ImperialLinearChargeDensityUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.LinearChargeDensity]
 * SI unit is `Coulomb per Meter`
 */
@Serializable
sealed class LinearChargeDensity : DefinedScientificUnit<PhysicalQuantity.LinearChargeDensity>() {

    /**
     * The [ElectricCharge] component
     */
    abstract val charge: ElectricCharge

    /**
     * The [Length] component
     */
    abstract val per: Length
    override val symbol: String by lazy { "${charge.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.LinearChargeDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(charge.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = charge.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [LinearChargeDensity] for [MeasurementSystem.Metric]
 * @param charge the [ElectricCharge] component
 * @param per the [MetricLength] component
 */
@Serializable
data class MetricLinearChargeDensity(override val charge: ElectricCharge, override val per: MetricLength) :
    LinearChargeDensity(),
    MetricScientificUnit<PhysicalQuantity.LinearChargeDensity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [LinearChargeDensity] for [MeasurementSystem.Imperial]
 * @param charge the [ElectricCharge] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class ImperialLinearChargeDensity(override val charge: ElectricCharge, override val per: ImperialLength) :
    LinearChargeDensity(),
    ImperialScientificUnit<PhysicalQuantity.LinearChargeDensity> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricLinearChargeDensity] from an [ElectricCharge] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricLinearChargeDensity] represented by the units
 */
infix fun ElectricCharge.per(length: MetricLength) = MetricLinearChargeDensity(this, length)

/**
 * Gets an [ImperialLinearChargeDensity] from an [ElectricCharge] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialLinearChargeDensity] represented by the units
 */
infix fun ElectricCharge.per(length: ImperialLength) = ImperialLinearChargeDensity(this, length)

internal fun SerializersModuleBuilder.setupForLinearChargeDensity() {
    polymorphic(LinearChargeDensity::class) {
        registerLinearChargeDensityClasses()
    }
}

internal fun PolymorphicModuleBuilder<LinearChargeDensity>.registerLinearChargeDensityClasses() {
    subclass(ImperialLinearChargeDensity::class, ImperialLinearChargeDensity.serializer())
    subclass(MetricLinearChargeDensity::class, MetricLinearChargeDensity.serializer())
}
