/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.massFlowRate

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.MetricMassFlowRate
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlowRate
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMassFlowRateDivMetricVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, MetricMassFlowRate>.div(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>) =
    (unit.weight per volumetricFlow.unit.volume).density(this, volumetricFlow)

@JvmName("imperialMassFlowRateDivImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>,
) = (unit.weight per volumetricFlow.unit.volume).density(this, volumetricFlow)

@JvmName("imperialMassFlowRateDivUKImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>,
) = (unit.weight per volumetricFlow.unit.volume).density(this, volumetricFlow)

@JvmName("imperialMassFlowRateDivUsCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>,
) = (unit.weight per volumetricFlow.unit.volume).density(this, volumetricFlow)

@JvmName("ukImperialMassFlowRateDivImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>,
) = (unit.weight per volumetricFlow.unit.volume).density(this, volumetricFlow)

@JvmName("ukImperialMassFlowRateDivUKImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>,
) = (unit.weight per volumetricFlow.unit.volume).density(this, volumetricFlow)

@JvmName("usCustomaryMassFlowRateDivImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>,
) = (unit.weight per volumetricFlow.unit.volume).density(this, volumetricFlow)

@JvmName("usCustomaryMassFlowRateDivUSCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>,
) = (unit.weight per volumetricFlow.unit.volume).density(this, volumetricFlow)

@JvmName("massFlowRateDivVolumetricFlow")
infix operator fun <MassFlowRateUnit : MassFlowRate, VolumetricFlowUnit : VolumetricFlow> ScientificValue<PhysicalQuantity.MassFlowRate, MassFlowRateUnit>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>,
) = (Kilogram per CubicMeter).density(this, volumetricFlow)
