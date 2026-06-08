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

package com.splendo.kaluga.scientific.converter.molarVolume

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarity.molarity
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialMolarVolume
import com.splendo.kaluga.scientific.unit.MetricMolarVolume
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.UKImperialMolarVolume
import com.splendo.kaluga.scientific.unit.USCustomaryMolarVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMolarVolumeMolarity")
fun ScientificValue<PhysicalQuantity.MolarVolume, MetricMolarVolume>.molarity() = (unit.per per unit.volume).molarity(this)

@JvmName("imperialMolarVolumeMolarity")
fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.molarity() = (unit.per per unit.volume).molarity(this)

@JvmName("ukImperialMolarVolumeMolarity")
fun ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>.molarity() = (unit.per per unit.volume).molarity(this)

@JvmName("usCustomaryMolarVolumeMolarity")
fun ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>.molarity() = (unit.per per unit.volume).molarity(this)

@JvmName("molarVolumeMolarity")
fun <MolarVolumeUnit : MolarVolume> ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>.molarity() = (unit.per per CubicMeter).molarity(this)
