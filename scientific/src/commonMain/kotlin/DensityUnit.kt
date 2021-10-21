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
sealed class Density : AbstractScientificUnit<MeasurementType.Density>() {
    abstract val weight: Weight
    abstract val per: Volume
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val type = MeasurementType.Density
    override fun fromSIUnit(value: Decimal): Decimal = weight.fromSIUnit(value) * per.convert(1.0.toDecimal(), CubicMeter)
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(value) / per.convert(1.0.toDecimal(), CubicMeter)
}

@Serializable
data class MetricDensity(override val weight: MetricWeight, override val per: MetricVolume) : Density(), MetricScientificUnit<MeasurementType.Density> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialDensity(override val weight: ImperialWeight, override val per: ImperialVolume) : Density(), ImperialScientificUnit<MeasurementType.Density> {
    override val system = MeasurementSystem.Imperial
}
@Serializable
data class USCustomaryDensity(override val weight: USCustomaryWeight, override val per: USCustomaryVolume) : Density(), USCustomaryScientificUnit<MeasurementType.Density> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialDensity(override val weight: UKImperialWeight, override val per: UKImperialVolume) : Density(), UKImperialScientificUnit<MeasurementType.Density> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(volume: MetricVolume) = MetricDensity(this, volume)
infix fun ImperialWeight.per(volume: ImperialVolume) = ImperialDensity(this, volume)
infix fun USCustomaryWeight.per(volume: USCustomaryVolume) = USCustomaryDensity(this, volume)
infix fun UKImperialWeight.per(volume: UKImperialVolume) = UKImperialDensity(this, volume)
infix fun <
    System : MeasurementSystem,
    WeightUnit,
    VolumeUnit
    > WeightUnit.per(volume: VolumeUnit): Density where WeightUnit : Weight, WeightUnit : SystemScientificUnit<System, MeasurementType.Weight>, VolumeUnit : Volume, VolumeUnit : SystemScientificUnit<System, MeasurementType.Volume> = when (this) {
    is MetricWeight -> this per (volume as MetricVolume)
    is ImperialWeight -> this per (volume as ImperialVolume)
    is USCustomaryWeight -> this per (volume as USCustomaryVolume)
    is UKImperialWeight -> this per (volume as UKImperialVolume)
    else -> error("Unknown Weight")
}

@JvmName("metricWeightDivMetricVolume")
infix operator fun <
    WeightUnit : MetricWeight,
    VolumeUnit : MetricVolume,
    > ScientificValue<
    MeasurementType.Weight,
    WeightUnit,
    >.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>): ScientificValue<MeasurementType.Density, MetricDensity> = (value / volume.value)(unit per volume.unit)

@JvmName("imperialWeightDivImperialVolume")
infix operator fun <
    WeightUnit : ImperialWeight,
    VolumeUnit : ImperialVolume,
    > ScientificValue<
    MeasurementType.Weight,
    WeightUnit,
    >.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>): ScientificValue<MeasurementType.Density, ImperialDensity> = (value / volume.value)(unit per volume.unit)

@JvmName("ukImperialWeightDivUKImperialVolume")
infix operator fun <
    WeightUnit : UKImperialWeight,
    VolumeUnit : UKImperialVolume,
    > ScientificValue<
    MeasurementType.Weight,
    WeightUnit,
    >.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>): ScientificValue<MeasurementType.Density, UKImperialDensity> = (value / volume.value)(unit per volume.unit)

@JvmName("usCustomaryWeightDivUSCustomaryVolume")
infix operator fun <
    WeightUnit : USCustomaryWeight,
    VolumeUnit : USCustomaryVolume,
    > ScientificValue<
    MeasurementType.Weight,
    WeightUnit,
    >.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>): ScientificValue<MeasurementType.Density, USCustomaryDensity> = (value / volume.value)(unit per volume.unit)

@JvmName("weightDivVolume")
infix operator fun <
    System : MeasurementSystem,
    WeightUnit,
    VolumeUnit,
    > ScientificValue<
    MeasurementType.Weight,
    WeightUnit,
    >.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>): ScientificValue<MeasurementType.Density, Density> where
    WeightUnit : Weight,
    WeightUnit : SystemScientificUnit<System, MeasurementType.Weight>,
    VolumeUnit : Volume, VolumeUnit : SystemScientificUnit<System, MeasurementType.Volume>
    = (value / volume.value)(unit per volume.unit)

@JvmName("metricDensityTimesMetricVolume")
infix operator fun <
    VolumeUnit : MetricVolume,
    >  ScientificValue<MeasurementType.Density, MetricDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) : ScientificValue< MeasurementType.Weight, MetricWeight> = (value * volume.convertValue(unit.per))(unit.weight)

