/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificVolume.specificVolume
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricDensitySpecificVolume")
fun ScientificValue<MeasurementType.Density, MetricDensity>.specificVolume() = (unit.per per unit.weight).specificVolume(this)
@JvmName("imperialDensitySpecificVolume")
fun ScientificValue<MeasurementType.Density, ImperialDensity>.specificVolume() = (unit.per per unit.weight).specificVolume(this)
@JvmName("ukImperialDensitySpecificVolume")
fun ScientificValue<MeasurementType.Density, UKImperialDensity>.specificVolume() = (unit.per per unit.weight).specificVolume(this)
@JvmName("usCustomaryDensitySpecificVolume")
fun ScientificValue<MeasurementType.Density, USCustomaryDensity>.specificVolume() = (unit.per per unit.weight).specificVolume(this)
@JvmName("densitySpecificVolume")
fun <DensityUnit : Density> ScientificValue<MeasurementType.Density, DensityUnit>.specificVolume() = (CubicMeter per Kilogram).specificVolume(this)

