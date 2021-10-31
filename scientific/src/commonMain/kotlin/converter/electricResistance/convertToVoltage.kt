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

package com.splendo.kaluga.scientific.converter.electricResistance

import com.splendo.kaluga.scientific.Abampere
import com.splendo.kaluga.scientific.Abohm
import com.splendo.kaluga.scientific.Biot
import com.splendo.kaluga.scientific.ElectricCurrent
import com.splendo.kaluga.scientific.ElectricResistance
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import kotlin.jvm.JvmName

@JvmName("abohmTimesAbampere")
infix operator fun ScientificValue<MeasurementType.ElectricResistance, Abohm>.times(current: ScientificValue<MeasurementType.ElectricCurrent, Abampere>) = current * this
@JvmName("abohmTimesBiot")
infix operator fun ScientificValue<MeasurementType.ElectricResistance, Abohm>.times(current: ScientificValue<MeasurementType.ElectricCurrent, Biot>) = current * this
@JvmName("resistanceTimesCurrent")
infix operator fun <CurrentUnit : ElectricCurrent, ResistanceUnit : ElectricResistance> ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>.times(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = current * this
