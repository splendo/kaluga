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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.MagneticFlux
import kotlin.jvm.JvmName

@JvmName("fluxFromEnergyAndCurrentDefault")
fun <
    EnergyUnit : Energy,
    CurrentUnit : ElectricCurrent,
    FluxUnit : MagneticFlux,
    > FluxUnit.flux(
    energy: ScientificValue<PhysicalQuantity.Energy, EnergyUnit>,
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
) = flux(energy, current, ::DefaultScientificValue)

@JvmName("fluxFromEnergyAndCurrent")
fun <
    EnergyUnit : Energy,
    CurrentUnit : ElectricCurrent,
    FluxUnit : MagneticFlux,
    Value : ScientificValue<PhysicalQuantity.MagneticFlux, FluxUnit>,
    > FluxUnit.flux(
    energy: ScientificValue<PhysicalQuantity.Energy, EnergyUnit>,
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
    factory: (Decimal, FluxUnit) -> Value,
) = byDividing(energy, current, factory)
