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

package com.splendo.kaluga.scientific.converter.electricInductance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.permeability.permeability
import com.splendo.kaluga.scientific.unit.ElectricInductance
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("electricInductanceDivMetricLength")
infix operator fun <InductanceUnit : ElectricInductance, LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.ElectricInductance, InductanceUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).permeability(this, length)

@JvmName("electricInductanceDivImperialLength")
infix operator fun <InductanceUnit : ElectricInductance, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.ElectricInductance, InductanceUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).permeability(this, length)

@JvmName("electricInductanceDivLength")
infix operator fun <InductanceUnit : ElectricInductance, LengthUnit : Length> ScientificValue<PhysicalQuantity.ElectricInductance, InductanceUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per Meter).permeability(this, length)
