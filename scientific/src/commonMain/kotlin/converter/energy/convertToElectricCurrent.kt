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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCurrent.current
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.MagneticFlux
import com.splendo.kaluga.scientific.unit.Maxwell
import kotlin.jvm.JvmName

@JvmName("ergDivMaxwell")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(flux: ScientificValue<PhysicalQuantity.MagneticFlux, Maxwell>) = Abampere.current(this, flux)

@JvmName("ergMultipleDivMaxwell")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(flux: ScientificValue<PhysicalQuantity.MagneticFlux, Maxwell>) =
    Abampere.current(this, flux)

@JvmName("energyDivFlux")
infix operator fun <EnergyUnit : Energy, FluxUnit : MagneticFlux> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    flux: ScientificValue<PhysicalQuantity.MagneticFlux, FluxUnit>,
) = Ampere.current(this, flux)
