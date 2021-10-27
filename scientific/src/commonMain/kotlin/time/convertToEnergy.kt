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

package com.splendo.kaluga.scientific.time

import com.splendo.kaluga.scientific.BritishThermalUnitPerHour
import com.splendo.kaluga.scientific.BritishThermalUnitPerMinute
import com.splendo.kaluga.scientific.BritishThermalUnitPerSecond
import com.splendo.kaluga.scientific.Centiwatt
import com.splendo.kaluga.scientific.Decawatt
import com.splendo.kaluga.scientific.Deciwatt
import com.splendo.kaluga.scientific.ErgPerSecond
import com.splendo.kaluga.scientific.FootPoundForcePerMinute
import com.splendo.kaluga.scientific.FootPoundForcePerSecond
import com.splendo.kaluga.scientific.Gigawatt
import com.splendo.kaluga.scientific.Hectowatt
import com.splendo.kaluga.scientific.Horsepower
import com.splendo.kaluga.scientific.Hour
import com.splendo.kaluga.scientific.ImperialMetricAndImperialPowerWrapper
import com.splendo.kaluga.scientific.ImperialPower
import com.splendo.kaluga.scientific.Kilowatt
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Megawatt
import com.splendo.kaluga.scientific.MetricMetricAndImperialPowerWrapper
import com.splendo.kaluga.scientific.MetricPower
import com.splendo.kaluga.scientific.Microwatt
import com.splendo.kaluga.scientific.Milliwatt
import com.splendo.kaluga.scientific.Nanowatt
import com.splendo.kaluga.scientific.Power
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.Watt
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.power.times
import kotlin.jvm.JvmName

@JvmName("hourTimesWatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Watt>) = power * this
@JvmName("hourTimesNanowatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Nanowatt>) = power * this
@JvmName("hourTimesMicrowatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Microwatt>) = power * this
@JvmName("hourTimesMilliwatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Milliwatt>) = power * this
@JvmName("hourTimesCentiwatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Centiwatt>) = power * this
@JvmName("hourTimesDeciwatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Deciwatt>) = power * this
@JvmName("hourTimesDecawatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Decawatt>) = power * this
@JvmName("hourTimesHectowatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Hectowatt>) = power * this
@JvmName("hourTimesKilowatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Kilowatt>) = power * this
@JvmName("hourTimesMegawatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Megawatt>) = power * this
@JvmName("hourTimesGigawatt")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Gigawatt>) = power * this
@JvmName("timeTimesMetricMetricAndImperialPowerWrapperTimesHour")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, MetricMetricAndImperialPowerWrapper>) = power * this
@JvmName("timeTimesErgPerSecond")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, ErgPerSecond>) = power * this
@JvmName("timeTimesPower")
infix operator fun <PowerUnit : MetricPower, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, PowerUnit>) = power * this
@JvmName("hourTimesHorsepower")
infix operator fun ScientificValue<MeasurementType.Time, Hour>.times(power: ScientificValue<MeasurementType.Power, Horsepower>) = power * this
@JvmName("timeTimesFootPoundForcePerSecond")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, FootPoundForcePerSecond>) = power * this
@JvmName("timeTimesFootPoundForcePerMinute")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, FootPoundForcePerMinute>) = power * this
@JvmName("timeTimesBritishThermalUnitPerSecond")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, BritishThermalUnitPerSecond>) = power * this
@JvmName("timeTimesBritishThermalUnitPerMinute")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, BritishThermalUnitPerMinute>) = power * this
@JvmName("timeTimesBritishThermalUnitPerHour")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, BritishThermalUnitPerHour>) = power * this
@JvmName("timeTimesImperialMetricAndImperialPowerWrapperTimesHour")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, ImperialMetricAndImperialPowerWrapper>) = power * this
@JvmName("timeTimesImperialPower")
infix operator fun <PowerUnit : ImperialPower, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, PowerUnit>) = power * this
@JvmName("timeTimesPower")
infix operator fun <PowerUnit : Power, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(power: ScientificValue<MeasurementType.Power, PowerUnit>) = power * this
