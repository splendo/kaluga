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
 * Set of all [MetricSurfaceChargeDensity]
 */
val MetricSurfaceChargeDensityUnits: Set<MetricSurfaceChargeDensity> get() = ElectricChargeUnits.flatMap { charge ->
    MetricAreaUnits.map { charge per it }
}.toSet()

/**
 * Set of all [ImperialSurfaceChargeDensity]
 */
val ImperialSurfaceChargeDensityUnits: Set<ImperialSurfaceChargeDensity> get() = ElectricChargeUnits.flatMap { charge ->
    ImperialAreaUnits.map { charge per it }
}.toSet()

/**
 * Set of all [SurfaceChargeDensity]
 */
val SurfaceChargeDensityUnits: Set<SurfaceChargeDensity> get() = MetricSurfaceChargeDensityUnits +
    ImperialSurfaceChargeDensityUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.SurfaceChargeDensity]
 * SI unit is `Coulomb per SquareMeter`
 */
@Serializable
sealed class SurfaceChargeDensity : DefinedScientificUnit<PhysicalQuantity.SurfaceChargeDensity>() {

    /**
     * The [ElectricCharge] component
     */
    abstract val charge: ElectricCharge

    /**
     * The [Area] component
     */
    abstract val per: Area
    override val symbol: String by lazy { "${charge.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.SurfaceChargeDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(charge.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = charge.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [SurfaceChargeDensity] for [MeasurementSystem.Metric]
 * @param charge the [ElectricCharge] component
 * @param per the [MetricArea] component
 */
@Serializable
data class MetricSurfaceChargeDensity(override val charge: ElectricCharge, override val per: MetricArea) :
    SurfaceChargeDensity(),
    MetricScientificUnit<PhysicalQuantity.SurfaceChargeDensity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [SurfaceChargeDensity] for [MeasurementSystem.Imperial]
 * @param charge the [ElectricCharge] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class ImperialSurfaceChargeDensity(override val charge: ElectricCharge, override val per: ImperialArea) :
    SurfaceChargeDensity(),
    ImperialScientificUnit<PhysicalQuantity.SurfaceChargeDensity> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricSurfaceChargeDensity] from an [ElectricCharge] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricSurfaceChargeDensity] represented by the units
 */
infix fun ElectricCharge.per(area: MetricArea) = MetricSurfaceChargeDensity(this, area)

/**
 * Gets an [ImperialSurfaceChargeDensity] from an [ElectricCharge] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialSurfaceChargeDensity] represented by the units
 */
infix fun ElectricCharge.per(area: ImperialArea) = ImperialSurfaceChargeDensity(this, area)

internal fun SerializersModuleBuilder.setupForSurfaceChargeDensity() {
    polymorphic(SurfaceChargeDensity::class) {
        registerSurfaceChargeDensityClasses()
    }
}

internal fun PolymorphicModuleBuilder<SurfaceChargeDensity>.registerSurfaceChargeDensityClasses() {
    subclass(ImperialSurfaceChargeDensity::class, ImperialSurfaceChargeDensity.serializer())
    subclass(MetricSurfaceChargeDensity::class, MetricSurfaceChargeDensity.serializer())
}
