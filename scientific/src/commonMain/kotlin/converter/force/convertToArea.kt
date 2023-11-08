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
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.KipSquareFoot
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.UsTonForce
import kotlin.jvm.JvmName

@JvmName("dyneDivBarye")
infix operator fun ScientificValue<PhysicalQuantity.Force, Dyne>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, Barye>) = SquareCentimeter.area(this, pressure)

@JvmName("dyneDivBaryeMultiple")
infix operator fun <BaryeUnit : BaryeMultiple> ScientificValue<PhysicalQuantity.Force, Dyne>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, BaryeUnit>) =
    SquareCentimeter.area(this, pressure)

@JvmName("dyneMultipleDivBarye")
infix operator fun <DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Force, DyneUnit>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, Barye>) =
    SquareCentimeter.area(this, pressure)

@JvmName("dyneMultipleDivBaryeMultiple")
infix operator fun <DyneUnit : DyneMultiple, BaryeUnit : BaryeMultiple> ScientificValue<PhysicalQuantity.Force, DyneUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, BaryeUnit>,
) = SquareCentimeter.area(this, pressure)

@JvmName("poundForcePoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Force, PoundForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>) = SquareInch.area(this, pressure)

@JvmName("poundForceDivPoundSquareFeet")
infix operator fun ScientificValue<PhysicalQuantity.Force, PoundForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareFoot>) = SquareFoot.area(this, pressure)

@JvmName("poundForceDivKiloPoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Force, PoundForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>) =
    SquareInch.area(this, pressure)

@JvmName("ounceForceDivOunceSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Force, OunceForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>) = SquareInch.area(this, pressure)

@JvmName("kipDivKipSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Force, Kip>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, KipSquareInch>) = SquareInch.area(this, pressure)

@JvmName("kipDivPoundSquareFeet")
infix operator fun ScientificValue<PhysicalQuantity.Force, Kip>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, KipSquareFoot>) = SquareFoot.area(this, pressure)

@JvmName("usTonForceDivUSTonSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Force, UsTonForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, USTonSquareInch>) = SquareInch.area(this, pressure)

@JvmName("usTonForceDivUSTonSquareFeet")
infix operator fun ScientificValue<PhysicalQuantity.Force, UsTonForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, USTonSquareFoot>) = SquareFoot.area(this, pressure)

@JvmName("imperialTonForceDivImperialTonSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Force, ImperialTonForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, ImperialTonSquareInch>) =
    SquareInch.area(this, pressure)

@JvmName("imperialTonForceDivImperialTonSquareFeet")
infix operator fun ScientificValue<PhysicalQuantity.Force, ImperialTonForce>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, ImperialTonSquareFoot>) =
    SquareFoot.area(this, pressure)

@JvmName("imperialForceDivImperialPressure")
infix operator fun <Force : ImperialForce, Pressure : ImperialPressure> ScientificValue<PhysicalQuantity.Force, Force>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = SquareFoot.area(this, pressure)

@JvmName("imperialForceDivUKImperialPressure")
infix operator fun <Force : ImperialForce, Pressure : UKImperialPressure> ScientificValue<PhysicalQuantity.Force, Force>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = SquareFoot.area(this, pressure)

@JvmName("imperialForceDivUSCustomaryPressure")
infix operator fun <Force : ImperialForce, Pressure : USCustomaryPressure> ScientificValue<PhysicalQuantity.Force, Force>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = SquareFoot.area(this, pressure)

@JvmName("ukImperialForceDivImperialPressure")
infix operator fun <Force : UKImperialForce, Pressure : ImperialPressure> ScientificValue<PhysicalQuantity.Force, Force>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = SquareFoot.area(this, pressure)

@JvmName("ukImperialForceDivUKImperialPressure")
infix operator fun <Force : UKImperialForce, Pressure : UKImperialPressure> ScientificValue<PhysicalQuantity.Force, Force>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = SquareFoot.area(this, pressure)

@JvmName("usCustomaryForceDivImperialPressure")
infix operator fun <Force : USCustomaryForce, Pressure : ImperialPressure> ScientificValue<PhysicalQuantity.Force, Force>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = SquareFoot.area(this, pressure)

@JvmName("usCustomaryForceDivUSCustomaryPressure")
infix operator fun <Force : USCustomaryForce, Pressure : USCustomaryPressure> ScientificValue<PhysicalQuantity.Force, Force>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = SquareFoot.area(this, pressure)

@JvmName("forceDivPressure")
infix operator fun <ForceUnit : Force, PressureUnit : Pressure> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = SquareMeter.area(this, pressure)
