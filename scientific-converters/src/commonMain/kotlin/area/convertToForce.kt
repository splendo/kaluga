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

package com.splendo.kaluga.scientific.converter.area

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.KipSquareFoot
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import kotlin.jvm.JvmName

@JvmName("squareCentimeterTimesBarye")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareCentimeter>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, Barye>) = pressure * this

@JvmName("squareCentimeterTimesBaryeMultiple")
infix operator fun <BaryeUnit : BaryeMultiple> ScientificValue<PhysicalQuantity.Area, SquareCentimeter>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, BaryeUnit>) =
    pressure * this

@JvmName("imperialAreaTimesOunceSquareInch")
infix operator fun <Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>) = pressure * this

@JvmName("imperialAreaTimesKipSquareInch")
infix operator fun <Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, KipSquareInch>) = pressure * this

@JvmName("imperialAreaTimesKipSquareFeet")
infix operator fun <Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, KipSquareFoot>) = pressure * this

@JvmName("imperialAreaTimesUsTonSquareInch")
infix operator fun <Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, USTonSquareInch>) = pressure * this

@JvmName("imperialAreaTimesUsTonSquareFeet")
infix operator fun <Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, USTonSquareFoot>) = pressure * this

@JvmName("imperialAreaTimesImperialTonSquareInch")
infix operator fun <Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, ImperialTonSquareInch>) =
    pressure * this

@JvmName("imperialAreaTimesImperialTonSquareFeet")
infix operator fun <Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, ImperialTonSquareFoot>) =
    pressure * this

@JvmName("imperialAreaTimesImperialPressure")
infix operator fun <Pressure : ImperialPressure, Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = pressure * this

@JvmName("imperialAreaTimesUKImperialPressure")
infix operator fun <Pressure : UKImperialPressure, Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = pressure * this

@JvmName("imperialAreaTimesUSCustomaryPressure")
infix operator fun <Pressure : USCustomaryPressure, Area : ImperialArea> ScientificValue<PhysicalQuantity.Area, Area>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, Pressure>,
) = pressure * this

@JvmName("areaTimesPressure")
infix operator fun <PressureUnit : Pressure, AreaUnit : Area> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = pressure * this
