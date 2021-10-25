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

@Serializable
sealed class Momentum : AbstractScientificUnit<MeasurementType.Momentum>() {
    abstract val mass: Weight
    abstract val speed: Speed
    override val type = MeasurementType.Momentum
    override val symbol: String by lazy { "${mass.symbol} * ${speed.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = speed.fromSIUnit(mass.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = mass.toSIUnit(speed.toSIUnit(value))
}

@Serializable
data class MetricMomentum(override val mass: MetricWeight, override val speed: MetricSpeed) : Momentum(), MetricScientificUnit<MeasurementType.Momentum> {
    override val system = MeasurementSystem.Metric
}

@Serializable
data class ImperialMomentum(override val mass: ImperialWeight, override val speed: ImperialSpeed) : Momentum(), ImperialScientificUnit<MeasurementType.Momentum> {
    override val system = MeasurementSystem.Imperial
}

@Serializable
data class UKImperialMomentum(override val mass: UKImperialWeight, override val speed: ImperialSpeed) : Momentum(), UKImperialScientificUnit<MeasurementType.Momentum> {
    override val system = MeasurementSystem.UKImperial
}

@Serializable
data class USCustomaryMomentum(override val mass: USCustomaryWeight, override val speed: ImperialSpeed) : Momentum(), USCustomaryScientificUnit<MeasurementType.Momentum> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricWeight.x(speed: MetricSpeed) = MetricMomentum(this, speed)
infix fun ImperialWeight.x(speed: ImperialSpeed) = ImperialMomentum(this, speed)
infix fun UKImperialWeight.x(speed: ImperialSpeed) = UKImperialMomentum(this, speed)
infix fun USCustomaryWeight.x(speed: ImperialSpeed) = USCustomaryMomentum(this, speed)

@JvmName("momentumFromMassAndSpeed")
fun <
    WeightUnit : Weight,
    SpeedUnit : Speed,
    MomentumUnit : Momentum
    > MomentumUnit.momentum(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>,
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>
) = byMultiplying(mass, speed)

@JvmName("momentumFromForceAndTime")
fun <
    ForceUnit : Force,
    TimeUnit : Time,
    MomentumUnit : Momentum
    > MomentumUnit.momentum(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(force, time)

@JvmName("metricWeightTimesMetricSpeed")
operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = (unit x speed.unit).momentum(this, speed)
@JvmName("metricSpeedTimesMetricWeight")
operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Speed, MetricSpeed>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("imperialWeightTimesImperialSpeed")
operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = (unit x speed.unit).momentum(this, speed)
@JvmName("imperialSpeedTimesImperialWeight")
operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Speed, ImperialSpeed>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("ukImperialWeightTimesImperialSpeed")
operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = (unit x speed.unit).momentum(this, speed)
@JvmName("imperialSpeedTimesUKImperialWeight")
operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Speed, ImperialSpeed>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("usCustomaryWeightTimesImperialSpeed")
operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = (unit x speed.unit).momentum(this, speed)
@JvmName("imperialSpeedTimesUSCustomaryWeight")
operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Speed, ImperialSpeed>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this

@JvmName("dyneTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, Dyne>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Gram x (Centimeter per Second)).momentum(this, time)
@JvmName("timeTimesDyne")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(dyne: ScientificValue<MeasurementType.Force, Dyne>) = dyne * this
@JvmName("dyneMultipleTimesTime")
operator fun <DyneUnit, TimeUnit : Time> ScientificValue<MeasurementType.Force, DyneUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) where DyneUnit : MetricForce, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = (Gram x (Centimeter per Second)).momentum(this, time)
@JvmName("timeTimesDyneMultiplee")
operator fun <DyneUnit, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(dyne: ScientificValue<MeasurementType.Force, DyneUnit>) where DyneUnit : MetricForce, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = dyne * this
@JvmName("tonneForceTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, TonneForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Tonne x (Meter per Second)).momentum(this, time)
@JvmName("timeTimesTonneForce")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(tonne: ScientificValue<MeasurementType.Force, TonneForce>) = tonne * this
@JvmName("gramForceTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, GramForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Gram x (Meter per Second)).momentum(this, time)
@JvmName("timeTimesGramForce")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(gram: ScientificValue<MeasurementType.Force, GramForce>) = gram * this
@JvmName("milligramForceTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, MilligramForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Milligram x (Meter per Second)).momentum(this, time)
@JvmName("timeTimesMilligramForce")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(milligram: ScientificValue<MeasurementType.Force, MilligramForce>) = milligram * this
@JvmName("metricForceTimesTime")
operator fun <ForceUnit : MetricForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Kilogram x (Meter per Second)).momentum(this, time)
@JvmName("timeTimesMetricForce")
operator fun <ForceUnit : MetricForce, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this

@JvmName("poundalTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, Poundal>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesPoundal")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(poundal: ScientificValue<MeasurementType.Force, Poundal>) = poundal * this
@JvmName("poundForceTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, PoundForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesPoundForce")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(poundForce: ScientificValue<MeasurementType.Force, PoundForce>) = poundForce * this
@JvmName("ounceForceTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, OunceForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Ounce x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesOunceForce")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(ounceForce: ScientificValue<MeasurementType.Force, OunceForce>) = ounceForce * this
@JvmName("grainForceTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, GrainForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Grain x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesGrainForce")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(grainForce: ScientificValue<MeasurementType.Force, GrainForce>) = grainForce * this
@JvmName("kipTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, Kip>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesKip")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(kip: ScientificValue<MeasurementType.Force, Kip>) = kip * this
@JvmName("usTonForceTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, UsTonForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (UsTon x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesUsTonForce")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(usTonForce: ScientificValue<MeasurementType.Force, UsTonForce>) = usTonForce * this
@JvmName("imperialTonForceTimesTime")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, ImperialTonForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (ImperialTon x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesImperialTonForce")
operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(imperialTonForce: ScientificValue<MeasurementType.Force, ImperialTonForce>) = imperialTonForce * this
@JvmName("imperialForceTimesTime")
operator fun <ForceUnit : ImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesImperialForce")
operator fun <ForceUnit : ImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("ukImperialForceTimesTime")
operator fun <ForceUnit : UKImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesUKImperialForce")
operator fun <ForceUnit : UKImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("usCustomaryForceTimesTime")
operator fun <ForceUnit : USCustomaryForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("timeTimesUSCustomaryForce")
operator fun <ForceUnit : USCustomaryForce, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this