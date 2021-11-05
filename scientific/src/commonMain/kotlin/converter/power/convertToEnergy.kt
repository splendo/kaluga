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

package com.splendo.kaluga.scientific.converter.power

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerHour
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerMinute
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerSecond
import com.splendo.kaluga.scientific.unit.Centiwatt
import com.splendo.kaluga.scientific.unit.CentiwattHour
import com.splendo.kaluga.scientific.unit.Decawatt
import com.splendo.kaluga.scientific.unit.DecawattHour
import com.splendo.kaluga.scientific.unit.Deciwatt
import com.splendo.kaluga.scientific.unit.DeciwattHour
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgPerSecond
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundForcePerMinute
import com.splendo.kaluga.scientific.unit.FootPoundForcePerSecond
import com.splendo.kaluga.scientific.unit.Gigawatt
import com.splendo.kaluga.scientific.unit.GigawattHour
import com.splendo.kaluga.scientific.unit.Hectowatt
import com.splendo.kaluga.scientific.unit.HectowattHour
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialMetricAndImperialPowerWrapper
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilowatt
import com.splendo.kaluga.scientific.unit.KilowattHour
import com.splendo.kaluga.scientific.unit.Megawatt
import com.splendo.kaluga.scientific.unit.MegawattHour
import com.splendo.kaluga.scientific.unit.MetricMetricAndImperialPowerWrapper
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.Microwatt
import com.splendo.kaluga.scientific.unit.MicrowattHour
import com.splendo.kaluga.scientific.unit.Milliwatt
import com.splendo.kaluga.scientific.unit.MilliwattHour
import com.splendo.kaluga.scientific.unit.Nanowatt
import com.splendo.kaluga.scientific.unit.NanowattHour
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import kotlin.jvm.JvmName

@JvmName("wattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Watt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    WattHour.energy(this, time)

@JvmName("nanowattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Nanowatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    NanowattHour.energy(this, time)

@JvmName("microwattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Microwatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    MicrowattHour.energy(this, time)

@JvmName("milliwattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Milliwatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    MilliwattHour.energy(this, time)

@JvmName("centiwattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Centiwatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    CentiwattHour.energy(this, time)

@JvmName("deciwattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Deciwatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    DeciwattHour.energy(this, time)

@JvmName("decawattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Decawatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    DecawattHour.energy(this, time)

@JvmName("hectowattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Hectowatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    HectowattHour.energy(this, time)

@JvmName("kilowattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Kilowatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    KilowattHour.energy(this, time)

@JvmName("megawattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Megawatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    MegawattHour.energy(this, time)

@JvmName("gigawattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Gigawatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    GigawattHour.energy(this, time)

@JvmName("metricMetricAndImperialPowerWrapperTimesHour")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, MetricMetricAndImperialPowerWrapper>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = convert(unit.metricAndImperialPower) * time

@JvmName("ergPerSecondTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, ErgPerSecond>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) =
    Erg.energy(this, time)

@JvmName("metricPowerTimesTime")
infix operator fun <PowerUnit : MetricPower, TimeUnit : Time> ScientificValue<MeasurementType.Power, PowerUnit>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = Joule.energy(this, time)

@JvmName("horsepowerTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Horsepower>.times(time: ScientificValue<MeasurementType.Time, Hour>) =
    HorsepowerHour.energy(this, time)

@JvmName("footPoundForcePerSecondTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, FootPoundForcePerSecond>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = FootPoundForce.energy(this, time)

@JvmName("footPoundForcePerMinuteTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, FootPoundForcePerMinute>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = FootPoundForce.energy(this, time)

@JvmName("britishThermalUnitPerSecondTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, BritishThermalUnitPerSecond>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = BritishThermalUnit.energy(this, time)

@JvmName("britishThermalUnitPerMinuteTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, BritishThermalUnitPerMinute>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = BritishThermalUnit.energy(this, time)

@JvmName("britishThermalUnitPerHourTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, BritishThermalUnitPerHour>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = BritishThermalUnit.energy(this, time)

@JvmName("imperialMetricAndImperialPowerWrapperTimesHour")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, ImperialMetricAndImperialPowerWrapper>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = convert(unit.metricAndImperialPower) * time

@JvmName("imperialPowerTimesTime")
infix operator fun <PowerUnit : ImperialPower, TimeUnit : Time> ScientificValue<MeasurementType.Power, PowerUnit>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = FootPoundForce.energy(this, time)

@JvmName("powerTimesTime")
infix operator fun <PowerUnit : Power, TimeUnit : Time> ScientificValue<MeasurementType.Power, PowerUnit>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = Joule.energy(this, time)
