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
 * Set of all [MetricCatalyticConcentration]
 */
val MetricCatalyticConcentrationUnits: Set<MetricCatalyticConcentration> get() = CatalysticActivityUnits.flatMap { catalysticActivity ->
    MetricVolumeUnits.map { catalysticActivity per it }
}.toSet()

/**
 * Set of all [ImperialCatalyticConcentration]
 */
val ImperialCatalyticConcentrationUnits: Set<ImperialCatalyticConcentration> get() = CatalysticActivityUnits.flatMap { catalysticActivity ->
    ImperialVolumeUnits.map { catalysticActivity per it }
}.toSet()

/**
 * Set of all [UKImperialCatalyticConcentration]
 */
val UKImperialCatalyticConcentrationUnits: Set<UKImperialCatalyticConcentration> get() = CatalysticActivityUnits.flatMap { catalysticActivity ->
    UKImperialVolumeUnits.map { catalysticActivity per it }
}.toSet()

/**
 * Set of all [USCustomaryCatalyticConcentration]
 */
val USCustomaryCatalyticConcentrationUnits: Set<USCustomaryCatalyticConcentration> get() = CatalysticActivityUnits.flatMap { catalysticActivity ->
    USCustomaryVolumeUnits.map { catalysticActivity per it }
}.toSet()

/**
 * Set of all [CatalyticConcentration]
 */
val CatalyticConcentrationUnits: Set<CatalyticConcentration> get() = MetricCatalyticConcentrationUnits +
    ImperialCatalyticConcentrationUnits +
    UKImperialCatalyticConcentrationUnits.filter { it.per !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryCatalyticConcentrationUnits.filter { it.per !is USCustomaryImperialVolumeWrapper }.toSet()

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.CatalyticConcentration]
 * SI unit is `Katal per CubicMeter`
 */
@Serializable
sealed class CatalyticConcentration : DefinedScientificUnit<PhysicalQuantity.CatalyticConcentration>() {

    /**
     * The [CatalysticActivity] component
     */
    abstract val catalysticActivity: CatalysticActivity

    /**
     * The [Volume] component
     */
    abstract val per: Volume
    override val symbol: String by lazy { "${catalysticActivity.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.CatalyticConcentration
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(catalysticActivity.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = catalysticActivity.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [CatalyticConcentration] for [MeasurementSystem.Metric]
 * @param catalysticActivity the [CatalysticActivity] component
 * @param per the [MetricVolume] component
 */
@Serializable
data class MetricCatalyticConcentration(override val catalysticActivity: CatalysticActivity, override val per: MetricVolume) :
    CatalyticConcentration(),
    MetricScientificUnit<PhysicalQuantity.CatalyticConcentration> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [CatalyticConcentration] for [MeasurementSystem.Imperial]
 * @param catalysticActivity the [CatalysticActivity] component
 * @param per the [ImperialVolume] component
 */
@Serializable
data class ImperialCatalyticConcentration(override val catalysticActivity: CatalysticActivity, override val per: ImperialVolume) :
    CatalyticConcentration(),
    ImperialScientificUnit<PhysicalQuantity.CatalyticConcentration> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialCatalyticConcentration] equivalent to this [ImperialCatalyticConcentration]
     */
    val ukImperial get() = catalysticActivity per per.ukImperial

    /**
     * The [USCustomaryCatalyticConcentration] equivalent to this [ImperialCatalyticConcentration]
     */
    val usCustomary get() = catalysticActivity per per.usCustomary
}

/**
 * A [CatalyticConcentration] for [MeasurementSystem.USCustomary]
 * @param catalysticActivity the [CatalysticActivity] component
 * @param per the [USCustomaryVolume] component
 */
@Serializable
data class USCustomaryCatalyticConcentration(override val catalysticActivity: CatalysticActivity, override val per: USCustomaryVolume) :
    CatalyticConcentration(),
    USCustomaryScientificUnit<PhysicalQuantity.CatalyticConcentration> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [CatalyticConcentration] for [MeasurementSystem.UKImperial]
 * @param catalysticActivity the [CatalysticActivity] component
 * @param per the [UKImperialVolume] component
 */
@Serializable
data class UKImperialCatalyticConcentration(override val catalysticActivity: CatalysticActivity, override val per: UKImperialVolume) :
    CatalyticConcentration(),
    UKImperialScientificUnit<PhysicalQuantity.CatalyticConcentration> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricCatalyticConcentration] from a [CatalysticActivity] and a [MetricVolume]
 * @param volume the [MetricVolume] component
 * @return the [MetricCatalyticConcentration] represented by the units
 */
infix fun CatalysticActivity.per(volume: MetricVolume) = MetricCatalyticConcentration(this, volume)

/**
 * Gets an [ImperialCatalyticConcentration] from a [CatalysticActivity] and an [ImperialVolume]
 * @param volume the [ImperialVolume] component
 * @return the [ImperialCatalyticConcentration] represented by the units
 */
infix fun CatalysticActivity.per(volume: ImperialVolume) = ImperialCatalyticConcentration(this, volume)

/**
 * Gets a [USCustomaryCatalyticConcentration] from a [CatalysticActivity] and a [USCustomaryVolume]
 * @param volume the [USCustomaryVolume] component
 * @return the [USCustomaryCatalyticConcentration] represented by the units
 */
infix fun CatalysticActivity.per(volume: USCustomaryVolume) = USCustomaryCatalyticConcentration(this, volume)

/**
 * Gets a [UKImperialCatalyticConcentration] from a [CatalysticActivity] and a [UKImperialVolume]
 * @param volume the [UKImperialVolume] component
 * @return the [UKImperialCatalyticConcentration] represented by the units
 */
infix fun CatalysticActivity.per(volume: UKImperialVolume) = UKImperialCatalyticConcentration(this, volume)

internal fun SerializersModuleBuilder.setupForCatalyticConcentration() {
    polymorphic(CatalyticConcentration::class) {
        registerCatalyticConcentrationClasses()
    }
}

internal fun PolymorphicModuleBuilder<CatalyticConcentration>.registerCatalyticConcentrationClasses() {
    subclass(ImperialCatalyticConcentration::class, ImperialCatalyticConcentration.serializer())
    subclass(MetricCatalyticConcentration::class, MetricCatalyticConcentration.serializer())
    subclass(UKImperialCatalyticConcentration::class, UKImperialCatalyticConcentration.serializer())
    subclass(USCustomaryCatalyticConcentration::class, USCustomaryCatalyticConcentration.serializer())
}
