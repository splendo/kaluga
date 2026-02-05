/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.Centiwatt
import com.splendo.kaluga.scientific.unit.CentiwattHour
import com.splendo.kaluga.scientific.unit.Decawatt
import com.splendo.kaluga.scientific.unit.DecawattHour
import com.splendo.kaluga.scientific.unit.Deciwatt
import com.splendo.kaluga.scientific.unit.DeciwattHour
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Gigawatt
import com.splendo.kaluga.scientific.unit.GigawattHour
import com.splendo.kaluga.scientific.unit.Hectowatt
import com.splendo.kaluga.scientific.unit.HectowattHour
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialCombinedPower
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilowatt
import com.splendo.kaluga.scientific.unit.KilowattHour
import com.splendo.kaluga.scientific.unit.Megawatt
import com.splendo.kaluga.scientific.unit.MegawattHour
import com.splendo.kaluga.scientific.unit.MetricAndImperialCombinedPower
import com.splendo.kaluga.scientific.unit.MetricCombinedPower
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
infix operator fun ScientificValue<PhysicalQuantity.Power, Watt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = WattHour.energy(this, time)

@JvmName("nanowattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Nanowatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = NanowattHour.energy(this, time)

@JvmName("microwattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Microwatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = MicrowattHour.energy(this, time)

@JvmName("milliwattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Milliwatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = MilliwattHour.energy(this, time)

@JvmName("centiwattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Centiwatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = CentiwattHour.energy(this, time)

@JvmName("deciwattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Deciwatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = DeciwattHour.energy(this, time)

@JvmName("decawattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Decawatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = DecawattHour.energy(this, time)

@JvmName("hectowattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Hectowatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = HectowattHour.energy(this, time)

@JvmName("kilowattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Kilowatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = KilowattHour.energy(this, time)

@JvmName("megawattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Megawatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = MegawattHour.energy(this, time)

@JvmName("gigawattTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Gigawatt>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = GigawattHour.energy(this, time)

@JvmName("metricAndImperialCombinedPowerTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Power, MetricAndImperialCombinedPower>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.energy.energy(this, time)

@JvmName("metricCombinedPowerTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Power, MetricCombinedPower>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.energy.energy(this, time)

@JvmName("metricPowerTimesTime")
infix operator fun <PowerUnit : MetricPower, TimeUnit : Time> ScientificValue<PhysicalQuantity.Power, PowerUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Joule.energy(this, time)

@JvmName("imperialCombinedPowerTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.energy.energy(this, time)

@JvmName("horsepowerTimesHour")
infix operator fun ScientificValue<PhysicalQuantity.Power, Horsepower>.times(time: ScientificValue<PhysicalQuantity.Time, Hour>) = HorsepowerHour.energy(this, time)

@JvmName("imperialPowerTimesTime")
infix operator fun <PowerUnit : ImperialPower, TimeUnit : Time> ScientificValue<PhysicalQuantity.Power, PowerUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    FootPoundForce.energy(this, time)

@JvmName("powerTimesTime")
infix operator fun <PowerUnit : Power, TimeUnit : Time> ScientificValue<PhysicalQuantity.Power, PowerUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Joule.energy(this, time)
