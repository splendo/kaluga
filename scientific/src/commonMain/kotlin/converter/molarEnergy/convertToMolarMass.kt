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

package com.splendo.kaluga.scientific.converter.molarEnergy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarMass.molarMass
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.MolarEnergy
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("molarEnergyDivMetricSpecificEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("molarEnergyDivImperialSpecificEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("molarEnergyDivUKImperialSpecificEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("molarEnergyDivUSCustomarySpecificEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("molarEnergyDivSpecificEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy, SpecificEnergyUnit : SpecificEnergy> ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>
) = (Kilogram per unit.per).molarMass(this, specificEnergy)
