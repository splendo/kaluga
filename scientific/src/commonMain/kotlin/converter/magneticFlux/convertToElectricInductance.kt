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

package com.splendo.kaluga.scientific.converter.magneticFlux

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricInductance.inductance
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abhenry
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Henry
import com.splendo.kaluga.scientific.unit.MagneticFlux
import com.splendo.kaluga.scientific.unit.Maxwell
import kotlin.jvm.JvmName

@JvmName("maxwellDivAbampere")
infix operator fun ScientificValue<PhysicalQuantity.MagneticFlux, Maxwell>.div(current: ScientificValue<PhysicalQuantity.ElectricCurrent, Abampere>) =
    Abhenry.inductance(this, current)

@JvmName("maxwellDivBiot")
infix operator fun ScientificValue<PhysicalQuantity.MagneticFlux, Maxwell>.div(current: ScientificValue<PhysicalQuantity.ElectricCurrent, Biot>) = Abhenry.inductance(this, current)

@JvmName("fluxDivCurrent")
infix operator fun <FluxUnit : MagneticFlux, CurrentUnit : ElectricCurrent> ScientificValue<PhysicalQuantity.MagneticFlux, FluxUnit>.div(
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
) = Henry.inductance(this, current)
