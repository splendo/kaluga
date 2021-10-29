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

import com.splendo.kaluga.scientific.Barye
import com.splendo.kaluga.scientific.FootOfWater
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialPressure
import com.splendo.kaluga.scientific.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.ImperialTonSquareInch
import com.splendo.kaluga.scientific.InchOfMercury
import com.splendo.kaluga.scientific.InchOfWater
import com.splendo.kaluga.scientific.KiloPoundSquareInch
import com.splendo.kaluga.scientific.KipSquareFoot
import com.splendo.kaluga.scientific.KipSquareInch
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.MetricPressure
import com.splendo.kaluga.scientific.OunceSquareInch
import com.splendo.kaluga.scientific.PoundSquareFoot
import com.splendo.kaluga.scientific.PoundSquareInch
import com.splendo.kaluga.scientific.Pressure
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.SquareFoot
import com.splendo.kaluga.scientific.SquareInch
import com.splendo.kaluga.scientific.SquareMeter
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.UKImperialPressure
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.USCustomaryPressure
import com.splendo.kaluga.scientific.USTonSquareFoot
import com.splendo.kaluga.scientific.USTonSquareInch
import com.splendo.kaluga.scientific.area.area
import kotlin.jvm.JvmName

@JvmName("metricForceDivBarye")
operator fun <Force : MetricForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Barye>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceDivBaryeMultiple")
operator fun <Force : MetricForce, B : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye>> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, B>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceDivMetricPressure")
operator fun <Force : MetricForce, Pressure : MetricPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareMeter.area(this, pressure)
@JvmName("imperialForcePoundSquareInch")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareInch>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivPoundSquareFeet")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareFoot>) = SquareFoot.area(this, pressure)
@JvmName("imperialForceDivKiloPoundSquareInch")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KiloPoundSquareInch>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivOunceSquareFeet")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, OunceSquareInch>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivInchOfMercury")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, InchOfMercury>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivInchOfWater")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, InchOfWater>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivFootOfWater")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, FootOfWater>) = SquareFoot.area(this, pressure)
@JvmName("usCustomaryForceDivKipSquareInch")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KipSquareInch>) = SquareInch.area(this, pressure)
@JvmName("usCustomaryForceDivPoundSquareFeet")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KipSquareFoot>) = SquareFoot.area(this, pressure)
@JvmName("usCustomaryForceDivUSTonSquareInch")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareInch>) = SquareInch.area(this, pressure)
@JvmName("usCustomaryForceDivUSTonSquareFeet")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareFoot>) = SquareFoot.area(this, pressure)
@JvmName("ukImperialForceDivImperialTonSquareInch")
operator fun <Force : UKImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>) = SquareInch.area(this, pressure)
@JvmName("ukImperialForceDivImperialTonSquareFeet")
operator fun <Force : UKImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareFoot>) = SquareFoot.area(this, pressure)
@JvmName("imperialForceDivImperialPressure")
operator fun <Force : ImperialForce, Pressure : ImperialPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("imperialForceDivUKImperialPressure")
operator fun <Force : ImperialForce, Pressure : UKImperialPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("imperialForceDivUSCustomaryPressure")
operator fun <Force : ImperialForce, Pressure : USCustomaryPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("ukImperialForceDivImperialPressure")
operator fun <Force : UKImperialForce, Pressure : ImperialPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("ukImperialForceDivUKImperialPressure")
operator fun <Force : UKImperialForce, Pressure : UKImperialPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("usCustomaryForceDivImperialPressure")
operator fun <Force : USCustomaryForce, Pressure : ImperialPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("usCustomaryForceDivUSCustomaryPressure")
operator fun <Force : USCustomaryForce, Pressure : USCustomaryPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("forceDivPressure")
operator fun <ForceUnit : Force, PressureUnit : Pressure> ScientificValue<MeasurementType.Force, ForceUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = SquareMeter.area(this, pressure)
