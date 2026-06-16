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

package com.splendo.kaluga.scientific.converter.power

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.volumetricFlow.volumetricFlow
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialCombinedPower
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.MetricCombinedPower
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricCombinedPowerDivBarye")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricCombinedPower>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, Barye>) =
    (CubicCentimeter per unit.per).volumetricFlow(this, pressure)

@JvmName("metricCombinedPowerDivBaryeMultiple")
infix operator fun <BaryeUnit : BaryeMultiple> ScientificValue<PhysicalQuantity.Power, MetricCombinedPower>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, BaryeUnit>) = (
    CubicCentimeter per
        unit.per
    ).volumetricFlow(this, pressure)

@JvmName("imperialCombinedPowerDivPoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>) = (
    CubicInch per
        unit.per
    ).volumetricFlow(this, pressure)

@JvmName("imperialCombinedPowerDivOunceSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>) = (
    CubicInch per
        unit.per
    ).volumetricFlow(this, pressure)

@JvmName("imperialCombinedPowerDivKiloPoundSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>) = (
    CubicInch per
        unit.per
    ).volumetricFlow(this, pressure)

@JvmName("imperialCombinedPowerDivKipSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, KipSquareInch>) =
    (CubicInch per unit.per).usCustomary.volumetricFlow(this, pressure)

@JvmName("imperialCombinedPowerDivUsTonSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, USTonSquareInch>) = (
    CubicInch per
        unit.per
    ).usCustomary.volumetricFlow(this, pressure)

@JvmName("imperialCombinedPowerDivImperialTonSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, ImperialTonSquareInch>) = (
    CubicInch per
        unit.per
    ).ukImperial.volumetricFlow(this, pressure)

@JvmName("imperialCombinedPowerDivImperialPressure")
infix operator fun <PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = (
    CubicFoot per
        unit.per
    ).volumetricFlow(this, pressure)

@JvmName("imperialCombinedPowerDivUKImperialPressure")
infix operator fun <PressureUnit : UKImperialPressure> ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = (
    CubicFoot per
        unit.per
    ).ukImperial.volumetricFlow(this, pressure)

@JvmName("imperialCombinedPowerDivUsCustomaryPressure")
infix operator fun <PressureUnit : USCustomaryPressure> ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = (
    CubicFoot per
        unit.per
    ).usCustomary.volumetricFlow(this, pressure)

@JvmName("imperialPowerDivPoundSquareInch")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>) = (
    CubicInch per
        Second
    ).volumetricFlow(this, pressure)

@JvmName("imperialPowerDivOunceSquareInch")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>) = (
    CubicInch per
        Second
    ).volumetricFlow(this, pressure)

@JvmName("imperialPowerDivKiloPoundSquareInch")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>) = (
    CubicInch per
        Second
    ).volumetricFlow(this, pressure)

@JvmName("imperialPowerDivKipSquareInch")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, KipSquareInch>) = (
    CubicInch per
        Second
    ).usCustomary.volumetricFlow(this, pressure)

@JvmName("imperialPowerDivUsTonSquareInch")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, USTonSquareInch>) = (
    CubicInch per
        Second
    ).usCustomary.volumetricFlow(this, pressure)

@JvmName("imperialPowerDivImperialTonSquareInch")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(pressure: ScientificValue<PhysicalQuantity.Pressure, ImperialTonSquareInch>) =
    (
        CubicInch per
            Second
        ).ukImperial.volumetricFlow(this, pressure)

@JvmName("imperialPowerDivImperialPressure")
infix operator fun <PowerUnit : ImperialPower, PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = (
    CubicFoot per
        Second
    ).volumetricFlow(this, pressure)

@JvmName("imperialPowerDivUKImperialPressure")
infix operator fun <PowerUnit : ImperialPower, PressureUnit : UKImperialPressure> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = (
    CubicFoot per
        Second
    ).ukImperial.volumetricFlow(this, pressure)

@JvmName("imperialPowerDivUsCustomaryPressure")
infix operator fun <PowerUnit : ImperialPower, PressureUnit : USCustomaryPressure> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = (
    CubicFoot per
        Second
    ).usCustomary.volumetricFlow(this, pressure)

@JvmName("powerDivPressure")
infix operator fun <PowerUnit : Power, PressureUnit : Pressure> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
) = (
    CubicMeter per
        Second
    ).volumetricFlow(this, pressure)
