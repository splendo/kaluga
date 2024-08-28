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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [MetricVolumetricFlux]
 */
val MetricVolumetricFluxUnits: Set<MetricVolumetricFlux> get() = MetricVolumetricFlowUnits.flatMap { volumetricFlow ->
    MetricAreaUnits.map { volumetricFlow per it }
}.toSet()

/**
 * Set of all [ImperialVolumetricFlux]
 */
val ImperialVolumetricFluxUnits: Set<ImperialVolumetricFlux> get() = ImperialVolumetricFlowUnits.flatMap { volumetricFlow ->
    ImperialAreaUnits.map { volumetricFlow per it }
}.toSet()

/**
 * Set of all [UKImperialVolumetricFlux]
 */
val UKImperialVolumetricFluxUnits: Set<UKImperialVolumetricFlux> get() = UKImperialVolumetricFlowUnits.flatMap { volumetricFlow ->
    ImperialAreaUnits.map { volumetricFlow per it }
}.toSet()

/**
 * Set of all [USCustomaryVolumetricFlux]
 */
val USCustomaryVolumetricFluxUnits: Set<USCustomaryVolumetricFlux> get() = USCustomaryVolumetricFlowUnits.flatMap { volumetricFlow ->
    ImperialAreaUnits.map { volumetricFlow per it }
}.toSet()

/**
 * Set of all [VolumetricFlux]
 */
val VolumetricFluxUnits: Set<VolumetricFlux> get() = MetricVolumetricFluxUnits +
    ImperialVolumetricFluxUnits +
    UKImperialVolumetricFluxUnits.filter { it.volumetricFlow.volume !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryVolumetricFluxUnits.filter { it.volumetricFlow.volume !is USCustomaryImperialVolumeWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.VolumetricFlux]
 * SI unit is `CubicMeter per Second per SquareMeter`
 */
@Serializable
sealed class VolumetricFlux : AbstractScientificUnit<PhysicalQuantity.VolumetricFlux>() {

    /**
     * The [VolumetricFlow] component
     */
    abstract val volumetricFlow: VolumetricFlow

    /**
     * The [Area] component
     */
    abstract val per: Area
    override val quantity = PhysicalQuantity.VolumetricFlux
    override val symbol: String by lazy { "${volumetricFlow.symbol}⋅${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(volumetricFlow.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = volumetricFlow.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [VolumetricFlux] for [MeasurementSystem.Metric]
 * @param volumetricFlow the [MetricVolumetricFlow] component
 * @param per the [MetricArea] component
 */
@Serializable
data class MetricVolumetricFlux(override val volumetricFlow: MetricVolumetricFlow, override val per: MetricArea) :
    VolumetricFlux(),
    MetricScientificUnit<PhysicalQuantity.VolumetricFlux> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [VolumetricFlux] for [MeasurementSystem.Imperial]
 * @param volumetricFlow the [ImperialVolumetricFlow] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class ImperialVolumetricFlux(override val volumetricFlow: ImperialVolumetricFlow, override val per: ImperialArea) :
    VolumetricFlux(),
    ImperialScientificUnit<PhysicalQuantity.VolumetricFlux> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialVolumetricFlux] equivalent to this [ImperialVolumetricFlux]
     */
    val ukImperial get() = volumetricFlow.ukImperial per per

    /**
     * The [USCustomaryVolumetricFlux] equivalent to this [ImperialVolumetricFlux]
     */
    val usCustomary get() = volumetricFlow.usCustomary per per
}

/**
 * A [VolumetricFlux] for [MeasurementSystem.UKImperial]
 * @param volumetricFlow the [UKImperialVolumetricFlow] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class UKImperialVolumetricFlux(override val volumetricFlow: UKImperialVolumetricFlow, override val per: ImperialArea) :
    VolumetricFlux(),
    UKImperialScientificUnit<PhysicalQuantity.VolumetricFlux> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * A [VolumetricFlux] for [MeasurementSystem.USCustomary]
 * @param volumetricFlow the [USCustomaryVolumetricFlow] component
 * @param per the [ImperialArea] component
 */
@Serializable
data class USCustomaryVolumetricFlux(override val volumetricFlow: USCustomaryVolumetricFlow, override val per: ImperialArea) :
    VolumetricFlux(),
    USCustomaryScientificUnit<PhysicalQuantity.VolumetricFlux> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * Gets a [MetricVolumetricFlux] from a [MetricVolumetricFlow] and a [MetricArea]
 * @param area the [MetricArea] component
 * @return the [MetricVolumetricFlux] represented by the units
 */
infix fun MetricVolumetricFlow.per(area: MetricArea) = MetricVolumetricFlux(this, area)

/**
 * Gets an [ImperialVolumetricFlux] from a [MetricVolumetricFlow] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [ImperialVolumetricFlux] represented by the units
 */
infix fun ImperialVolumetricFlow.per(area: ImperialArea) = ImperialVolumetricFlux(this, area)

/**
 * Gets a [UKImperialVolumetricFlux] from a [MetricVolumetricFlow] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [UKImperialVolumetricFlux] represented by the units
 */
infix fun UKImperialVolumetricFlow.per(area: ImperialArea) = UKImperialVolumetricFlux(this, area)

/**
 * Gets a [USCustomaryVolumetricFlux] from a [MetricVolumetricFlow] and an [ImperialArea]
 * @param area the [ImperialArea] component
 * @return the [USCustomaryVolumetricFlux] represented by the units
 */
infix fun USCustomaryVolumetricFlow.per(area: ImperialArea) = USCustomaryVolumetricFlux(this, area)
