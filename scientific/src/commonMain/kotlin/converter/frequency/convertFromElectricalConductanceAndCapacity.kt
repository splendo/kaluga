/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.frequency

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.ElectricCapacitance
import com.splendo.kaluga.scientific.unit.ElectricConductance
import com.splendo.kaluga.scientific.unit.Frequency
import kotlin.jvm.JvmName

@JvmName("frequencyFromConductanceAndCapacityDefault")
fun <
    CapacitanceUnit : ElectricCapacitance,
    ConductanceUnit : ElectricConductance,
    FrequencyUnit : Frequency
    > FrequencyUnit.frequency(
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>,
    capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>
) = frequency(conductance, capacitance, ::DefaultScientificValue)

@JvmName("frequencyFromConductanceAndCapacity")
fun <
    CapacitanceUnit : ElectricCapacitance,
    ConductanceUnit : ElectricConductance,
    FrequencyUnit : Frequency,
    Value : ScientificValue<MeasurementType.Frequency, FrequencyUnit>
    > FrequencyUnit.frequency(
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>,
    capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>,
    factory: (Decimal, FrequencyUnit) -> Value
) = byDividing(conductance, capacitance, factory)
