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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificWeight.specificWeight
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricForceDivMetricVolume")
infix operator fun <ForceUnit : MetricForce, VolumeUnit : MetricVolume> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = (unit per volume.unit).specificWeight(this, volume)

@JvmName("imperialForceDivImperialVolume")
infix operator fun <ForceUnit : ImperialForce, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = (unit per volume.unit).specificWeight(this, volume)

@JvmName("imperialForceDivUKImperialVolume")
infix operator fun <ForceUnit : ImperialForce, VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = (unit per volume.unit).specificWeight(this, volume)

@JvmName("imperialForceDivUSCustomaryVolume")
infix operator fun <ForceUnit : ImperialForce, VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = (unit per volume.unit).specificWeight(this, volume)

@JvmName("ukImperialForceDivImperialVolume")
infix operator fun <ForceUnit : UKImperialForce, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = (unit per volume.unit).specificWeight(this, volume)

@JvmName("ukImperialForceDivUKImperialVolume")
infix operator fun <ForceUnit : UKImperialForce, VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = (unit per volume.unit).specificWeight(this, volume)

@JvmName("usCustomaryForceDivImperialVolume")
infix operator fun <ForceUnit : USCustomaryForce, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = (unit per volume.unit).specificWeight(this, volume)

@JvmName("usCustomaryForceDivUSCustomaryVolume")
infix operator fun <ForceUnit : USCustomaryForce, VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = (unit per volume.unit).specificWeight(this, volume)

@JvmName("forceDivVolume")
infix operator fun <ForceUnit : Force, VolumeUnit : Volume> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>) =
    (Newton per CubicMeter).specificWeight(this, volume)
