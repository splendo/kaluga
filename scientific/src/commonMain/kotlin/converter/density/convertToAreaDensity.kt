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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.scientific.Density
import com.splendo.kaluga.scientific.ImperialDensity
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricDensity
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareMeter
import com.splendo.kaluga.scientific.UKImperialDensity
import com.splendo.kaluga.scientific.USCustomaryDensity
import com.splendo.kaluga.scientific.converter.areaDensity.areaDensity
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricDensityTimesMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Density, MetricDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)
@JvmName("imperialDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Density, ImperialDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)
@JvmName("ukImperialDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Density, UKImperialDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)
@JvmName("usCustomaryDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Density, USCustomaryDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)
@JvmName("densityTimesLength")
infix operator fun <DensityUnit : Density, LengthUnit : Length> ScientificValue<MeasurementType.Density, DensityUnit>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (Kilogram per SquareMeter).areaDensity(this, length)
