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

val MetricDensityUnits: Set<MetricDensity> get() = MetricWeightUnits.flatMap { weight ->
    MetricVolumeUnits.map { weight per it }
}.toSet()

val ImperialDensityUnits: Set<ImperialDensity> get() = ImperialWeightUnits.flatMap { weight ->
    ImperialVolumeUnits.map { weight per it }
}.toSet()

val UKImperialDensityUnits: Set<UKImperialDensity> get() = UKImperialWeightUnits.flatMap { weight ->
    UKImperialVolumeUnits.map { weight per it }
}.toSet()

val USCustomaryDensityUnits: Set<USCustomaryDensity> get() = USCustomaryWeightUnits.flatMap { weight ->
    USCustomaryVolumeUnits.map { weight per it }
}.toSet()

val DensityUnits: Set<Density> get() = MetricDensityUnits +
    ImperialDensityUnits +
    UKImperialDensityUnits.filter { it.weight !is UKImperialImperialWeightWrapper || it.per !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryDensityUnits.filter { it.weight !is USCustomaryImperialWeightWrapper || it.per !is USCustomaryImperialVolumeWrapper }.toSet()

@Serializable
sealed class Density : AbstractScientificUnit<PhysicalQuantity.Density>() {
    abstract val weight: Weight
    abstract val per: Volume
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val type = PhysicalQuantity.Density
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricDensity(override val weight: MetricWeight, override val per: MetricVolume) : Density(), MetricScientificUnit<PhysicalQuantity.Density> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialDensity(override val weight: ImperialWeight, override val per: ImperialVolume) : Density(), ImperialScientificUnit<PhysicalQuantity.Density> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per.ukImperial
    val usCustomary get() = weight.usCustomary per per.usCustomary
}
@Serializable
data class USCustomaryDensity(override val weight: USCustomaryWeight, override val per: USCustomaryVolume) : Density(), USCustomaryScientificUnit<PhysicalQuantity.Density> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialDensity(override val weight: UKImperialWeight, override val per: UKImperialVolume) : Density(), UKImperialScientificUnit<PhysicalQuantity.Density> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(volume: MetricVolume) = MetricDensity(this, volume)
infix fun ImperialWeight.per(volume: ImperialVolume) = ImperialDensity(this, volume)
infix fun ImperialWeight.per(volume: UKImperialVolume) = UKImperialDensity(this.ukImperial, volume)
infix fun ImperialWeight.per(volume: USCustomaryVolume) = USCustomaryDensity(this.usCustomary, volume)
infix fun USCustomaryWeight.per(volume: USCustomaryVolume) = USCustomaryDensity(this, volume)
infix fun USCustomaryWeight.per(volume: ImperialVolume) = USCustomaryDensity(this, volume.usCustomary)
infix fun UKImperialWeight.per(volume: ImperialVolume) = UKImperialDensity(this, volume.ukImperial)
infix fun UKImperialWeight.per(volume: UKImperialVolume) = UKImperialDensity(this, volume)
