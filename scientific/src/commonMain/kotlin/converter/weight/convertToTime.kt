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

import com.splendo.kaluga.scientific.ImperialMassFlowRate
import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.MassFlowRate
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMassFlowRate
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomaryMassFlowRate
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.time.time
import kotlin.jvm.JvmName

@JvmName("metricWeightDivMetricMassFlowRate")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MetricMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("imperialWeightDivImperialMassFlowRate")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, ImperialMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("ukImperialWeightDivImperialMassFlowRate")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, ImperialMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("ukImperialWeightDivUKImperialMassFlowRate")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, UKImperialMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("usCustomaryWeightDivImperialMassFlowRate")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, ImperialMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("usCustomaryWeightDivUSCustomaryMassFlowRate")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, USCustomaryMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("weightDivMassFlowRate")
infix operator fun <WeightUnit : Weight, MassFlowRateUnit : MassFlowRate> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MassFlowRateUnit>) = massFlowRate.unit.per.time(this, massFlowRate)
