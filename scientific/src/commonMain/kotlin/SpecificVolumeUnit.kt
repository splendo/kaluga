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

val MetricSpecificVolumeUnits: Set<MetricSpecificVolume> = MetricVolumeUnits.flatMap { volume ->
    MetricWeightUnits.map { volume per it }
}.toSet()

val ImperialSpecificVolumeUnits: Set<ImperialSpecificVolume> = ImperialVolumeUnits.flatMap { volume ->
    ImperialWeightUnits.map { volume per it }
}.toSet()

val UKImperialSpecificVolumeUnits: Set<UKImperialSpecificVolume> = UKImperialVolumeUnits.flatMap { volume ->
    UKImperialWeightUnits.map { volume per it }
}.toSet()

val USCustomarySpecificVolumeUnits: Set<USCustomarySpecificVolume> = USCustomaryVolumeUnits.flatMap { volume ->
    USCustomaryWeightUnits.map { volume per it }
}.toSet()

val SpecificVolumeUnits: Set<SpecificVolume> = MetricSpecificVolumeUnits +
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

fun <
    SpecificVolumeUnit : SpecificVolume,
    DensityUnit : Density
    > DensityUnit.density(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>
) = byInverting(specificVolume)

fun <
    SpecificVolumeUnit : SpecificVolume,
    DensityUnit : Density
    > SpecificVolumeUnit.specificVolume(
    density: ScientificValue<MeasurementType.Density, DensityUnit>
) = byInverting(density)

@JvmName("metricSpecificVolumeDensity")
fun ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.density() = (unit.per per unit.volume).density(this)
@JvmName("imperialSpecificVolumeDensity")
fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.density() = (unit.per per unit.volume).density(this)
@JvmName("ukImperialSpecificVolumeDensity")
fun ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.density() = (unit.per per unit.volume).density(this)
@JvmName("usCustomarySpecificVolumeDensity")
fun ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.density() = (unit.per per unit.volume).density(this)
@JvmName("specificVolumeDensity")
fun <SpecificVolumeUnit : SpecificVolume> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.density() = (Kilogram per CubicMeter).density(this)

@JvmName("metricDensitySpecificVolume")
fun ScientificValue<MeasurementType.Density, MetricDensity>.specificVolume() = (unit.per per unit.weight).specificVolume(this)
@JvmName("imperialDensitySpecificVolume")
fun ScientificValue<MeasurementType.Density, ImperialDensity>.specificVolume() = (unit.per per unit.weight).specificVolume(this)
@JvmName("ukImperialDensitySpecificVolume")
fun ScientificValue<MeasurementType.Density, UKImperialDensity>.specificVolume() = (unit.per per unit.weight).specificVolume(this)
@JvmName("usCustomaryDensitySpecificVolume")
fun ScientificValue<MeasurementType.Density, USCustomaryDensity>.specificVolume() = (unit.per per unit.weight).specificVolume(this)
@JvmName("densitySpecificVolume")
fun <DensityUnit : Density> ScientificValue<MeasurementType.Density, DensityUnit>.specificVolume() = (CubicMeter per Kilogram).specificVolume(this)

