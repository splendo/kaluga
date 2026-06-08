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
import com.splendo.kaluga.scientific.converter.volumetricFlow.volumetricFlow
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricMassFlowRate
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlowRate
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMassFlowRateDivMetricDensity")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, MetricMassFlowRate>.div(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    (density.unit.per per unit.per).volumetricFlow(this, density)

@JvmName("imperialMassFlowRateDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (density.unit.per per unit.per).volumetricFlow(this, density)

@JvmName("imperialMassFlowRateDivUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (density.unit.per per unit.per).volumetricFlow(this, density)

@JvmName("imperialMassFlowRateDivUsCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (density.unit.per per unit.per).volumetricFlow(this, density)

@JvmName("ukImperialMassFlowRateDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (density.unit.per per unit.per).volumetricFlow(this, density)

@JvmName("ukImperialMassFlowRateDivUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (density.unit.per per unit.per).volumetricFlow(this, density)

@JvmName("usCustomaryMassFlowRateDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (density.unit.per per unit.per).volumetricFlow(this, density)

@JvmName("usCustomaryMassFlowRateDivUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (density.unit.per per unit.per).volumetricFlow(this, density)

@JvmName("massFlowRateDivDensity")
infix operator fun <MassFlowRateUnit : MassFlowRate, DensityUnit : Density> ScientificValue<PhysicalQuantity.MassFlowRate, MassFlowRateUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = (CubicMeter per Second).volumetricFlow(this, density)
