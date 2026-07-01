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
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.LinearChargeDensity
import kotlin.jvm.JvmName

@JvmName("lengthFromElectricChargeAndLinearChargeDensityDefault")
fun <
    ChargeUnit : ElectricCharge,
    LengthUnit : Length,
    LinearChargeDensityUnit : LinearChargeDensity,
    > LengthUnit.length(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
    linearChargeDensity: ScientificValue<PhysicalQuantity.LinearChargeDensity, LinearChargeDensityUnit>,
) = length(charge, linearChargeDensity, ::DefaultScientificValue)

@JvmName("lengthFromElectricChargeAndLinearChargeDensity")
fun <
    ChargeUnit : ElectricCharge,
    LengthUnit : Length,
    LinearChargeDensityUnit : LinearChargeDensity,
    Value : ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    > LengthUnit.length(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
    linearChargeDensity: ScientificValue<PhysicalQuantity.LinearChargeDensity, LinearChargeDensityUnit>,
    factory: (Decimal, LengthUnit) -> Value,
) = byDividing(charge, linearChargeDensity, factory)
