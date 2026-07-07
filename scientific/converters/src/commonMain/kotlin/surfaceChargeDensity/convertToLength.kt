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
import com.splendo.kaluga.scientific.converter.length.length
import com.splendo.kaluga.scientific.unit.ElectricChargeDensity
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.ImperialElectricChargeDensity
import com.splendo.kaluga.scientific.unit.ImperialSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.SurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.UKImperialElectricChargeDensity
import com.splendo.kaluga.scientific.unit.USCustomaryElectricChargeDensity
import kotlin.jvm.JvmName

@JvmName("imperialSurfaceChargeDensityDivImperialElectricChargeDensity")
infix operator fun ScientificValue<PhysicalQuantity.SurfaceChargeDensity, ImperialSurfaceChargeDensity>.div(
    electricChargeDensity: ScientificValue<PhysicalQuantity.ElectricChargeDensity, ImperialElectricChargeDensity>,
) = Foot.length(this, electricChargeDensity)

@JvmName("imperialSurfaceChargeDensityDivUKImperialElectricChargeDensity")
infix operator fun ScientificValue<PhysicalQuantity.SurfaceChargeDensity, ImperialSurfaceChargeDensity>.div(
    electricChargeDensity: ScientificValue<PhysicalQuantity.ElectricChargeDensity, UKImperialElectricChargeDensity>,
) = Foot.length(this, electricChargeDensity)

@JvmName("imperialSurfaceChargeDensityDivUSCustomaryElectricChargeDensity")
infix operator fun ScientificValue<PhysicalQuantity.SurfaceChargeDensity, ImperialSurfaceChargeDensity>.div(
    electricChargeDensity: ScientificValue<PhysicalQuantity.ElectricChargeDensity, USCustomaryElectricChargeDensity>,
) = Foot.length(this, electricChargeDensity)

@JvmName("surfaceChargeDensityDivElectricChargeDensity")
infix operator fun <SurfaceChargeDensityUnit, ElectricChargeDensityUnit> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, SurfaceChargeDensityUnit>.div(
    electricChargeDensity: ScientificValue<PhysicalQuantity.ElectricChargeDensity, ElectricChargeDensityUnit>,
) where SurfaceChargeDensityUnit : SurfaceChargeDensity, ElectricChargeDensityUnit : ElectricChargeDensity = Meter.length(this, electricChargeDensity)
