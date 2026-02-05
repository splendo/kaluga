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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import kotlin.jvm.JvmName

@JvmName("volumetricFlowFromMassFlowRateAndVolumetricFlowDefault")
fun <
    MassFlowRateUnit : MassFlowRate,
    VolumetricFlowUnit : VolumetricFlow,
    DensityUnit : Density,
    > DensityUnit.density(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, MassFlowRateUnit>,
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>,
) = density(massFlowRate, volumetricFlow, ::DefaultScientificValue)

@JvmName("volumetricFlowFromMassFlowRateAndVolumetricFlow")
fun <
    MassFlowRateUnit : MassFlowRate,
    VolumetricFlowUnit : VolumetricFlow,
    DensityUnit : Density,
    Value : ScientificValue<PhysicalQuantity.Density, DensityUnit>,
    > DensityUnit.density(
    massFlowRate: ScientificValue<PhysicalQuantity.MassFlowRate, MassFlowRateUnit>,
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>,
    factory: (Decimal, DensityUnit) -> Value,
) = byDividing(massFlowRate, volumetricFlow, factory)
