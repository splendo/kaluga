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
 * Set of all [MetricThermalConductivity]
 */
val MetricThermalConductivityUnits: Set<MetricThermalConductivity> get() = MetricThermalConductanceUnits.flatMap { thermalConductance ->
    MetricLengthUnits.map { thermalConductance per it }
}.toSet()

/**
 * Set of all [UKImperialThermalConductivity]
 */
val UKImperialThermalConductivityUnits: Set<UKImperialThermalConductivity> get() = UKImperialThermalConductanceUnits.flatMap { thermalConductance ->
    ImperialLengthUnits.map { thermalConductance per it }
}.toSet()

/**
 * Set of all [USCustomaryThermalConductivity]
 */
val USCustomaryThermalConductivityUnits: Set<USCustomaryThermalConductivity> get() = USCustomaryThermalConductanceUnits.flatMap { thermalConductance ->
    ImperialLengthUnits.map { thermalConductance per it }
}.toSet()

/**
 * Set of all [ThermalConductivity]
 */
val ThermalConductivityUnits: Set<ThermalConductivity> get() = MetricThermalConductivityUnits +
    UKImperialThermalConductivityUnits +
    USCustomaryThermalConductivityUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.ThermalConductivity]
 * SI unit is `Watt per Kelvin per Meter`
 */
@Serializable
sealed class ThermalConductivity : DefinedScientificUnit<PhysicalQuantity.ThermalConductivity>() {

    /**
     * The [ThermalConductance] component
     */
    abstract val thermalConductance: ThermalConductance

    /**
     * The [Length] component
     */
    abstract val per: Length
    override val quantity = PhysicalQuantity.ThermalConductivity
    override val symbol: String by lazy { "${thermalConductance.power.symbol}/${thermalConductance.per.symbol}⋅${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(thermalConductance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = thermalConductance.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [ThermalConductivity] for [MeasurementSystem.Metric]
 * @param thermalConductance the [MetricThermalConductance] component
 * @param per the [MetricLength] component
 */
@Serializable
data class MetricThermalConductivity(override val thermalConductance: MetricThermalConductance, override val per: MetricLength) :
    ThermalConductivity(),
    MetricScientificUnit<PhysicalQuantity.ThermalConductivity> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [ThermalConductivity] for [MeasurementSystem.UKImperial]
 * @param thermalConductance the [UKImperialThermalConductance] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class UKImperialThermalConductivity(override val thermalConductance: UKImperialThermalConductance, override val per: ImperialLength) :
    ThermalConductivity(),
    UKImperialScientificUnit<PhysicalQuantity.ThermalConductivity> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [ThermalConductivity] for [MeasurementSystem.USCustomary]
 * @param thermalConductance the [USCustomaryThermalConductance] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class USCustomaryThermalConductivity(override val thermalConductance: USCustomaryThermalConductance, override val per: ImperialLength) :
    ThermalConductivity(),
    USCustomaryScientificUnit<PhysicalQuantity.ThermalConductivity> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricThermalConductivity] from a [MetricAndUKImperialThermalConductance] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricThermalConductivity] represented by the units
 */
infix fun MetricAndUKImperialThermalConductance.per(length: MetricLength) = MetricThermalConductivity(metric, length)

/**
 * Gets a [UKImperialThermalConductivity] from a [MetricAndUKImperialThermalConductance] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [UKImperialThermalConductivity] represented by the units
 */
infix fun MetricAndUKImperialThermalConductance.per(length: ImperialLength) = UKImperialThermalConductivity(ukImperial, length)

/**
 * Gets a [MetricThermalConductivity] from a [MetricThermalConductance] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricThermalConductivity] represented by the units
 */
infix fun MetricThermalConductance.per(length: MetricLength) = MetricThermalConductivity(this, length)

/**
 * Gets a [UKImperialThermalConductivity] from a [UKImperialThermalConductance] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [UKImperialThermalConductivity] represented by the units
 */
infix fun UKImperialThermalConductance.per(length: ImperialLength) = UKImperialThermalConductivity(this, length)

/**
 * Gets a [USCustomaryThermalConductivity] from a [USCustomaryThermalConductance] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [USCustomaryThermalConductivity] represented by the units
 */
infix fun USCustomaryThermalConductance.per(length: ImperialLength) = USCustomaryThermalConductivity(this, length)

internal fun SerializersModuleBuilder.setupForThermalConductivity() {
    polymorphic(ThermalConductivity::class) {
        registerThermalConductivityClasses()
    }
}

internal fun PolymorphicModuleBuilder<ThermalConductivity>.registerThermalConductivityClasses() {
    subclass(MetricThermalConductivity::class, MetricThermalConductivity.serializer())
    subclass(UKImperialThermalConductivity::class, UKImperialThermalConductivity.serializer())
    subclass(USCustomaryThermalConductivity::class, USCustomaryThermalConductivity.serializer())
}
