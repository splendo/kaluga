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
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.volume.div
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricAreaDensityUnits: Set<MetricAreaDensity> = MetricWeightUnits.flatMap { weight ->
    MetricAreaUnits.map { weight per it }
}.toSet()

val ImperialAreaDensityUnits: Set<ImperialAreaDensity> = ImperialWeightUnits.flatMap { weight ->
    ImperialAreaUnits.map { weight per it }
}.toSet()

val UKImperialAreaDensityUnits: Set<UKImperialAreaDensity> = UKImperialWeightUnits.flatMap { weight ->
    ImperialAreaUnits.map { weight per it }
}.toSet()

val USCustomaryAreaDensityUnits: Set<USCustomaryAreaDensity> = USCustomaryWeightUnits.flatMap { weight ->
    ImperialAreaUnits.map { weight per it }
}.toSet()

val AreaDensityUnits: Set<AreaDensity> = MetricAreaDensityUnits +
    ImperialAreaDensityUnits +
    UKImperialAreaDensityUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryAreaDensityUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class AreaDensity : AbstractScientificUnit<MeasurementType.AreaDensity>() {
    abstract val weight: Weight
    abstract val per: Area
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val type = MeasurementType.AreaDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricAreaDensity(override val weight: MetricWeight, override val per: MetricArea) : AreaDensity(), MetricScientificUnit<MeasurementType.AreaDensity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialAreaDensity(override val weight: ImperialWeight, override val per: ImperialArea) : AreaDensity(), ImperialScientificUnit<MeasurementType.AreaDensity> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per
    val usCustomary get() = weight.usCustomary per per
}
@Serializable
data class USCustomaryAreaDensity(override val weight: USCustomaryWeight, override val per: ImperialArea) : AreaDensity(), USCustomaryScientificUnit<MeasurementType.AreaDensity> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialAreaDensity(override val weight: UKImperialWeight, override val per: ImperialArea) : AreaDensity(), UKImperialScientificUnit<MeasurementType.AreaDensity> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(area: MetricArea) = MetricAreaDensity(this, area)
infix fun ImperialWeight.per(area: ImperialArea) = ImperialAreaDensity(this, area)
infix fun USCustomaryWeight.per(area: ImperialArea) = USCustomaryAreaDensity(this, area)
infix fun UKImperialWeight.per(area: ImperialArea) = UKImperialAreaDensity(this, area)
