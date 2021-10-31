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
import com.splendo.kaluga.scientific.force.times
import com.splendo.kaluga.scientific.momentum.div
import com.splendo.kaluga.scientific.pressure.times
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricDynamicViscosityUnits: Set<MetricDynamicViscosity> = MetricPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

val ImperialDynamicViscosityUnits: Set<ImperialDynamicViscosity> = ImperialPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

val UKImperialDynamicViscosityUnits: Set<UKImperialDynamicViscosity> = UKImperialPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

val USCustomaryDynamicViscosityUnits: Set<USCustomaryDynamicViscosity> = USCustomaryPressureUnits.flatMap { pressure ->
    TimeUnits.map { pressure x it }
}.toSet()

val DynamicViscosityUnits: Set<DynamicViscosity> = MetricDynamicViscosityUnits +
    ImperialDynamicViscosityUnits +
    UKImperialDynamicViscosityUnits.filter { it.pressure !is UKImperialPressureWrapper }.toSet() +
    USCustomaryDynamicViscosityUnits.filter { it.pressure !is USCustomaryImperialPressureWrapper }.toSet()

@Serializable
sealed class DynamicViscosity : AbstractScientificUnit<MeasurementType.DynamicViscosity>() {
    abstract val pressure: Pressure
    abstract val time: Time
    override val type = MeasurementType.DynamicViscosity
    override val symbol: String by lazy { "${pressure.symbol}⋅${time.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = time.fromSIUnit(pressure.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = pressure.toSIUnit(time.toSIUnit(value))
}

@Serializable
data class MetricDynamicViscosity(override val pressure: MetricPressure, override val time: Time) : DynamicViscosity(), MetricScientificUnit<MeasurementType.DynamicViscosity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialDynamicViscosity(override val pressure: ImperialPressure, override val time: Time) : DynamicViscosity(), ImperialScientificUnit<MeasurementType.DynamicViscosity> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = pressure.ukImperial x time
    val usCustomary get() = pressure.usCustomary x time
}
@Serializable
data class UKImperialDynamicViscosity(override val pressure: UKImperialPressure, override val time: Time) : DynamicViscosity(), UKImperialScientificUnit<MeasurementType.DynamicViscosity> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryDynamicViscosity(override val pressure: USCustomaryPressure, override val time: Time) : DynamicViscosity(), USCustomaryScientificUnit<MeasurementType.DynamicViscosity> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricPressure.x(time: Time) = MetricDynamicViscosity(this, time)
infix fun ImperialPressure.x(time: Time) = ImperialDynamicViscosity(this, time)
infix fun UKImperialPressure.x(time: Time) = UKImperialDynamicViscosity(this, time)
infix fun USCustomaryPressure.x(time: Time) = USCustomaryDynamicViscosity(this, time)

