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
 * Set of all [MetricMassFlux]
 */
val MetricMassFluxUnits: Set<MetricMassFlux> get() = MetricMassFlowRateUnits.flatMap { massFlowRate ->
    MetricAreaUnits.map { massFlowRate per it }
}.toSet()

/**
 * Set of all [ImperialMassFlux]
 */
val ImperialMassFluxUnits: Set<ImperialMassFlux> get() = ImperialMassFlowRateUnits.flatMap { massFlowRate ->
    ImperialAreaUnits.map { massFlowRate per it }
}.toSet()

/**
 * Set of all [UKImperialMassFlux]
 */
val UKImperialMassFluxUnits: Set<UKImperialMassFlux> get() = UKImperialMassFlowRateUnits.flatMap { massFlowRate ->
    ImperialAreaUnits.map { massFlowRate per it }
}.toSet()

/**
 * Set of all [USCustomaryMassFlux]
 */
val USCustomaryMassFluxUnits: Set<USCustomaryMassFlux> get() = USCustomaryMassFlowRateUnits.flatMap { massFlowRate ->
    ImperialAreaUnits.map { massFlowRate per it }
}.toSet()

/**
 * Set of all [MassFlux]
 */
val MassFluxUnits: Set<MassFlux> get() = MetricMassFluxUnits +
    ImperialMassFluxUnits +
    UKImperialMassFluxUnits.filter { it.massFlowRate.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMassFluxUnits.filter { it.massFlowRate.weight !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.MassFlux]
 * SI unit is `Kilogram per Second per SquareMeter`
 */
@Serializable
sealed class MassFlux : DefinedScientificUnit<PhysicalQuantity.MassFlux>() {

    /**
     * The [MassFlowRate] component
     */
    abstract val massFlowRate: MassFlowRate

    /**
     * The [Area] component
     */
    abstract val per: Area
    override val quantity = PhysicalQuantity.MassFlux
    override val symbol: String by lazy { "${massFlowRate.symbol}⋅${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(massFlowRate.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = massFlowRate.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [MassFlux] for [MeasurementSystem.Metric]
 * @param massFlowRate the [MetricMassFlowRate] component
 * @param per the [MetricArea] component
 */
@Serializable
data class MetricMassFlux(override val massFlowRate: MetricMassFlowRate, override val per: MetricArea) :
    MassFlux(),
    MetricScientificUnit<PhysicalQuantity.MassFlux> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [MassFlux] for [MeasurementSystem.Imperial]
 * @param massFlowRate the [ImperialMassFlowRate] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class ImperialMassFlux(override val massFlowRate: ImperialMassFlowRate, override val per: ImperialArea) :
    MassFlux(),
    ImperialScientificUnit<PhysicalQuantity.MassFlux> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialMassFlux] equivalent to this [ImperialMassFlux]
     */
    val ukImperial get() = massFlowRate.ukImperial per per

    /**
     * The [USCustomaryMassFlux] equivalent to this [ImperialMassFlux]
     */
    val usCustomary get() = massFlowRate.usCustomary per per
}

/**
 * A [MassFlux] for [MeasurementSystem.UKImperial]
 * @param massFlowRate the [UKImperialMassFlowRate] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class UKImperialMassFlux(override val massFlowRate: UKImperialMassFlowRate, override val per: ImperialArea) :
    MassFlux(),
    UKImperialScientificUnit<PhysicalQuantity.MassFlux> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [MassFlux] for [MeasurementSystem.USCustomary]
 * @param massFlowRate the [USCustomaryMassFlowRate] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class USCustomaryMassFlux(override val massFlowRate: USCustomaryMassFlowRate, override val per: ImperialArea) :
    MassFlux(),
    USCustomaryScientificUnit<PhysicalQuantity.MassFlux> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricMassFlux] from a [MetricMassFlowRate] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricMassFlux] represented by the units
 */
infix fun MetricMassFlowRate.per(area: MetricArea) = MetricMassFlux(this, area)

/**
 * Gets an [ImperialMassFlux] from a [MetricMassFlowRate] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialMassFlux] represented by the units
 */
infix fun ImperialMassFlowRate.per(area: ImperialArea) = ImperialMassFlux(this, area)

/**
 * Gets a [UKImperialMassFlux] from a [MetricMassFlowRate] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [UKImperialMassFlux] represented by the units
 */
infix fun UKImperialMassFlowRate.per(area: ImperialArea) = UKImperialMassFlux(this, area)

/**
 * Gets a [USCustomaryMassFlux] from a [MetricMassFlowRate] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [USCustomaryMassFlux] represented by the units
 */
infix fun USCustomaryMassFlowRate.per(area: ImperialArea) = USCustomaryMassFlux(this, area)

internal fun SerializersModuleBuilder.setupForMassFlux() {
    polymorphic(MassFlux::class) {
        registerMassFluxClasses()
    }
}

internal fun PolymorphicModuleBuilder<MassFlux>.registerMassFluxClasses() {
    subclass(ImperialMassFlux::class, ImperialMassFlux.serializer())
    subclass(MetricMassFlux::class, MetricMassFlux.serializer())
    subclass(UKImperialMassFlux::class, UKImperialMassFlux.serializer())
    subclass(USCustomaryMassFlux::class, USCustomaryMassFlux.serializer())
}
