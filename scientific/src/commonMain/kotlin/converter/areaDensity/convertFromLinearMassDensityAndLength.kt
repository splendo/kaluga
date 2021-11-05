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
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import kotlin.jvm.JvmName

@JvmName("areaDensityFromLinearMassDensityAndLengthDefault")
fun <
    AreaDensityUnit : AreaDensity,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > AreaDensityUnit.areaDensity(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, LinearMassDensityUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = areaDensity(linearMassDensity, length, ::DefaultScientificValue)

@JvmName("areaDensityFromLinearMassDensityAndLength")
fun <
    AreaDensityUnit : AreaDensity,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity,
    Value : ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>
    > AreaDensityUnit.areaDensity(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, LinearMassDensityUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    factory: (Decimal, AreaDensityUnit) -> Value
) = byDividing(linearMassDensity, length, factory)
