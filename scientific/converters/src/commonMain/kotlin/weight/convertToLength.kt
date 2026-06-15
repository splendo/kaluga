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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.length.length
import com.splendo.kaluga.scientific.unit.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricLinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("metricWeightDivMetricLinearMassDensity")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, MetricLinearMassDensity>,
) = linearMassDensity.unit.per.length(this, linearMassDensity)

@JvmName("imperialWeightDivImperialLinearMassDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>,
) = linearMassDensity.unit.per.length(this, linearMassDensity)

@JvmName("imperialWeightDivUKImperialLinearMassDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>,
) = linearMassDensity.unit.per.length(this, linearMassDensity)

@JvmName("imperialWeightDivUSCustomaryLinearMassDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>,
) = linearMassDensity.unit.per.length(this, linearMassDensity)

@JvmName("ukImperialWeightDivImperialLinearMassDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>,
) = linearMassDensity.unit.per.length(this, linearMassDensity)

@JvmName("ukImperialWeightDivUKImperialLinearMassDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>,
) = linearMassDensity.unit.per.length(this, linearMassDensity)

@JvmName("usCustomaryWeightDivImperialLinearMassDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>,
) = linearMassDensity.unit.per.length(this, linearMassDensity)

@JvmName("usCustomaryWeightDivUSCustomaryLinearMassDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>,
) = linearMassDensity.unit.per.length(this, linearMassDensity)

@JvmName("weightDivLinearMassDensity")
infix operator fun <WeightUnit : Weight, LinearMassDensityUnit : LinearMassDensity> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    linearMassDensity: ScientificValue<PhysicalQuantity.LinearMassDensity, LinearMassDensityUnit>,
) = Meter.length(this, linearMassDensity)
