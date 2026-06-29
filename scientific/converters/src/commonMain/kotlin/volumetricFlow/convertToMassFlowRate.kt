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

package com.splendo.kaluga.scientific.converter.volumetricFlow

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import kotlin.jvm.JvmName

@JvmName("metricVolumetricFlowTimesMetricDensity")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>.times(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) = density * this

@JvmName("imperialVolumetricFlowTimesImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    density * this

@JvmName("imperialVolumetricFlowTimesUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    density * this

@JvmName("imperialVolumetricFlowTimesUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    density * this

@JvmName("ukImperialVolumetricFlowTimesImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    density * this

@JvmName("ukImperialVolumetricFlowTimesUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.times(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    density * this

@JvmName("usCustomaryVolumetricFlowTimesImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    density * this

@JvmName("usCustomaryVolumetricFlowTimesUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    density * this

@JvmName("volumetricFlowTimesDensity")
infix operator fun <VolumetricFlowUnit : VolumetricFlow, DensityUnit : Density> ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = density * this
