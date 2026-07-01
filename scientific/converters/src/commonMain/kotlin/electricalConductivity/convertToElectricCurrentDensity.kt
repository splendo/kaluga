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

package com.splendo.kaluga.scientific.converter.electricalConductivity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCurrentDensity.electricCurrentDensity
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.ElectricFieldStrength
import com.splendo.kaluga.scientific.unit.ElectricalConductivity
import com.splendo.kaluga.scientific.unit.ImperialElectricFieldStrength
import com.splendo.kaluga.scientific.unit.ImperialElectricalConductivity
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("imperialElectricalConductivityTimesImperialElectricFieldStrength")
infix operator fun ScientificValue<PhysicalQuantity.ElectricalConductivity, ImperialElectricalConductivity>.times(
    electricFieldStrength: ScientificValue<PhysicalQuantity.ElectricFieldStrength, ImperialElectricFieldStrength>,
) = (Ampere per SquareFoot).electricCurrentDensity(this, electricFieldStrength)

@JvmName("electricalConductivityTimesElectricFieldStrength")
infix operator fun <ConductivityUnit, FieldStrengthUnit> ScientificValue<PhysicalQuantity.ElectricalConductivity, ConductivityUnit>.times(
    electricFieldStrength: ScientificValue<PhysicalQuantity.ElectricFieldStrength, FieldStrengthUnit>,
) where ConductivityUnit : ElectricalConductivity, FieldStrengthUnit : ElectricFieldStrength = (Ampere per SquareMeter).electricCurrentDensity(this, electricFieldStrength)