@JvmName("specificVolumeFromVolumeAndWeight")
fun <
    WeightUnit : Weight,
    VolumeUnit : Volume,
    SpecificVolumeUnit : SpecificVolume
    > SpecificVolumeUnit.specificVolume(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = byDividing(volume, weight)

@JvmName("metricVolumeDivMetricWeight")
infix operator fun <VolumeUnit : MetricVolume, WeightUnit : MetricWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("imperialVolumeDivImperialWeight")
infix operator fun <VolumeUnit : ImperialVolume, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("imperialVolumeDivUKImperialWeight")
infix operator fun <VolumeUnit : ImperialVolume, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("imperialVolumeDivUSCustomaryWeight")
infix operator fun <VolumeUnit : ImperialVolume, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("ukImperialVolumeDivImperialWeight")
infix operator fun <VolumeUnit : UKImperialVolume, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("ukImperialVolumeDivUKImperialWeight")
infix operator fun <VolumeUnit : UKImperialVolume, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("usCustomaryVolumeDivImperialWeight")
infix operator fun <VolumeUnit : USCustomaryVolume, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("usCustomaryVolumeDivUSCustomaryWeight")
infix operator fun <VolumeUnit : USCustomaryVolume, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("volumeDivWeight")
infix operator fun <VolumeUnit : Volume, WeightUnit : Weight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (CubicMeter per Kilogram).specificVolume(this, weight)

@JvmName("weightFromVolumeAndSpecificVolume")
fun <
    VolumeUnit : Volume,
    WeightUnit : Weight,
    SpecificVolumeUnit : SpecificVolume
    > WeightUnit.weight(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>
) = byDividing(volume, specificVolume)

@JvmName("metricVolumeDivMetricSpecificVolume")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("imperialVolumeDivImperialSpecificVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("ukImperialVolumeDivImperialSpecificVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("ukImperialVolumeDivUKImperialSpecificVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("usCustomaryVolumeDivImperialSpecificVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("usCustomaryVolumeDivUSCustomarySpecificVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("volumeDivSpecificVolume")
infix operator fun <VolumeUnit : Volume, SpecificVolumeUnit : SpecificVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = Kilogram.weight(this, specificVolume)

@JvmName("volumeFromSpecificVolumeAndWeight")
fun <
    VolumeUnit : Volume,
    SpecificVolumeUnit : SpecificVolume,
    WeightUnit : Weight
    > VolumeUnit.volume(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = byMultiplying(specificVolume, weight)

@JvmName("metricSpecificVolumeTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("imperialSpecificVolumeTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("ukImperialSpecificVolumeTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("ukImperialSpecificVolumeTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("usCustomarySpecificVolumeTimesUKImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("usCustomarySpecificVolumeTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("specificVolumeTimesWeight")
infix operator fun <SpecificVolumeUnit : SpecificVolume, WeightUnit : Weight> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = CubicMeter.volume(this, weight)

@JvmName("metricWeightTimesMetricSpecificVolume")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = specificVolume * this
@JvmName("imperialWeightTimesImperialSpecificVolume")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume * this
@JvmName("imperialWeightTimesUKImperialSpecificVolume")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = specificVolume * this
@JvmName("imperialWeightTimesUSCustomarySpecificVolume")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = specificVolume * this
@JvmName("ukImperialWeightTimesUKImperialSpecificVolume")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = specificVolume * this
@JvmName("usCustomaryWeightTimesUSCustomarySpecificVolume")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = specificVolume * this
@JvmName("weightTimesSpecificVolume")
infix operator fun <SpecificVolumeUnit : SpecificVolume, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = specificVolume * this

@JvmName("areaDensityFromLengthAndSpecificVolume")
fun <
    SpecificVolumeUnit : SpecificVolume,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity
    > AreaDensityUnit.areaDensity(
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>
) = byDividing(length, specificVolume)

@JvmName("specificVolumeFromLengthAndAreaDensity")
fun <
    SpecificVolumeUnit : SpecificVolume,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity
    > SpecificVolumeUnit.specificVolume(
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>
) = byDividing(length, areaDensity)

@JvmName("lengthFromAreaDensityAndSpecificVolume")
fun <
    SpecificVolumeUnit : SpecificVolume,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity
    > LengthUnit.length(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>,
    areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>
) = byMultiplying(specificVolume, areaDensity)

@JvmName("metricLengthDivMetricSpecificVolume")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(this, specificVolume)
@JvmName("imperialLengthDivImperialSpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(this, specificVolume)
@JvmName("imperialLengthDivUKImperialSpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(this, specificVolume)
@JvmName("imperialLengthDivUSCustomarySpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(this, specificVolume)
@JvmName("lengthDivSpecificVolume")
infix operator fun <SpecificVolumeUnit : SpecificVolume, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = (Kilogram per SquareMeter).areaDensity(this, specificVolume)

@JvmName("metricLengthDivMetricAreaDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>) = ((1(areaDensity.unit.per) * 1(unit)).unit per areaDensity.unit.weight).specificVolume(this, areaDensity)
@JvmName("imperialLengthDivImperialAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = ((1(areaDensity.unit.per) * 1(unit)).unit per areaDensity.unit.weight).specificVolume(this, areaDensity)
@JvmName("imperialLengthDivUKImperialAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = ((1(areaDensity.unit.per) * 1(unit)).unit per areaDensity.unit.weight).specificVolume(this, areaDensity)
@JvmName("imperialLengthDivUSCustomaryAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = ((1(areaDensity.unit.per) * 1(unit)).unit per areaDensity.unit.weight).specificVolume(this, areaDensity)
@JvmName("lengthDivAreaDensity")
infix operator fun <AreaDensityUnit : AreaDensity, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>) = (CubicMeter per Kilogram).specificVolume(this, areaDensity)

@JvmName("metricSpecificVolumeTimesMetricAreaDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)
@JvmName("imperialSpecificVolumeTimesImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)
@JvmName("imperialSpecificVolumeTimesUKImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)
@JvmName("imperialSpecificVolumeTimesUSCustomaryAreaDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)
@JvmName("ukImperialSpecificVolumeTimesImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)
@JvmName("ukImperialSpecificVolumeTimesUKImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)
@JvmName("usCustomarySpecificVolumeTimesImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)
@JvmName("usCustomarySpecificVolumeTimesUSCustomaryAreaDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = (1(unit.volume) / 1(areaDensity.unit.per)).unit.length(this, areaDensity)
@JvmName("specificVolumeTimesAreaDensity")
infix operator fun <SpecificVolumeUnit : SpecificVolume, AreaDensityUnit : AreaDensity> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>) = Meter.length(this, areaDensity)

@JvmName("metricAreaDensityTimesMetricSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = specificVolume * this
@JvmName("imperialAreaDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume * this
@JvmName("imperialAreaDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = specificVolume * this
@JvmName("imperialAreaDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = specificVolume * this
@JvmName("ukImperialAreaDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume * this
@JvmName("ukImperialAreaDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = specificVolume * this
@JvmName("usCustomaryAreaDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume * this
@JvmName("usCustomaryAreaDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = specificVolume * this
@JvmName("areaDensityTimesSpecificVolume")
infix operator fun <AreaDensityUnit : AreaDensity, SpecificVolumeUnit : SpecificVolume> ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = specificVolume * this

@JvmName("linearMassDensityFromLengthAndSpecificVolume")
fun <
    SpecificVolumeUnit : SpecificVolume,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > LinearMassDensityUnit.linearMassDensity(
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>
) = byDividing(length, specificVolume)

@JvmName("specificVolumeFromLengthAndLinearMassDensity")
fun <
    SpecificVolumeUnit : SpecificVolume,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > SpecificVolumeUnit.specificVolume(
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>
) = byDividing(length, linearMassDensity)

@JvmName("lengthFromLinearMassDensityAndSpecificVolume")
fun <
    SpecificVolumeUnit : SpecificVolume,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > LengthUnit.length(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>,
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>
) = byMultiplying(specificVolume, linearMassDensity)

@JvmName("metricLengthDivMetricSpecificVolume")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).linearMassDensity(this, specificVolume)
@JvmName("imperialLengthDivImperialSpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).linearMassDensity(this, specificVolume)
@JvmName("imperialLengthDivUKImperialSpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).linearMassDensity(this, specificVolume)
@JvmName("imperialLengthDivUSCustomarySpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).linearMassDensity(this, specificVolume)
@JvmName("lengthDivSpecificVolume")
infix operator fun <SpecificVolumeUnit : SpecificVolume, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = (Kilogram per SquareMeter).linearMassDensity(this, specificVolume)

@JvmName("metricLengthDivMetricLinearMassDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>) = ((1(linearMassDensity.unit.per) * 1(unit)).unit per linearMassDensity.unit.weight).specificVolume(this, linearMassDensity)
@JvmName("imperialLengthDivImperialLinearMassDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = ((1(linearMassDensity.unit.per) * 1(unit)).unit per linearMassDensity.unit.weight).specificVolume(this, linearMassDensity)
@JvmName("imperialLengthDivUKImperialLinearMassDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = ((1(linearMassDensity.unit.per) * 1(unit)).unit per linearMassDensity.unit.weight).specificVolume(this, linearMassDensity)
@JvmName("imperialLengthDivUSCustomaryLinearMassDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = ((1(linearMassDensity.unit.per) * 1(unit)).unit per linearMassDensity.unit.weight).specificVolume(this, linearMassDensity)
@JvmName("lengthDivLinearMassDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>) = (CubicMeter per Kilogram).specificVolume(this, linearMassDensity)

@JvmName("metricSpecificVolumeTimesMetricLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.length(this, linearMassDensity)
@JvmName("imperialSpecificVolumeTimesImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.length(this, linearMassDensity)
@JvmName("imperialSpecificVolumeTimesUKImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.length(this, linearMassDensity)
@JvmName("imperialSpecificVolumeTimesUSCustomaryLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.length(this, linearMassDensity)
@JvmName("ukImperialSpecificVolumeTimesImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.length(this, linearMassDensity)
@JvmName("ukImperialSpecificVolumeTimesUKImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.length(this, linearMassDensity)
@JvmName("usCustomarySpecificVolumeTimesImperialLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.length(this, linearMassDensity)
@JvmName("usCustomarySpecificVolumeTimesUSCustomaryLinearMassDensity")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = (1(unit.volume) / 1(linearMassDensity.unit.per)).unit.length(this, linearMassDensity)
@JvmName("specificVolumeTimesLinearMassDensity")
infix operator fun <SpecificVolumeUnit : SpecificVolume, LinearMassDensityUnit : LinearMassDensity> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>) = Meter.length(this, linearMassDensity)

@JvmName("metricLinearMassDensityTimesMetricSpecificVolume")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = specificVolume * this
@JvmName("imperialLinearMassDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume * this
@JvmName("imperialLinearMassDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = specificVolume * this
@JvmName("imperialLinearMassDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = specificVolume * this
@JvmName("ukImperialLinearMassDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume * this
@JvmName("ukImperialLinearMassDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = specificVolume * this
@JvmName("usCustomaryLinearMassDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume * this
@JvmName("usCustomaryLinearMassDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = specificVolume * this
@JvmName("linearMassDensityTimesSpecificVolume")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, SpecificVolumeUnit : SpecificVolume> ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = specificVolume * this

