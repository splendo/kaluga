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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.SurfaceTension
import kotlin.jvm.JvmName

@JvmName("forceFromSurfaceTensionAndLengthDefault")
fun <
    ForceUnit : Force,
    LengthUnit : Length,
    SurfaceTensionUnit : SurfaceTension
    > ForceUnit.force(
    surfaceTension: ScientificValue<MeasurementType.SurfaceTension, SurfaceTensionUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = force(surfaceTension, length, ::DefaultScientificValue)

@JvmName("forceFromSurfaceTensionAndLength")
fun <
    ForceUnit : Force,
    LengthUnit : Length,
    SurfaceTensionUnit : SurfaceTension,
    Value : ScientificValue<MeasurementType.Force, ForceUnit>
    > ForceUnit.force(
    surfaceTension: ScientificValue<MeasurementType.SurfaceTension, SurfaceTensionUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    factory: (Decimal, ForceUnit) -> Value
) = byMultiplying(surfaceTension, length, factory)
