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

package com.splendo.kaluga.scientific.power

import com.splendo.kaluga.scientific.BritishThermalUnit
import com.splendo.kaluga.scientific.BritishThermalUnitPerHour
import com.splendo.kaluga.scientific.BritishThermalUnitPerMinute
import com.splendo.kaluga.scientific.BritishThermalUnitPerSecond
import com.splendo.kaluga.scientific.Centiwatt
import com.splendo.kaluga.scientific.CentiwattHour
import com.splendo.kaluga.scientific.Decawatt
import com.splendo.kaluga.scientific.DecawattHour
import com.splendo.kaluga.scientific.Deciwatt
import com.splendo.kaluga.scientific.DeciwattHour
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.ErgPerSecond
import com.splendo.kaluga.scientific.FootPoundForce
import com.splendo.kaluga.scientific.FootPoundForcePerMinute
import com.splendo.kaluga.scientific.FootPoundForcePerSecond
import com.splendo.kaluga.scientific.Gigawatt
import com.splendo.kaluga.scientific.GigawattHour
import com.splendo.kaluga.scientific.Hectowatt
import com.splendo.kaluga.scientific.HectowattHour
import com.splendo.kaluga.scientific.Horsepower
import com.splendo.kaluga.scientific.HorsepowerHour
import com.splendo.kaluga.scientific.Hour
import com.splendo.kaluga.scientific.ImperialMetricAndImperialPowerWrapper
import com.splendo.kaluga.scientific.ImperialPower
import com.splendo.kaluga.scientific.Joule
import com.splendo.kaluga.scientific.Kilowatt
import com.splendo.kaluga.scientific.KilowattHour
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Megawatt
import com.splendo.kaluga.scientific.MegawattHour
import com.splendo.kaluga.scientific.MetricMetricAndImperialPowerWrapper
import com.splendo.kaluga.scientific.MetricPower
import com.splendo.kaluga.scientific.Microwatt
import com.splendo.kaluga.scientific.MicrowattHour
import com.splendo.kaluga.scientific.Milliwatt
import com.splendo.kaluga.scientific.MilliwattHour
import com.splendo.kaluga.scientific.Nanowatt
import com.splendo.kaluga.scientific.NanowattHour
import com.splendo.kaluga.scientific.Power
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.Watt
import com.splendo.kaluga.scientific.WattHour
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.energy.energy
import kotlin.jvm.JvmName

@JvmName("wattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Watt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = WattHour.energy(this, time)
@JvmName("nanowattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Nanowatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = NanowattHour.energy(this, time)
@JvmName("microwattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Microwatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = MicrowattHour.energy(this, time)
@JvmName("milliwattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Milliwatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = MilliwattHour.energy(this, time)
@JvmName("centiwattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Centiwatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = CentiwattHour.energy(this, time)
@JvmName("deciwattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Deciwatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = DeciwattHour.energy(this, time)
@JvmName("decawattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Decawatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = DecawattHour.energy(this, time)
@JvmName("hectowattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Hectowatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = HectowattHour.energy(this, time)
@JvmName("kilowattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Kilowatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = KilowattHour.energy(this, time)
@JvmName("megawattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Megawatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = MegawattHour.energy(this, time)
@JvmName("gigawattTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Gigawatt>.times(time: ScientificValue<MeasurementType.Time, Hour>) = GigawattHour.energy(this, time)
@JvmName("metricMetricAndImperialPowerWrapperTimesHour")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, MetricMetricAndImperialPowerWrapper>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = convert(unit.metricAndImperialPower) * time
@JvmName("ergPerSecondTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, ErgPerSecond>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Erg.energy(this, time)
@JvmName("metricPowerTimesTime")
infix operator fun <PowerUnit : MetricPower, TimeUnit : Time> ScientificValue<MeasurementType.Power, PowerUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Joule.energy(this, time)
@JvmName("horsepowerTimesHour")
infix operator fun ScientificValue<MeasurementType.Power, Horsepower>.times(time: ScientificValue<MeasurementType.Time, Hour>) = HorsepowerHour.energy(this, time)
@JvmName("footPoundForcePerSecondTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, FootPoundForcePerSecond>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = FootPoundForce.energy(this, time)
@JvmName("footPoundForcePerMinuteTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, FootPoundForcePerMinute>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = FootPoundForce.energy(this, time)
@JvmName("britishThermalUnitPerSecondTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, BritishThermalUnitPerSecond>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = BritishThermalUnit.energy(this, time)
@JvmName("britishThermalUnitPerMinuteTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, BritishThermalUnitPerMinute>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = BritishThermalUnit.energy(this, time)
@JvmName("britishThermalUnitPerHourTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, BritishThermalUnitPerHour>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = BritishThermalUnit.energy(this, time)
@JvmName("imperialMetricAndImperialPowerWrapperTimesHour")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Power, ImperialMetricAndImperialPowerWrapper>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = convert(unit.metricAndImperialPower) * time
@JvmName("imperialPowerTimesTime")
infix operator fun <PowerUnit : ImperialPower, TimeUnit : Time> ScientificValue<MeasurementType.Power, PowerUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = FootPoundForce.energy(this, time)
@JvmName("powerTimesTime")
infix operator fun <PowerUnit : Power, TimeUnit : Time> ScientificValue<MeasurementType.Power, PowerUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Joule.energy(this, time)
