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
 * Set of all [MetricMagneticFieldStrength]
 */
val MetricMagneticFieldStrengthUnits: Set<MetricMagneticFieldStrength> get() = ElectricCurrentUnits.flatMap { current ->
    MetricLengthUnits.map { current per it }
}.toSet()

/**
 * Set of all [ImperialMagneticFieldStrength]
 */
val ImperialMagneticFieldStrengthUnits: Set<ImperialMagneticFieldStrength> get() = ElectricCurrentUnits.flatMap { current ->
    ImperialLengthUnits.map { current per it }
}.toSet()

/**
 * Set of all [MagneticFieldStrength]
 */
val MagneticFieldStrengthUnits: Set<MagneticFieldStrength> get() = MetricMagneticFieldStrengthUnits +
    ImperialMagneticFieldStrengthUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.MagneticFieldStrength]
 * SI unit is `Ampere per Meter`
 */
@Serializable
sealed class MagneticFieldStrength : DefinedScientificUnit<PhysicalQuantity.MagneticFieldStrength>() {

    /**
     * The [ElectricCurrent] component
     */
    abstract val current: ElectricCurrent

    /**
     * The [Length] component
     */
    abstract val per: Length
    override val symbol: String by lazy { "${current.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.MagneticFieldStrength
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(current.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = current.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [MagneticFieldStrength] for [MeasurementSystem.Metric]
 * @param current the [ElectricCurrent] component
 * @param per the [MetricLength] component
 */
@Serializable
data class MetricMagneticFieldStrength(override val current: ElectricCurrent, override val per: MetricLength) :
    MagneticFieldStrength(),
    MetricScientificUnit<PhysicalQuantity.MagneticFieldStrength> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [MagneticFieldStrength] for [MeasurementSystem.Imperial]
 * @param current the [ElectricCurrent] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class ImperialMagneticFieldStrength(override val current: ElectricCurrent, override val per: ImperialLength) :
    MagneticFieldStrength(),
    ImperialScientificUnit<PhysicalQuantity.MagneticFieldStrength> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricMagneticFieldStrength] from an [ElectricCurrent] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricMagneticFieldStrength] represented by the units
 */
infix fun ElectricCurrent.per(length: MetricLength) = MetricMagneticFieldStrength(this, length)

/**
 * Gets an [ImperialMagneticFieldStrength] from an [ElectricCurrent] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialMagneticFieldStrength] represented by the units
 */
infix fun ElectricCurrent.per(length: ImperialLength) = ImperialMagneticFieldStrength(this, length)

internal fun SerializersModuleBuilder.setupForMagneticFieldStrength() {
    polymorphic(MagneticFieldStrength::class) {
        registerMagneticFieldStrengthClasses()
    }
}

internal fun PolymorphicModuleBuilder<MagneticFieldStrength>.registerMagneticFieldStrengthClasses() {
    subclass(ImperialMagneticFieldStrength::class, ImperialMagneticFieldStrength.serializer())
    subclass(MetricMagneticFieldStrength::class, MetricMagneticFieldStrength.serializer())
}
