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
 * Set of all [MetricElectricFieldStrength]
 */
val MetricElectricFieldStrengthUnits: Set<MetricElectricFieldStrength> get() = VoltageUnits.flatMap { voltage ->
    MetricLengthUnits.map { voltage per it }
}.toSet()

/**
 * Set of all [ImperialElectricFieldStrength]
 */
val ImperialElectricFieldStrengthUnits: Set<ImperialElectricFieldStrength> get() = VoltageUnits.flatMap { voltage ->
    ImperialLengthUnits.map { voltage per it }
}.toSet()

/**
 * Set of all [ElectricFieldStrength]
 */
val ElectricFieldStrengthUnits: Set<ElectricFieldStrength> get() = MetricElectricFieldStrengthUnits +
    ImperialElectricFieldStrengthUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.ElectricFieldStrength]
 * SI unit is `Volt per Meter`
 */
@Serializable
sealed class ElectricFieldStrength : DefinedScientificUnit<PhysicalQuantity.ElectricFieldStrength>() {

    /**
     * The [Voltage] component
     */
    abstract val voltage: Voltage

    /**
     * The [Length] component
     */
    abstract val per: Length
    override val symbol: String by lazy { "${voltage.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.ElectricFieldStrength
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(voltage.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = voltage.toSIUnit(per.fromSIUnit(value))
}

/**
 * An [ElectricFieldStrength] for [MeasurementSystem.Metric]
 * @param voltage the [Voltage] component
 * @param per the [MetricLength] component
 */
@Serializable
data class MetricElectricFieldStrength(override val voltage: Voltage, override val per: MetricLength) :
    ElectricFieldStrength(),
    MetricScientificUnit<PhysicalQuantity.ElectricFieldStrength> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [ElectricFieldStrength] for [MeasurementSystem.Imperial]
 * @param voltage the [Voltage] component
 * @param per the [ImperialLength] component
 */
@Serializable
data class ImperialElectricFieldStrength(override val voltage: Voltage, override val per: ImperialLength) :
    ElectricFieldStrength(),
    ImperialScientificUnit<PhysicalQuantity.ElectricFieldStrength> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricElectricFieldStrength] from a [Voltage] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricElectricFieldStrength] represented by the units
 */
infix fun Voltage.per(length: MetricLength) = MetricElectricFieldStrength(this, length)

/**
 * Gets an [ImperialElectricFieldStrength] from a [Voltage] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialElectricFieldStrength] represented by the units
 */
infix fun Voltage.per(length: ImperialLength) = ImperialElectricFieldStrength(this, length)

internal fun SerializersModuleBuilder.setupForElectricFieldStrength() {
    polymorphic(ElectricFieldStrength::class) {
        registerElectricFieldStrengthClasses()
    }
}

internal fun PolymorphicModuleBuilder<ElectricFieldStrength>.registerElectricFieldStrengthClasses() {
    subclass(ImperialElectricFieldStrength::class, ImperialElectricFieldStrength.serializer())
    subclass(MetricElectricFieldStrength::class, MetricElectricFieldStrength.serializer())
}
