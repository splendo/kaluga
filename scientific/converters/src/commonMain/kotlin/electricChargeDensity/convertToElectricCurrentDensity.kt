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

package com.splendo.kaluga.scientific.converter.electricChargeDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCurrentDensity.electricCurrentDensity
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.ElectricChargeDensity
import com.splendo.kaluga.scientific.unit.ImperialElectricChargeDensity
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.UKImperialElectricChargeDensity
import com.splendo.kaluga.scientific.unit.USCustomaryElectricChargeDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("imperialElectricChargeDensityTimesImperialSpeed")
infix operator fun ScientificValue<PhysicalQuantity.ElectricChargeDensity, ImperialElectricChargeDensity>.times(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) =
    (Ampere per SquareFoot).electricCurrentDensity(this, speed)

@JvmName("ukImperialElectricChargeDensityTimesImperialSpeed")
infix operator fun ScientificValue<PhysicalQuantity.ElectricChargeDensity, UKImperialElectricChargeDensity>.times(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) =
    (Ampere per SquareFoot).electricCurrentDensity(this, speed)

@JvmName("usCustomaryElectricChargeDensityTimesImperialSpeed")
infix operator fun ScientificValue<PhysicalQuantity.ElectricChargeDensity, USCustomaryElectricChargeDensity>.times(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) =
    (Ampere per SquareFoot).electricCurrentDensity(this, speed)

@JvmName("electricChargeDensityTimesSpeed")
infix operator fun <ElectricChargeDensityUnit : ElectricChargeDensity, SpeedUnit : Speed> ScientificValue<PhysicalQuantity.ElectricChargeDensity, ElectricChargeDensityUnit>.times(
    speed: ScientificValue<PhysicalQuantity.Speed, SpeedUnit>,
) = (Ampere per SquareMeter).electricCurrentDensity(this, speed)
