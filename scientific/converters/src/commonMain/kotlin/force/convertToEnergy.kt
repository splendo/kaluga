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
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import kotlin.jvm.JvmName

@JvmName("dyneTimesCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.Force, Dyne>.times(distance: ScientificValue<PhysicalQuantity.Length, Centimeter>) = Erg.energy(this, distance)

@JvmName("dyneMultipleTimesCentimeter")
infix operator fun <DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Force, DyneUnit>.times(distance: ScientificValue<PhysicalQuantity.Length, Centimeter>) =
    Erg.energy(this, distance)

@JvmName("metricForceTimesMetricLength")
infix operator fun <ForceUnit : MetricForce, LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = Joule.energy(this, distance)

@JvmName("poundalTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Force, Poundal>.times(distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>) =
    FootPoundal.energy(this, distance)

@JvmName("poundForceTimesInch")
infix operator fun ScientificValue<PhysicalQuantity.Force, PoundForce>.times(distance: ScientificValue<PhysicalQuantity.Length, Inch>) = InchPoundForce.energy(this, distance)

@JvmName("ounceForceTimesInch")
infix operator fun ScientificValue<PhysicalQuantity.Force, OunceForce>.times(distance: ScientificValue<PhysicalQuantity.Length, Inch>) = InchOunceForce.energy(this, distance)

@JvmName("poundForceTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Force, PoundForce>.times(distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>) =
    FootPoundForce.energy(this, distance)

@JvmName("imperialForceTimesImperialLength")
infix operator fun <ForceUnit : ImperialForce, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = FootPoundForce.energy(this, distance)

@JvmName("ukImperialForceTimesImperialLength")
infix operator fun <ForceUnit : UKImperialForce, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = FootPoundForce.energy(this, distance)

@JvmName("usCustomaryForceTimesImperialLength")
infix operator fun <ForceUnit : USCustomaryForce, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = FootPoundForce.energy(this, distance)

@JvmName("forceTimesLength")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = Joule.energy(this, distance)
