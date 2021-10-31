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

package com.splendo.kaluga.scientific.force

import com.splendo.kaluga.scientific.Centimeter
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Foot
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.Grain
import com.splendo.kaluga.scientific.GrainForce
import com.splendo.kaluga.scientific.Gram
import com.splendo.kaluga.scientific.GramForce
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialTon
import com.splendo.kaluga.scientific.ImperialTonForce
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.Kip
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.Milligram
import com.splendo.kaluga.scientific.MilligramForce
import com.splendo.kaluga.scientific.Ounce
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.Pound
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.Poundal
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Second
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.Tonne
import com.splendo.kaluga.scientific.TonneForce
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.UsTon
import com.splendo.kaluga.scientific.UsTonForce
import com.splendo.kaluga.scientific.momentum.momentum
import com.splendo.kaluga.scientific.per
import com.splendo.kaluga.scientific.x
import kotlin.jvm.JvmName

@JvmName("dyneTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, Dyne>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Gram x (Centimeter per Second)).momentum(this, time)
@JvmName("dyneMultipleTimesTime")
infix operator fun <DyneUnit, TimeUnit : Time> ScientificValue<MeasurementType.Force, DyneUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) where DyneUnit : MetricForce, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = (Gram x (Centimeter per Second)).momentum(this, time)
@JvmName("tonneForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, TonneForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Tonne x (Meter per Second)).momentum(this, time)
@JvmName("gramForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, GramForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Gram x (Meter per Second)).momentum(this, time)
@JvmName("milligramForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, MilligramForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Milligram x (Meter per Second)).momentum(this, time)
@JvmName("metricForceTimesTime")
infix operator fun <ForceUnit : MetricForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Kilogram x (Meter per Second)).momentum(this, time)
@JvmName("poundalTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, Poundal>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("poundForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, PoundForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("ounceForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, OunceForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Ounce x (Foot per Second)).momentum(this, time)
@JvmName("grainForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, GrainForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Grain x (Foot per Second)).momentum(this, time)
@JvmName("kipTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, Kip>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("usTonForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, UsTonForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (UsTon x (Foot per Second)).momentum(this, time)
@JvmName("imperialTonForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Force, ImperialTonForce>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (ImperialTon x (Foot per Second)).momentum(this, time)
@JvmName("imperialForceTimesTime")
infix operator fun <ForceUnit : ImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("ukImperialForceTimesTime")
infix operator fun <ForceUnit : UKImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("usCustomaryForceTimesTime")
infix operator fun <ForceUnit : USCustomaryForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Pound x (Foot per Second)).momentum(this, time)
@JvmName("forceTimesTime")
infix operator fun <ForceUnit : Force, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Kilogram x (Meter per Second)).momentum(this, time)
