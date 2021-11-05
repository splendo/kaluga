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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.areaDensity.areaDensity
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricLinearMassDensity
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricLinearMassDensityDivMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>.div(
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).areaDensity(this, length)

@JvmName("imperialLinearMassDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).areaDensity(this, length)

@JvmName("ukImperialLinearMassDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).areaDensity(this, length)

@JvmName("usCustomaryLinearMassDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).areaDensity(this, length)

@JvmName("linearMassDensityDivLength")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, LengthUnit : Length> ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>.div(
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = (Kilogram per SquareMeter).areaDensity(this, length)
