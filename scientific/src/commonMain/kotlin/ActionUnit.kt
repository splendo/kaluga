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
import com.splendo.kaluga.scientific.energy.div
import com.splendo.kaluga.scientific.force.times
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricAndImperialActionUnits: Set<MetricAndImperialAction> = MetricAndImperialEnergyUnits.map { energy ->
    TimeUnits.map { energy x it }
}.flatten().toSet()

val MetricActionUnits: Set<MetricAction> = MetricEnergyUnits.map { energy ->
    TimeUnits.map { energy x it }
}.flatten().toSet()

val ImperialActionUnits: Set<ImperialAction> = ImperialEnergyUnits.map { energy ->
    TimeUnits.map { energy x it }
}.flatten().toSet()

val ActionUnits: Set<Action> = MetricAndImperialActionUnits +
    MetricActionUnits.filter { it.energy !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    ImperialActionUnits.filter { it.energy !is ImperialMetricAndImperialEnergyWrapper }.toSet()

@Serializable
sealed class Action : AbstractScientificUnit<MeasurementType.Action>() {
    abstract val energy: Energy
    abstract val time: Time
    override val type = MeasurementType.Action
    override val symbol: String by lazy { "${energy.symbol}⋅${time.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = time.fromSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(time.toSIUnit(value))
}

@Serializable
data class MetricAndImperialAction(override val energy: MetricAndImperialEnergy, override val time: Time) : Action(), MetricAndImperialScientificUnit<MeasurementType.Action> {
    override val system = MeasurementSystem.MetricAndImperial
    val metric get() = energy.metric x time
    val imperial get() = energy.imperial x time
}
@Serializable
data class MetricAction(override val energy: MetricEnergy, override val time: Time) : Action(), MetricScientificUnit<MeasurementType.Action> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialAction(override val energy: ImperialEnergy, override val time: Time) : Action(), ImperialScientificUnit<MeasurementType.Action> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricAndImperialEnergy.x(time: Time) = MetricAndImperialAction(this, time)
infix fun MetricEnergy.x(time: Time) = MetricAction(this, time)
infix fun ImperialEnergy.x(time: Time) = ImperialAction(this, time)

@JvmName("energyFromActionAndTime")
fun <
    ActionUnit : Action,
    TimeUnit : Time,
    EnergyUnit : Energy
    > EnergyUnit.energy(
    action: ScientificValue<MeasurementType.Action, ActionUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(action, time)

@JvmName("actionFromEnergyAndTime")
fun <
    ActionUnit : Action,
    TimeUnit : Time,
    EnergyUnit : Energy
    > ActionUnit.action(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(energy, time)

@JvmName("timeFromActionAndEnergy")
fun <
    ActionUnit : Action,
    TimeUnit : Time,
    EnergyUnit : Energy
    > TimeUnit.time(
    action: ScientificValue<MeasurementType.Action, ActionUnit>,
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>
) = byDividing(action, energy)

@JvmName("actionFromMassAndSpeed")
fun <
    ForceUnit : Force,
    SpeedUnit : Speed,
    ActionUnit : Action
    > ActionUnit.action(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>
) = byMultiplying(force, speed)

@JvmName("forceFromActionAndSpeed")
fun <
    ForceUnit : Force,
    SpeedUnit : Speed,
    ActionUnit : Action
    > ForceUnit.force(
    action: ScientificValue<MeasurementType.Action, ActionUnit>,
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>
) = byDividing(action, speed)

@JvmName("speedFromActionAndMass")
fun <
    ForceUnit : Force,
    SpeedUnit : Speed,
    ActionUnit : Action
    > SpeedUnit.speed(
    action: ScientificValue<MeasurementType.Action, ActionUnit>,
    force: ScientificValue<MeasurementType.Force, ForceUnit>
) = byDividing(action, force)

@JvmName("metricAndImperialActionDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Action, MetricAndImperialAction>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.energy).energy(this, time)
@JvmName("metricActionDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Action, MetricAction>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.energy).energy(this, time)
@JvmName("imperialActionDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Action, ImperialAction>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.energy).energy(this, time)
@JvmName("actionDivTime")
infix operator fun <ActionUnit : Action, TimeUnit : Time> ScientificValue<MeasurementType.Action, ActionUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.energy).energy(this, time)

@JvmName("metricAndImperialEnergyTimesTime")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, TimeUnit : Time> ScientificValue<MeasurementType.Energy, EnergyUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).action(this, time)
@JvmName("metricEnergyTimesTime")
infix operator fun <EnergyUnit : MetricEnergy, TimeUnit : Time> ScientificValue<MeasurementType.Energy, EnergyUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).action(this, time)
@JvmName("imperialEnergyTimesTime")
infix operator fun <EnergyUnit : ImperialEnergy, TimeUnit : Time> ScientificValue<MeasurementType.Energy, EnergyUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).action(this, time)
@JvmName("energyTimesTime")
infix operator fun <EnergyUnit : Energy, TimeUnit : Time> ScientificValue<MeasurementType.Energy, EnergyUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Joule x time.unit).action(this, time)

@JvmName("timeTimesMetricAndImperialEnergy")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(energy: ScientificValue<MeasurementType.Energy, EnergyUnit>) = energy * this
@JvmName("timeTimesMetricEnergy")
infix operator fun <EnergyUnit : MetricEnergy, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(energy: ScientificValue<MeasurementType.Energy, EnergyUnit>) = energy * this
@JvmName("timeTimesImperialEnergy")
infix operator fun <EnergyUnit : ImperialEnergy, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(energy: ScientificValue<MeasurementType.Energy, EnergyUnit>) = energy * this
@JvmName("timeTimesEnergy")
infix operator fun <EnergyUnit : Energy, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(energy: ScientificValue<MeasurementType.Energy, EnergyUnit>) = energy * this

@JvmName("actionDivEnergy")
infix operator fun <ActionUnit : Action, EnergyUnit : Energy> ScientificValue<MeasurementType.Action, ActionUnit>.div(energy: ScientificValue<MeasurementType.Energy, EnergyUnit>) = unit.time.time(this, energy)

@JvmName("metricForceTimesMetricSpeed")
infix operator fun <ForceUnit : MetricForce> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = ((this * 1(speed.unit.distance)).unit x speed.unit.per).action(this, speed)
@JvmName("imperialForceTimesImperialSpeed")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = ((this * 1(speed.unit.distance)).unit x speed.unit.per).action(this, speed)
@JvmName("ukImperialForceTimesImperialSpeed")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = ((this * 1(speed.unit.distance)).unit x speed.unit.per).action(this, speed)
@JvmName("usCustomaryForceTimesImperialSpeed")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = ((this * 1(speed.unit.distance)).unit x speed.unit.per).action(this, speed)
@JvmName("forceTimesSpeed")
infix operator fun <ForceUnit : Force, SpeedUnit : Speed> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, SpeedUnit>) = ((this * 1(speed.unit.distance)).unit x speed.unit.per).action(this, speed)

@JvmName("metricSpeedTimesMetricForce")
infix operator fun <ForceUnit : MetricForce> ScientificValue<MeasurementType.Speed, MetricSpeed>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("imperialSpeedTimesImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<MeasurementType.Speed, ImperialSpeed>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("imperialSpeedTimesUKImperialForce")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Speed, ImperialSpeed>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("imperialSpeedTimesUSCustomaryForce")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Speed, ImperialSpeed>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("speedTimesForce")
infix operator fun <ForceUnit : Force, SpeedUnit : Speed> ScientificValue<MeasurementType.Speed, SpeedUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this

@JvmName("metricAndImperialActionDivMetricSpeed")
infix operator fun ScientificValue<MeasurementType.Action, MetricAndImperialAction>.div(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = (1(unit.energy) / 1(speed.unit.distance)).unit.force(this, speed)
@JvmName("metricAndImperialActionDivImperialSpeed")
infix operator fun ScientificValue<MeasurementType.Action, MetricAndImperialAction>.div(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = (1(unit.energy) / 1(speed.unit.distance)).unit.force(this, speed)
@JvmName("metricActionDivMetricSpeed")
infix operator fun ScientificValue<MeasurementType.Action, MetricAction>.div(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = (1(unit.energy) / 1(speed.unit.distance)).unit.force(this, speed)
@JvmName("imperialActionDivImperialSpeed")
infix operator fun ScientificValue<MeasurementType.Action, ImperialAction>.div(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = (1(unit.energy) / 1(speed.unit.distance)).unit.force(this, speed)
@JvmName("actionDivSpeed")
infix operator fun <ActionUnit : Action, SpeedUnit : Speed> ScientificValue<MeasurementType.Action, ActionUnit>.div(speed: ScientificValue<MeasurementType.Speed, SpeedUnit>) = (1(unit.energy) / 1(speed.unit.distance)).unit.force(this, speed)

@JvmName("metricActionDivMetricForce")
infix operator fun <ForceUnit : MetricForce> ScientificValue<MeasurementType.Action, MetricAction>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = ((1(unit.energy) / 1(force.unit)).unit per unit.time).speed(this, force)
@JvmName("imperialActionDivImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<MeasurementType.Action, ImperialAction>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = ((1(unit.energy) / 1(force.unit)).unit per unit.time).speed(this, force)
@JvmName("imperialActionDivUKImperialForce")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Action, ImperialAction>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = ((1(unit.energy) / 1(force.unit)).unit per unit.time).speed(this, force)
@JvmName("imperialActionDivUSCustomaryForce")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Action, ImperialAction>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = ((1(unit.energy) / 1(force.unit)).unit per unit.time).speed(this, force)
@JvmName("actionDivForce")
infix operator fun <ActionUnit : Action, ForceUnit : Force> ScientificValue<MeasurementType.Action, ActionUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = ((1(unit.energy) / 1(force.unit)).unit per unit.time).speed(this, force)
