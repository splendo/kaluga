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

package com.splendo.kaluga.scientific.converter.magneticFlux

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCurrent.current
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abhenry
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.ElectricInductance
import com.splendo.kaluga.scientific.unit.MagneticFlux
import com.splendo.kaluga.scientific.unit.Maxwell
import kotlin.jvm.JvmName

@JvmName("maxwellDivAbhenry")
infix operator fun ScientificValue<PhysicalQuantity.MagneticFlux, Maxwell>.div(inductance: ScientificValue<PhysicalQuantity.ElectricInductance, Abhenry>) =
    Abampere.current(this, inductance)

@JvmName("fluxDivInductance")
infix operator fun <FluxUnit : MagneticFlux, InductanceUnit : ElectricInductance> ScientificValue<PhysicalQuantity.MagneticFlux, FluxUnit>.div(
    inductance: ScientificValue<PhysicalQuantity.ElectricInductance, InductanceUnit>
) = Ampere.current(this, inductance)
