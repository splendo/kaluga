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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlinx.serialization.Serializable

val MetricVolumetricFlowUnits: Set<MetricVolumetricFlow> = MetricVolumeUnits.map { volume ->
    TimeUnits.map { MetricVolumetricFlow(volume, it) }
}.flatten().toSet()

val ImperialVolumetricFlowUnits: Set<ImperialVolumetricFlow> = ImperialVolumeUnits.map { volume ->
    TimeUnits.map { ImperialVolumetricFlow(volume, it) }
}.flatten().toSet()

val UKImperialVolumetricFlowUnits: Set<UKImperialVolumetricFlow> = UKImperialVolumeUnits.map { volume ->
    TimeUnits.map { UKImperialVolumetricFlow(volume, it) }
}.flatten().toSet()

val USCustomaryVolumetricFlowUnits: Set<USCustomaryVolumetricFlow> = USCustomaryVolumeUnits.map { volume ->
    TimeUnits.map { USCustomaryVolumetricFlow(volume, it) }
}.flatten().toSet()

val VolumetricFlowUnits: Set<VolumetricFlow> = MetricVolumetricFlowUnits +
    ImperialVolumetricFlowUnits +
    UKImperialVolumetricFlowUnits.filter { it.volume !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryVolumetricFlowUnits.filter { it.volume !is USCustomaryImperialVolumeWrapper }.toSet()

@Serializable
sealed class VolumetricFlow : AbstractScientificUnit<MeasurementType.VolumetricFlow>() {
    abstract val volume: Volume
    abstract val per: Time
    override val type = MeasurementType.VolumetricFlow
    override val symbol: String by lazy { "${volume.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = volume.fromSIUnit(value) * per.convert(1.0.toDecimal(), Second)
    override fun toSIUnit(value: Decimal): Decimal = volume.toSIUnit(value) / per.convert(1.0.toDecimal(), Second)
}

@Serializable
data class MetricVolumetricFlow(override val volume: MetricVolume, override val per: Time) : VolumetricFlow(), MetricScientificUnit<MeasurementType.VolumetricFlow> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialVolumetricFlow(override val volume: ImperialVolume, override val per: Time) : VolumetricFlow(), ImperialScientificUnit<MeasurementType.VolumetricFlow> {
    override val system = MeasurementSystem.Imperial
}
@Serializable
data class UKImperialVolumetricFlow(override val volume: UKImperialVolume, override val per: Time) : VolumetricFlow(), UKImperialScientificUnit<MeasurementType.VolumetricFlow> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryVolumetricFlow(override val volume: USCustomaryVolume, override val per: Time) : VolumetricFlow(), USCustomaryScientificUnit<MeasurementType.VolumetricFlow> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricVolume.per(time: Time) = MetricVolumetricFlow(this, time)
infix fun ImperialVolume.per(time: Time) = ImperialVolumetricFlow(this, time)
infix fun UKImperialVolume.per(time: Time) = UKImperialVolumetricFlow(this, time)
infix fun USCustomaryVolume.per(time: Time) = USCustomaryVolumetricFlow(this, time)
