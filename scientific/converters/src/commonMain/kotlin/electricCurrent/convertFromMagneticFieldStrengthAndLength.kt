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

package com.splendo.kaluga.scientific.converter.electricCurrent

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MagneticFieldStrength
import kotlin.jvm.JvmName

@JvmName("currentFromMagneticFieldStrengthAndLengthDefault")
fun <
    CurrentUnit : ElectricCurrent,
    LengthUnit : Length,
    MagneticFieldStrengthUnit : MagneticFieldStrength,
    > CurrentUnit.current(
    magneticFieldStrength: ScientificValue<PhysicalQuantity.MagneticFieldStrength, MagneticFieldStrengthUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = current(magneticFieldStrength, length, ::DefaultScientificValue)

@JvmName("currentFromMagneticFieldStrengthAndLength")
fun <
    CurrentUnit : ElectricCurrent,
    LengthUnit : Length,
    MagneticFieldStrengthUnit : MagneticFieldStrength,
    Value : ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
    > CurrentUnit.current(
    magneticFieldStrength: ScientificValue<PhysicalQuantity.MagneticFieldStrength, MagneticFieldStrengthUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    factory: (Decimal, CurrentUnit) -> Value,
) = byMultiplying(magneticFieldStrength, length, factory)
