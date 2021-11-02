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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.Newton
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.converter.surfaceTension.surfaceTension
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricForceDivMetricLength")
infix operator fun <ForceUnit : MetricForce, LengthUnit : MetricLength> ScientificValue<MeasurementType.Force, ForceUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit per length.unit).surfaceTension(this, length)
@JvmName("imperialForceDivImperialLength")
infix operator fun <ForceUnit : ImperialForce, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Force, ForceUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit per length.unit).surfaceTension(this, length)
@JvmName("ukImperialForceDivImperialLength")
infix operator fun <ForceUnit : UKImperialForce, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Force, ForceUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit per length.unit).surfaceTension(this, length)
@JvmName("usCustomaryForceDivImperialLength")
infix operator fun <ForceUnit : USCustomaryForce, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Force, ForceUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit per length.unit).surfaceTension(this, length)
@JvmName("forceDivLength")
infix operator fun <ForceUnit : Force, LengthUnit : Length> ScientificValue<MeasurementType.Force, ForceUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (Newton per Meter).surfaceTension(this, length)
