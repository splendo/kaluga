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

val MetricMolarVolumeUnits: Set<MetricMolarVolume> get() = MetricVolumeUnits.flatMap { volume ->
    AmountOfSubstanceUnits.map { volume per it }
}.toSet()

val ImperialMolarVolumeUnits: Set<ImperialMolarVolume> get() = ImperialVolumeUnits.flatMap { volume ->
    AmountOfSubstanceUnits.map { volume per it }
}.toSet()

val UKImperialMolarVolumeUnits: Set<UKImperialMolarVolume> get() = UKImperialVolumeUnits.flatMap { volume ->
    AmountOfSubstanceUnits.map { volume per it }
}.toSet()

val USCustomaryMolarVolumeUnits: Set<USCustomaryMolarVolume> get() = USCustomaryVolumeUnits.flatMap { volume ->
    AmountOfSubstanceUnits.map { volume per it }
}.toSet()

val MolarVolumeUnits: Set<MolarVolume> get() = MetricMolarVolumeUnits +
    ImperialMolarVolumeUnits +
    UKImperialMolarVolumeUnits.filter { it.volume !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryMolarVolumeUnits.filter { it.volume !is USCustomaryImperialVolumeWrapper }.toSet()

@Serializable
sealed class MolarVolume : AbstractScientificUnit<PhysicalQuantity.MolarVolume>() {
    abstract val volume: Volume
    abstract val per: AmountOfSubstance
    override val symbol: String by lazy { "${volume.symbol} / ${per.symbol}" }
    override val quantity = PhysicalQuantity.MolarVolume
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(volume.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = volume.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricMolarVolume(override val volume: MetricVolume, override val per: AmountOfSubstance) : MolarVolume(), MetricScientificUnit<PhysicalQuantity.MolarVolume> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialMolarVolume(override val volume: ImperialVolume, override val per: AmountOfSubstance) : MolarVolume(), ImperialScientificUnit<PhysicalQuantity.MolarVolume> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = volume.ukImperial per per
    val usCustomary get() = volume.usCustomary per per
}
@Serializable
data class USCustomaryMolarVolume(override val volume: USCustomaryVolume, override val per: AmountOfSubstance) : MolarVolume(), USCustomaryScientificUnit<PhysicalQuantity.MolarVolume> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialMolarVolume(override val volume: UKImperialVolume, override val per: AmountOfSubstance) : MolarVolume(), UKImperialScientificUnit<PhysicalQuantity.MolarVolume> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricVolume.per(amountOfSubstance: AmountOfSubstance) = MetricMolarVolume(this, amountOfSubstance)
infix fun ImperialVolume.per(amountOfSubstance: AmountOfSubstance) = ImperialMolarVolume(this, amountOfSubstance)
infix fun USCustomaryVolume.per(amountOfSubstance: AmountOfSubstance) = USCustomaryMolarVolume(this, amountOfSubstance)
infix fun UKImperialVolume.per(amountOfSubstance: AmountOfSubstance) = UKImperialMolarVolume(this, amountOfSubstance)
