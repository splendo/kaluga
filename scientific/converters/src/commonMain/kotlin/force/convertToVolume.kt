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
import com.splendo.kaluga.scientific.converter.volume.volume
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialSpecificWeight
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricSpecificWeight
import com.splendo.kaluga.scientific.unit.SpecificWeight
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.UKImperialSpecificWeight
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.USCustomarySpecificWeight
import kotlin.jvm.JvmName

@JvmName("metricForceDivMetricSpecificWeight")
infix operator fun <ForceUnit : MetricForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, MetricSpecificWeight>,
) = specificWeight.unit.per.volume(this, specificWeight)

@JvmName("imperialForceDivImperialSpecificWeight")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, ImperialSpecificWeight>,
) = specificWeight.unit.per.volume(this, specificWeight)

@JvmName("imperialForceDivUKImperialSpecificWeight")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, UKImperialSpecificWeight>,
) = specificWeight.unit.per.volume(this, specificWeight)

@JvmName("imperialForceDivUSCustomarySpecificWeight")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, USCustomarySpecificWeight>,
) = specificWeight.unit.per.volume(this, specificWeight)

@JvmName("ukImperialForceDivImperialSpecificWeight")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, ImperialSpecificWeight>,
) = specificWeight.unit.per.volume(this, specificWeight)

@JvmName("ukImperialForceDivUKImperialSpecificWeight")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, UKImperialSpecificWeight>,
) = specificWeight.unit.per.volume(this, specificWeight)

@JvmName("usCustomaryForceDivImperialSpecificWeight")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, ImperialSpecificWeight>,
) = specificWeight.unit.per.volume(this, specificWeight)

@JvmName("usCustomaryForceDivUSCustomarySpecificWeight")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, USCustomarySpecificWeight>,
) = specificWeight.unit.per.volume(this, specificWeight)

@JvmName("forceDivSpecificWeight")
infix operator fun <ForceUnit : Force, SpecificWeightUnit : SpecificWeight> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, SpecificWeightUnit>,
) = CubicMeter.volume(this, specificWeight)
