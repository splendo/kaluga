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
import com.splendo.kaluga.scientific.MeasurementType
import kotlinx.serialization.Serializable

val MetricSpecificVolumeUnits: Set<MetricSpecificVolume> get() = MetricVolumeUnits.flatMap { volume ->
    MetricWeightUnits.map { volume per it }
}.toSet()

val ImperialSpecificVolumeUnits: Set<ImperialSpecificVolume> get() = ImperialVolumeUnits.flatMap { volume ->
    ImperialWeightUnits.map { volume per it }
}.toSet()

val UKImperialSpecificVolumeUnits: Set<UKImperialSpecificVolume> get() = UKImperialVolumeUnits.flatMap { volume ->
    UKImperialWeightUnits.map { volume per it }
}.toSet()

val USCustomarySpecificVolumeUnits: Set<USCustomarySpecificVolume> get() = USCustomaryVolumeUnits.flatMap { volume ->
    USCustomaryWeightUnits.map { volume per it }
}.toSet()

val SpecificVolumeUnits: Set<SpecificVolume> get() = MetricSpecificVolumeUnits +
    ImperialSpecificVolumeUnits +
    UKImperialSpecificVolumeUnits.filter { it.volume !is UKImperialImperialVolumeWrapper || it.per !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomarySpecificVolumeUnits.filter { it.volume !is USCustomaryImperialVolumeWrapper || it.per !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class SpecificVolume : AbstractScientificUnit<MeasurementType.SpecificVolume>() {
    abstract val volume: Volume
    abstract val per: Weight
    override val symbol: String by lazy { "${volume.symbol} / ${per.symbol}" }
    override val type = MeasurementType.SpecificVolume
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(volume.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = volume.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricSpecificVolume(override val volume: MetricVolume, override val per: MetricWeight) : SpecificVolume(), MetricScientificUnit<MeasurementType.SpecificVolume> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialSpecificVolume(override val volume: ImperialVolume, override val per: ImperialWeight) : SpecificVolume(), ImperialScientificUnit<MeasurementType.SpecificVolume> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = volume.ukImperial per per.ukImperial
    val usCustomary get() = volume.usCustomary per per.usCustomary
}
@Serializable
data class USCustomarySpecificVolume(override val volume: USCustomaryVolume, override val per: USCustomaryWeight) : SpecificVolume(), USCustomaryScientificUnit<MeasurementType.SpecificVolume> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialSpecificVolume(override val volume: UKImperialVolume, override val per: UKImperialWeight) : SpecificVolume(), UKImperialScientificUnit<MeasurementType.SpecificVolume> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricVolume.per(weight: MetricWeight) = MetricSpecificVolume(this, weight)
infix fun ImperialVolume.per(weight: ImperialWeight) = ImperialSpecificVolume(this, weight)
infix fun ImperialVolume.per(weight: UKImperialWeight) = UKImperialSpecificVolume(this.ukImperial, weight)
infix fun ImperialVolume.per(weight: USCustomaryWeight) = USCustomarySpecificVolume(this.usCustomary, weight)
infix fun USCustomaryVolume.per(weight: USCustomaryWeight) = USCustomarySpecificVolume(this, weight)
infix fun USCustomaryVolume.per(weight: ImperialWeight) = USCustomarySpecificVolume(this, weight.usCustomary)
infix fun UKImperialVolume.per(weight: ImperialWeight) = UKImperialSpecificVolume(this, weight.ukImperial)
infix fun UKImperialVolume.per(weight: UKImperialWeight) = UKImperialSpecificVolume(this, weight)
