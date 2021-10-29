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
import com.splendo.kaluga.scientific.force.div
import com.splendo.kaluga.scientific.weight.times
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricYankUnits: Set<MetricYank> = MetricForceUnits.map { force ->
    TimeUnits.map { force per it }
}.flatten().toSet()

val ImperialYankUnits: Set<ImperialYank> = ImperialForceUnits.map { force ->
    TimeUnits.map { force per it }
}.flatten().toSet()

val UKImperialYankUnits: Set<UKImperialYank> = UKImperialForceUnits.map { force ->
    TimeUnits.map { force per it }
}.flatten().toSet()

val USCustomaryYankUnits: Set<USCustomaryYank> = USCustomaryForceUnits.map { force ->
    TimeUnits.map { force per it }
}.flatten().toSet()

val YankUnits: Set<Yank> = MetricYankUnits +
    ImperialYankUnits +
    UKImperialYankUnits.filter { it.force !is UKImperialImperialForceWrapper }.toSet() +
    USCustomaryYankUnits.filter { it.force !is USCustomaryImperialForceWrapper }.toSet()

@Serializable
sealed class Yank : AbstractScientificUnit<MeasurementType.Yank>() {
    abstract val force: Force
    abstract val per: Time
    override val type = MeasurementType.Yank
    override val symbol: String by lazy { "${force.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(force.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = force.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricYank(override val force: MetricForce, override val per: Time) : Yank(), MetricScientificUnit<MeasurementType.Yank> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialYank(override val force: ImperialForce, override val per: Time) : Yank(), ImperialScientificUnit<MeasurementType.Yank> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = UKImperialYank(force.ukImperial, per)
    val usCustomary get() = USCustomaryYank(force.usCustomary, per)
}
@Serializable
data class UKImperialYank(override val force: UKImperialForce, override val per: Time) : Yank(), UKImperialScientificUnit<MeasurementType.Yank> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryYank(override val force: USCustomaryForce, override val per: Time) : Yank(), USCustomaryScientificUnit<MeasurementType.Yank> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricForce.per(time: Time) = MetricYank(this, time)
infix fun ImperialForce.per(time: Time) = ImperialYank(this, time)
infix fun UKImperialForce.per(time: Time) = UKImperialYank(this, time)
infix fun USCustomaryForce.per(time: Time) = USCustomaryYank(this, time)

@JvmName("yankFromForceAndTime")
fun <
    ForceUnit : Force,
    TimeUnit : Time,
    YankUnit : Yank
    > YankUnit.yank(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(force, time)

@JvmName("forceFromYankAndTime")
fun <
    ForceUnit : Force,
    TimeUnit : Time,
    YankUnit : Yank
    > ForceUnit.force(
    yank: ScientificValue<MeasurementType.Yank, YankUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(yank, time)

@JvmName("timeFromForceAndYank")
fun <
    ForceUnit : Force,
    TimeUnit : Time,
    YankUnit : Yank
    > TimeUnit.time(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    yank: ScientificValue<MeasurementType.Yank, YankUnit>
) = byDividing(force, yank)

@JvmName("yankFromMassAndJolt")
fun <
    WeightUnit : Weight,
    JoltUnit : Jolt,
    YankUnit : Yank
    > YankUnit.yank(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    jolt: ScientificValue<MeasurementType.Jolt, JoltUnit>
) = byMultiplying(weight, jolt)

@JvmName("massFromYankAndJolt")
fun <
    WeightUnit : Weight,
    JoltUnit : Jolt,
    YankUnit : Yank
    > WeightUnit.mass(
    yank: ScientificValue<MeasurementType.Yank, YankUnit>,
    jolt: ScientificValue<MeasurementType.Jolt, JoltUnit>
) = byDividing(yank, jolt)

@JvmName("joltFromYankAndMass")
fun <
    WeightUnit : Weight,
    JoltUnit : Jolt,
    YankUnit : Yank
    > JoltUnit.jolt(
    yank: ScientificValue<MeasurementType.Yank, YankUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = byDividing(yank, weight)

@JvmName("metricForceDivTime")
infix operator fun <ForceUnit : MetricForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).yank(this, time)
@JvmName("imperialForceDivTime")
infix operator fun <ForceUnit : ImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).yank(this, time)
@JvmName("ukImperialForceDivTime")
infix operator fun <ForceUnit : UKImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).yank(this, time)
@JvmName("usCustomaryForceDivTime")
infix operator fun <ForceUnit : USCustomaryForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).yank(this, time)
@JvmName("forceDivTime")
infix operator fun <ForceUnit : Force, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Newton per time.unit).yank(this, time)

