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

package com.splendo.kaluga.scientific.converter.time

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCapacitance.capacitance
import com.splendo.kaluga.scientific.unit.Abfarad
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Farad
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("timeDivAbohm")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.div(resistance: ScientificValue<MeasurementType.ElectricResistance, Abohm>) =
    Abfarad.capacitance(this, resistance)

@JvmName("timeDivResistance")
infix operator fun <TimeUnit : Time, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.Time, TimeUnit>.div(
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>
) = Farad.capacitance(this, resistance)
