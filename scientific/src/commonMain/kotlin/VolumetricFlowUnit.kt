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
import kotlin.jvm.JvmName

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
data class ImperialVolumetricFlow(override val volume: ImperialVolume, override val per: Time) : VolumetricFlow(), CommonImperialScientificUnit<MeasurementType.VolumetricFlow> {
    override val system = MeasurementSystem.CommonImperial
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
infix fun Volume.per(time: Time) = when (this) {
    is MetricVolume -> this per time
    is ImperialVolume -> this per time
    is UKImperialVolume -> this per time
    is USCustomaryVolume -> this per time
}

@JvmName("metricVolumeDivTime")
infix operator fun <
    VolumeUnit : MetricVolume,
    > ScientificValue<
    MeasurementType.Volume,
    VolumeUnit,
    >.div(time: ScientificValue<MeasurementType.Time, Time>): ScientificValue<MeasurementType.VolumetricFlow, MetricVolumetricFlow> = (value / time.value)(unit per time.unit)

@JvmName("commonImperialVolumeDivTime")
infix operator fun <
    VolumeUnit : ImperialVolume,
    > ScientificValue<
    MeasurementType.Volume,
    VolumeUnit,
    >.div(time: ScientificValue<MeasurementType.Time, Time>): ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow> = (value / time.value)(unit per time.unit)

@JvmName("ukImperialVolumeDivTime")
infix operator fun <
    VolumeUnit : UKImperialVolume,
    > ScientificValue<
    MeasurementType.Volume,
    VolumeUnit,
    >.div(time: ScientificValue<MeasurementType.Time, Time>): ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow> = (value / time.value)(unit per time.unit)

@JvmName("usCustomaryVolumeDivTime")
infix operator fun <
    VolumeUnit : USCustomaryVolume,
    > ScientificValue<
    MeasurementType.Volume,
    VolumeUnit,
    >.div(time: ScientificValue<MeasurementType.Time, Time>): ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow> = (value / time.value)(unit per time.unit)

@JvmName("metricVolumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, MetricVolumetricFlow>.times(time: ScientificValue<MeasurementType.Time, Time>) : ScientificValue<MeasurementType.Volume, MetricVolume> = (value * time.convertValue(unit.per))(unit.volume)
@JvmName("timeTimesMetricVolumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.Time, Time>.times(volumetricFlow: ScientificValue<MeasurementType.VolumetricFlow, MetricVolumetricFlow>) : ScientificValue<MeasurementType.Volume, MetricVolume> = volumetricFlow * this
@JvmName("commonImperialVolumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>.times(time: ScientificValue<MeasurementType.Time, Time>) : ScientificValue<MeasurementType.Volume, ImperialVolume> = (value * time.convertValue(unit.per))(unit.volume)
@JvmName("timeTimesCommonImperialVolumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.Time, Time>.times(volumetricFlow: ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>) : ScientificValue<MeasurementType.Volume, ImperialVolume> = volumetricFlow * this
@JvmName("ukImperialVolumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow>.times(time: ScientificValue<MeasurementType.Time, Time>) : ScientificValue<MeasurementType.Volume, UKImperialVolume> = (value * time.convertValue(unit.per))(unit.volume)
@JvmName("timeTimesUKImperialVolumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.Time, Time>.times(volumetricFlow: ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow>) : ScientificValue<MeasurementType.Volume, UKImperialVolume> = volumetricFlow * this
@JvmName("usCustomaryVolumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow>.times(time: ScientificValue<MeasurementType.Time, Time>) : ScientificValue<MeasurementType.Volume, USCustomaryVolume> = (value * time.convertValue(unit.per))(unit.volume)
@JvmName("timeTimesUSCustomaryVolumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.Time, Time>.times(volumetricFlow: ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow>) : ScientificValue<MeasurementType.Volume, USCustomaryVolume> = volumetricFlow * this
@JvmName("volumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlow>.times(time: ScientificValue<MeasurementType.Time, Time>) : ScientificValue<MeasurementType.Volume, Volume> = (value * time.convertValue(unit.per))(unit.volume)
@JvmName("timeTimesVolumetricFlowTimesTime")
infix operator fun ScientificValue<MeasurementType.Time, Time>.times(volumetricFlow: ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlow>) : ScientificValue<MeasurementType.Volume, Volume> = volumetricFlow * this

@JvmName("volumeDivVolumetricFlow")
infix operator fun <
    VolumeUnit : Volume,
    VolumetricFlowUnit : VolumetricFlow
    >  ScientificValue<MeasurementType.Volume, VolumeUnit>.div(volumetricFlow: ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlowUnit>) : ScientificValue<MeasurementType.Time, Time> {
    return ScientificValue(convertValue(volumetricFlow.unit.volume) / volumetricFlow.value, volumetricFlow.unit.per)
}
