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

package com.splendo.kaluga.scientific.converter.area

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.KipSquareFoot
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricPressure
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import kotlin.jvm.JvmName

@JvmName("squareCentimeterTimesBarye")
infix operator fun ScientificValue<MeasurementType.Area, SquareCentimeter>.times(pressure: ScientificValue<MeasurementType.Pressure, Barye>) =
    pressure * this

@JvmName("squareCentimeterTimesBaryeMultiple")
infix operator fun <BaryeUnit> ScientificValue<MeasurementType.Area, SquareCentimeter>.times(
    pressure: ScientificValue<MeasurementType.Pressure, BaryeUnit>
) where BaryeUnit : Pressure, BaryeUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> =
    pressure * this

@JvmName("metricAreaTimesMetricPressure")
infix operator fun <Pressure : MetricPressure, Area : MetricArea> ScientificValue<MeasurementType.Area, Area>.times(
    pressure: ScientificValue<MeasurementType.Pressure, Pressure>
) = pressure * this

@JvmName("imperialAreaTimesOunceSquareInch")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, OunceSquareInch>) =
    pressure * this

@JvmName("imperialAreaTimesKipSquareInch")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, KipSquareInch>) =
    pressure * this

@JvmName("imperialAreaTimesKipSquareFeet")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, KipSquareFoot>) =
    pressure * this

@JvmName("imperialAreaTimesUsTonSquareInch")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareInch>) =
    pressure * this

@JvmName("imperialAreaTimesUsTonSquareFeet")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareFoot>) =
    pressure * this

@JvmName("imperialAreaTimesImperialTonSquareInch")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>) =
    pressure * this

@JvmName("imperialAreaTimesImperialTonSquareFeet")
infix operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareFoot>) =
    pressure * this

@JvmName("imperialAreaTimesImperialPressure")
infix operator fun <Pressure : ImperialPressure, Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(
    pressure: ScientificValue<MeasurementType.Pressure, Pressure>
) = pressure * this

@JvmName("imperialAreaTimesUKImperialPressure")
infix operator fun <Pressure : UKImperialPressure, Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(
    pressure: ScientificValue<MeasurementType.Pressure, Pressure>
) = pressure * this

@JvmName("imperialAreaTimesUSCustomaryPressure")
infix operator fun <Pressure : USCustomaryPressure, Area : ImperialArea> ScientificValue<MeasurementType.Area, Area>.times(
    pressure: ScientificValue<MeasurementType.Pressure, Pressure>
) = pressure * this

@JvmName("areaTimesPressure")
infix operator fun <PressureUnit : Pressure, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this
