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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.power.power
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerHour
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerMinute
import com.splendo.kaluga.scientific.unit.BritishThermalUnitPerSecond
import com.splendo.kaluga.scientific.unit.Centijoule
import com.splendo.kaluga.scientific.unit.Centiwatt
import com.splendo.kaluga.scientific.unit.CentiwattHour
import com.splendo.kaluga.scientific.unit.Decajoule
import com.splendo.kaluga.scientific.unit.Decawatt
import com.splendo.kaluga.scientific.unit.DecawattHour
import com.splendo.kaluga.scientific.unit.Decijoule
import com.splendo.kaluga.scientific.unit.Deciwatt
import com.splendo.kaluga.scientific.unit.DeciwattHour
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.ErgPerSecond
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundForcePerMinute
import com.splendo.kaluga.scientific.unit.FootPoundForcePerSecond
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.Gigajoule
import com.splendo.kaluga.scientific.unit.Gigawatt
import com.splendo.kaluga.scientific.unit.GigawattHour
import com.splendo.kaluga.scientific.unit.Hectojoule
import com.splendo.kaluga.scientific.unit.Hectowatt
import com.splendo.kaluga.scientific.unit.HectowattHour
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.InchPoundForcePerMinute
import com.splendo.kaluga.scientific.unit.InchPoundForcePerSecond
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilojoule
import com.splendo.kaluga.scientific.unit.Kilowatt
import com.splendo.kaluga.scientific.unit.KilowattHour
import com.splendo.kaluga.scientific.unit.Megajoule
import com.splendo.kaluga.scientific.unit.Megawatt
import com.splendo.kaluga.scientific.unit.MegawattHour
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.Microjoule
import com.splendo.kaluga.scientific.unit.Microwatt
import com.splendo.kaluga.scientific.unit.MicrowattHour
import com.splendo.kaluga.scientific.unit.Millijoule
import com.splendo.kaluga.scientific.unit.Milliwatt
import com.splendo.kaluga.scientific.unit.MilliwattHour
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Nanojoule
import com.splendo.kaluga.scientific.unit.Nanowatt
import com.splendo.kaluga.scientific.unit.NanowattHour
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.imperial
import com.splendo.kaluga.scientific.unit.metric
import kotlin.jvm.JvmName

@JvmName("wattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, WattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Watt.power(this, time)

@JvmName("nanowattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, NanowattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Nanowatt.power(this, time)

@JvmName("microwattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, MicrowattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Microwatt.power(this, time)

@JvmName("milliwattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, MilliwattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Milliwatt.power(this, time)

@JvmName("centiwattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, CentiwattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Centiwatt.power(this, time)

@JvmName("deciwattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, DeciwattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Deciwatt.power(this, time)

@JvmName("decawattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, DecawattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Decawatt.power(this, time)

@JvmName("hectowattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, HectowattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Hectowatt.power(this, time)

@JvmName("kilowattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, KilowattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Kilowatt.power(this, time)

@JvmName("megawattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, MegawattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Megawatt.power(this, time)

@JvmName("gigawattHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, GigawattHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Gigawatt.power(this, time)

@JvmName("jouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Joule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) = Watt.metric.power(this, time)

@JvmName("nanojouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Nanojoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Nanowatt.metric.power(this, time)

@JvmName("microjouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Microjoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Microwatt.metric.power(this, time)

@JvmName("millijouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Millijoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Milliwatt.metric.power(this, time)

@JvmName("centijouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Centijoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Centiwatt.metric.power(this, time)

@JvmName("decijouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Decijoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Deciwatt.metric.power(this, time)

@JvmName("decajouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Decajoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Decawatt.metric.power(this, time)

@JvmName("hectojouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Hectojoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Hectowatt.metric.power(this, time)

@JvmName("kilojouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Kilojoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Kilowatt.metric.power(this, time)

@JvmName("megajouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Megajoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Megawatt.metric.power(this, time)

@JvmName("gigajouleDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, Gigajoule>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Gigawatt.metric.power(this, time)

@JvmName("ergDivSecond")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(time: ScientificValue<PhysicalQuantity.Time, Second>) = ErgPerSecond.power(this, time)

@JvmName("ergMultipleDivSecond")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(time: ScientificValue<PhysicalQuantity.Time, Second>) =
    ErgPerSecond.power(this, time)

@JvmName("footPoundalDivSecond")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundal>.div(time: ScientificValue<PhysicalQuantity.Time, Second>) = FootPoundForcePerSecond.power(this, time)

@JvmName("footPoundalDivMinute")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundal>.div(time: ScientificValue<PhysicalQuantity.Time, Minute>) = FootPoundForcePerMinute.power(this, time)

@JvmName("footPoundForceDivSecond")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundForce>.div(time: ScientificValue<PhysicalQuantity.Time, Second>) = FootPoundForcePerSecond.power(this, time)

@JvmName("footPoundForceDivMinute")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundForce>.div(time: ScientificValue<PhysicalQuantity.Time, Minute>) = FootPoundForcePerMinute.power(this, time)

@JvmName("inchPoundForceDivSecond")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchPoundForce>.div(time: ScientificValue<PhysicalQuantity.Time, Second>) = InchPoundForcePerSecond.power(this, time)

@JvmName("inchPoundForceDivMinute")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchPoundForce>.div(time: ScientificValue<PhysicalQuantity.Time, Minute>) = InchPoundForcePerMinute.power(this, time)

@JvmName("inchOunceForceDivSecond")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchOunceForce>.div(time: ScientificValue<PhysicalQuantity.Time, Second>) = InchPoundForcePerSecond.power(this, time)

@JvmName("inchOunceForceDivMinute")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchOunceForce>.div(time: ScientificValue<PhysicalQuantity.Time, Minute>) = InchPoundForcePerMinute.power(this, time)

@JvmName("horsepowerHourDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, HorsepowerHour>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) = Horsepower.power(this, time)

@JvmName("britishThermalUnitDivHour")
infix operator fun ScientificValue<PhysicalQuantity.Energy, BritishThermalUnit>.div(time: ScientificValue<PhysicalQuantity.Time, Hour>) =
    BritishThermalUnitPerHour.power(this, time)

@JvmName("britishThermalUnitDivMinute")
infix operator fun ScientificValue<PhysicalQuantity.Energy, BritishThermalUnit>.div(time: ScientificValue<PhysicalQuantity.Time, Minute>) =
    BritishThermalUnitPerMinute.power(this, time)

@JvmName("britishThermalUnitDivSecond")
infix operator fun ScientificValue<PhysicalQuantity.Energy, BritishThermalUnit>.div(time: ScientificValue<PhysicalQuantity.Time, Second>) =
    BritishThermalUnitPerSecond.power(this, time)

@JvmName("metricEnergyDivTime")
infix operator fun <EnergyUnit : MetricEnergy, TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Watt.metric.power(this, time)

@JvmName("imperialEnergyDivTime")
infix operator fun <EnergyUnit : ImperialEnergy, TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = Watt.imperial.power(this, time)

@JvmName("energyDivTime")
infix operator fun <EnergyUnit : Energy, TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Watt.power(this, time)
