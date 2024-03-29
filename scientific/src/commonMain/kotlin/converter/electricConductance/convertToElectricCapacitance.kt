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

package com.splendo.kaluga.scientific.converter.electricConductance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCapacitance.capacitance
import com.splendo.kaluga.scientific.unit.Abfarad
import com.splendo.kaluga.scientific.unit.Absiemens
import com.splendo.kaluga.scientific.unit.ElectricConductance
import com.splendo.kaluga.scientific.unit.Farad
import com.splendo.kaluga.scientific.unit.Frequency
import kotlin.jvm.JvmName

@JvmName("absiemensDivFrequency")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.ElectricConductance, Absiemens>.div(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = Abfarad.capacitance(this, frequency)

@JvmName("conductanceDivFrequency")
infix operator fun <ConductanceUnit : ElectricConductance, FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.ElectricConductance, ConductanceUnit>.div(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = Farad.capacitance(this, frequency)
