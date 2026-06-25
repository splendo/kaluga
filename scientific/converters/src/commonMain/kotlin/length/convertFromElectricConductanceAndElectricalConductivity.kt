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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.ElectricConductance
import com.splendo.kaluga.scientific.unit.ElectricalConductivity
import com.splendo.kaluga.scientific.unit.Length
import kotlin.jvm.JvmName

@JvmName("lengthFromElectricConductanceAndElectricalConductivityDefault")
fun <
    ConductanceUnit : ElectricConductance,
    LengthUnit : Length,
    ElectricalConductivityUnit : ElectricalConductivity,
    > LengthUnit.length(
    conductance: ScientificValue<PhysicalQuantity.ElectricConductance, ConductanceUnit>,
    electricalConductivity: ScientificValue<PhysicalQuantity.ElectricalConductivity, ElectricalConductivityUnit>,
) = length(conductance, electricalConductivity, ::DefaultScientificValue)

@JvmName("lengthFromElectricConductanceAndElectricalConductivity")
fun <
    ConductanceUnit : ElectricConductance,
    LengthUnit : Length,
    ElectricalConductivityUnit : ElectricalConductivity,
    Value : ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    > LengthUnit.length(
    conductance: ScientificValue<PhysicalQuantity.ElectricConductance, ConductanceUnit>,
    electricalConductivity: ScientificValue<PhysicalQuantity.ElectricalConductivity, ElectricalConductivityUnit>,
    factory: (Decimal, LengthUnit) -> Value,
) = byDividing(conductance, electricalConductivity, factory)
