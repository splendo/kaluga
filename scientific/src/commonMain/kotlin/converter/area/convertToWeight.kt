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

package com.splendo.kaluga.scientific.converter.area

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.AreaDensity
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialAreaDensity
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricAreaDensity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialAreaDensity
import com.splendo.kaluga.scientific.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.converter.areaDensity.times
import kotlin.jvm.JvmName

@JvmName("metricAreaTimesMetricAreaDenisty")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>) = areaDensity * this
@JvmName("imperialAreaTimesImperialAreaDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = areaDensity * this
@JvmName("imperialAreaDensityTimesUKImperialAreaDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = areaDensity * this
@JvmName("imperialAreaTimesImperialUSCustomaryAreaDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = areaDensity * this
@JvmName("areaTimesAreaDensity")
infix operator fun <AreaDensityUnit : AreaDensity, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>) = areaDensity * this
