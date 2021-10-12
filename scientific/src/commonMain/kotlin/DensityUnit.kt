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
sealed class Density<System : MeasurementSystem, WeightUnit : Weight<System>, VolumeUnit : Volume<System>> : AbstractScientificUnit<System, MeasurementType.Density>() {
    abstract val weight: WeightUnit
    abstract val per: VolumeUnit
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = weight.fromSIUnit(value) * per.convert(1.0.toDecimal(), CubicMeter)
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(value) / per.convert(1.0.toDecimal(), CubicMeter)
}

@Serializable
data class MetricDensity(override val weight: MetricWeight, override val per: MetricVolume) : Density<MeasurementSystem.Metric, MetricWeight, MetricVolume>()
@Serializable
data class ImperialDensity(override val weight: ImperialWeight, override val per: ImperialVolume) : Density<MeasurementSystem.Imperial, ImperialWeight, ImperialVolume>()
@Serializable
data class USImperialDensity(override val weight: USCustomaryWeight, override val per: USCustomaryVolume) : Density<MeasurementSystem.USCustomary, USCustomaryWeight, USCustomaryVolume>()
@Serializable
data class UKImperialDensity(override val weight: UKImperialWeight, override val per: UKImperialVolume) : Density<MeasurementSystem.UKImperial, UKImperialWeight, UKImperialVolume>()

infix fun MetricWeight.per(volume: MetricVolume) = MetricDensity(this, volume)
infix fun ImperialWeight.per(volume: ImperialVolume) = ImperialDensity(this, volume)
infix fun USCustomaryWeight.per(volume: USCustomaryVolume) = USImperialDensity(this, volume)
infix fun UKImperialWeight.per(volume: UKImperialVolume) = UKImperialDensity(this, volume)
infix fun <System : MeasurementSystem> Weight<System>.per(volume: Volume<System>) = when (this) {
    is MetricWeight -> this per (volume as MetricVolume)
    is ImperialWeight -> this per (volume as ImperialVolume)
    is USCustomaryWeight -> this per (volume as USCustomaryVolume)
    is UKImperialWeight -> this per (volume as UKImperialVolume)
}

@JvmName("weightDivVolume")
inline operator fun <
    System : MeasurementSystem,
    WeightUnit : Weight<System>,
    VolumeUnit : Volume<System>,
    reified DensityUnit : Density<System, WeightUnit, VolumeUnit>,
    > ScientificValue<
    System,
    MeasurementType.Weight,
    WeightUnit,
    >.div(volume: ScientificValue<System, MeasurementType.Volume, VolumeUnit>): ScientificValue<System, MeasurementType.Density, DensityUnit> {
    val densityUnit = (unit per volume.unit) as DensityUnit
    return ScientificValue(value / volume.value, densityUnit)
}

@JvmName("densityTimesVolume")
infix operator fun <
    WeightSystem : MeasurementSystem,
    WeightUnit : Weight<WeightSystem>,
    VolumeSystem : MeasurementSystem,
    VolumeUnit : Volume<VolumeSystem>,
    DensitySystem : MeasurementSystem,
    DensityUnit : Density<DensitySystem, WeightUnit, VolumeUnit>
    >  ScientificValue<DensitySystem, MeasurementType.Density, DensityUnit>.times(volume: ScientificValue<VolumeSystem, MeasurementType.Volume, VolumeUnit>) : ScientificValue<WeightSystem, MeasurementType.Weight, WeightUnit> {
    return ScientificValue(value * volume.convertValue(unit.per), unit.weight)
}

@JvmName("weightDivDensity")
infix operator fun <
    WeightSystem : MeasurementSystem,
    WeightUnit : Weight<WeightSystem>,
    VolumeSystem : MeasurementSystem,
    VolumeUnit : Volume<VolumeSystem>,
    DensitySystem : MeasurementSystem,
    DensityUnit : Density<DensitySystem, WeightUnit, VolumeUnit>
    >  ScientificValue<WeightSystem, MeasurementType.Weight, WeightUnit>.div(density: ScientificValue<DensitySystem, MeasurementType.Density, DensityUnit>) : ScientificValue<VolumeSystem, MeasurementType.Volume, VolumeUnit> {
    return ScientificValue(convertValue(density.unit.weight) / density.value, density.unit.per)
}
