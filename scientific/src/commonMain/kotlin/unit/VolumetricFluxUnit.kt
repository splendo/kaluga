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

val MetricVolumetricFluxUnits: Set<MetricVolumetricFlux> get() = MetricVolumetricFlowUnits.flatMap { volumetricFlow ->
    MetricAreaUnits.map { volumetricFlow per it }
}.toSet()

val ImperialVolumetricFluxUnits: Set<ImperialVolumetricFlux> get() = ImperialVolumetricFlowUnits.flatMap { volumetricFlow ->
    ImperialAreaUnits.map { volumetricFlow per it }
}.toSet()

val UKImperialVolumetricFluxUnits: Set<UKImperialVolumetricFlux> get() = UKImperialVolumetricFlowUnits.flatMap { volumetricFlow ->
    ImperialAreaUnits.map { volumetricFlow per it }
}.toSet()

val USCustomaryVolumetricFluxUnits: Set<USCustomaryVolumetricFlux> get() = USCustomaryVolumetricFlowUnits.flatMap { volumetricFlow ->
    ImperialAreaUnits.map { volumetricFlow per it }
}.toSet()

val VolumetricFluxUnits: Set<VolumetricFlux> get() = MetricVolumetricFluxUnits +
    ImperialVolumetricFluxUnits +
    UKImperialVolumetricFluxUnits.filter { it.volumetricFlow.volume !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryVolumetricFluxUnits.filter { it.volumetricFlow.volume !is USCustomaryImperialVolumeWrapper }.toSet()

@Serializable
sealed class VolumetricFlux : AbstractScientificUnit<PhysicalQuantity.VolumetricFlux>() {
    abstract val volumetricFlow: VolumetricFlow
    abstract val per: Area
    override val quantity = PhysicalQuantity.VolumetricFlux
    override val symbol: String by lazy { "${volumetricFlow.symbol}⋅${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(volumetricFlow.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = volumetricFlow.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricVolumetricFlux(override val volumetricFlow: MetricVolumetricFlow, override val per: MetricArea) : VolumetricFlux(), MetricScientificUnit<PhysicalQuantity.VolumetricFlux> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialVolumetricFlux(override val volumetricFlow: ImperialVolumetricFlow, override val per: ImperialArea) : VolumetricFlux(), ImperialScientificUnit<PhysicalQuantity.VolumetricFlux> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = volumetricFlow.ukImperial per per
    val usCustomary get() = volumetricFlow.usCustomary per per
}
@Serializable
data class UKImperialVolumetricFlux(override val volumetricFlow: UKImperialVolumetricFlow, override val per: ImperialArea) : VolumetricFlux(), UKImperialScientificUnit<PhysicalQuantity.VolumetricFlux> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryVolumetricFlux(override val volumetricFlow: USCustomaryVolumetricFlow, override val per: ImperialArea) : VolumetricFlux(), USCustomaryScientificUnit<PhysicalQuantity.VolumetricFlux> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricVolumetricFlow.per(area: MetricArea) = MetricVolumetricFlux(this, area)
infix fun ImperialVolumetricFlow.per(area: ImperialArea) = ImperialVolumetricFlux(this, area)
infix fun UKImperialVolumetricFlow.per(area: ImperialArea) = UKImperialVolumetricFlux(this, area)
infix fun USCustomaryVolumetricFlow.per(area: ImperialArea) = USCustomaryVolumetricFlux(this, area)
