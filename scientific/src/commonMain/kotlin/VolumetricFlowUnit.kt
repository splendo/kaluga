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
sealed class VolumetricFlow<System : MeasurementSystem, VolumeUnit : Volume<System>> : AbstractScientificUnit<System, MeasurementType.VolumetricFlow>() {
    abstract val volume: VolumeUnit
    abstract val per: Time
    override val symbol: String by lazy { "${volume.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = volume.fromSIUnit(value) * per.convert(1.0.toDecimal(), Second)
    override fun toSIUnit(value: Decimal): Decimal = volume.toSIUnit(value) / per.convert(1.0.toDecimal(), Second)
}

@Serializable
data class MetricVolumetricFlow(override val volume: MetricVolume, override val per: Time) : VolumetricFlow<MeasurementSystem.Metric, MetricVolume>()
@Serializable
data class ImperialVolumetricFlow(override val volume: ImperialVolume, override val per: Time) : VolumetricFlow<MeasurementSystem.Imperial, ImperialVolume>()
@Serializable
data class UKImperialVolumetricFlow(override val volume: UKImperialVolume, override val per: Time) : VolumetricFlow<MeasurementSystem.UKImperial, UKImperialVolume>()
@Serializable
data class USCustomaryVolumetricFlow(override val volume: USCustomaryVolume, override val per: Time) : VolumetricFlow<MeasurementSystem.USCustomary, USCustomaryVolume>()


infix fun MetricVolume.per(time: Time) = MetricVolumetricFlow(this, time)
infix fun ImperialVolume.per(time: Time) = ImperialVolumetricFlow(this, time)
infix fun UKImperialVolume.per(time: Time) = UKImperialVolumetricFlow(this, time)
infix fun USCustomaryVolume.per(time: Time) = USCustomaryVolumetricFlow(this, time)
infix fun Volume<*>.per(time: Time) = when (this) {
    is MetricVolume -> this per time
    is ImperialVolume -> this per time
    is UKImperialVolume -> this per time
    is USCustomaryVolume -> this per time
}

@JvmName("volumeDivTime")
inline operator fun <
    System : MeasurementSystem,
    VolumeUnit : Volume<System>,
    reified VolumetricFlowUnit : VolumetricFlow<System, VolumeUnit>,
    > ScientificValue<
    System,
    MeasurementType.Volume,
    VolumeUnit,
    >.div(time: ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Time>): ScientificValue<System, MeasurementType.VolumetricFlow, VolumetricFlowUnit> {
    val volumetricFlowUnit = (unit per time.unit) as VolumetricFlowUnit
    return ScientificValue(value / time.value, volumetricFlowUnit)
}

@JvmName("volumetricFlowTimesTime")
infix operator fun <
    System : MeasurementSystem,
    VolumeUnit : Volume<System>,
    VolumetricFlowUnit : VolumetricFlow<System, VolumeUnit>
    >  ScientificValue<System, MeasurementType.VolumetricFlow, VolumetricFlowUnit>.times(time: ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Time>) : ScientificValue<System, MeasurementType.Volume, VolumeUnit> {
    return ScientificValue(value * time.convertValue(unit.per), unit.volume)
}

@JvmName("volumeDivVolumetricFlow")
infix operator fun <
    VolumeSystem : MeasurementSystem,
    VolumeUnit : Volume<VolumeSystem>,
    VolumetricFlowSystem : MeasurementSystem,
    VolumetricFlowVolumeUnit : Volume<VolumetricFlowSystem>,
    VolumetricFlowUnit : VolumetricFlow<VolumetricFlowSystem, VolumetricFlowVolumeUnit>
    >  ScientificValue<VolumeSystem, MeasurementType.Volume, VolumeUnit>.div(volumetricFlow: ScientificValue<VolumetricFlowSystem, MeasurementType.VolumetricFlow, VolumetricFlowUnit>) : ScientificValue<MeasurementSystem.Global, MeasurementType.Time, Time> {
    return ScientificValue(convertValue(volumetricFlow.unit.volume) / volumetricFlow.value, volumetricFlow.unit.per)
}
