/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

val MetricVolumetricFlowUnits: Set<MetricVolumetricFlow> get() = MetricVolumeUnits.flatMap { volume ->
    TimeUnits.map { volume per it }
}.toSet()

val ImperialVolumetricFlowUnits: Set<ImperialVolumetricFlow> get() = ImperialVolumeUnits.flatMap { volume ->
    TimeUnits.map { volume per it }
}.toSet()

val UKImperialVolumetricFlowUnits: Set<UKImperialVolumetricFlow> get() = UKImperialVolumeUnits.flatMap { volume ->
    TimeUnits.map { volume per it }
}.toSet()

val USCustomaryVolumetricFlowUnits: Set<USCustomaryVolumetricFlow> get() = USCustomaryVolumeUnits.flatMap { volume ->
    TimeUnits.map { volume per it }
}.toSet()

val VolumetricFlowUnits: Set<VolumetricFlow> get() = MetricVolumetricFlowUnits +
    ImperialVolumetricFlowUnits +
    UKImperialVolumetricFlowUnits.filter { it.volume !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryVolumetricFlowUnits.filter { it.volume !is USCustomaryImperialVolumeWrapper }.toSet()

@Serializable
sealed class VolumetricFlow : AbstractScientificUnit<PhysicalQuantity.VolumetricFlow>() {
    abstract val volume: Volume
    abstract val per: Time
    override val type = PhysicalQuantity.VolumetricFlow
    override val symbol: String by lazy { "${volume.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(volume.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = volume.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricVolumetricFlow(override val volume: MetricVolume, override val per: Time) : VolumetricFlow(), MetricScientificUnit<PhysicalQuantity.VolumetricFlow> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialVolumetricFlow(override val volume: ImperialVolume, override val per: Time) : VolumetricFlow(), ImperialScientificUnit<PhysicalQuantity.VolumetricFlow> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = UKImperialVolumetricFlow(volume.ukImperial, per)
    val usCustomary get() = USCustomaryVolumetricFlow(volume.usCustomary, per)
}
@Serializable
data class UKImperialVolumetricFlow(override val volume: UKImperialVolume, override val per: Time) : VolumetricFlow(), UKImperialScientificUnit<PhysicalQuantity.VolumetricFlow> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryVolumetricFlow(override val volume: USCustomaryVolume, override val per: Time) : VolumetricFlow(), USCustomaryScientificUnit<PhysicalQuantity.VolumetricFlow> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricVolume.per(time: Time) = MetricVolumetricFlow(this, time)
infix fun ImperialVolume.per(time: Time) = ImperialVolumetricFlow(this, time)
infix fun UKImperialVolume.per(time: Time) = UKImperialVolumetricFlow(this, time)
infix fun USCustomaryVolume.per(time: Time) = USCustomaryVolumetricFlow(this, time)
