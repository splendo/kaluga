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
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricSurfaceTension
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.SurfaceTension
import com.splendo.kaluga.scientific.unit.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.USCustomarySurfaceTension
import kotlin.jvm.JvmName

@JvmName("ergDivMetricSurfaceTension")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, MetricSurfaceTension>) =
    SquareCentimeter.area(this, surfaceTension)

@JvmName("ergMultipleDivMetricSurfaceTension")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, MetricSurfaceTension>,
) = SquareCentimeter.area(this, surfaceTension)

@JvmName("metricAndImperialEnergyDivMetricSurfaceTension")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, MetricSurfaceTension>,
) = SquareMeter.area(this, surfaceTension)

@JvmName("inchPoundForceDivImperialSurfaceTension")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchPoundForce>.div(surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>) =
    SquareInch.area(this, surfaceTension)

@JvmName("inchPoundForceDivUKImperialSurfaceTension")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchPoundForce>.div(surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>) =
    SquareInch.area(this, surfaceTension)

@JvmName("inchPoundForceDivUSCustomarySurfaceTension")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchPoundForce>.div(surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>) =
    SquareInch.area(this, surfaceTension)

@JvmName("inchOunceForceDivImperialSurfaceTension")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchOunceForce>.div(surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>) =
    SquareInch.area(this, surfaceTension)

@JvmName("inchOunceForceDivUKImperialSurfaceTension")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchOunceForce>.div(surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>) =
    SquareInch.area(this, surfaceTension)

@JvmName("inchOunceForceDivUSCustomarySurfaceTension")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchOunceForce>.div(surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>) =
    SquareInch.area(this, surfaceTension)

@JvmName("metricAndImperialEnergyDivImperialSurfaceTension")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>,
) = SquareFoot.area(this, surfaceTension)

@JvmName("metricAndImperialEnergyDivUKImperialSurfaceTension")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>,
) = SquareFoot.area(this, surfaceTension)

@JvmName("metricAndImperialEnergyDivUSCustomarySurfaceTension")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>,
) = SquareFoot.area(this, surfaceTension)

@JvmName("imperialEnergyDivImperialSurfaceTension")
infix operator fun <EnergyUnit : ImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>,
) = SquareFoot.area(this, surfaceTension)

@JvmName("imperialEnergyDivUKImperialSurfaceTension")
infix operator fun <EnergyUnit : ImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>,
) = SquareFoot.area(this, surfaceTension)

@JvmName("imperialEnergyDivUSCustomarySurfaceTension")
infix operator fun <EnergyUnit : ImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>,
) = SquareFoot.area(this, surfaceTension)

@JvmName("energyDivSurfaceTension")
infix operator fun <EnergyUnit : Energy, SurfaceTensionUnit : SurfaceTension> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, SurfaceTensionUnit>,
) = SquareMeter.area(this, surfaceTension)
