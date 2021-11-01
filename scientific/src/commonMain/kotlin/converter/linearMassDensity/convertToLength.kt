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

package com.splendo.kaluga.scientific.converter.linearMassDensity

import com.splendo.kaluga.scientific.AreaDensity
import com.splendo.kaluga.scientific.ImperialAreaDensity
import com.splendo.kaluga.scientific.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.LinearMassDensity
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAreaDensity
import com.splendo.kaluga.scientific.MetricLinearMassDensity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialAreaDensity
import com.splendo.kaluga.scientific.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.length.length
import com.splendo.kaluga.scientific.invoke
import kotlin.jvm.JvmName

@JvmName("metricLinearMassDensityDivMetricAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("imperialLinearMassDensityDivImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("imperialLinearMassDensityDivUKImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("imperialLinearMassDensityDivUSCustomaryAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("ukImperialLinearMassDensityDivImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("ukImperialLinearMassDensityDivUKImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("usCustomaryLinearMassDensityDivImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("usCustomaryLinearMassDensityDivUSCustomaryAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("linearMassDensityDivAreaDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, AreaDensityUnit : AreaDensity> ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
