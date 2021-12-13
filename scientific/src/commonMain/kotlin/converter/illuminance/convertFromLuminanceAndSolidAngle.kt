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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.Luminance
import com.splendo.kaluga.scientific.unit.SolidAngle
import kotlin.jvm.JvmName

@JvmName("illuminanceFromLuminanceAndSolidAngleDefault")
fun <
    LuminanceUnit : Luminance,
    SolidAngleUnit : SolidAngle,
    IlluminanceUnit : Illuminance
    > IlluminanceUnit.illuminance(
    luminance: ScientificValue<PhysicalQuantity.Luminance, LuminanceUnit>,
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>
) = illuminance(luminance, solidAngle, ::DefaultScientificValue)

@JvmName("illuminanceFromLuminanceAndSolidAngle")
fun <
    LuminanceUnit : Luminance,
    SolidAngleUnit : SolidAngle,
    IlluminanceUnit : Illuminance,
    Value : ScientificValue<PhysicalQuantity.Illuminance, IlluminanceUnit>
    > IlluminanceUnit.illuminance(
    luminance: ScientificValue<PhysicalQuantity.Luminance, LuminanceUnit>,
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
    factory: (Decimal, IlluminanceUnit) -> Value
) = byMultiplying(luminance, solidAngle, factory)
