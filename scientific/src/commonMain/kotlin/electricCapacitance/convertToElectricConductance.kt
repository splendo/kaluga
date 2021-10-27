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

package com.splendo.kaluga.scientific.electricCapacitance

import com.splendo.kaluga.scientific.Abfarad
import com.splendo.kaluga.scientific.Absiemens
import com.splendo.kaluga.scientific.ElectricCapacitance
import com.splendo.kaluga.scientific.Frequency
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Siemens
import com.splendo.kaluga.scientific.electricConductance.conductance
import kotlin.jvm.JvmName

@JvmName("abfaradTimesFrequency")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<MeasurementType.ElectricCapacitance, Abfarad>.times(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>) = Absiemens.conductance(this, frequency)
@JvmName("capacitanceTimesFrequency")
infix operator fun <CapacitanceUnit : ElectricCapacitance, FrequencyUnit : Frequency> ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>.times(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>) = Siemens.conductance(this, frequency)
