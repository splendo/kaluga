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

import com.splendo.kaluga.scientific.Density
import com.splendo.kaluga.scientific.ImperialDensity
import com.splendo.kaluga.scientific.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.LinearMassDensity
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricDensity
import com.splendo.kaluga.scientific.MetricLinearMassDensity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialDensity
import com.splendo.kaluga.scientific.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.USCustomaryDensity
import com.splendo.kaluga.scientific.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import kotlin.jvm.JvmName

@JvmName("metricLinearMassDensityDivMetricDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, MetricDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("imperialLinearMassDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("imperialLinearMassDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("imperialLinearMassDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("ukImperialLinearMassDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("ukImperialLinearMassDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("usCustomaryLinearMassDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("usCustomaryLinearMassDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("linearMassDensityDivDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, DensityUnit : Density> ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>.div(density: ScientificValue<MeasurementType.Density, DensityUnit>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
