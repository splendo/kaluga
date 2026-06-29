/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.volumetricFlow

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import kotlin.jvm.JvmName

@JvmName("metricVolumetricFlowTimesBarye")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, Barye>) = pressure * this

@JvmName("metricVolumetricFlowTimesBaryeMultiple")
infix operator fun <BaryeUnit : BaryeMultiple> ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, BaryeUnit>,
) = pressure * this

@JvmName("imperialVolumetricFlowTimesPoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>) =
    pressure * this

@JvmName("uKImperialVolumetricFlowTimesPoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>) =
    pressure * this

@JvmName("uSCustomaryVolumetricFlowTimesPoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>) =
    pressure * this

@JvmName("imperialVolumetricFlowTimesOunceSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>) =
    pressure * this

@JvmName("ukImperialVolumetricFlowTimesOunceSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>) =
    pressure * this

@JvmName("usCustomaryVolumetricFlowTimesOunceSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>) =
    pressure * this

@JvmName("imperialVolumetricFlowTimesKilopoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>) =
    pressure * this

@JvmName("ukImperialVolumetricFlowTimesKilopoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>) =
    pressure * this

@JvmName("usCustomaryVolumetricFlowTimesKilopoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>) =
    pressure * this

@JvmName("usCustomaryVolumetricFlowTimesKipSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, KipSquareInch>) =
    pressure * this

@JvmName("usCustomaryVolumetricFlowTimesUsTonSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, USTonSquareInch>) =
    pressure * this

@JvmName("ukImperialVolumetricFlowTimesImperialTonSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.times(pressure: ScientificValue<PhysicalQuantity.Pressure, ImperialTonSquareInch>) =
    pressure * this

@JvmName("imperialVolumetricFlowTimesImperialPressure")
infix operator fun <PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = pressure * this

@JvmName("uKImperialVolumetricFlowTimesImperialPressure")
infix operator fun <PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = pressure * this

@JvmName("uSCustomaryVolumetricFlowTimesImperialPressure")
infix operator fun <PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = pressure * this

@JvmName("imperialVolumetricFlowTimesUkImperialPressure")
infix operator fun <PressureUnit : UKImperialPressure> ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = pressure * this

@JvmName("uKImperialVolumetricFlowTimesUkImperialPressure")
infix operator fun <PressureUnit : UKImperialPressure> ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = pressure * this

@JvmName("imperialVolumetricFlowTimesUsCustomaryPressure")
infix operator fun <PressureUnit : USCustomaryPressure> ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = pressure * this

@JvmName("uSCustomaryVolumetricFlowTimesUsCustomaryPressure")
infix operator fun <PressureUnit : USCustomaryPressure> ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = pressure * this

@JvmName("volumeTimesPressure")
infix operator fun <PressureUnit : Pressure, VolumetricFlowUnit : VolumetricFlow> ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = pressure * this
