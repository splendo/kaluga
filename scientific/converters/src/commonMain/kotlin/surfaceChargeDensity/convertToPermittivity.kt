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
import com.splendo.kaluga.scientific.converter.permittivity.permittivity
import com.splendo.kaluga.scientific.unit.ElectricFieldStrength
import com.splendo.kaluga.scientific.unit.Farad
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.ImperialSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.SurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("imperialSurfaceChargeDensityDivElectricFieldStrength")
infix operator fun <ElectricFieldStrengthUnit : ElectricFieldStrength> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, ImperialSurfaceChargeDensity>.div(
    electricFieldStrength: ScientificValue<PhysicalQuantity.ElectricFieldStrength, ElectricFieldStrengthUnit>,
) = (Farad per Foot).permittivity(this, electricFieldStrength)

@JvmName("surfaceChargeDensityDivElectricFieldStrength")
infix operator fun <SurfaceChargeDensityUnit, ElectricFieldStrengthUnit> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, SurfaceChargeDensityUnit>.div(
    electricFieldStrength: ScientificValue<PhysicalQuantity.ElectricFieldStrength, ElectricFieldStrengthUnit>,
) where SurfaceChargeDensityUnit : SurfaceChargeDensity, ElectricFieldStrengthUnit : ElectricFieldStrength = (Farad per Meter).permittivity(this, electricFieldStrength)
