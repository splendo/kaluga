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

package com.splendo.kaluga.scientific.converter.surfaceChargeDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricFieldStrength.electricFieldStrength
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.ImperialSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Permittivity
import com.splendo.kaluga.scientific.unit.SurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Volt
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("imperialSurfaceChargeDensityDivPermittivity")
infix operator fun <PermittivityUnit : Permittivity> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, ImperialSurfaceChargeDensity>.div(
    permittivity: ScientificValue<PhysicalQuantity.Permittivity, PermittivityUnit>,
) = (Volt per Foot).electricFieldStrength(this, permittivity)

@JvmName("surfaceChargeDensityDivPermittivity")
infix operator fun <SurfaceChargeDensityUnit, PermittivityUnit> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, SurfaceChargeDensityUnit>.div(
    permittivity: ScientificValue<PhysicalQuantity.Permittivity, PermittivityUnit>,
) where SurfaceChargeDensityUnit : SurfaceChargeDensity, PermittivityUnit : Permittivity = (Volt per Meter).electricFieldStrength(this, permittivity)
