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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import kotlin.jvm.JvmName

@JvmName("densityFromLinearMassDensityAndAreaDefault")
fun <
    DensityUnit : Density,
    AreaUnit : Area,
    LinearMassDensityUnit : LinearMassDensity
> DensityUnit.density(
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = density(linearMassDensity, area, ::DefaultScientificValue)

@JvmName("densityFromLinearMassDensityAndArea")
fun <
    DensityUnit : Density,
    AreaUnit : Area,
    LinearMassDensityUnit : LinearMassDensity,
    Value : ScientificValue<MeasurementType.Density, DensityUnit>
> DensityUnit.density(
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>,
    factory: (Decimal, DensityUnit) -> Value
) = byDividing(linearMassDensity, area, factory)
