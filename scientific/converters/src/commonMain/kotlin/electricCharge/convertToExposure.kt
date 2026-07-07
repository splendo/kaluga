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

package com.splendo.kaluga.scientific.converter.electricCharge

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.exposure.exposure
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("electricChargeDivMetricWeight")
infix operator fun <ChargeUnit : ElectricCharge, WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (unit per weight.unit).exposure(this, weight)

@JvmName("electricChargeDivImperialWeight")
infix operator fun <ChargeUnit : ElectricCharge, WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (unit per weight.unit).exposure(this, weight)

@JvmName("electricChargeDivUKImperialWeight")
infix operator fun <ChargeUnit : ElectricCharge, WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (unit per weight.unit).exposure(this, weight)

@JvmName("electricChargeDivUSCustomaryWeight")
infix operator fun <ChargeUnit : ElectricCharge, WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (unit per weight.unit).exposure(this, weight)

@JvmName("electricChargeDivWeight")
infix operator fun <ChargeUnit : ElectricCharge, WeightUnit : Weight> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.div(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (unit per Kilogram).exposure(this, weight)
