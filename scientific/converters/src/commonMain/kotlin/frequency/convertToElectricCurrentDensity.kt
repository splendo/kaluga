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

package com.splendo.kaluga.scientific.converter.frequency

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCurrentDensity.electricCurrentDensity
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.ImperialSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.MetricSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.SurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("frequencyTimesMetricSurfaceChargeDensity")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>.times(
    surfaceChargeDensity: ScientificValue<PhysicalQuantity.SurfaceChargeDensity, MetricSurfaceChargeDensity>,
) = (Ampere per surfaceChargeDensity.unit.per).electricCurrentDensity(surfaceChargeDensity, this)

@JvmName("frequencyTimesImperialSurfaceChargeDensity")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>.times(
    surfaceChargeDensity: ScientificValue<PhysicalQuantity.SurfaceChargeDensity, ImperialSurfaceChargeDensity>,
) = (Ampere per surfaceChargeDensity.unit.per).electricCurrentDensity(surfaceChargeDensity, this)

@JvmName("frequencyTimesSurfaceChargeDensity")
infix operator fun <FrequencyUnit, SurfaceChargeDensityUnit> ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>.times(
    surfaceChargeDensity: ScientificValue<PhysicalQuantity.SurfaceChargeDensity, SurfaceChargeDensityUnit>,
) where FrequencyUnit : Frequency, SurfaceChargeDensityUnit : SurfaceChargeDensity = (Ampere per SquareMeter).electricCurrentDensity(surfaceChargeDensity, this)
