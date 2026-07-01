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
 * Set of all [MetricElectricalConductivity]
 */
val MetricElectricalConductivityUnits: Set<MetricElectricalConductivity> get() = ElectricConductanceUnits.flatMap { conductance ->
    MetricLengthUnits.map { conductance per it }
}.toSet()

/**
 * Set of all [ImperialElectricalConductivity]
 */
val ImperialElectricalConductivityUnits: Set<ImperialElectricalConductivity> get() = ElectricConductanceUnits.flatMap { conductance ->
    ImperialLengthUnits.map { conductance per it }
}.toSet()

/**
 * Set of all [ElectricalConductivity]
 */
val ElectricalConductivityUnits: Set<ElectricalConductivity> get() = MetricElectricalConductivityUnits +
    ImperialElectricalConductivityUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.ElectricalConductivity]
 * SI unit is `Siemens per Meter`
 */
@Serializable
sealed class ElectricalConductivity : DefinedScientificUnit<PhysicalQuantity.ElectricalConductivity>() {

    /**
     * The [ElectricConductance] component
     */
    abstract val conductance: ElectricConductance

    /**
     * The [Length] component
     */
    abstract val per: Length
    override val symbol: String by lazy { "${conductance.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.ElectricalConductivity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(conductance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = conductance.toSIUnit(per.fromSIUnit(value))
}

/**
 * An [ElectricalConductivity] for [MeasurementSystem.Metric]
 * @param conductance the [ElectricConductance] component
 * @param per the [MetricLength] component
 */
@Serializable
data class MetricElectricalConductivity(override val conductance: ElectricConductance, override val per: MetricLength) :
    ElectricalConductivity(),
    MetricScientificUnit<PhysicalQuantity.ElectricalConductivity> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [ElectricalConductivity] for [MeasurementSystem.Imperial]
 * @param conductance the [ElectricConductance] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class ImperialElectricalConductivity(override val conductance: ElectricConductance, override val per: ImperialLength) :
    ElectricalConductivity(),
    ImperialScientificUnit<PhysicalQuantity.ElectricalConductivity> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricElectricalConductivity] from an [ElectricConductance] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricElectricalConductivity] represented by the units
 */
infix fun ElectricConductance.per(length: MetricLength) = MetricElectricalConductivity(this, length)

/**
 * Gets an [ImperialElectricalConductivity] from an [ElectricConductance] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialElectricalConductivity] represented by the units
 */
infix fun ElectricConductance.per(length: ImperialLength) = ImperialElectricalConductivity(this, length)

internal fun SerializersModuleBuilder.setupForElectricalConductivity() {
    polymorphic(ElectricalConductivity::class) {
        registerElectricalConductivityClasses()
    }
}

internal fun PolymorphicModuleBuilder<ElectricalConductivity>.registerElectricalConductivityClasses() {
    subclass(ImperialElectricalConductivity::class, ImperialElectricalConductivity.serializer())
    subclass(MetricElectricalConductivity::class, MetricElectricalConductivity.serializer())
}
