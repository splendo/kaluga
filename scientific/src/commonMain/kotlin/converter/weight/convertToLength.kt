/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.scientific.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.LinearMassDensity
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricLinearMassDensity
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.length.length
import kotlin.jvm.JvmName

@JvmName("metricWeightDivMetricLinearMassDensity")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("imperialWeightDivImperialLinearMassDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("ukImperialWeightDivImperialLinearMassDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("ukImperialWeightDivUKImperialLinearMassDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("usCustomaryWeightDivImperialLinearMassDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("usCustomaryWeightDivUSCustomaryLinearMassDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("weightDivLinearMassDensity")
infix operator fun <WeightUnit : Weight, LinearMassDensityUnit : LinearMassDensity> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>) = Meter.length(this, linearMassDensity)
