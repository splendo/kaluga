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

package com.splendo.kaluga.scientific.converter.illuminance

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.luminance.luminance
import com.splendo.kaluga.scientific.unit.FootLambert
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.ImperialIlluminance
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricIlluminance
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.Nit
import com.splendo.kaluga.scientific.unit.Phot
import com.splendo.kaluga.scientific.unit.SolidAngle
import com.splendo.kaluga.scientific.unit.Stilb
import kotlin.jvm.JvmName

@JvmName("photDivSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, Phot>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Stilb.luminance(this, solidAngle)
@JvmName("photMultipleDivSolidAngle")
infix operator fun <PhotUnit, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, PhotUnit>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) where PhotUnit : Illuminance, PhotUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> = Stilb.luminance(this, solidAngle)
@JvmName("metricIlluminanceDivSolidAngle")
infix operator fun <IlluminanceUnit : MetricIlluminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Nit.luminance(this, solidAngle)
@JvmName("imperialIlluminanceDivSolidAngle")
infix operator fun <IlluminanceUnit : ImperialIlluminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = FootLambert.luminance(this, solidAngle)
@JvmName("illuminanceDivSolidAngle")
infix operator fun <IlluminanceUnit : Illuminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Nit.luminance(this, solidAngle)
