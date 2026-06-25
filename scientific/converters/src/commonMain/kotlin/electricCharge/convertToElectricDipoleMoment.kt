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
import com.splendo.kaluga.scientific.converter.electricDipoleMoment.electricDipoleMoment
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("electricChargeTimesMetricLength")
infix operator fun <ChargeUnit : ElectricCharge, LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit x length.unit).electricDipoleMoment(this, length)

@JvmName("electricChargeTimesImperialLength")
infix operator fun <ChargeUnit : ElectricCharge, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit x length.unit).electricDipoleMoment(this, length)

@JvmName("electricChargeTimesLength")
infix operator fun <ChargeUnit : ElectricCharge, LengthUnit : Length> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit x Meter).electricDipoleMoment(this, length)
