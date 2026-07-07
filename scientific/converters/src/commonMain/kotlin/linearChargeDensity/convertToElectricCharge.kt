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

package com.splendo.kaluga.scientific.converter.linearChargeDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCharge.charge
import com.splendo.kaluga.scientific.unit.ImperialLinearChargeDensity
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.LinearChargeDensity
import com.splendo.kaluga.scientific.unit.MetricLinearChargeDensity
import com.splendo.kaluga.scientific.unit.Coulomb
import kotlin.jvm.JvmName

@JvmName("metricLinearChargeDensityTimesLength")
infix operator fun <LengthUnit : Length> ScientificValue<PhysicalQuantity.LinearChargeDensity, MetricLinearChargeDensity>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = unit.charge.charge(this, length)

@JvmName("imperialLinearChargeDensityTimesLength")
infix operator fun <LengthUnit : Length> ScientificValue<PhysicalQuantity.LinearChargeDensity, ImperialLinearChargeDensity>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = unit.charge.charge(this, length)

@JvmName("linearChargeDensityTimesLength")
infix operator fun <LinearChargeDensityUnit : LinearChargeDensity, LengthUnit : Length> ScientificValue<PhysicalQuantity.LinearChargeDensity, LinearChargeDensityUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = Coulomb.charge(this, length)
