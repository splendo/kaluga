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
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.surfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.ElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.ImperialElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.MetricElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricElectricCurrentDensityTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.ElectricCurrentDensity, MetricElectricCurrentDensity>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (Coulomb per unit.per).surfaceChargeDensity(this, time)

@JvmName("imperialElectricCurrentDensityTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.ElectricCurrentDensity, ImperialElectricCurrentDensity>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (Coulomb per unit.per).surfaceChargeDensity(this, time)

@JvmName("electricCurrentDensityTimesTime")
infix operator fun <ElectricCurrentDensityUnit, TimeUnit> ScientificValue<PhysicalQuantity.ElectricCurrentDensity, ElectricCurrentDensityUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) where ElectricCurrentDensityUnit : ElectricCurrentDensity, TimeUnit : Time = (Coulomb per SquareMeter).surfaceChargeDensity(this, time)

@JvmName("metricElectricCurrentDensityDivFrequency")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.ElectricCurrentDensity, MetricElectricCurrentDensity>.div(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = (Coulomb per unit.per).surfaceChargeDensity(this, frequency)

@JvmName("imperialElectricCurrentDensityDivFrequency")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.ElectricCurrentDensity, ImperialElectricCurrentDensity>.div(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = (Coulomb per unit.per).surfaceChargeDensity(this, frequency)

@JvmName("electricCurrentDensityDivFrequency")
infix operator fun <ElectricCurrentDensityUnit, FrequencyUnit> ScientificValue<PhysicalQuantity.ElectricCurrentDensity, ElectricCurrentDensityUnit>.div(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) where ElectricCurrentDensityUnit : ElectricCurrentDensity, FrequencyUnit : Frequency = (Coulomb per SquareMeter).surfaceChargeDensity(this, frequency)
