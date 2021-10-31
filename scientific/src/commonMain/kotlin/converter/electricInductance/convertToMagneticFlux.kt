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

package com.splendo.kaluga.scientific.converter.electricInductance

import com.splendo.kaluga.scientific.Abampere
import com.splendo.kaluga.scientific.Abhenry
import com.splendo.kaluga.scientific.Biot
import com.splendo.kaluga.scientific.ElectricCurrent
import com.splendo.kaluga.scientific.ElectricInductance
import com.splendo.kaluga.scientific.Maxwell
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Weber
import com.splendo.kaluga.scientific.converter.magneticFlux.flux
import kotlin.jvm.JvmName

@JvmName("abhenryTimesAbampere")
infix operator fun ScientificValue<MeasurementType.ElectricInductance, Abhenry>.times(current: ScientificValue<MeasurementType.ElectricCurrent, Abampere>) = Maxwell.flux(this, current)
@JvmName("abhenryTimesBiot")
infix operator fun ScientificValue<MeasurementType.ElectricInductance, Abhenry>.times(current: ScientificValue<MeasurementType.ElectricCurrent, Biot>) = Maxwell.flux(this, current)
@JvmName("inductanceTimesCurrent")
infix operator fun <InductanceUnit : ElectricInductance, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>.times(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Weber.flux(this, current)
