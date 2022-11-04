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

package com.splendo.kaluga.scientific.converter.linearMassDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.length.length
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.MetricLinearMassDensity
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryLinearMassDensity
import kotlin.jvm.JvmName

@JvmName("metricLinearMassDensityDivMetricAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, MetricLinearMassDensity>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, MetricAreaDensity>
) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)

@JvmName("imperialLinearMassDensityDivImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>
) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)

@JvmName("imperialLinearMassDensityDivUKImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>
) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)

@JvmName("imperialLinearMassDensityDivUSCustomaryAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>
) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)

@JvmName("ukImperialLinearMassDensityDivImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>
) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)

@JvmName("ukImperialLinearMassDensityDivUKImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>
) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)

@JvmName("usCustomaryLinearMassDensityDivImperialAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>
) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)

@JvmName("usCustomaryLinearMassDensityDivUSCustomaryAreaDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>
) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)

@JvmName("linearMassDensityDivAreaDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, AreaDensityUnit : AreaDensity> ScientificValue<PhysicalQuantity.LinearMassDensity, LinearMassDensityUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>
) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
