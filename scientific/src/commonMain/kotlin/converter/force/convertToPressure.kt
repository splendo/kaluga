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
import com.splendo.kaluga.scientific.converter.pressure.pressure
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.KipSquareFoot
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("dyneDivSquareCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.Force, Dyne>.div(area: ScientificValue<PhysicalQuantity.Area, SquareCentimeter>) = Barye.pressure(this, area)

@JvmName("dyneMultipleDivSquareCentimeter")
infix operator fun <DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Force, DyneUnit>.div(area: ScientificValue<PhysicalQuantity.Area, SquareCentimeter>) =
    Barye.pressure(this, area)

@JvmName("poundalDivSquareFoot")
infix operator fun ScientificValue<PhysicalQuantity.Force, Poundal>.div(area: ScientificValue<PhysicalQuantity.Area, SquareFoot>) = PoundSquareFoot.pressure(this, area)

@JvmName("poundalDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, Poundal>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    PoundSquareInch.pressure(this, area)

@JvmName("poundForceDivSquareFoot")
infix operator fun ScientificValue<PhysicalQuantity.Force, PoundForce>.div(area: ScientificValue<PhysicalQuantity.Area, SquareFoot>) = PoundSquareFoot.pressure(this, area)

@JvmName("poundForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, PoundForce>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    PoundSquareInch.pressure(this, area)

@JvmName("ounceForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, OunceForce>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    OunceSquareInch.pressure(this, area)

@JvmName("grainForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, GrainForce>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    OunceSquareInch.pressure(this, area)

@JvmName("kipDivSquareFoot")
infix operator fun ScientificValue<PhysicalQuantity.Force, Kip>.div(area: ScientificValue<PhysicalQuantity.Area, SquareFoot>) = KipSquareFoot.pressure(this, area)

@JvmName("kipDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, Kip>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    KipSquareInch.pressure(this, area)

@JvmName("usTonForceDivSquareFoot")
infix operator fun ScientificValue<PhysicalQuantity.Force, UsTonForce>.div(area: ScientificValue<PhysicalQuantity.Area, SquareFoot>) = USTonSquareFoot.pressure(this, area)

@JvmName("usTonForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, UsTonForce>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    USTonSquareInch.pressure(this, area)

@JvmName("imperialTonForceDivSquareFoot")
infix operator fun ScientificValue<PhysicalQuantity.Force, ImperialTonForce>.div(area: ScientificValue<PhysicalQuantity.Area, SquareFoot>) =
    ImperialTonSquareFoot.pressure(this, area)

@JvmName("imperialTonForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, ImperialTonForce>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    ImperialTonSquareInch.pressure(this, area)

@JvmName("imperialForceDivImperialArea")
infix operator fun <ForceUnit : ImperialForce, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = PoundSquareInch.pressure(this, area)

@JvmName("usCustomaryForceDivImperialArea")
infix operator fun <ForceUnit : USCustomaryForce, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = PoundSquareInch.usCustomary.pressure(this, area)

@JvmName("ukImperialForceDivImperialArea")
infix operator fun <ForceUnit : UKImperialForce, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = PoundSquareInch.ukImperial.pressure(this, area)

@JvmName("forceDivArea")
infix operator fun <ForceUnit : Force, AreaUnit : Area> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    Pascal.pressure(this, area)
