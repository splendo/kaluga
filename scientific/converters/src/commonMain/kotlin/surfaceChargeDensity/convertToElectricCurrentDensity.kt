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
import com.splendo.kaluga.scientific.converter.electricCurrentDensity.electricCurrentDensity
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.ImperialSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.MetricSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.SurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricSurfaceChargeDensityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, MetricSurfaceChargeDensity>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (Ampere per unit.per).electricCurrentDensity(this, time)

@JvmName("imperialSurfaceChargeDensityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, ImperialSurfaceChargeDensity>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (Ampere per unit.per).electricCurrentDensity(this, time)

@JvmName("surfaceChargeDensityDivTime")
infix operator fun <SurfaceChargeDensityUnit, TimeUnit> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, SurfaceChargeDensityUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) where SurfaceChargeDensityUnit : SurfaceChargeDensity, TimeUnit : Time = (Ampere per SquareMeter).electricCurrentDensity(this, time)

@JvmName("metricSurfaceChargeDensityTimesFrequency")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, MetricSurfaceChargeDensity>.times(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = (Ampere per unit.per).electricCurrentDensity(this, frequency)

@JvmName("imperialSurfaceChargeDensityTimesFrequency")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, ImperialSurfaceChargeDensity>.times(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = (Ampere per unit.per).electricCurrentDensity(this, frequency)

@JvmName("surfaceChargeDensityTimesFrequency")
infix operator fun <SurfaceChargeDensityUnit, FrequencyUnit> ScientificValue<PhysicalQuantity.SurfaceChargeDensity, SurfaceChargeDensityUnit>.times(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) where SurfaceChargeDensityUnit : SurfaceChargeDensity, FrequencyUnit : Frequency = (Ampere per SquareMeter).electricCurrentDensity(this, frequency)
