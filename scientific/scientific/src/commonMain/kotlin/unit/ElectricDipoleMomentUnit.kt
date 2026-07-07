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
 * Set of all [MetricElectricDipoleMoment]
 */
val MetricElectricDipoleMomentUnits: Set<MetricElectricDipoleMoment> get() = ElectricChargeUnits.flatMap { charge ->
    MetricLengthUnits.map { charge x it }
}.toSet()

/**
 * Set of all [ImperialElectricDipoleMoment]
 */
val ImperialElectricDipoleMomentUnits: Set<ImperialElectricDipoleMoment> get() = ElectricChargeUnits.flatMap { charge ->
    ImperialLengthUnits.map { charge x it }
}.toSet()

/**
 * Set of all [ElectricDipoleMoment]
 */
val ElectricDipoleMomentUnits: Set<ElectricDipoleMoment> get() = MetricElectricDipoleMomentUnits +
    ImperialElectricDipoleMomentUnits

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.ElectricDipoleMoment]
 * SI unit is `Coulomb x Meter`
 */
@Serializable
sealed class ElectricDipoleMoment : DefinedScientificUnit<PhysicalQuantity.ElectricDipoleMoment>() {

    /**
     * The [ElectricCharge] component
     */
    abstract val charge: ElectricCharge

    /**
     * The [Length] component
     */
    abstract val length: Length
    override val quantity = PhysicalQuantity.ElectricDipoleMoment
    override val symbol: String by lazy { "${charge.symbol}⋅${length.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = length.fromSIUnit(charge.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = charge.toSIUnit(length.toSIUnit(value))
}

/**
 * An [ElectricDipoleMoment] for [MeasurementSystem.Metric]
 * @param charge the [ElectricCharge] component
 * @param length the [MetricLength] component
 */
@Serializable
data class MetricElectricDipoleMoment(override val charge: ElectricCharge, override val length: MetricLength) :
    ElectricDipoleMoment(),
    MetricScientificUnit<PhysicalQuantity.ElectricDipoleMoment> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [ElectricDipoleMoment] for [MeasurementSystem.Imperial]
 * @param charge the [ElectricCharge] component
 * @param length the [ImperialLength] component
 */
@Serializable
data class ImperialElectricDipoleMoment(override val charge: ElectricCharge, override val length: ImperialLength) :
    ElectricDipoleMoment(),
    ImperialScientificUnit<PhysicalQuantity.ElectricDipoleMoment> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricElectricDipoleMoment] from an [ElectricCharge] and a [MetricLength]
 * @param length the [MetricLength] component
 * @return the [MetricElectricDipoleMoment] represented by the units
 */
infix fun ElectricCharge.x(length: MetricLength) = MetricElectricDipoleMoment(this, length)

/**
 * Gets an [ImperialElectricDipoleMoment] from an [ElectricCharge] and an [ImperialLength]
 * @param length the [ImperialLength] component
 * @return the [ImperialElectricDipoleMoment] represented by the units
 */
infix fun ElectricCharge.x(length: ImperialLength) = ImperialElectricDipoleMoment(this, length)

internal fun SerializersModuleBuilder.setupForElectricDipoleMoment() {
    polymorphic(ElectricDipoleMoment::class) {
        registerElectricDipoleMomentClasses()
    }
}

internal fun PolymorphicModuleBuilder<ElectricDipoleMoment>.registerElectricDipoleMomentClasses() {
    subclass(ImperialElectricDipoleMoment::class, ImperialElectricDipoleMoment.serializer())
    subclass(MetricElectricDipoleMoment::class, MetricElectricDipoleMoment.serializer())
}
