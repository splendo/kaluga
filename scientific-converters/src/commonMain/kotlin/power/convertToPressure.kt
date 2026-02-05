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

package com.splendo.kaluga.scientific.converter.power

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.pressure.pressure
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.ImperialCombinedPower
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.MetricCombinedPower
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("metricCombinedPowerDivMetricVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Power, MetricCombinedPower>.div(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>) =
    Barye.pressure(this, volumetricFlow)

@JvmName("imperialCombinedPowerDivImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>) =
    PoundSquareInch.pressure(this, volumetricFlow)

@JvmName("imperialCombinedPowerDivUKImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>) =
    PoundSquareInch.ukImperial.pressure(this, volumetricFlow)

@JvmName("imperialCombinedPowerDivUSCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Power, ImperialCombinedPower>.div(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>) =
    PoundSquareInch.usCustomary.pressure(this, volumetricFlow)

@JvmName("imperialPowerDivImperialVolumetricFlow")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>,
) = PoundSquareInch.pressure(this, volumetricFlow)

@JvmName("imperialPowerDivUKImperialVolumetricFlow")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>,
) = PoundSquareInch.ukImperial.pressure(this, volumetricFlow)

@JvmName("imperialPowerDivUSCustomaryVolumetricFlow")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>,
) = PoundSquareInch.usCustomary.pressure(this, volumetricFlow)

@JvmName("powerDivVolumetricFlow")
infix operator fun <PowerUnit : Power, VolumetricFlowUnit : VolumetricFlow> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>,
) = Pascal.pressure(this, volumetricFlow)
