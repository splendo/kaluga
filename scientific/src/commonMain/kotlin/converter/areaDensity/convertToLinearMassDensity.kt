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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.linearMassDensity.linearMassDensity
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAreaDensityTimesMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).linearMassDensity(this, length)
@JvmName("imperialAreaDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).linearMassDensity(this, length)
@JvmName("ukImperialAreaDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).linearMassDensity(this, length)
@JvmName("usCustomaryAreaDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).linearMassDensity(this, length)
@JvmName("areaDensityTimesLength")
infix operator fun <AreaDensityUnit : AreaDensity, LengthUnit : Length> ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (Kilogram per Meter).linearMassDensity(this, length)
