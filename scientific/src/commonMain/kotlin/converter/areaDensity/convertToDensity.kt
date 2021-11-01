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

package com.splendo.kaluga.scientific.converter.areaDensity

import com.splendo.kaluga.scientific.AreaDensity
import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.ImperialAreaDensity
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAreaDensity
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialAreaDensity
import com.splendo.kaluga.scientific.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricAreaDensityDivMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)
@JvmName("imperialAreaDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)
@JvmName("ukImperialAreaDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)
@JvmName("usCustomaryAreaDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)
@JvmName("areaDensityDivLength")
infix operator fun <AreaDensityUnit : AreaDensity, LengthUnit : Length> ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (Kilogram per CubicMeter).density(this, length)
