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

package com.splendo.kaluga.scientific.converter.specificVolume

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificVolumeDensity")
fun ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>.density() = (unit.per per unit.volume).density(this)

@JvmName("imperialSpecificVolumeDensity")
fun ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.density() = (unit.per per unit.volume).density(this)

@JvmName("ukImperialSpecificVolumeDensity")
fun ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>.density() = (unit.per per unit.volume).density(this)

@JvmName("usCustomarySpecificVolumeDensity")
fun ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>.density() = (unit.per per unit.volume).density(this)

@JvmName("specificVolumeDensity")
fun <SpecificVolumeUnit : SpecificVolume> ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>.density() = (Kilogram per CubicMeter).density(this)
