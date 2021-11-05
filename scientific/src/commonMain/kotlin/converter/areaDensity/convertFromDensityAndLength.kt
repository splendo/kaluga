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

package com.splendo.kaluga.scientific.converter.areaDensity

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.Length
import kotlin.jvm.JvmName

@JvmName("areaDensityFromDensityAndLengthDefault")
fun <
    DensityUnit : Density,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity
> AreaDensityUnit.areaDensity(
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = areaDensity(density, length, ::DefaultScientificValue)

@JvmName("areaDensityFromDensityAndLength")
fun <
    DensityUnit : Density,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity,
    Value : ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>
> AreaDensityUnit.areaDensity(
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    factory: (Decimal, AreaDensityUnit) -> Value
) = byMultiplying(density, length, factory)
