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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.time.time
import com.splendo.kaluga.scientific.unit.ImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.MetricMassFlowRate
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlowRate
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
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
