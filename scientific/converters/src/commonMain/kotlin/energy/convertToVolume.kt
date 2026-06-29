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
import com.splendo.kaluga.scientific.converter.volume.volume
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("ergDivBarye")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, Barye>) = CubicCentimeter.volume(this, pressure)

@JvmName("ergMultipleDivBarye")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, Barye>) =
    CubicCentimeter.volume(this, pressure)

@JvmName("ergDivBaryeMultiple")
infix operator fun <BaryeUnit : BaryeMultiple> ScientificValue<PhysicalQuantity.Energy, Erg>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, BaryeUnit>) =
    CubicCentimeter.volume(this, pressure)

@JvmName("ergMultipleDivBaryeMultiple")
infix operator fun <ErgUnit : ErgMultiple, BaryeUnit : BaryeMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, BaryeUnit>,
) = CubicCentimeter.volume(this, pressure)

@JvmName("footPoundalDivPoundSquareFoot")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundal>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareFoot>) =
    CubicFoot.volume(this, pressure)

@JvmName("inchPoundForceDivPoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchPoundForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>) =
    CubicInch.volume(this, pressure)

@JvmName("inchOunceForceDivPoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchOunceForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>) =
    CubicInch.volume(this, pressure)

@JvmName("imperialEnergyDivImperialPressure")
infix operator fun <EnergyUnit : ImperialEnergy, PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = CubicFoot.volume(this, pressure)

@JvmName("metricAndImperialEnergyDivImperialPressure")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = CubicFoot.volume(this, pressure)

@JvmName("imperialEnergyDivUKImperialPressure")
infix operator fun <EnergyUnit : ImperialEnergy, PressureUnit : UKImperialPressure> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = CubicFoot.ukImperial.volume(this, pressure)

@JvmName("metricAndImperialEnergyDivUKImperialPressure")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, PressureUnit : UKImperialPressure> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = CubicFoot.ukImperial.volume(this, pressure)

@JvmName("imperialEnergyDivUSCustomaryPressure")
infix operator fun <EnergyUnit : ImperialEnergy, PressureUnit : USCustomaryPressure> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = CubicFoot.usCustomary.volume(this, pressure)

@JvmName("metricAndImperialEnergyDivUSCustomaryPressure")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, PressureUnit : USCustomaryPressure> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = CubicFoot.usCustomary.volume(this, pressure)

@JvmName("energyDivPressure")
infix operator fun <EnergyUnit : Energy, PressureUnit : Pressure> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = CubicMeter.volume(this, pressure)
