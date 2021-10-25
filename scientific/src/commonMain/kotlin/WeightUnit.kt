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
sealed class Weight : AbstractScientificUnit<MeasurementType.Weight>()

@Serializable
sealed class MetricWeight : Weight(), MetricScientificUnit<MeasurementType.Weight>

@Serializable
sealed class ImperialWeight(override val symbol: String) : Weight(), ImperialScientificUnit<MeasurementType.Weight> {
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Weight
}

@Serializable
sealed class USCustomaryWeight(override val symbol: String) : Weight(), USCustomaryScientificUnit<MeasurementType.Weight> {
    override val system = MeasurementSystem.USCustomary
    override val type = MeasurementType.Weight
}

@Serializable
sealed class UKImperialWeight(override val symbol: String) : Weight(), UKImperialScientificUnit<MeasurementType.Weight> {
    override val system = MeasurementSystem.UKImperial
    override val type = MeasurementType.Weight
}

// Metric Weight

@Serializable
object Gram : MetricWeight(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Weight> {
    override val symbol: String = "g"
    private const val GRAMS_IN_KILOGRAM = 1000.0
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Weight
    override fun toSIUnit(value: Decimal): Decimal = value / GRAMS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * GRAMS_IN_KILOGRAM.toDecimal()
}

@Serializable
object Nanogram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Nano(Gram)

@Serializable
object Microgram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Micro(Gram)

@Serializable
object Milligram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Milli(Gram)

@Serializable
object Centigram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Centi(Gram)

@Serializable
object Decigram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Deci(Gram)

@Serializable
object Decagram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Deca(Gram)

@Serializable
object Hectogram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Hecto(Gram)

@Serializable
object Kilogram : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Kilo(Gram)

@Serializable
object Tonne : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Gram> by Mega(Gram) {
    override val symbol: String = "t"
}

@Serializable
object Dalton : MetricWeight(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Weight> {
    override val symbol: String = "Da"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Weight
    override fun toSIUnit(value: Decimal): Decimal = Gram.toSIUnit(value) / AvogadroConstant
    override fun fromSIUnit(value: Decimal): Decimal = Gram.fromSIUnit(value) * AvogadroConstant
}

@Serializable
object KiloDalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Dalton> by Kilo(Dalton)
@Serializable
object MegaDalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Dalton> by Mega(Dalton)
@Serializable
object GigaDalton : MetricWeight(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Weight, Dalton> by Giga(Dalton)

// Imperial Weight
@Serializable
object Grain : ImperialWeight("gr") {
    private const val GRAIN_IN_POUND = 7000
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / GRAIN_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * GRAIN_IN_POUND.toDecimal()
}

@Serializable
object Dram : ImperialWeight("dr") {
    private const val DRAMS_IN_POUND = 256
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / DRAMS_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * DRAMS_IN_POUND.toDecimal()
}

@Serializable
object Ounce : ImperialWeight("oz") {
    private const val OUNCES_IN_POUND = 16
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value / OUNCES_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) * OUNCES_IN_POUND.toDecimal()
}

@Serializable
object Pound : ImperialWeight("lb") {
    private const val KILOGRAM_IN_POUND = 0.45359237
    override fun toSIUnit(value: Decimal): Decimal = value * KILOGRAM_IN_POUND.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value / KILOGRAM_IN_POUND.toDecimal()
}

@Serializable
object Stone : ImperialWeight("st") {
    private const val STONES_IN_POUND = 14
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * STONES_IN_POUND.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / STONES_IN_POUND.toDecimal()
}

// also long ton
@Serializable
object ImperialTon : UKImperialWeight("ton") {
    private const val POUND_IN_LONG_TONES = 2240
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * POUND_IN_LONG_TONES.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / POUND_IN_LONG_TONES.toDecimal()
}

@Serializable
data class UKImperialImperialWeightWrapper(val imperial: ImperialWeight) : UKImperialWeight(imperial.symbol) {
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

// also short ton
@Serializable
object UsTon : USCustomaryWeight("ton") {
    private const val POUND_IN_SHORT_TONES = 2000
    override fun toSIUnit(value: Decimal): Decimal = Pound.toSIUnit(value * POUND_IN_SHORT_TONES.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Pound.fromSIUnit(value) / POUND_IN_SHORT_TONES.toDecimal()
}

@Serializable
data class USCustomaryImperialWeightWrapper(val imperial: ImperialWeight) : USCustomaryWeight(imperial.symbol) {
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

@JvmName("massFromMomentumAndSpeed")
fun <
    WeightUnit : Weight,
    SpeedUnit : Speed,
    MomentumUnit : Momentum
    > WeightUnit.mass(
    momentum: ScientificValue<MeasurementType.Momentum, MomentumUnit>,
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>
) = byDividing(momentum, speed)

@JvmName("massFromForceAndAcceleration")
fun <
    MassUnit : Weight,
    AccelerationUnit : Acceleration,
    ForceUnit : Force
    > MassUnit.mass(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>
) = byDividing(force, acceleration)

@JvmName("weightFromDensityAndVolume")
fun <
    MassUnit : Weight,
    DensityUnit : Density,
    VolumeUnit : Volume
    > MassUnit.mass(
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = byMultiplying(density, volume)

@JvmName("weightFromEnergyAndAbsorbedDose")
fun <
    EnergyUnit : Energy,
    WeightUnit : Weight,
    AbsorbedDoseUnit : IonizingRadiationAbsorbedDose
    >
    WeightUnit.weight(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>
) = byDividing(energy, absorbedDose)

@JvmName("weightFromEnergyAndEquivalentDose")
fun <
    EnergyUnit : Energy,
    WeightUnit : Weight,
    EquivalentDoseUnit : IonizingRadiationEquivalentDose
    >
    WeightUnit.weight(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>
) = byDividing(energy, equivalentDose)

@JvmName("metricMomentumDivMetricSpeed")
operator fun ScientificValue<MeasurementType.Momentum, MetricMomentum>.div(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = unit.mass.mass(this, speed)
@JvmName("imperialMomentumDivImperialSpeed")
operator fun ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = unit.mass.mass(this, speed)
@JvmName("ukImperialMomentumDivImperialSpeed")
operator fun ScientificValue<MeasurementType.Momentum, UKImperialMomentum>.div(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = unit.mass.mass(this, speed)
@JvmName("usCustomaryMomentumDivImperialSpeed")
operator fun ScientificValue<MeasurementType.Momentum, USCustomaryMomentum>.div(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = unit.mass.mass(this, speed)
@JvmName("momentumDivSpeed")
operator fun <MomentumUnit : Momentum, SpeedUnit : Speed> ScientificValue<MeasurementType.Momentum, MomentumUnit>.div(speed: ScientificValue<MeasurementType.Speed, SpeedUnit>) = unit.mass.mass(this, speed)

@JvmName("newtonDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Newton>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("newtonMultipleDivAcceleration")
operator fun <NewtonUnit> ScientificValue<MeasurementType.Force, NewtonUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) where NewtonUnit : MetricForce, NewtonUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton> = Kilogram.mass(this, acceleration)
@JvmName("dyneDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Dyne>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("dyneMultipleDivAcceleration")
operator fun <DyneUnit> ScientificValue<MeasurementType.Force, DyneUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) where DyneUnit : MetricForce, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Gram.mass(this, acceleration)
@JvmName("kilogramForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, KilogramForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("gramForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, GramForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("milligramForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, MilligramForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Milligram.mass(this, acceleration)
@JvmName("tonneForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, TonneForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Tonne.mass(this, acceleration)
@JvmName("metricForceDivMetricAcceleration")
operator fun <ForceUnit : MetricForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("poundalDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Poundal>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("poundForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("ounceForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, OunceForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Ounce.mass(this, acceleration)
@JvmName("grainForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, GrainForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Grain.mass(this, acceleration)
@JvmName("kipDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Kip>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("usTonForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = UsTon.mass(this, acceleration)
@JvmName("imperialTonForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = ImperialTon.mass(this, acceleration)
@JvmName("forceDivAcceleration")
operator fun <ForceUnit : Force, AccelerationUnit : Acceleration> ScientificValue<MeasurementType.Force, ForceUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>) = Kilogram.mass(this, acceleration)

@JvmName("metricDensityTimesMetricVolume")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Density, MetricDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.weight.mass(this, volume)
@JvmName("metricVolumeTimesMetricDensity")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, MetricDensity>) = density * this
@JvmName("imperialDensityTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Density, ImperialDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.weight.mass(this, volume)
@JvmName("imperialVolumeTimesImperialDensity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = density * this
@JvmName("ukImperialDensityTimesUKImperialVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Density, UKImperialDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.weight.mass(this, volume)
@JvmName("ukImperialVolumeTimesUKImperialDensity")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = density * this
@JvmName("usCustomaryDensityTimesUSCustomaryVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Density, USCustomaryDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.weight.mass(this, volume)
@JvmName("usCustomaryVolumeTimesUSCustomaryDensity")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = density * this
@JvmName("densityTimesVolume")
infix operator fun <VolumeUnit : Volume> ScientificValue<MeasurementType.Density, Density>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = Kilogram.mass(this, volume)
@JvmName("volumeTimesDensity")
infix operator fun <VolumeUnit : Volume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, Density>) = density * this

@JvmName("ergDivRad")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, Rad>) = Gram.weight(this, absorbedDose)
@JvmName("ergMultipleDivRad")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, Rad>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Gram.weight(this, absorbedDose)
@JvmName("ergMultipleDivRadMultiple")
infix operator fun <AbsorbedDoseUnit> ScientificValue<MeasurementType.Energy, Erg>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) where
    AbsorbedDoseUnit : IonizingRadiationAbsorbedDose,
    AbsorbedDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad>
    = Gram.weight(this, absorbedDose)
@JvmName("ergMultipleDivRadMultiple")
infix operator fun <ErgUnit, AbsorbedDoseUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) where
    ErgUnit : Energy,
    ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg>,
    AbsorbedDoseUnit : IonizingRadiationAbsorbedDose,
    AbsorbedDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad>
    = Gram.weight(this, absorbedDose)
@JvmName("energyDivAbsorbedDose")
infix operator fun <EnergyUnit : Energy, AbsorbedDoseUnit : IonizingRadiationAbsorbedDose> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) = Kilogram.weight(this, absorbedDose)

@JvmName("ergDivRem")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>) = Gram.weight(this, equivalentDose)
@JvmName("ergMultipleDivRem")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Gram.weight(this, equivalentDose)
@JvmName("ergMultipleDivRemMultiple")
infix operator fun <EquivalentDoseUnit> ScientificValue<MeasurementType.Energy, Erg>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) where
    EquivalentDoseUnit : IonizingRadiationEquivalentDose,
    EquivalentDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>
    = Gram.weight(this, equivalentDose)
@JvmName("ergMultipleDivRemMultiple")
infix operator fun <ErgUnit, EquivalentDoseUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) where
    ErgUnit : Energy,
    ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg>,
    EquivalentDoseUnit : IonizingRadiationEquivalentDose,
    EquivalentDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>
    = Gram.weight(this, equivalentDose)
@JvmName("energyDivEquivalentDose")
infix operator fun <EnergyUnit : Energy, EquivalentDoseUnit : IonizingRadiationEquivalentDose> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) = Kilogram.weight(this, equivalentDose)
