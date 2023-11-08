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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.converter.volume.volume
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("metricWeightDivMetricDensity")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    density.unit.per.volume(this, density)

@JvmName("imperialWeightDivImperialDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    density.unit.per.volume(this, density)

@JvmName("imperialWeightDivUKImperialDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    density.unit.per.volume(this, density)

@JvmName("imperialWeightDivUSCustomaryDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    density.unit.per.volume(this, density)

@JvmName("ukImperialWeightDivImperialDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    density.unit.per.volume(this, density)

@JvmName("ukImperialWeightDivUKImperialDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>,
) = density.unit.per.volume(this, density)

@JvmName("usCustomaryWeightDivImperialDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    density.unit.per.volume(this, density)

@JvmName("usCustomaryWeightDivUSCustomaryDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>,
) = density.unit.per.volume(this, density)

@JvmName("weightDivDensity")
infix operator fun <WeightUnit : Weight, DensityUnit : Density> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = CubicMeter.volume(this, density)

@JvmName("metricWeightTimesMetricSpecificVolume")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>,
) = specificVolume * this

@JvmName("imperialWeightTimesImperialSpecificVolume")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume * this

@JvmName("imperialWeightTimesUKImperialSpecificVolume")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = specificVolume * this

@JvmName("imperialWeightTimesUSCustomarySpecificVolume")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = specificVolume * this

@JvmName("ukImperialWeightTimesImperialSpecificVolume")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume * this

@JvmName("ukImperialWeightTimesUKImperialSpecificVolume")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = specificVolume * this

@JvmName("usCustomaryWeightTimesImperialSpecificVolume")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume * this

@JvmName("usCustomaryWeightTimesUSCustomarySpecificVolume")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = specificVolume * this

@JvmName("weightTimesSpecificVolume")
infix operator fun <SpecificVolumeUnit : SpecificVolume, WeightUnit : Weight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
) = specificVolume * this
