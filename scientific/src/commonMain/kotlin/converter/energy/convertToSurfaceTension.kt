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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Centimeter
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.Foot
import com.splendo.kaluga.scientific.FootPoundal
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialEnergy
import com.splendo.kaluga.scientific.Inch
import com.splendo.kaluga.scientific.InchOunceForce
import com.splendo.kaluga.scientific.InchPoundForce
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricEnergy
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.Newton
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.Poundal
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.SquareFoot
import com.splendo.kaluga.scientific.SquareInch
import com.splendo.kaluga.scientific.converter.surfaceTension.surfaceTension
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("ergDivSquareCentimeter")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = (Dyne per Centimeter).surfaceTension(this, area)
@JvmName("ergMultipleDivSquareCentimeter")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = (Dyne per Centimeter).surfaceTension(this, area)
@JvmName("metricAndImperialEnergyDivMetricArea")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, AreaUnit : MetricArea> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (Newton per Meter).surfaceTension(this, area)
@JvmName("metricEnergyDivMetricArea")
infix operator fun <EnergyUnit : MetricEnergy, AreaUnit : MetricArea> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (Newton per Meter).surfaceTension(this, area)
@JvmName("footPoundalDivSquareFoot")
infix operator fun ScientificValue<MeasurementType.Energy, FootPoundal>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = (Poundal per Foot).surfaceTension(this, area)
@JvmName("inchPoundForceDivSquareInch")
infix operator fun ScientificValue<MeasurementType.Energy, InchPoundForce>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = (PoundForce per Inch).surfaceTension(this, area)
@JvmName("inchOunceForceDivSquareInch")
infix operator fun ScientificValue<MeasurementType.Energy, InchOunceForce>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = (OunceForce per Inch).surfaceTension(this, area)
@JvmName("metricAndImperialEnergyDivImperialArea")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (PoundForce per Foot).surfaceTension(this, area)
@JvmName("imperialEnergyDivImperialArea")
infix operator fun <EnergyUnit : ImperialEnergy, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (PoundForce per Foot).surfaceTension(this, area)
@JvmName("energyDivArea")
infix operator fun <EnergyUnit : Energy, AreaUnit : Area> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (Newton per Meter).surfaceTension(this, area)
