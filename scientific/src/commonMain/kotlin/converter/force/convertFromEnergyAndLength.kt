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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Length
import kotlin.jvm.JvmName

@JvmName("forceFromEnergyAndLengthDefault")
fun <
    EnergyUnit : Energy,
    LengthUnit : Length,
    ForceUnit : Force
> ForceUnit.force(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = force(energy, length, ::DefaultScientificValue)

@JvmName("forceFromEnergyAndLength")
fun <
    EnergyUnit : Energy,
    LengthUnit : Length,
    ForceUnit : Force,
    Value : ScientificValue<MeasurementType.Force, ForceUnit>
> ForceUnit.force(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    factory: (Decimal, ForceUnit) -> Value
) = byDividing(energy, length, factory)