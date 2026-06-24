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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.surfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.ElectricFieldStrength
import com.splendo.kaluga.scientific.unit.ImperialElectricFieldStrength
import com.splendo.kaluga.scientific.unit.Permittivity
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("permittivityTimesImperialElectricFieldStrength")
infix operator fun <PermittivityUnit : Permittivity> ScientificValue<PhysicalQuantity.Permittivity, PermittivityUnit>.times(
    electricFieldStrength: ScientificValue<PhysicalQuantity.ElectricFieldStrength, ImperialElectricFieldStrength>,
) = (Coulomb per SquareFoot).surfaceChargeDensity(this, electricFieldStrength)

@JvmName("permittivityTimesElectricFieldStrength")
infix operator fun <PermittivityUnit, ElectricFieldStrengthUnit> ScientificValue<PhysicalQuantity.Permittivity, PermittivityUnit>.times(
    electricFieldStrength: ScientificValue<PhysicalQuantity.ElectricFieldStrength, ElectricFieldStrengthUnit>,
) where PermittivityUnit : Permittivity, ElectricFieldStrengthUnit : ElectricFieldStrength = (Coulomb per SquareMeter).surfaceChargeDensity(this, electricFieldStrength)
