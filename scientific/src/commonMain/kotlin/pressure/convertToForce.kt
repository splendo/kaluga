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

package com.splendo.kaluga.scientific.pressure

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Barye
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialPressure
import com.splendo.kaluga.scientific.ImperialTonForce
import com.splendo.kaluga.scientific.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.ImperialTonSquareInch
import com.splendo.kaluga.scientific.Kip
import com.splendo.kaluga.scientific.KipSquareFoot
import com.splendo.kaluga.scientific.KipSquareInch
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.MetricPressure
import com.splendo.kaluga.scientific.Newton
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.OunceSquareInch
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.Pressure
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.UKImperialPressure
import com.splendo.kaluga.scientific.USCustomaryPressure
import com.splendo.kaluga.scientific.USTonSquareFoot
import com.splendo.kaluga.scientific.USTonSquareInch
import com.splendo.kaluga.scientific.UsTonForce
import com.splendo.kaluga.scientific.force.force
import com.splendo.kaluga.scientific.ukImperial
import com.splendo.kaluga.scientific.usCustomary
import kotlin.jvm.JvmName

@JvmName("baryeTimesSquareCentimeter")
operator fun ScientificValue<MeasurementType.Pressure, Barye>.times(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Dyne.force(this, area)
@JvmName("baryeMultipleTimesSquareCentimeter")
operator fun <BaryeUnit> ScientificValue<MeasurementType.Pressure, BaryeUnit>.times(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) where BaryeUnit : Pressure, BaryeUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> = Dyne.force(this, area)
@JvmName("metricPressureTimesMetricArea")
operator fun <Pressure : MetricPressure, Area : MetricArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(area: ScientificValue<MeasurementType.Area, Area>) = Newton.force(this, area)
@JvmName("ounceSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, OunceSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = OunceForce.force(this, area)
@JvmName("kipSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KipSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = Kip.force(this, area)
@JvmName("kipSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KipSquareFoot>.times(area: ScientificValue<MeasurementType.Area, Area>) = Kip.force(this, area)
@JvmName("usTonSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, USTonSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = UsTonForce.force(this, area)
@JvmName("usTonSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, USTonSquareFoot>.times(area: ScientificValue<MeasurementType.Area, Area>) = UsTonForce.force(this, area)
@JvmName("imperialTonSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = ImperialTonForce.force(this, area)
@JvmName("imperialTonSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, ImperialTonSquareFoot>.times(area: ScientificValue<MeasurementType.Area, Area>) = ImperialTonForce.force(this, area)
@JvmName("imperialPressureTimesImperialArea")
operator fun <Pressure : ImperialPressure, Area : ImperialArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("ukImperialPressureTimesImperialArea")
operator fun <Pressure : UKImperialPressure, Area : ImperialArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.ukImperial.force(this, area)
@JvmName("usCustomaryPressureTimesImperialArea")
operator fun <Pressure : USCustomaryPressure, Area : ImperialArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.usCustomary.force(this, area)
@JvmName("pressureTimesArea")
operator fun <PressureUnit : Pressure, AreaUnit : Area> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Newton.force(this, area)
