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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.surfaceTension.surfaceTension
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricForceDivMetricLength")
infix operator fun <ForceUnit : MetricForce, LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).surfaceTension(this, length)

@JvmName("imperialForceDivImperialLength")
infix operator fun <ForceUnit : ImperialForce, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).surfaceTension(this, length)

@JvmName("ukImperialForceDivImperialLength")
infix operator fun <ForceUnit : UKImperialForce, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).surfaceTension(this, length)

@JvmName("usCustomaryForceDivImperialLength")
infix operator fun <ForceUnit : USCustomaryForce, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).surfaceTension(this, length)

@JvmName("forceDivLength")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(length: ScientificValue<PhysicalQuantity.Length, LengthUnit>) =
    (Newton per Meter).surfaceTension(this, length)
