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

package com.splendo.kaluga.scientific.converter.solidAngle

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.Luminance
import com.splendo.kaluga.scientific.unit.SolidAngle
import kotlin.jvm.JvmName

@JvmName("solidAngleFromIlluminanceAndLuminanceDefault")
fun <
    LuminanceUnit : Luminance,
    SolidAngleUnit : SolidAngle,
    IlluminanceUnit : Illuminance
> SolidAngleUnit.solidAngle(
    illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>,
    luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>
) = solidAngle(illuminance, luminance, ::DefaultScientificValue)

@JvmName("solidAngleFromIlluminanceAndLuminance")
fun <
    LuminanceUnit : Luminance,
    SolidAngleUnit : SolidAngle,
    IlluminanceUnit : Illuminance,
    Value : ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>
> SolidAngleUnit.solidAngle(
    illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>,
    luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>,
    factory: (Decimal, SolidAngleUnit) -> Value
) = byDividing(illuminance, luminance, factory)
