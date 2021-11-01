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
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricMolarityUnits: Set<MetricMolarity> = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    MetricVolumeUnits.map { amountOfSubstance per it }
}.toSet()

val ImperialMolarityUnits: Set<ImperialMolarity> = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    ImperialVolumeUnits.map { amountOfSubstance per it }
}.toSet()

val UKImperialMolarityUnits: Set<UKImperialMolarity> = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    UKImperialVolumeUnits.map { amountOfSubstance per it }
}.toSet()

val USCustomaryMolarityUnits: Set<USCustomaryMolarity> = AmountOfSubstanceUnits.flatMap { amountOfSubstance ->
    USCustomaryVolumeUnits.map { amountOfSubstance per it }
}.toSet()

val MolarityUnits: Set<Molarity> = MetricMolarityUnits +
    ImperialMolarityUnits +
    UKImperialMolarityUnits.filter { it.per !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryMolarityUnits.filter { it.per !is USCustomaryImperialVolumeWrapper }.toSet()

@Serializable
sealed class Molarity : AbstractScientificUnit<MeasurementType.Molarity>() {
    abstract val amountOfSubstance: AmountOfSubstance
    abstract val per: Volume
    override val symbol: String by lazy { "${amountOfSubstance.symbol} / ${per.symbol}" }
    override val type = MeasurementType.Molarity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(amountOfSubstance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = amountOfSubstance.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricMolarity(override val amountOfSubstance: AmountOfSubstance, override val per: MetricVolume) : Molarity(), MetricScientificUnit<MeasurementType.Molarity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialMolarity(override val amountOfSubstance: AmountOfSubstance, override val per: ImperialVolume) : Molarity(), ImperialScientificUnit<MeasurementType.Molarity> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = amountOfSubstance per per.ukImperial
    val usCustomary get() = amountOfSubstance per per.usCustomary
}
@Serializable
data class USCustomaryMolarity(override val amountOfSubstance: AmountOfSubstance, override val per: USCustomaryVolume) : Molarity(), USCustomaryScientificUnit<MeasurementType.Molarity> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialMolarity(override val amountOfSubstance: AmountOfSubstance, override val per: UKImperialVolume) : Molarity(), UKImperialScientificUnit<MeasurementType.Molarity> {
    override val system = MeasurementSystem.UKImperial
}

infix fun AmountOfSubstance.per(volume: MetricVolume) = MetricMolarity(this, volume)
infix fun AmountOfSubstance.per(volume: ImperialVolume) = ImperialMolarity(this, volume)
infix fun AmountOfSubstance.per(volume: USCustomaryVolume) = USCustomaryMolarity(this, volume)
infix fun AmountOfSubstance.per(volume: UKImperialVolume) = UKImperialMolarity(this, volume)

@JvmName("molarityFromAmountOfSubstanceAndVolume")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    VolumeUnit : Volume,
    MolarityUnit : Molarity
    > MolarityUnit.molarity(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>,
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = byDividing(amountOfSubstance, volume)

@JvmName("amountOfSubstanceFromMolarityAndVolume")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarityUnit : Molarity,
    VolumeUnit : Volume
    > AmountOfSubstanceUnit.amountOfSubstance(
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>,
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = byMultiplying(molarity, volume)

@JvmName("volumeFromAmountOfSubstanceAndMolarity")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    VolumeUnit : Volume,
    MolarityUnit : Molarity
    > VolumeUnit.volume(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>,
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>
) = byDividing(amountOfSubstance, molarity)

@JvmName("amountOfSubstanceDivMetricVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : MetricVolume> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).molarity(this, volume)
@JvmName("amountOfSubstanceDivImperialVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).molarity(this, volume)
@JvmName("amountOfSubstanceDivUKImperialVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).molarity(this, volume)
@JvmName("amountOfSubstanceDivUSCustomaryVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).molarity(this, volume)
@JvmName("amountOfSubstanceDivVolume")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, VolumeUnit : Volume> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (Mole per CubicMeter).molarity(this, volume)

@JvmName("metricMolarityTimesMetricVolume")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Molarity, MetricMolarity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.amountOfSubstance.amountOfSubstance(this, volume)
@JvmName("imperialMolarityTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Molarity, ImperialMolarity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.amountOfSubstance.amountOfSubstance(this, volume)
@JvmName("ukImperialMolarityTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.amountOfSubstance.amountOfSubstance(this, volume)
@JvmName("ukImperialMolarityTimesUKImperialVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.amountOfSubstance.amountOfSubstance(this, volume)
@JvmName("usCustomaryMolarityTimesUKImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.amountOfSubstance.amountOfSubstance(this, volume)
@JvmName("usCustomaryMolarityTimesUSCustomaryVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.amountOfSubstance.amountOfSubstance(this, volume)
@JvmName("molarityTimesVolume")
infix operator fun <MolarityUnit : Molarity, VolumeUnit : Volume> ScientificValue<MeasurementType.Molarity, MolarityUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = Mole.amountOfSubstance(this, volume)

@JvmName("metricVolumeTimesMetricMolarity")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, MetricMolarity>) = molarity * this
@JvmName("imperialVolumeTimesImperialMolarity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, ImperialMolarity>) = molarity * this
@JvmName("imperialVolumeTimesUKImperialMolarity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, UKImperialMolarity>) = molarity * this
@JvmName("imperialVolumeTimesUSCustomaryMolarity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>) = molarity * this
@JvmName("ukImperialVolumeTimesUKImperialMolarity")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, UKImperialMolarity>) = molarity * this
@JvmName("usCustomaryVolumeTimesUSCustomaryMolarity")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>) = molarity * this
@JvmName("volumeTimesMolarity")
infix operator fun <MolarityUnit : Molarity, VolumeUnit : Volume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = molarity * this

@JvmName("amountOfSubstanceDivMetricMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molarity: ScientificValue<MeasurementType.Molarity, MetricMolarity>) = molarity.unit.per.volume(this, molarity)
@JvmName("amountOfSubstanceDivImperialMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molarity: ScientificValue<MeasurementType.Molarity, ImperialMolarity>) = molarity.unit.per.volume(this, molarity)
@JvmName("amountOfSubstanceDivUKImperialMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molarity: ScientificValue<MeasurementType.Molarity, UKImperialMolarity>) = molarity.unit.per.volume(this, molarity)
@JvmName("amountOfSubstanceDivUSCustomaryMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molarity: ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>) = molarity.unit.per.volume(this, molarity)
@JvmName("amountOfSubstanceDivMolarity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, MolarityUnit : Molarity> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = CubicMeter.volume(this, molarity)

@JvmName("molarityFromDensityAndMolarMass")
fun <
    DensityUnit : Density,
    MolarMassUnit : MolarMass,
    MolarityUnit : Molarity
    > MolarityUnit.molarity(
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
    molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>
) = byDividing(density, molarMass)

@JvmName("densityFromMolarityAndMolarMass")
fun <
    DensityUnit : Density,
    MolarityUnit : Molarity,
    MolarMassUnit : MolarMass
    > DensityUnit.density(
    molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>,
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>
) = byMultiplying(molarMass, molarity)

@JvmName("molarMassFromDensityAndMolarity")
fun <
    DensityUnit : Density,
    MolarityUnit : Molarity,
    MolarMassUnit : MolarMass
    > MolarMassUnit.molarMass(
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>
) = byDividing(density, molarity)

@JvmName("metricDensityDivMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.Density, MetricDensity>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (molarMass.unit.per per unit.per).molarity(this, molarMass)
@JvmName("imperialDensityDivMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.Density, ImperialDensity>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (molarMass.unit.per per unit.per).molarity(this, molarMass)
@JvmName("ukImperialDensityDivMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.Density, UKImperialDensity>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (molarMass.unit.per per unit.per).molarity(this, molarMass)
@JvmName("usCustomaryDensityDivMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.Density, USCustomaryDensity>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (molarMass.unit.per per unit.per).molarity(this, molarMass)
@JvmName("densityDivMolarMass")
infix operator fun <DensityUnit : Density, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.Density, DensityUnit>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (molarMass.unit.per per CubicMeter).molarity(this, molarMass)

@JvmName("metricMolarMassTimesMetricMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, MetricMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, MetricMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("imperialMolarMassTimesImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, ImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("imperialMolarMassTimesUKImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, UKImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("imperialMolarMassTimesUSCustomaryMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("ukImperialMolarMassTimesImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, ImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("ukImperialMolarMassTimesUKImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, UKImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("usCustomaryMolarMassTimesImperialMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, ImperialMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("usCustomaryMolarMassTimesUSCustomaryMolarity")
infix operator fun ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.times(molarity: ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>) = (unit.weight per molarity.unit.per).density(this, molarity)
@JvmName("molarMassTimesMolarity")
infix operator fun <MolarMassUnit : MolarMass, MolarityUnit : Molarity> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = (Kilogram per CubicMeter).density(this, molarity)

@JvmName("metricMolarityTimesMetricMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, MetricMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MetricMolarMass>) = molarMass * this
@JvmName("imperialMolarityTimesImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("imperialMolarityTimesUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = molarMass * this
@JvmName("imperialMolarityTimesUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = molarMass * this
@JvmName("ukImperialMolarityTimesImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("ukImperialMolarityTimesUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = molarMass * this
@JvmName("usCustomaryMolarityTimesImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("usCustomaryMolarityTimesUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = molarMass * this
@JvmName("molarityTimesMolarMass")
infix operator fun <MolarityUnit : Molarity, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.Molarity, MolarityUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = molarMass * this

@JvmName("metricDensityDivMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.Density, MetricDensity>.div(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = (unit.weight per molarity.unit.amountOfSubstance).molarMass(this, molarity)
@JvmName("imperialDensityDivMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.Density, ImperialDensity>.div(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = (unit.weight per molarity.unit.amountOfSubstance).molarMass(this, molarity)
@JvmName("ukImperialDensityDivMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.Density, UKImperialDensity>.div(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = (unit.weight per molarity.unit.amountOfSubstance).molarMass(this, molarity)
@JvmName("usCustomaryDensityDivMolarity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.Density, USCustomaryDensity>.div(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = (unit.weight per molarity.unit.amountOfSubstance).molarMass(this, molarity)
@JvmName("densityDivMolarity")
infix operator fun <DensityUnit : Density, MolarityUnit : Molarity> ScientificValue<MeasurementType.Density, DensityUnit>.div(molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>) = (Kilogram per molarity.unit.amountOfSubstance).molarMass(this, molarity)

@JvmName("densityFromMolarityAndMolality")
fun <
    MolarityUnit : Molarity,
    MolalityUnit : Molality,
    DensityUnit : Density
    > DensityUnit.density(
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>,
    molality: ScientificValue<MeasurementType.Molality, MolalityUnit>
) = byDividing(molarity, molality)

@JvmName("molarityFromDensityAndMolality")
fun <
    MolarityUnit : Molarity,
    DensityUnit : Density,
    MolalityUnit : Molality
    > MolarityUnit.molarity(
    molality: ScientificValue<MeasurementType.Molality, MolalityUnit>,
    density: ScientificValue<MeasurementType.Density, DensityUnit>
) = byMultiplying(molality, density)

@JvmName("molalityFromMolarityAndDensity")
fun <
    MolarityUnit : Molarity,
    DensityUnit : Density,
    MolalityUnit : Molality
    > MolalityUnit.molality(
    molarity: ScientificValue<MeasurementType.Molarity, MolarityUnit>,
    density: ScientificValue<MeasurementType.Density, DensityUnit>
) = byDividing(molarity, density)

@JvmName("metricMolarityDivMetricMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, MetricMolarity>.div(molality: ScientificValue<MeasurementType.Molality, MetricMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("imperialMolarityDivImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("imperialMolarityDivUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("imperialMolarityDivUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("ukImperialMolarityDivImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("ukImperialMolarityDivUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("usCustomaryMolarityDivImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.div(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("usCustomaryMolarityDivUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.div(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("molarityDivMolality")
infix operator fun <MolarityUnit : Molarity, MolalityUnit : Molality> ScientificValue<MeasurementType.Molarity, MolarityUnit>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (Kilogram per CubicMeter).density(this, molality)

@JvmName("molalityTimesMetricDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, MetricDensity>) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)
@JvmName("molalityTimesImperialDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)
@JvmName("molalityTimesUKImperialDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)
@JvmName("molalityTimesUSCustomaryDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)
@JvmName("molalityTimesDensity")
infix operator fun <MolalityUnit : Molality, DensityUnit : Density> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, DensityUnit>) = (unit.amountOfSubstance per CubicMeter).molarity(this, density)

@JvmName("metricDensityTimesMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Density, MetricDensity>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = molality * this
@JvmName("imperialDensityTimesMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Density, ImperialDensity>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = molality * this
@JvmName("ukImperialDensityTimesMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Density, UKImperialDensity>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = molality * this
@JvmName("usCustomaryDensityTimesMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Density, USCustomaryDensity>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = molality * this
@JvmName("densityTimesMolalityTimes")
infix operator fun <MolalityUnit : Molality, DensityUnit : Density> ScientificValue<MeasurementType.Density, DensityUnit>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = molality * this

@JvmName("molarityDivMetricDensity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.Molarity, MolarityUnit>.div(density: ScientificValue<MeasurementType.Density, MetricDensity>) = (unit.amountOfSubstance per density.unit.weight).molality(this, density)
@JvmName("molarityDivImperialDensity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.Molarity, MolarityUnit>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (unit.amountOfSubstance per density.unit.weight).molality(this, density)
@JvmName("molarityDivUKImperialDensity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.Molarity, MolarityUnit>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (unit.amountOfSubstance per density.unit.weight).molality(this, density)
@JvmName(",olarityDivUSCustomaryDensity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<MeasurementType.Molarity, MolarityUnit>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (unit.amountOfSubstance per density.unit.weight).molality(this, density)
@JvmName("molarityDivDensity")
infix operator fun <MolarityUnit : Molarity, DensityUnit : Density> ScientificValue<MeasurementType.Molarity, MolarityUnit>.div(density: ScientificValue<MeasurementType.Density, DensityUnit>) = (Mole per Kilogram).molality(this, density)