@JvmName("imperialDensityTimesImperialVolume")
infix operator fun <
    VolumeUnit : ImperialVolume,
    >  ScientificValue<MeasurementType.Density, ImperialDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) : ScientificValue< MeasurementType.Weight, ImperialWeight> = (value * volume.convertValue(unit.per))(unit.weight)

@JvmName("ukImperialDensityTimesUKImperialVolume")
infix operator fun <
    VolumeUnit : UKImperialVolume,
    >  ScientificValue<MeasurementType.Density, UKImperialDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) : ScientificValue< MeasurementType.Weight, UKImperialWeight> = (value * volume.convertValue(unit.per))(unit.weight)

@JvmName("usCustomaryDensityTimesUSCustomaryVolume")
infix operator fun <
    VolumeUnit : USCustomaryVolume,
    >  ScientificValue<MeasurementType.Density, USCustomaryDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) : ScientificValue< MeasurementType.Weight, USCustomaryWeight> = (value * volume.convertValue(unit.per))(unit.weight)

@JvmName("densityTimesVolume")
infix operator fun <
    VolumeUnit : Volume,
    >  ScientificValue<MeasurementType.Density, Density>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) : ScientificValue< MeasurementType.Weight, Weight> = (value * volume.convertValue(unit.per))(unit.weight)

@JvmName("metricVolumeTimesMetricDensity")
infix operator fun <
    VolumeUnit : MetricVolume,
    >  ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, MetricDensity>) : ScientificValue< MeasurementType.Weight, MetricWeight> = (density.value * convertValue(density.unit.per))(density.unit.weight)

@JvmName("imperialVolumeTimesImperialDensity")
infix operator fun <
    VolumeUnit : ImperialVolume,
    >  ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, ImperialDensity>) : ScientificValue< MeasurementType.Weight, ImperialWeight> = (density.value * convertValue(density.unit.per))(density.unit.weight)

@JvmName("ukImperialVolumeTimesUKImperialDensity")
infix operator fun <
    VolumeUnit : UKImperialVolume,
    >  ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) : ScientificValue< MeasurementType.Weight, UKImperialWeight> = (density.value * convertValue(density.unit.per))(density.unit.weight)

@JvmName("usCustomaryVolumeTimesUSCustomaryDensity")
infix operator fun <
    VolumeUnit : USCustomaryVolume,
    >  ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) : ScientificValue< MeasurementType.Weight, USCustomaryWeight> = (density.value * convertValue(density.unit.per))(density.unit.weight)

@JvmName("volumeTimesDensity")
infix operator fun <
    VolumeUnit : Volume,
    >  ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, Density>) : ScientificValue< MeasurementType.Weight, Weight> = (density.value * convertValue(density.unit.per))(density.unit.weight)

@JvmName("metricWeightDivMetricDensity")
infix operator fun <
    WeightUnit : MetricWeight,
    > ScientificValue<MeasurementType.Weight, WeightUnit>.div(density: ScientificValue<MeasurementType.Density, MetricDensity>) : ScientificValue<MeasurementType.Volume, MetricVolume> = (convertValue(density.unit.weight) / density.value)(density.unit.per)

@JvmName("imperialWeightDivImperialDensity")
infix operator fun <
    WeightUnit : ImperialWeight,
    > ScientificValue<MeasurementType.Weight, WeightUnit>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) : ScientificValue<MeasurementType.Volume, ImperialVolume> = (convertValue(density.unit.weight) / density.value)(density.unit.per)

@JvmName("ukImperialWeightDivUKImperialDensity")
infix operator fun <
    WeightUnit : UKImperialWeight,
    > ScientificValue<MeasurementType.Weight, WeightUnit>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) : ScientificValue<MeasurementType.Volume, UKImperialVolume> = (convertValue(density.unit.weight) / density.value)(density.unit.per)

@JvmName("usCustomaryWeightDivUSCustomaryDensity")
infix operator fun <
    WeightUnit : USCustomaryWeight,
    > ScientificValue<MeasurementType.Weight, WeightUnit>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) : ScientificValue<MeasurementType.Volume, USCustomaryVolume> = (convertValue(density.unit.weight) / density.value)(density.unit.per)

@JvmName("weightDivDensity")
infix operator fun <
    WeightUnit : Weight
    > ScientificValue<MeasurementType.Weight, WeightUnit>.div(density: ScientificValue<MeasurementType.Density, Density>) : ScientificValue<MeasurementType.Volume, Volume> = (convertValue(density.unit.weight) / density.value)(density.unit.per)
