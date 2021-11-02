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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.Density
import com.splendo.kaluga.scientific.ImperialDensity
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.ImperialSpecificVolume
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricDensity
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.MetricSpecificVolume
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificVolume
import com.splendo.kaluga.scientific.SquareMeter
import com.splendo.kaluga.scientific.UKImperialDensity
import com.splendo.kaluga.scientific.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.USCustomaryDensity
import com.splendo.kaluga.scientific.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.converter.areaDensity.areaDensity
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricLengthTimesMetricDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, MetricDensity>) = density * this
@JvmName("imperialLengthTimesImperialDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = density * this
@JvmName("imperialLengthTimesUKImperialDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = density * this
@JvmName("imperialLengthTimesUSCustomaryDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = density * this
@JvmName("lengthTimesDensity")
infix operator fun <DensityUnit : Density, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, DensityUnit>) = density * this

@JvmName("metricLengthDivMetricSpecificVolume")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(this, specificVolume)
@JvmName("imperialLengthDivImperialSpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(this, specificVolume)
@JvmName("imperialLengthDivUKImperialSpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(this, specificVolume)
@JvmName("imperialLengthDivUSCustomarySpecificVolume")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = (specificVolume.unit.per per (1(specificVolume.unit.volume) / 1(unit)).unit).areaDensity(this, specificVolume)
@JvmName("lengthDivSpecificVolume")
infix operator fun <SpecificVolumeUnit : SpecificVolume, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = (Kilogram per SquareMeter).areaDensity(this, specificVolume)
