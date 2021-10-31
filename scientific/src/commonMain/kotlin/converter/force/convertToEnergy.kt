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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.Centimeter
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.FootPoundForce
import com.splendo.kaluga.scientific.FootPoundal
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.Inch
import com.splendo.kaluga.scientific.InchOunceForce
import com.splendo.kaluga.scientific.InchPoundForce
import com.splendo.kaluga.scientific.Joule
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
import com.splendo.kaluga.scientific.converter.energy.energy
import kotlin.jvm.JvmName

@JvmName("dyneTimesCentimeter")
infix operator fun ScientificValue<MeasurementType.Force, Dyne>.times(distance: ScientificValue<MeasurementType.Length, Centimeter>) = Erg.energy(this, distance)
@JvmName("dyneMultipleTimesCentimeter")
infix operator fun <DyneUnit> ScientificValue<MeasurementType.Force, DyneUnit>.times(distance: ScientificValue<MeasurementType.Length, Centimeter>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Erg.energy(this, distance)
@JvmName("metricForceTimesMetricLength")
infix operator fun <ForceUnit : MetricForce, LengthUnit : MetricLength> ScientificValue<MeasurementType.Force, ForceUnit>.times(distance: ScientificValue<MeasurementType.Length, LengthUnit>) = Joule.energy(this, distance)
@JvmName("poundalTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Force, Poundal>.times(distance: ScientificValue<MeasurementType.Length, LengthUnit>) = FootPoundal.energy(this, distance)
@JvmName("poundForceTimesInch")
infix operator fun ScientificValue<MeasurementType.Force, PoundForce>.times(distance: ScientificValue<MeasurementType.Length, Inch>) = InchPoundForce.energy(this, distance)
@JvmName("ounceForceTimesInch")
infix operator fun ScientificValue<MeasurementType.Force, OunceForce>.times(distance: ScientificValue<MeasurementType.Length, Inch>) = InchOunceForce.energy(this, distance)
@JvmName("poundForceTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Force, PoundForce>.times(distance: ScientificValue<MeasurementType.Length, LengthUnit>) = FootPoundForce.energy(this, distance)
@JvmName("imperialForceTimesImperialLength")
infix operator fun <ForceUnit : ImperialForce, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Force, ForceUnit>.times(distance: ScientificValue<MeasurementType.Length, LengthUnit>) = FootPoundForce.energy(this, distance)
@JvmName("ukImperialForceTimesImperialLength")
infix operator fun <ForceUnit : UKImperialForce, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Force, ForceUnit>.times(distance: ScientificValue<MeasurementType.Length, LengthUnit>) = FootPoundForce.energy(this, distance)
@JvmName("usCustomaryForceTimesImperialLength")
infix operator fun <ForceUnit : USCustomaryForce, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Force, ForceUnit>.times(distance: ScientificValue<MeasurementType.Length, LengthUnit>) = FootPoundForce.energy(this, distance)
@JvmName("forceTimesLength")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<MeasurementType.Force, ForceUnit>.times(distance: ScientificValue<MeasurementType.Length, LengthUnit>) = Joule.energy(this, distance)