@JvmName("pressureFromDynamicViscosityAndTime")
fun <
    DynamicViscosityUnit : DynamicViscosity,
    TimeUnit : Time,
    PressureUnit : Pressure
    > PressureUnit.pressure(
    dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(dynamicViscosity, time)

@JvmName("dynamicViscosityFromPressureAndTime")
fun <
    DynamicViscosityUnit : DynamicViscosity,
    TimeUnit : Time,
    PressureUnit : Pressure
    > DynamicViscosityUnit.dynamicViscosity(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(pressure, time)

@JvmName("timeFromDynamicViscosityAndPressure")
fun <
    DynamicViscosityUnit : DynamicViscosity,
    TimeUnit : Time,
    PressureUnit : Pressure
    > TimeUnit.time(
    dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>,
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = byDividing(dynamicViscosity, pressure)

@JvmName("dynamicViscosityFromMomentumAndArea")
fun <
    MomentumUnit : Momentum,
    AreaUnit : Area,
    DynamicViscosityUnit : DynamicViscosity
    > DynamicViscosityUnit.dynamicViscosity(
    momentum: ScientificValue<MeasurementType.Momentum, MomentumUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byDividing(momentum, area)

@JvmName("momentumFromDynamicViscosityAndArea")
fun <
    MomentumUnit : Momentum,
    AreaUnit : Area,
    DynamicViscosityUnit : DynamicViscosity
    > MomentumUnit.momentum(
    dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byMultiplying(dynamicViscosity, area)

@JvmName("areaFromMomentumAndDynamicViscosity")
fun <
    MomentumUnit : Momentum,
    AreaUnit : Area,
    DynamicViscosityUnit : DynamicViscosity
    > AreaUnit.area(
    momentum: ScientificValue<MeasurementType.Momentum, MomentumUnit>,
    dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>,
) = byDividing(momentum, dynamicViscosity)

@JvmName("metricDynamicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.DynamicViscosity, MetricDynamicViscosity>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.pressure).pressure(this, time)
@JvmName("imperialDynamicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.DynamicViscosity, ImperialDynamicViscosity>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.pressure).pressure(this, time)
@JvmName("ukImperialDynamicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.DynamicViscosity, UKImperialDynamicViscosity>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.pressure).pressure(this, time)
@JvmName("usCustomaryDynamicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.DynamicViscosity, USCustomaryDynamicViscosity>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.pressure).pressure(this, time)
@JvmName("dynamicViscosityDivTime")
infix operator fun <DynamicViscosityUnit : DynamicViscosity, TimeUnit : Time> ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.pressure).pressure(this, time)

@JvmName("metricPressureTimesTime")
infix operator fun <PressureUnit : MetricPressure, TimeUnit : Time> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).dynamicViscosity(this, time)
@JvmName("imperialPressureTimesTime")
infix operator fun <PressureUnit : ImperialPressure, TimeUnit : Time> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).dynamicViscosity(this, time)
@JvmName("ukImperialPressureTimesTime")
infix operator fun <PressureUnit : UKImperialPressure, TimeUnit : Time> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).dynamicViscosity(this, time)
@JvmName("usCustomaryPressureTimesTime")
infix operator fun <PressureUnit : USCustomaryPressure, TimeUnit : Time> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).dynamicViscosity(this, time)
@JvmName("pressureTimesTime")
infix operator fun <PressureUnit : Pressure, TimeUnit : Time> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pascal x time.unit).dynamicViscosity(this, time)

@JvmName("timeTimesMetricPressure")
infix operator fun <PressureUnit : MetricPressure, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = pressure * this
@JvmName("timeTimesImperialPressure")
infix operator fun <PressureUnit : ImperialPressure, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = pressure * this
@JvmName("timeTimesUKImperialPressure")
infix operator fun <PressureUnit : UKImperialPressure, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = pressure * this
@JvmName("timeTimesPressure")
infix operator fun <PressureUnit : Pressure, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = pressure * this

@JvmName("dynamicViscosityDivPressure")
infix operator fun <DynamicViscosityUnit : DynamicViscosity, PressureUnit : Pressure> ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = unit.time.time(this, pressure)

@JvmName("metricMomentumTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Momentum, MetricMomentum>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
@JvmName("imperialMomentumTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
@JvmName("ukImperialMomentumTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Momentum, UKImperialMomentum>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
@JvmName("usCustomaryMomentumTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Momentum, USCustomaryMomentum>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
@JvmName("momentumTimesArea")
infix operator fun <MomentumUnit : Momentum, AreaUnit : Area> ScientificValue<MeasurementType.Momentum, MomentumUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)

@JvmName("metricDynamicViscosityTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.DynamicViscosity, MetricDynamicViscosity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.pressure) * area) * 1(unit.time)).unit.momentum(this, area)
@JvmName("imperialDynamicViscosityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.DynamicViscosity, ImperialDynamicViscosity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.pressure) * area) * 1(unit.time)).unit.momentum(this, area)
@JvmName("ukImperialDynamicViscosityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.DynamicViscosity, UKImperialDynamicViscosity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.pressure) * area) * 1(unit.time)).unit.momentum(this, area)
@JvmName("usCustomaryDynamicViscosityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.DynamicViscosity, USCustomaryDynamicViscosity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.pressure) * area) * 1(unit.time)).unit.momentum(this, area)
@JvmName("dynamicViscosityTimesArea")
infix operator fun <DynamicViscosityUnit : DynamicViscosity, AreaUnit : Area> ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ((1(unit.pressure) * area) * 1(unit.time)).unit.momentum(this, area)

@JvmName("metricAreaTimesMetricDynamicViscosity")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, MetricDynamicViscosity>) = dynamicViscosity * this
@JvmName("imperialAreaTimesImperialDynamicViscosity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, ImperialDynamicViscosity>) = dynamicViscosity * this
@JvmName("imperialAreaTimesUKImperialDynamicViscosity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, UKImperialDynamicViscosity>) = dynamicViscosity * this
@JvmName("imperialAreaTimesUSCustomaryDynamicViscosity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, USCustomaryDynamicViscosity>) = dynamicViscosity * this
@JvmName("areaTimesDynamicViscosity")
infix operator fun <DynamicViscosityUnit : DynamicViscosity, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>) = dynamicViscosity * this

@JvmName("metricMomentumDivMetricDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, MetricMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, MetricDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("imperialMomentumDivImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, ImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("imperialMomentumDivUKImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, UKImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("imperialMomentumDivUSCustomaryDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, USCustomaryDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("ukImperialMomentumDivImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, UKImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, ImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("ukImperialMomentumDivUKImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, UKImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, UKImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("usCustomaryMomentumDivImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, USCustomaryMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, ImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("usCustomaryMomentumDivUSCustomaryDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, USCustomaryMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, USCustomaryDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("momentumDivDynamicViscosity")
infix operator fun <DynamicViscosityUnit : DynamicViscosity, MomentumUnit : Momentum> ScientificValue<MeasurementType.Momentum, MomentumUnit>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
