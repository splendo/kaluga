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

package com.splendo.kaluga.scientific.converter.permittivity

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.ElectricFieldStrength
import com.splendo.kaluga.scientific.unit.Permittivity
import com.splendo.kaluga.scientific.unit.SurfaceChargeDensity
import kotlin.jvm.JvmName

@JvmName("permittivityFromSurfaceChargeDensityAndElectricFieldStrengthDefault")
fun <
    PermittivityUnit : Permittivity,
    SurfaceChargeDensityUnit : SurfaceChargeDensity,
    ElectricFieldStrengthUnit : ElectricFieldStrength,
    > PermittivityUnit.permittivity(
    surfaceChargeDensity: ScientificValue<PhysicalQuantity.SurfaceChargeDensity, SurfaceChargeDensityUnit>,
    electricFieldStrength: ScientificValue<PhysicalQuantity.ElectricFieldStrength, ElectricFieldStrengthUnit>,
) = permittivity(surfaceChargeDensity, electricFieldStrength, ::DefaultScientificValue)

@JvmName("permittivityFromSurfaceChargeDensityAndElectricFieldStrength")
fun <
    PermittivityUnit : Permittivity,
    SurfaceChargeDensityUnit : SurfaceChargeDensity,
    ElectricFieldStrengthUnit : ElectricFieldStrength,
    Value : ScientificValue<PhysicalQuantity.Permittivity, PermittivityUnit>,
    > PermittivityUnit.permittivity(
    surfaceChargeDensity: ScientificValue<PhysicalQuantity.SurfaceChargeDensity, SurfaceChargeDensityUnit>,
    electricFieldStrength: ScientificValue<PhysicalQuantity.ElectricFieldStrength, ElectricFieldStrengthUnit>,
    factory: (Decimal, PermittivityUnit) -> Value,
) = byDividing(surfaceChargeDensity, electricFieldStrength, factory)
