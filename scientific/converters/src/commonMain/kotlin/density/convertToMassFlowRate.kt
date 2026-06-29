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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.massFlowRate.massFlowRate
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricDensityTimesMetricVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Density, MetricDensity>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>) =
    (unit.weight per volumetricFlow.unit.per).massFlowRate(this, volumetricFlow)

@JvmName("imperialDensityTimesImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>) =
    (unit.weight per volumetricFlow.unit.per).massFlowRate(this, volumetricFlow)

@JvmName("imperialDensityTimesUKImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>) =
    (unit.weight per volumetricFlow.unit.per).massFlowRate(this, volumetricFlow)

@JvmName("imperialDensityTimesUSCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>) =
    (unit.weight per volumetricFlow.unit.per).massFlowRate(this, volumetricFlow)

@JvmName("ukImperialDensityTimesImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>) =
    (unit.weight per volumetricFlow.unit.per).massFlowRate(this, volumetricFlow)

@JvmName("ukImperialDensityTimesUKImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>) =
    (unit.weight per volumetricFlow.unit.per).massFlowRate(this, volumetricFlow)

@JvmName("usCustomaryDensityTimesImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>) =
    (unit.weight per volumetricFlow.unit.per).massFlowRate(this, volumetricFlow)

@JvmName("usCustomaryDensityTimesUSCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>,
) = (unit.weight per volumetricFlow.unit.per).massFlowRate(this, volumetricFlow)

@JvmName("densityTimesVolumetricFlow")
infix operator fun <DensityUnit : Density, VolumetricFlowUnit : VolumetricFlow> ScientificValue<PhysicalQuantity.Density, DensityUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>,
) = (Kilogram per Second).massFlowRate(this, volumetricFlow)
