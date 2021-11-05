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

package com.splendo.kaluga.scientific.converter.pressure

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.force
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.KipSquareFoot
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricPressure
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("baryeTimesSquareCentimeter")
infix operator fun ScientificValue<MeasurementType.Pressure, Barye>.times(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) =
    Dyne.force(this, area)

@JvmName("baryeMultipleTimesSquareCentimeter")
infix operator fun <BaryeUnit> ScientificValue<MeasurementType.Pressure, BaryeUnit>.times(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) where BaryeUnit : Pressure, BaryeUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> =
    Dyne.force(this, area)

@JvmName("metricPressureTimesMetricArea")
infix operator fun <Pressure : MetricPressure, Area : MetricArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = Newton.force(this, area)

@JvmName("ounceSquareInchTimesImperialArea")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, OunceSquareInch>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = OunceForce.force(this, area)

@JvmName("kipSquareInchTimesImperialArea")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KipSquareInch>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = Kip.force(this, area)

@JvmName("kipSquareFeetTimesImperialArea")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KipSquareFoot>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = Kip.force(this, area)

@JvmName("usTonSquareInchTimesImperialArea")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, USTonSquareInch>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = UsTonForce.force(this, area)

@JvmName("usTonSquareFeetTimesImperialArea")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, USTonSquareFoot>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = UsTonForce.force(this, area)

@JvmName("imperialTonSquareInchTimesImperialArea")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = ImperialTonForce.force(this, area)

@JvmName("imperialTonSquareFeetTimesImperialArea")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, ImperialTonSquareFoot>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = ImperialTonForce.force(this, area)

@JvmName("imperialPressureTimesImperialArea")
infix operator fun <Pressure : ImperialPressure, Area : ImperialArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = PoundForce.force(this, area)

@JvmName("ukImperialPressureTimesImperialArea")
infix operator fun <Pressure : UKImperialPressure, Area : ImperialArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = PoundForce.ukImperial.force(this, area)

@JvmName("usCustomaryPressureTimesImperialArea")
infix operator fun <Pressure : USCustomaryPressure, Area : ImperialArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(
    area: ScientificValue<MeasurementType.Area, Area>
) = PoundForce.usCustomary.force(this, area)

@JvmName("pressureTimesArea")
infix operator fun <PressureUnit : Pressure, AreaUnit : Area> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = Newton.force(this, area)
