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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.linearMassDensity.times
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricLinearMassDensity
import com.splendo.kaluga.scientific.unit.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.USCustomaryLinearMassDensity
import kotlin.jvm.JvmName

@JvmName("metricLengthTimesMetricLinearMassDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>) = linearMassDensity * this
@JvmName("imperialLengthTimesImperialLinearMassDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = linearMassDensity * this
@JvmName("imperialLengthUKImperialLinearMassDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = linearMassDensity * this
@JvmName("imperialLengthTimesUSCustomaryLinearMassDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = linearMassDensity * this
@JvmName("lengthTimesLinearMassDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>) = linearMassDensity * this

