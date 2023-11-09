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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.momentum.momentum
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.GramForce
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.Milligram
import com.splendo.kaluga.scientific.unit.MilligramForce
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.Tonne
import com.splendo.kaluga.scientific.unit.TonneForce
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("dyneTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, Dyne>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Gram x (Centimeter per Second)).momentum(this, time)

@JvmName("dyneMultipleTimesTime")
infix operator fun <DyneUnit : DyneMultiple, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, DyneUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Gram x (Centimeter per Second)).momentum(this, time)

@JvmName("tonneForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, TonneForce>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Tonne x (Meter per Second)).momentum(this, time)

@JvmName("gramForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, GramForce>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Gram x (Meter per Second)).momentum(this, time)

@JvmName("milligramForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, MilligramForce>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Milligram x (Meter per Second)).momentum(this, time)

@JvmName("metricForceTimesTime")
infix operator fun <ForceUnit : MetricForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Kilogram x (Meter per Second)).momentum(this, time)

@JvmName("poundalTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, Poundal>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Pound x (Foot per Second)).momentum(this, time)

@JvmName("ounceForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, OunceForce>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Ounce x (Foot per Second)).momentum(this, time)

@JvmName("grainForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, GrainForce>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Grain x (Foot per Second)).momentum(this, time)

@JvmName("usTonForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, UsTonForce>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (UsTon x (Foot per Second)).momentum(this, time)

@JvmName("imperialTonForceTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ImperialTonForce>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (ImperialTon x (Foot per Second)).momentum(this, time)

@JvmName("imperialForceTimesTime")
infix operator fun <ForceUnit : ImperialForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Pound x (Foot per Second)).momentum(this, time)

@JvmName("ukImperialForceTimesTime")
infix operator fun <ForceUnit : UKImperialForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (Pound x (Foot per Second)).ukImperial.momentum(this, time)

@JvmName("usCustomaryForceTimesTime")
infix operator fun <ForceUnit : USCustomaryForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (Pound x (Foot per Second)).usCustomary.momentum(this, time)

@JvmName("forceTimesTime")
infix operator fun <ForceUnit : Force, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Kilogram x (Meter per Second)).momentum(this, time)
