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

package com.splendo.kaluga.scientific.converter.time

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.GramForce
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MilligramForce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.TonneForce
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.UsTonForce
import kotlin.jvm.JvmName

@JvmName("timeTimesDyne")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(dyne: ScientificValue<PhysicalQuantity.Force, Dyne>) = dyne * this

@JvmName("timeTimesDyneMultiple")
infix operator fun <DyneUnit : DyneMultiple, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(dyne: ScientificValue<PhysicalQuantity.Force, DyneUnit>) =
    dyne * this

@JvmName("timeTimesTonneForce")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(tonne: ScientificValue<PhysicalQuantity.Force, TonneForce>) = tonne * this

@JvmName("timeTimesGramForce")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(gram: ScientificValue<PhysicalQuantity.Force, GramForce>) = gram * this

@JvmName("timeTimesMilligramForce")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(milligram: ScientificValue<PhysicalQuantity.Force, MilligramForce>) = milligram * this

@JvmName("timeTimesMetricForce")
infix operator fun <ForceUnit : MetricForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    force * this

@JvmName("timeTimesPoundal")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(poundal: ScientificValue<PhysicalQuantity.Force, Poundal>) = poundal * this

@JvmName("timeTimesOunceForce")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(ounceForce: ScientificValue<PhysicalQuantity.Force, OunceForce>) = ounceForce * this

@JvmName("timeTimesGrainForce")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(grainForce: ScientificValue<PhysicalQuantity.Force, GrainForce>) = grainForce * this

@JvmName("timeTimesUsTonForce")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(usTonForce: ScientificValue<PhysicalQuantity.Force, UsTonForce>) = usTonForce * this

@JvmName("timeTimesImperialTonForce")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(imperialTonForce: ScientificValue<PhysicalQuantity.Force, ImperialTonForce>) =
    imperialTonForce * this

@JvmName("timeTimesImperialForce")
infix operator fun <ForceUnit : ImperialForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    force * this

@JvmName("timeTimesUKImperialForce")
infix operator fun <ForceUnit : UKImperialForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = force * this

@JvmName("timeTimesUSCustomaryForce")
infix operator fun <ForceUnit : USCustomaryForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = force * this

@JvmName("timeTimesForce")
infix operator fun <ForceUnit : Force, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    force * this
