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

package com.splendo.kaluga.scientific.converter.surfaceChargeDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.time.time
import com.splendo.kaluga.scientific.unit.ElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SurfaceChargeDensity
import kotlin.jvm.JvmName

@JvmName("surfaceChargeDensityDivElectricCurrentDensity")
infix operator fun <SurfaceChargeDensityUnit, ElectricCurrentDensityUnit> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, SurfaceChargeDensityUnit>.div(
    electricCurrentDensity: ScientificValue<PhysicalQuantity.ElectricCurrentDensity, ElectricCurrentDensityUnit>,
) where SurfaceChargeDensityUnit : SurfaceChargeDensity, ElectricCurrentDensityUnit : ElectricCurrentDensity = Second.time(this, electricCurrentDensity)
