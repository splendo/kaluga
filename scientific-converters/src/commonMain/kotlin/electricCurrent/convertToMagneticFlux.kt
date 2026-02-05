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

package com.splendo.kaluga.scientific.converter.electricCurrent

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricInductance.times
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abhenry
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.ElectricInductance
import kotlin.jvm.JvmName

@JvmName("abampereTimesAbhenry")
infix operator fun ScientificValue<PhysicalQuantity.ElectricCurrent, Abampere>.times(inductance: ScientificValue<PhysicalQuantity.ElectricInductance, Abhenry>) = inductance * this

@JvmName("biotTimesAbhenry")
infix operator fun ScientificValue<PhysicalQuantity.ElectricCurrent, Biot>.times(inductance: ScientificValue<PhysicalQuantity.ElectricInductance, Abhenry>) = inductance * this

@JvmName("currentTimesInductance")
infix operator fun <InductanceUnit : ElectricInductance, CurrentUnit : ElectricCurrent> ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>.times(
    inductance: ScientificValue<PhysicalQuantity.ElectricInductance, InductanceUnit>,
) = inductance * this
