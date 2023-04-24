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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.specificVolume.specificVolume
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricLengthDivMetricAreaDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, MetricAreaDensity>,
) = ((1(areaDensity.unit.per) * 1(unit)).unit per areaDensity.unit.weight).specificVolume(
    this,
    areaDensity,
)

@JvmName("imperialLengthDivImperialAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>,
) = ((1(areaDensity.unit.per) * 1(unit)).unit per areaDensity.unit.weight).specificVolume(
    this,
    areaDensity,
)

@JvmName("imperialLengthDivUKImperialAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>,
) = ((1(areaDensity.unit.per) * 1(unit)).unit per areaDensity.unit.weight).specificVolume(
    this,
    areaDensity,
)

@JvmName("imperialLengthDivUSCustomaryAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>,
) = ((1(areaDensity.unit.per) * 1(unit)).unit per areaDensity.unit.weight).specificVolume(
    this,
    areaDensity,
)

@JvmName("lengthDivAreaDensity")
infix operator fun <AreaDensityUnit : AreaDensity, LengthUnit : Length> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>,
) = (CubicMeter per Kilogram).specificVolume(this, areaDensity)
