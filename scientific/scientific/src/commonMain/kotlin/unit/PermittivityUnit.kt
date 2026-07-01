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
 * Set of all [MetricPermittivity]
 */
val MetricPermittivityUnits: Set<MetricPermittivity> get() = ElectricCapacitanceUnits.flatMap { capacitance ->
    MetricLengthUnits.map { capacitance per it }
}.toSet()

/**
 * Set of all [ImperialPermittivity]
 */
val ImperialPermittivityUnits: Set<ImperialPermittivity> get() = ElectricCapacitanceUnits.flatMap { capacitance ->
    ImperialLengthUnits.map { capacitance per it }
}.toSet()

/**
 * Set of all [Permittivity]
 */
val PermittivityUnits: Set<Permittivity> get() = MetricPermittivityUnits +
    ImperialPermittivityUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Permittivity]
 * SI unit is `Farad per Meter`
 */
@Serializable
sealed class Permittivity : DefinedScientificUnit<PhysicalQuantity.Permittivity>() {

    /**
     * The [ElectricCapacitance] component
     */
    abstract val capacitance: ElectricCapacitance

    /**
     * The [Length] component
     */
    abstract val per: Length
    override val symbol: String by lazy { "${capacitance.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.Permittivity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(capacitance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = capacitance.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Permittivity] for [MeasurementSystem.Metric]
 * @param capacitance the [ElectricCapacitance] component
 * @param per the [MetricLength] component
 */
@Serializable
data class MetricPermittivity(override val capacitance: ElectricCapacitance, override val per: MetricLength) :
    Permittivity(),
    MetricScientificUnit<PhysicalQuantity.Permittivity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Permittivity] for [MeasurementSystem.Imperial]
 * @param capacitance the [ElectricCapacitance] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class ImperialPermittivity(override val capacitance: ElectricCapacitance, override val per: ImperialLength) :
    Permittivity(),
    ImperialScientificUnit<PhysicalQuantity.Permittivity> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricPermittivity] from an [ElectricCapacitance] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricPermittivity] represented by the units
 */
infix fun ElectricCapacitance.per(length: MetricLength) = MetricPermittivity(this, length)

/**
 * Gets an [ImperialPermittivity] from an [ElectricCapacitance] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialPermittivity] represented by the units
 */
infix fun ElectricCapacitance.per(length: ImperialLength) = ImperialPermittivity(this, length)

internal fun SerializersModuleBuilder.setupForPermittivity() {
    polymorphic(Permittivity::class) {
        registerPermittivityClasses()
    }
}

internal fun PolymorphicModuleBuilder<Permittivity>.registerPermittivityClasses() {
    subclass(ImperialPermittivity::class, ImperialPermittivity.serializer())
    subclass(MetricPermittivity::class, MetricPermittivity.serializer())
}
