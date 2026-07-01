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

package com.splendo.kaluga.scientific.converter.magneticFieldStrength

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.length.length
import com.splendo.kaluga.scientific.unit.ElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.ImperialElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.ImperialMagneticFieldStrength
import com.splendo.kaluga.scientific.unit.MagneticFieldStrength
import com.splendo.kaluga.scientific.unit.Meter
import kotlin.jvm.JvmName

@JvmName("imperialMagneticFieldStrengthDivImperialElectricCurrentDensity")
infix operator fun ScientificValue<PhysicalQuantity.MagneticFieldStrength, ImperialMagneticFieldStrength>.div(
    electricCurrentDensity: ScientificValue<PhysicalQuantity.ElectricCurrentDensity, ImperialElectricCurrentDensity>,
) = Foot.length(this, electricCurrentDensity)

@JvmName("magneticFieldStrengthDivElectricCurrentDensity")
infix operator fun <MagneticFieldStrengthUnit, ElectricCurrentDensityUnit> ScientificValue<PhysicalQuantity.MagneticFieldStrength, MagneticFieldStrengthUnit>.div(
    electricCurrentDensity: ScientificValue<PhysicalQuantity.ElectricCurrentDensity, ElectricCurrentDensityUnit>,
) where MagneticFieldStrengthUnit : MagneticFieldStrength, ElectricCurrentDensityUnit : ElectricCurrentDensity = Meter.length(this, electricCurrentDensity)
