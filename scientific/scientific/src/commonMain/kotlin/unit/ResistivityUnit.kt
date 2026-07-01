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
 * Set of all [MetricResistivity]
 */
val MetricResistivityUnits: Set<MetricResistivity> get() = ElectricResistanceUnits.flatMap { resistance ->
    MetricLengthUnits.map { resistance x it }
}.toSet()

/**
 * Set of all [ImperialResistivity]
 */
val ImperialResistivityUnits: Set<ImperialResistivity> get() = ElectricResistanceUnits.flatMap { resistance ->
    ImperialLengthUnits.map { resistance x it }
}.toSet()

/**
 * Set of all [Resistivity]
 */
val ResistivityUnits: Set<Resistivity> get() = MetricResistivityUnits +
    ImperialResistivityUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Resistivity]
 * SI unit is `Ohm x Meter`
 */
@Serializable
sealed class Resistivity : DefinedScientificUnit<PhysicalQuantity.Resistivity>() {

    /**
     * The [ElectricResistance] component
     */
    abstract val resistance: ElectricResistance

    /**
     * The [Length] component
     */
    abstract val length: Length
    override val quantity = PhysicalQuantity.Resistivity
    override val symbol: String by lazy { "${resistance.symbol}⋅${length.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = length.fromSIUnit(resistance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = resistance.toSIUnit(length.toSIUnit(value))
}

/**
 * A [Resistivity] for [MeasurementSystem.Metric]
 * @param resistance the [ElectricResistance] component
 * @param length the [MetricLength] component
 */
@Serializable
data class MetricResistivity(override val resistance: ElectricResistance, override val length: MetricLength) :
    Resistivity(),
    MetricScientificUnit<PhysicalQuantity.Resistivity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Resistivity] for [MeasurementSystem.Imperial]
 * @param resistance the [ElectricResistance] component
 * @param length the [ImperialLength] component
 */
@Serializable
data class ImperialResistivity(override val resistance: ElectricResistance, override val length: ImperialLength) :
    Resistivity(),
    ImperialScientificUnit<PhysicalQuantity.Resistivity> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricResistivity] from an [ElectricResistance] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricResistivity] represented by the units
 */
infix fun ElectricResistance.x(length: MetricLength) = MetricResistivity(this, length)

/**
 * Gets an [ImperialResistivity] from an [ElectricResistance] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialResistivity] represented by the units
 */
infix fun ElectricResistance.x(length: ImperialLength) = ImperialResistivity(this, length)

internal fun SerializersModuleBuilder.setupForResistivity() {
    polymorphic(Resistivity::class) {
        registerResistivityClasses()
    }
}

internal fun PolymorphicModuleBuilder<Resistivity>.registerResistivityClasses() {
    subclass(ImperialResistivity::class, ImperialResistivity.serializer())
    subclass(MetricResistivity::class, MetricResistivity.serializer())
}
