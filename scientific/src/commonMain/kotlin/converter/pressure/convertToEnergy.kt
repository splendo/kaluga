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

package com.splendo.kaluga.scientific.converter.pressure

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.Volume
import kotlin.jvm.JvmName

@JvmName("baryeTimesCubicCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, Barye>.times(volume: ScientificValue<PhysicalQuantity.Volume, CubicCentimeter>) = Erg.energy(this, volume)

@JvmName("baryeMultipleTimesCubicCentimeter")
infix operator fun <BaryeUnit : BaryeMultiple> ScientificValue<PhysicalQuantity.Pressure, BaryeUnit>.times(volume: ScientificValue<PhysicalQuantity.Volume, CubicCentimeter>) =
    Erg.energy(this, volume)

@JvmName("poundSquareFootTimesCubicFoot")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, PoundSquareFoot>.times(volume: ScientificValue<PhysicalQuantity.Volume, CubicFoot>) =
    FootPoundForce.energy(this, volume)

@JvmName("poundSquareInchTimesCubicInch")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>.times(volume: ScientificValue<PhysicalQuantity.Volume, CubicInch>) =
    InchPoundForce.energy(this, volume)

@JvmName("ounceSquareInchTimesCubicInch")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>.times(volume: ScientificValue<PhysicalQuantity.Volume, CubicInch>) =
    InchOunceForce.energy(this, volume)

@JvmName("kilopoundSquareInchTimesCubicInch")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>.times(volume: ScientificValue<PhysicalQuantity.Volume, CubicInch>) =
    InchPoundForce.energy(this, volume)

@JvmName("kipSquareInchTimesCubicInch")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, KipSquareInch>.times(volume: ScientificValue<PhysicalQuantity.Volume, CubicInch>) =
    InchPoundForce.energy(this, volume)

@JvmName("usTonSquareInchTimesCubicInch")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, USTonSquareInch>.times(volume: ScientificValue<PhysicalQuantity.Volume, CubicInch>) =
    InchPoundForce.energy(this, volume)

@JvmName("imperialTonSquareInchTimesCubicInch")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, ImperialTonSquareInch>.times(volume: ScientificValue<PhysicalQuantity.Volume, CubicInch>) =
    InchPoundForce.energy(this, volume)

@JvmName("imperialPressureTimesImperialVolume")
infix operator fun <PressureUnit : ImperialPressure, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = FootPoundForce.energy(this, volume)

@JvmName("imperialPressureTimesUKImperialVolume")
infix operator fun <PressureUnit : ImperialPressure, VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = FootPoundForce.energy(this, volume)

@JvmName("imperialPressureTimesUSCustomaryVolume")
infix operator fun <PressureUnit : ImperialPressure, VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = FootPoundForce.energy(this, volume)

@JvmName("ukImperialPressureTimesImperialVolume")
infix operator fun <PressureUnit : UKImperialPressure, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = FootPoundForce.energy(this, volume)

@JvmName("ukImperialPressureTimesUKImperialVolume")
infix operator fun <PressureUnit : UKImperialPressure, VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = FootPoundForce.energy(this, volume)

@JvmName("usCustomaryPressureTimesImperialVolume")
infix operator fun <PressureUnit : USCustomaryPressure, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = FootPoundForce.energy(this, volume)

@JvmName("usCustomaryPressureTimesUSCustomaryVolume")
infix operator fun <PressureUnit : USCustomaryPressure, VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = FootPoundForce.energy(this, volume)

@JvmName("pressureTimesVolume")
infix operator fun <PressureUnit : Pressure, VolumeUnit : Volume> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = Joule.energy(this, volume)
