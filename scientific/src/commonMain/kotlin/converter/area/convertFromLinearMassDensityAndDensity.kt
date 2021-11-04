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

package com.splendo.kaluga.scientific.converter.area

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Density
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.LinearMassDensity
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import kotlin.jvm.JvmName

@JvmName("areaFromLinearMassDensityAndDensityDefault")
fun <
    DensityUnit : Density,
    AreaUnit : Area,
    LinearMassDensityUnit : LinearMassDensity
> AreaUnit.area(
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    density: ScientificValue<MeasurementType.Density, DensityUnit>
) = area(linearMassDensity, density, ::DefaultScientificValue)

@JvmName("areaFromLinearMassDensityAndDensity")
fun <
    DensityUnit : Density,
    AreaUnit : Area,
    LinearMassDensityUnit : LinearMassDensity,
    Value : ScientificValue<MeasurementType.Area, AreaUnit>
> AreaUnit.area(
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
    factory: (Decimal, AreaUnit) -> Value
) = byDividing(linearMassDensity, density, factory)