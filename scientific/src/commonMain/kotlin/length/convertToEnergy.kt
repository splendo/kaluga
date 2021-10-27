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

package com.splendo.kaluga.scientific.length

import com.splendo.kaluga.scientific.Centimeter
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.Inch
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.Poundal
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.force.times
import kotlin.jvm.JvmName

@JvmName("centimeterTimesDyne")
infix operator fun ScientificValue<MeasurementType.Length, Centimeter>.times(force: ScientificValue<MeasurementType.Force, Dyne>) = force * this
@JvmName("centimeterTimesDyneMultiple")
infix operator fun <DyneUnit> ScientificValue<MeasurementType.Length, Centimeter>.times(force: ScientificValue<MeasurementType.Force, DyneUnit>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = force * this
@JvmName("metricLengthTimesMetricForce")
infix operator fun <ForceUnit : MetricForce, LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("imperialLengthTimesPoundal")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(force: ScientificValue<MeasurementType.Force, Poundal>) = force * this
@JvmName("inchTimesPoundForce")
infix operator fun ScientificValue<MeasurementType.Length, Inch>.times(force: ScientificValue<MeasurementType.Force, PoundForce>) = force * this
@JvmName("inchTimesOunceForce")
infix operator fun ScientificValue<MeasurementType.Length, Inch>.times(force: ScientificValue<MeasurementType.Force, OunceForce>) = force * this
@JvmName("imperialLengthTimesPoundForce")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(force: ScientificValue<MeasurementType.Force, PoundForce>) = force * this
@JvmName("imperialLengthTimesImperialForce")
infix operator fun <ForceUnit : ImperialForce, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("imperialLengthTimesUKImperialForce")
infix operator fun <ForceUnit : UKImperialForce, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("imperialLengthTimesUSCustomaryForce")
infix operator fun <ForceUnit : USCustomaryForce, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
@JvmName("lengthTimesForce")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(force: ScientificValue<MeasurementType.Force, ForceUnit>) = force * this
