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

package com.splendo.kaluga.scientific.force

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Barye
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.GrainForce
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialTonForce
import com.splendo.kaluga.scientific.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.ImperialTonSquareInch
import com.splendo.kaluga.scientific.Kip
import com.splendo.kaluga.scientific.KipSquareFoot
import com.splendo.kaluga.scientific.KipSquareInch
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.OunceSquareInch
import com.splendo.kaluga.scientific.Pascal
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.PoundSquareFoot
import com.splendo.kaluga.scientific.PoundSquareInch
import com.splendo.kaluga.scientific.Poundal
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareFoot
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.USTonSquareFoot
import com.splendo.kaluga.scientific.USTonSquareInch
import com.splendo.kaluga.scientific.UsTonForce
import com.splendo.kaluga.scientific.pressure.pressure
import com.splendo.kaluga.scientific.ukImperial
import com.splendo.kaluga.scientific.usCustomary
import kotlin.jvm.JvmName

@JvmName("dyneDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Force, Dyne>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Barye.pressure(this, area)
@JvmName("dyneMultipleDivMetricArea")
infix operator fun <DyneUnit, AreaUnit : MetricArea> ScientificValue<MeasurementType.Force, DyneUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Barye.pressure(this, area)
@JvmName("metricForceDivMetricArea")
infix operator fun <ForceUnit : MetricForce, AreaUnit : MetricArea> ScientificValue<MeasurementType.Force, ForceUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Pascal.pressure(this, area)
@JvmName("poundalDivSquareFoot")
infix operator fun ScientificValue<MeasurementType.Force, Poundal>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = PoundSquareFoot.pressure(this, area)
@JvmName("poundalDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, Poundal>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = PoundSquareInch.pressure(this, area)
@JvmName("poundForceDivSquareFoot")
infix operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = PoundSquareFoot.pressure(this, area)
@JvmName("poundForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, PoundForce>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = PoundSquareInch.pressure(this, area)
@JvmName("ounceForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, OunceForce>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = OunceSquareInch.pressure(this, area)
@JvmName("grainForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, GrainForce>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = OunceSquareInch.pressure(this, area)
@JvmName("kipDivSquareFoot")
infix operator fun ScientificValue<MeasurementType.Force, Kip>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = KipSquareFoot.pressure(this, area)
@JvmName("kipDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, Kip>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = KipSquareInch.pressure(this, area)
@JvmName("usTonForceDivSquareFoot")
infix operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = USTonSquareFoot.pressure(this, area)
@JvmName("usTonForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, UsTonForce>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = USTonSquareInch.pressure(this, area)
@JvmName("imperialTonForceDivSquareFoot")
infix operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = ImperialTonSquareFoot.pressure(this, area)
@JvmName("imperialTonForceDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, ImperialTonForce>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = ImperialTonSquareInch.pressure(this, area)
@JvmName("imperialForceDivImperialArea")
infix operator fun <ForceUnit : ImperialForce, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, ForceUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = PoundSquareInch.pressure(this, area)
@JvmName("usCustomaryForceDivImperialArea")
infix operator fun <ForceUnit : USCustomaryForce, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, ForceUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = PoundSquareInch.usCustomary.pressure(this, area)
@JvmName("ukImperialForceDivImperialArea")
infix operator fun <ForceUnit : UKImperialForce, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Force, ForceUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = PoundSquareInch.ukImperial.pressure(this, area)
@JvmName("forceDivArea")
infix operator fun <ForceUnit : Force, AreaUnit : Area> ScientificValue<MeasurementType.Force, ForceUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Pascal.pressure(this, area)
