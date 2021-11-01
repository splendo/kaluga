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

import com.splendo.kaluga.scientific.AreaDensity
import com.splendo.kaluga.scientific.ImperialAreaDensity
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAreaDensity
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialAreaDensity
import com.splendo.kaluga.scientific.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.converter.areaDensity.times
import kotlin.jvm.JvmName

@JvmName("metricLengthTimesMetricAreaDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>) = areaDensity * this
@JvmName("imperialLengthTimesImperialAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = areaDensity * this
@JvmName("imperialLengthTimesUKImperialAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = areaDensity * this
@JvmName("imperialLengthTimesUSCustomaryAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = areaDensity * this
@JvmName("lengthTimesAreaDensity")
infix operator fun <AreaDensityUnit : AreaDensity, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>) = areaDensity * this
