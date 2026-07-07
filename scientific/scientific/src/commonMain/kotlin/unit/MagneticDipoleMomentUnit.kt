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
 * Set of all [MetricMagneticDipoleMoment]
 */
val MetricMagneticDipoleMomentUnits: Set<MetricMagneticDipoleMoment> get() = ElectricCurrentUnits.flatMap { current ->
    MetricAreaUnits.map { current x it }
}.toSet()

/**
 * Set of all [ImperialMagneticDipoleMoment]
 */
val ImperialMagneticDipoleMomentUnits: Set<ImperialMagneticDipoleMoment> get() = ElectricCurrentUnits.flatMap { current ->
    ImperialAreaUnits.map { current x it }
}.toSet()

/**
 * Set of all [MagneticDipoleMoment]
 */
val MagneticDipoleMomentUnits: Set<MagneticDipoleMoment> get() = MetricMagneticDipoleMomentUnits +
    ImperialMagneticDipoleMomentUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.MagneticDipoleMoment]
 * SI unit is `Ampere x SquareMeter`
 */
@Serializable
sealed class MagneticDipoleMoment : DefinedScientificUnit<PhysicalQuantity.MagneticDipoleMoment>() {

    /**
     * The [ElectricCurrent] component
     */
    abstract val current: ElectricCurrent

    /**
     * The [Area] component
     */
    abstract val area: Area
    override val quantity = PhysicalQuantity.MagneticDipoleMoment
    override val symbol: String by lazy { "${current.symbol}⋅${area.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = area.fromSIUnit(current.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = current.toSIUnit(area.toSIUnit(value))
}

/**
 * A [MagneticDipoleMoment] for [MeasurementSystem.Metric]
 * @param current the [ElectricCurrent] component
 * @param area the [MetricArea] component
 */
@Serializable
data class MetricMagneticDipoleMoment(override val current: ElectricCurrent, override val area: MetricArea) :
    MagneticDipoleMoment(),
    MetricScientificUnit<PhysicalQuantity.MagneticDipoleMoment> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [MagneticDipoleMoment] for [MeasurementSystem.Imperial]
 * @param current the [ElectricCurrent] component
 * @param area the [ImperialArea] component
 */
@Serializable
data class ImperialMagneticDipoleMoment(override val current: ElectricCurrent, override val area: ImperialArea) :
    MagneticDipoleMoment(),
    ImperialScientificUnit<PhysicalQuantity.MagneticDipoleMoment> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricMagneticDipoleMoment] from an [ElectricCurrent] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricMagneticDipoleMoment] represented by the units
 */
infix fun ElectricCurrent.x(area: MetricArea) = MetricMagneticDipoleMoment(this, area)

/**
 * Gets an [ImperialMagneticDipoleMoment] from an [ElectricCurrent] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialMagneticDipoleMoment] represented by the units
 */
infix fun ElectricCurrent.x(area: ImperialArea) = ImperialMagneticDipoleMoment(this, area)

internal fun SerializersModuleBuilder.setupForMagneticDipoleMoment() {
    polymorphic(MagneticDipoleMoment::class) {
        registerMagneticDipoleMomentClasses()
    }
}

internal fun PolymorphicModuleBuilder<MagneticDipoleMoment>.registerMagneticDipoleMomentClasses() {
    subclass(ImperialMagneticDipoleMoment::class, ImperialMagneticDipoleMoment.serializer())
    subclass(MetricMagneticDipoleMoment::class, MetricMagneticDipoleMoment.serializer())
}
