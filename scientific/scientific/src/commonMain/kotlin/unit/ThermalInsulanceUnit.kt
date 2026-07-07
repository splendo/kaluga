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
 * Set of all [MetricThermalInsulance]
 */
val MetricThermalInsulanceUnits: Set<MetricThermalInsulance> get() = MetricThermalResistanceUnits.flatMap { thermalResistance ->
    MetricAreaUnits.map { thermalResistance x it }
}.toSet()

/**
 * Set of all [UKImperialThermalInsulance]
 */
val UKImperialThermalInsulanceUnits: Set<UKImperialThermalInsulance> get() = UKImperialThermalResistanceUnits.flatMap { thermalResistance ->
    ImperialAreaUnits.map { thermalResistance x it }
}.toSet()

/**
 * Set of all [USCustomaryThermalInsulance]
 */
val USCustomaryThermalInsulanceUnits: Set<USCustomaryThermalInsulance> get() = USCustomaryThermalResistanceUnits.flatMap { thermalResistance ->
    ImperialAreaUnits.map { thermalResistance x it }
}.toSet()

/**
 * Set of all [ThermalInsulance]
 */
val ThermalInsulanceUnits: Set<ThermalInsulance> get() = MetricThermalInsulanceUnits +
    UKImperialThermalInsulanceUnits +
    USCustomaryThermalInsulanceUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.ThermalInsulance]
 * SI unit is `(Kelvin per Watt) x SquareMeter`
 */
@Serializable
sealed class ThermalInsulance : DefinedScientificUnit<PhysicalQuantity.ThermalInsulance>() {

    /**
     * The [ThermalResistance] component
     */
    abstract val thermalResistance: ThermalResistance

    /**
     * The [Area] component
     */
    abstract val area: Area
    override val quantity = PhysicalQuantity.ThermalInsulance
    override val symbol: String by lazy { "${thermalResistance.temperature.symbol}⋅${area.symbol}/${thermalResistance.per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = area.fromSIUnit(thermalResistance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = thermalResistance.toSIUnit(area.toSIUnit(value))
}

/**
 * A [ThermalInsulance] for [MeasurementSystem.Metric]
 * @param thermalResistance the [MetricThermalResistance] component
 * @param area the [MetricArea] component
 */
@Serializable
data class MetricThermalInsulance(override val thermalResistance: MetricThermalResistance, override val area: MetricArea) :
    ThermalInsulance(),
    MetricScientificUnit<PhysicalQuantity.ThermalInsulance> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [ThermalInsulance] for [MeasurementSystem.UKImperial]
 * @param thermalResistance the [UKImperialThermalResistance] component
 * @param area the [ImperialArea] component
 */
@Serializable
data class UKImperialThermalInsulance(override val thermalResistance: UKImperialThermalResistance, override val area: ImperialArea) :
    ThermalInsulance(),
    UKImperialScientificUnit<PhysicalQuantity.ThermalInsulance> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [ThermalInsulance] for [MeasurementSystem.USCustomary]
 * @param thermalResistance the [USCustomaryThermalResistance] component
 * @param area the [ImperialArea] component
 */
@Serializable
data class USCustomaryThermalInsulance(override val thermalResistance: USCustomaryThermalResistance, override val area: ImperialArea) :
    ThermalInsulance(),
    USCustomaryScientificUnit<PhysicalQuantity.ThermalInsulance> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricThermalInsulance] from a [MetricAndUKImperialThermalResistance] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricThermalInsulance] represented by the units
 */
infix fun MetricAndUKImperialThermalResistance.x(area: MetricArea) = MetricThermalInsulance(metric, area)

/**
 * Gets a [UKImperialThermalInsulance] from a [MetricAndUKImperialThermalResistance] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [UKImperialThermalInsulance] represented by the units
 */
infix fun MetricAndUKImperialThermalResistance.x(area: ImperialArea) = UKImperialThermalInsulance(ukImperial, area)

/**
 * Gets a [MetricThermalInsulance] from a [MetricThermalResistance] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricThermalInsulance] represented by the units
 */
infix fun MetricThermalResistance.x(area: MetricArea) = MetricThermalInsulance(this, area)

/**
 * Gets a [UKImperialThermalInsulance] from a [UKImperialThermalResistance] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [UKImperialThermalInsulance] represented by the units
 */
infix fun UKImperialThermalResistance.x(area: ImperialArea) = UKImperialThermalInsulance(this, area)

/**
 * Gets a [USCustomaryThermalInsulance] from a [USCustomaryThermalResistance] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [USCustomaryThermalInsulance] represented by the units
 */
infix fun USCustomaryThermalResistance.x(area: ImperialArea) = USCustomaryThermalInsulance(this, area)

internal fun SerializersModuleBuilder.setupForThermalInsulance() {
    polymorphic(ThermalInsulance::class) {
        registerThermalInsulanceClasses()
    }
}

internal fun PolymorphicModuleBuilder<ThermalInsulance>.registerThermalInsulanceClasses() {
    subclass(MetricThermalInsulance::class, MetricThermalInsulance.serializer())
    subclass(UKImperialThermalInsulance::class, UKImperialThermalInsulance.serializer())
    subclass(USCustomaryThermalInsulance::class, USCustomaryThermalInsulance.serializer())
}
