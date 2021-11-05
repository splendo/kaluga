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
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.Barye
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
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricPressure
import com.splendo.kaluga.scientific.unit.MetricVolume
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
infix operator fun ScientificValue<MeasurementType.Pressure, Barye>.times(volume: ScientificValue<MeasurementType.Volume, CubicCentimeter>) = Erg.energy(this, volume)
@JvmName("baryeMultipleTimesCubicCentimeter")
infix operator fun <BaryeUnit> ScientificValue<MeasurementType.Pressure, BaryeUnit>.times(volume: ScientificValue<MeasurementType.Volume, CubicCentimeter>) where BaryeUnit : Pressure, BaryeUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> = Erg.energy(this, volume)
@JvmName("metricPressureTimesMetricVolume")
infix operator fun <PressureUnit : MetricPressure, VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = Joule.energy(this, volume)
@JvmName("poundSquareFootTimesCubicFoot")
infix operator fun ScientificValue<MeasurementType.Pressure, PoundSquareFoot>.times(volume: ScientificValue<MeasurementType.Volume, CubicFoot>) = FootPoundForce.energy(this, volume)
@JvmName("poundSquareInchTimesCubicInch")
infix operator fun ScientificValue<MeasurementType.Pressure, PoundSquareInch>.times(volume: ScientificValue<MeasurementType.Volume, CubicInch>) = InchPoundForce.energy(this, volume)
@JvmName("ounceSquareInchTimesCubicInch")
infix operator fun ScientificValue<MeasurementType.Pressure, OunceSquareInch>.times(volume: ScientificValue<MeasurementType.Volume, CubicInch>) = InchOunceForce.energy(this, volume)
@JvmName("kilopoundSquareInchTimesCubicInch")
infix operator fun ScientificValue<MeasurementType.Pressure, KiloPoundSquareInch>.times(volume: ScientificValue<MeasurementType.Volume, CubicInch>) = InchPoundForce.energy(this, volume)
@JvmName("kipSquareInchTimesCubicInch")
infix operator fun ScientificValue<MeasurementType.Pressure, KipSquareInch>.times(volume: ScientificValue<MeasurementType.Volume, CubicInch>) = InchPoundForce.energy(this, volume)
@JvmName("usTonSquareInchTimesCubicInch")
infix operator fun ScientificValue<MeasurementType.Pressure, USTonSquareInch>.times(volume: ScientificValue<MeasurementType.Volume, CubicInch>) = InchPoundForce.energy(this, volume)
@JvmName("imperialTonSquareInchTimesCubicInch")
infix operator fun ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>.times(volume: ScientificValue<MeasurementType.Volume, CubicInch>) = InchPoundForce.energy(this, volume)
@JvmName("imperialPressureTimesImperialVolume")
infix operator fun <PressureUnit : ImperialPressure, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = FootPoundForce.energy(this, volume)
@JvmName("imperialPressureTimesUKImperialVolume")
infix operator fun <PressureUnit : ImperialPressure, VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = FootPoundForce.energy(this, volume)
@JvmName("imperialPressureTimesUSCustomaryVolume")
infix operator fun <PressureUnit : ImperialPressure, VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = FootPoundForce.energy(this, volume)
@JvmName("ukImperialPressureTimesImperialVolume")
infix operator fun <PressureUnit : UKImperialPressure, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = FootPoundForce.energy(this, volume)
@JvmName("ukImperialPressureTimesUKImperialVolume")
infix operator fun <PressureUnit : UKImperialPressure, VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = FootPoundForce.energy(this, volume)
@JvmName("ukImperialPressureTimesUSCustomaryVolume")
infix operator fun <PressureUnit : UKImperialPressure, VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = FootPoundForce.energy(this, volume)
@JvmName("usCustomaryPressureTimesImperialVolume")
infix operator fun <PressureUnit : USCustomaryPressure, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = FootPoundForce.energy(this, volume)
@JvmName("usCustomaryPressureTimesUKImperialVolume")
infix operator fun <PressureUnit : USCustomaryPressure, VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = FootPoundForce.energy(this, volume)
@JvmName("usCustomaryPressureTimesUSCustomaryVolume")
infix operator fun <PressureUnit : USCustomaryPressure, VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = FootPoundForce.energy(this, volume)
@JvmName("pressureTimesVolume")
infix operator fun <PressureUnit : Pressure, VolumeUnit : Volume> ScientificValue<MeasurementType.Pressure, PressureUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = Joule.energy(this, volume)
