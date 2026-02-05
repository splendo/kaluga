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
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricLinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import kotlin.jvm.JvmName

@JvmName("metricLinearMassDensityDivMetricDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, MetricLinearMassDensity>.div(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("imperialLinearMassDensityDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("imperialLinearMassDensityDivUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("imperialLinearMassDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("ukImperialLinearMassDensityDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("ukImperialLinearMassDensityDivUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("usCustomaryLinearMassDensityDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("usCustomaryLinearMassDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("linearMassDensityDivDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, DensityUnit : Density> ScientificValue<PhysicalQuantity.LinearMassDensity, LinearMassDensityUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("metricLinearMassDensityTimesMetricSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, MetricLinearMassDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>,
) = specificVolume * this

@JvmName("imperialLinearMassDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume * this

@JvmName("imperialLinearMassDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = specificVolume * this

@JvmName("imperialLinearMassDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = specificVolume * this

@JvmName("ukImperialLinearMassDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume * this

@JvmName("ukImperialLinearMassDensityTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = specificVolume * this

@JvmName("usCustomaryLinearMassDensityTimesImperialSpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume * this

@JvmName("usCustomaryLinearMassDensityTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = specificVolume * this

@JvmName("linearMassDensityTimesSpecificVolume")
infix operator fun <LinearMassDensityUnit, SpecificVolumeUnit> ScientificValue<PhysicalQuantity.LinearMassDensity, LinearMassDensityUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
) where LinearMassDensityUnit : LinearMassDensity, SpecificVolumeUnit : SpecificVolume = specificVolume * this
