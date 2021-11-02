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
import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.LinearMassDensity
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricLinearMassDensity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.specificVolume.specificVolume
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricAreaDivMetricLinearMassDensity")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Area, AreaUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>) = ((1(linearMassDensity.unit.per) * 1(unit)).unit per linearMassDensity.unit.weight).specificVolume(this, linearMassDensity)
@JvmName("imperialAreaDivImperialLinearMassDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = ((1(linearMassDensity.unit.per) * 1(unit)).unit per linearMassDensity.unit.weight).specificVolume(this, linearMassDensity)
@JvmName("imperialAreaDivUKImperialLinearMassDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = ((1(linearMassDensity.unit.per) * 1(unit)).unit per linearMassDensity.unit.weight).specificVolume(this, linearMassDensity)
@JvmName("imperialAreaDivUSCustomaryLinearMassDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = ((1(linearMassDensity.unit.per) * 1(unit)).unit per linearMassDensity.unit.weight).specificVolume(this, linearMassDensity)
@JvmName("areaDivLinearMassDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>) = (CubicMeter per Kilogram).specificVolume(this, linearMassDensity)
