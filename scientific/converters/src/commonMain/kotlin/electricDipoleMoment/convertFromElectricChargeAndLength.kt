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

package com.splendo.kaluga.scientific.converter.electricDipoleMoment

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricDipoleMoment
import com.splendo.kaluga.scientific.unit.Length
import kotlin.jvm.JvmName

@JvmName("electricDipoleMomentFromElectricChargeAndLengthDefault")
fun <
    ChargeUnit : ElectricCharge,
    LengthUnit : Length,
    ElectricDipoleMomentUnit : ElectricDipoleMoment,
    > ElectricDipoleMomentUnit.electricDipoleMoment(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = electricDipoleMoment(charge, length, ::DefaultScientificValue)

@JvmName("electricDipoleMomentFromElectricChargeAndLength")
fun <
    ChargeUnit : ElectricCharge,
    LengthUnit : Length,
    ElectricDipoleMomentUnit : ElectricDipoleMoment,
    Value : ScientificValue<PhysicalQuantity.ElectricDipoleMoment, ElectricDipoleMomentUnit>,
    > ElectricDipoleMomentUnit.electricDipoleMoment(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    factory: (Decimal, ElectricDipoleMomentUnit) -> Value,
) = byMultiplying(charge, length, factory)
