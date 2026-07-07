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
 * Set of all [MetricPermeability]
 */
val MetricPermeabilityUnits: Set<MetricPermeability> get() = ElectricInductanceUnits.flatMap { inductance ->
    MetricLengthUnits.map { inductance per it }
}.toSet()

/**
 * Set of all [ImperialPermeability]
 */
val ImperialPermeabilityUnits: Set<ImperialPermeability> get() = ElectricInductanceUnits.flatMap { inductance ->
    ImperialLengthUnits.map { inductance per it }
}.toSet()

/**
 * Set of all [Permeability]
 */
val PermeabilityUnits: Set<Permeability> get() = MetricPermeabilityUnits +
    ImperialPermeabilityUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Permeability]
 * SI unit is `Henry per Meter`
 */
@Serializable
sealed class Permeability : DefinedScientificUnit<PhysicalQuantity.Permeability>() {

    /**
     * The [ElectricInductance] component
     */
    abstract val inductance: ElectricInductance

    /**
     * The [Length] component
     */
    abstract val per: Length
    override val symbol: String by lazy { "${inductance.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.Permeability
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(inductance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = inductance.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Permeability] for [MeasurementSystem.Metric]
 * @param inductance the [ElectricInductance] component
 * @param per the [MetricLength] component
 */
@Serializable
data class MetricPermeability(override val inductance: ElectricInductance, override val per: MetricLength) :
    Permeability(),
    MetricScientificUnit<PhysicalQuantity.Permeability> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Permeability] for [MeasurementSystem.Imperial]
 * @param inductance the [ElectricInductance] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class ImperialPermeability(override val inductance: ElectricInductance, override val per: ImperialLength) :
    Permeability(),
    ImperialScientificUnit<PhysicalQuantity.Permeability> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricPermeability] from an [ElectricInductance] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricPermeability] represented by the units
 */
infix fun ElectricInductance.per(length: MetricLength) = MetricPermeability(this, length)

/**
 * Gets an [ImperialPermeability] from an [ElectricInductance] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialPermeability] represented by the units
 */
infix fun ElectricInductance.per(length: ImperialLength) = ImperialPermeability(this, length)

internal fun SerializersModuleBuilder.setupForPermeability() {
    polymorphic(Permeability::class) {
        registerPermeabilityClasses()
    }
}

internal fun PolymorphicModuleBuilder<Permeability>.registerPermeabilityClasses() {
    subclass(ImperialPermeability::class, ImperialPermeability.serializer())
    subclass(MetricPermeability::class, MetricPermeability.serializer())
}
