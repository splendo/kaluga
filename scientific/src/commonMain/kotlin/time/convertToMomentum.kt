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

import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.GrainForce
import com.splendo.kaluga.scientific.GramForce
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialTonForce
import com.splendo.kaluga.scientific.Kip
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.MilligramForce
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.Poundal
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.TonneForce
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.UsTonForce
import com.splendo.kaluga.scientific.force.times
import kotlin.jvm.JvmName

@JvmName("timeTimesDyne")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(dyne: ScientificValue<MeasurementType.Force, Dyne>) = dyne * this
@JvmName("timeTimesDyneMultiple")
infix operator fun <DyneUnit, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(dyne: ScientificValue<MeasurementType.Force, DyneUnit>) where DyneUnit : MetricForce, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = dyne * this
@JvmName("timeTimesTonneForce")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(tonne: ScientificValue<MeasurementType.Force, TonneForce>) = tonne * this
@JvmName("timeTimesGramForce")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(gram: ScientificValue<MeasurementType.Force, GramForce>) = gram * this
@JvmName("timeTimesMilligramForce")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(milligram: ScientificValue<MeasurementType.Force, MilligramForce>) = milligram * this
@JvmName("timeTimesMetricForce")
infix operator fun <ForceUnit : MetricForce, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("timeTimesPoundal")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(poundal: ScientificValue<MeasurementType.Force, Poundal>) = poundal * this
@JvmName("timeTimesPoundForce")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(poundForce: ScientificValue<MeasurementType.Force, PoundForce>) = poundForce * this
@JvmName("timeTimesOunceForce")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(ounceForce: ScientificValue<MeasurementType.Force, OunceForce>) = ounceForce * this
@JvmName("timeTimesGrainForce")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(grainForce: ScientificValue<MeasurementType.Force, GrainForce>) = grainForce * this
@JvmName("timeTimesKip")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(kip: ScientificValue<MeasurementType.Force, Kip>) = kip * this
@JvmName("timeTimesUsTonForce")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(usTonForce: ScientificValue<MeasurementType.Force, UsTonForce>) = usTonForce * this
@JvmName("timeTimesImperialTonForce")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(imperialTonForce: ScientificValue<MeasurementType.Force, ImperialTonForce>) = imperialTonForce * this
@JvmName("timeTimesImperialForce")
infix operator fun <ForceUnit : ImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("timeTimesUKImperialForce")
infix operator fun <ForceUnit : UKImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("timeTimesUSCustomaryForce")
infix operator fun <ForceUnit : USCustomaryForce, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("timeTimesForce")
infix operator fun <ForceUnit : Force, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
