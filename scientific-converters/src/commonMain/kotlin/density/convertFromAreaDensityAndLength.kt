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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.Length
import kotlin.jvm.JvmName

@JvmName("densityFromAreaDensityAndLengthDefault")
fun <
    DensityUnit : Density,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity,
    > DensityUnit.density(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = density(areaDensity, length, ::DefaultScientificValue)

@JvmName("densityFromAreaDensityAndLength")
fun <
    DensityUnit : Density,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity,
    Value : ScientificValue<PhysicalQuantity.Density, DensityUnit>,
    > DensityUnit.density(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    factory: (Decimal, DensityUnit) -> Value,
) = byDividing(areaDensity, length, factory)