@JvmName("metricYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Yank, MetricYank>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)
@JvmName("imperialYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Yank, ImperialYank>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)
@JvmName("ukImperialYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Yank, UKImperialYank>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)
@JvmName("usCustomaryYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Yank, USCustomaryYank>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)
@JvmName("yankTimesTime")
infix operator fun <YankUnit : Yank, TimeUnit : Time> ScientificValue<MeasurementType.Yank, YankUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)

@JvmName("timeTimesMetricYank")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(yank: ScientificValue<MeasurementType.Yank, MetricYank>) = yank * this
@JvmName("timeTimesImperialYank")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(yank: ScientificValue<MeasurementType.Yank, ImperialYank>) = yank * this
@JvmName("timeTimesUKImperialYank")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(yank: ScientificValue<MeasurementType.Yank, UKImperialYank>) = yank * this
@JvmName("timeTimesUSCustomaryYank")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(yank: ScientificValue<MeasurementType.Yank, USCustomaryYank>) = yank * this
@JvmName("timeTimesYank")
infix operator fun <YankUnit : Yank, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(yank: ScientificValue<MeasurementType.Yank, YankUnit>) = yank * this

@JvmName("forceDivYank")
infix operator fun <ForceUnit : Force, YankUnit : Yank> ScientificValue<MeasurementType.Force, ForceUnit>.div(yank: ScientificValue<MeasurementType.Yank, YankUnit>) = yank.unit.per.time(this, yank)

@JvmName("metricWeightTimesMetricJolt")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(jolt: ScientificValue<MeasurementType.Jolt, MetricJolt>) = ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)
@JvmName("imperialWeightTimesImperialJolt")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) = ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)
@JvmName("ukImperialWeightTimesImperialJolt")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) = ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)
@JvmName("usCustomaryWeightTimesImperialJolt")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) = ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)
@JvmName("weightTimesJolt")
infix operator fun <WeightUnit : Weight, JoltUnit : Jolt> ScientificValue<MeasurementType.Weight, WeightUnit>.times(jolt: ScientificValue<MeasurementType.Jolt, JoltUnit>) = ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)

@JvmName("metricJoltTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Jolt, MetricJolt>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("imperialJoltTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Jolt, ImperialJolt>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("imperialJoltTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Jolt, ImperialJolt>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("imperialJoltTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Jolt, ImperialJolt>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("joltTimesWeight")
infix operator fun <WeightUnit : Weight, JoltUnit : Jolt> ScientificValue<MeasurementType.Jolt, JoltUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this

@JvmName("metricYankDivMetricJolt")
infix operator fun ScientificValue<MeasurementType.Yank, MetricYank>.div(jolt: ScientificValue<MeasurementType.Jolt, MetricJolt>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
@JvmName("imperialYankDivImperialJolt")
infix operator fun ScientificValue<MeasurementType.Yank, ImperialYank>.div(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
@JvmName("ukImperialYankDivImperialJolt")
infix operator fun ScientificValue<MeasurementType.Yank, UKImperialYank>.div(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
@JvmName("usCustomaryYankDivImperialJolt")
infix operator fun ScientificValue<MeasurementType.Yank, USCustomaryYank>.div(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
@JvmName("yankDivJolt")
infix operator fun <YankUnit : Yank, JoltUnit : Jolt> ScientificValue<MeasurementType.Yank, YankUnit>.div(jolt: ScientificValue<MeasurementType.Jolt, JoltUnit>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)

@JvmName("metricYankDivMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Yank, MetricYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("imperialYankDivImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Yank, ImperialYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("imperialYankDivUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Yank, ImperialYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("imperialYankDivUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Yank, ImperialYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("ukImperialYankDivImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Yank, UKImperialYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("ukImperialYankDivUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Yank, UKImperialYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("ukImperialYankDivUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Yank, UKImperialYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("usCustomaryYankDivImperialWeight")
infix operator fun <WeightUnit : ImperialWeight>  ScientificValue<MeasurementType.Yank, USCustomaryYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("usCustomaryYankDivUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight>  ScientificValue<MeasurementType.Yank, USCustomaryYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("usCustomaryYankDivUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight>  ScientificValue<MeasurementType.Yank, USCustomaryYank>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
@JvmName("yankDivWeight")
infix operator fun <YankUnit : Yank, WeightUnit : Weight> ScientificValue<MeasurementType.Yank, YankUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
