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

package com.splendo.kaluga.scientific.converter.surfaceTension

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.SurfaceTension
import kotlin.jvm.JvmName

@JvmName("surfaceTensionFromForceAndLengthDefault")
fun <
    ForceUnit : Force,
    LengthUnit : Length,
    SurfaceTensionUnit : SurfaceTension,
    > SurfaceTensionUnit.surfaceTension(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = surfaceTension(force, length, ::DefaultScientificValue)

@JvmName("surfaceTensionFromForceAndLength")
fun <
    ForceUnit : Force,
    LengthUnit : Length,
    SurfaceTensionUnit : SurfaceTension,
    Value : ScientificValue<PhysicalQuantity.SurfaceTension, SurfaceTensionUnit>,
    > SurfaceTensionUnit.surfaceTension(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    factory: (Decimal, SurfaceTensionUnit) -> Value,
) = byDividing(force, length, factory)
