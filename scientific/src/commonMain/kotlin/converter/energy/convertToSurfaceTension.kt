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
import com.splendo.kaluga.scientific.converter.surfaceTension.surfaceTension
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("ergDivSquareCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(area: ScientificValue<PhysicalQuantity.Area, SquareCentimeter>) =
    (Dyne per Centimeter).surfaceTension(this, area)

@JvmName("ergMultipleDivSquareCentimeter")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(area: ScientificValue<PhysicalQuantity.Area, SquareCentimeter>) =
    (Dyne per Centimeter).surfaceTension(this, area)

@JvmName("metricAndImperialEnergyDivMetricArea")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (Newton per Meter).surfaceTension(this, area)

@JvmName("footPoundalDivSquareFoot")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundal>.div(area: ScientificValue<PhysicalQuantity.Area, SquareFoot>) =
    (Poundal per Foot).surfaceTension(this, area)

@JvmName("inchPoundForceDivSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchPoundForce>.div(area: ScientificValue<PhysicalQuantity.Area, SquareInch>) =
    (PoundForce per Inch).surfaceTension(this, area)

@JvmName("inchOunceForceDivSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchOunceForce>.div(area: ScientificValue<PhysicalQuantity.Area, SquareInch>) =
    (OunceForce per Inch).surfaceTension(this, area)

@JvmName("metricAndImperialEnergyDivImperialArea")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (PoundForce per Foot).surfaceTension(this, area)

@JvmName("imperialEnergyDivImperialArea")
infix operator fun <EnergyUnit : ImperialEnergy, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (PoundForce per Foot).surfaceTension(this, area)

@JvmName("energyDivArea")
infix operator fun <EnergyUnit : Energy, AreaUnit : Area> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (Newton per Meter).surfaceTension(this, area)
