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

package com.splendo.kaluga.scientific.converter.electricResistance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.resistivity.resistivity
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("electricResistanceTimesMetricLength")
infix operator fun <ResistanceUnit : ElectricResistance, LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit x length.unit).resistivity(this, length)

@JvmName("electricResistanceTimesImperialLength")
infix operator fun <ResistanceUnit : ElectricResistance, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit x length.unit).resistivity(this, length)

@JvmName("electricResistanceTimesLength")
infix operator fun <ResistanceUnit : ElectricResistance, LengthUnit : Length> ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit x Meter).resistivity(this, length)
