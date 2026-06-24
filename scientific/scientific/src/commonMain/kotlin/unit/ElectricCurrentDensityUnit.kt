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
 * Set of all [MetricElectricCurrentDensity]
 */
val MetricElectricCurrentDensityUnits: Set<MetricElectricCurrentDensity> get() = ElectricCurrentUnits.flatMap { current ->
    MetricAreaUnits.map { current per it }
}.toSet()

/**
 * Set of all [ImperialElectricCurrentDensity]
 */
val ImperialElectricCurrentDensityUnits: Set<ImperialElectricCurrentDensity> get() = ElectricCurrentUnits.flatMap { current ->
    ImperialAreaUnits.map { current per it }
}.toSet()

/**
 * Set of all [ElectricCurrentDensity]
 */
val ElectricCurrentDensityUnits: Set<ElectricCurrentDensity> get() = MetricElectricCurrentDensityUnits +
    ImperialElectricCurrentDensityUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.ElectricCurrentDensity]
 * SI unit is `Ampere per SquareMeter`
 */
@Serializable
sealed class ElectricCurrentDensity : DefinedScientificUnit<PhysicalQuantity.ElectricCurrentDensity>() {

    /**
     * The [ElectricCurrent] component
     */
    abstract val current: ElectricCurrent

    /**
     * The [Area] component
     */
    abstract val per: Area
    override val symbol: String by lazy { "${current.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.ElectricCurrentDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(current.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = current.toSIUnit(per.fromSIUnit(value))
}

/**
 * An [ElectricCurrentDensity] for [MeasurementSystem.Metric]
 * @param current the [ElectricCurrent] component
 * @param per the [MetricArea] component
 */
@Serializable
data class MetricElectricCurrentDensity(override val current: ElectricCurrent, override val per: MetricArea) :
    ElectricCurrentDensity(),
    MetricScientificUnit<PhysicalQuantity.ElectricCurrentDensity> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [ElectricCurrentDensity] for [MeasurementSystem.Imperial]
 * @param current the [ElectricCurrent] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class ImperialElectricCurrentDensity(override val current: ElectricCurrent, override val per: ImperialArea) :
    ElectricCurrentDensity(),
    ImperialScientificUnit<PhysicalQuantity.ElectricCurrentDensity> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricElectricCurrentDensity] from an [ElectricCurrent] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricElectricCurrentDensity] represented by the units
 */
infix fun ElectricCurrent.per(area: MetricArea) = MetricElectricCurrentDensity(this, area)

/**
 * Gets an [ImperialElectricCurrentDensity] from an [ElectricCurrent] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialElectricCurrentDensity] represented by the units
 */
infix fun ElectricCurrent.per(area: ImperialArea) = ImperialElectricCurrentDensity(this, area)

internal fun SerializersModuleBuilder.setupForElectricCurrentDensity() {
    polymorphic(ElectricCurrentDensity::class) {
        registerElectricCurrentDensityClasses()
    }
}

internal fun PolymorphicModuleBuilder<ElectricCurrentDensity>.registerElectricCurrentDensityClasses() {
    subclass(ImperialElectricCurrentDensity::class, ImperialElectricCurrentDensity.serializer())
    subclass(MetricElectricCurrentDensity::class, MetricElectricCurrentDensity.serializer())
}
