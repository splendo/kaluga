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

package com.splendo.kaluga.scientific.converter.electricCurrentDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricChargeDensity.electricChargeDensity
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.ImperialElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("imperialElectricCurrentDensityDivSpeed")
infix operator fun <SpeedUnit : Speed> ScientificValue<PhysicalQuantity.ElectricCurrentDensity, ImperialElectricCurrentDensity>.div(
    speed: ScientificValue<PhysicalQuantity.Speed, SpeedUnit>,
) = (Coulomb per CubicFoot).electricChargeDensity(this, speed)

@JvmName("electricCurrentDensityDivSpeed")
infix operator fun <ElectricCurrentDensityUnit, SpeedUnit> ScientificValue<PhysicalQuantity.ElectricCurrentDensity, ElectricCurrentDensityUnit>.div(
    speed: ScientificValue<PhysicalQuantity.Speed, SpeedUnit>,
) where ElectricCurrentDensityUnit : ElectricCurrentDensity, SpeedUnit : Speed = (Coulomb per CubicMeter).electricChargeDensity(this, speed)
