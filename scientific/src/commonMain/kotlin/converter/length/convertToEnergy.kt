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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import kotlin.jvm.JvmName

@JvmName("centimeterTimesDyne")
infix operator fun ScientificValue<PhysicalQuantity.Length, Centimeter>.times(force: ScientificValue<PhysicalQuantity.Force, Dyne>) = force * this

@JvmName("centimeterTimesDyneMultiple")
infix operator fun <DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Length, Centimeter>.times(force: ScientificValue<PhysicalQuantity.Force, DyneUnit>) = force * this

@JvmName("metricLengthTimesMetricForce")
infix operator fun <ForceUnit : MetricForce, LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = force * this

@JvmName("imperialLengthTimesPoundal")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(force: ScientificValue<PhysicalQuantity.Force, Poundal>) = force * this

@JvmName("inchTimesPoundForce")
infix operator fun ScientificValue<PhysicalQuantity.Length, Inch>.times(force: ScientificValue<PhysicalQuantity.Force, PoundForce>) = force * this

@JvmName("inchTimesOunceForce")
infix operator fun ScientificValue<PhysicalQuantity.Length, Inch>.times(force: ScientificValue<PhysicalQuantity.Force, OunceForce>) = force * this

@JvmName("imperialLengthTimesPoundForce")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(force: ScientificValue<PhysicalQuantity.Force, PoundForce>) =
    force * this

@JvmName("imperialLengthTimesImperialForce")
infix operator fun <ForceUnit : ImperialForce, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = force * this

@JvmName("imperialLengthTimesUKImperialForce")
infix operator fun <ForceUnit : UKImperialForce, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = force * this

@JvmName("imperialLengthTimesUSCustomaryForce")
infix operator fun <ForceUnit : USCustomaryForce, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = force * this

@JvmName("lengthTimesForce")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    force * this
