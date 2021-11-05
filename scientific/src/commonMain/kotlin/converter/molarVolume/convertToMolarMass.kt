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

package com.splendo.kaluga.scientific.converter.molarVolume

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarMass.molarMass
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialMolarVolume
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricMolarVolume
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialMolarVolume
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryMolarVolume
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMolarVolumeTimesMetricDensity")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, MetricMolarVolume>.times(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    (density.unit.weight per unit.per).molarMass(this, density)

@JvmName("imperialMolarVolumeTimesImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (density.unit.weight per unit.per).molarMass(this, density)

@JvmName("imperialMolarVolumeTimesUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.times(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (density.unit.weight per unit.per).molarMass(this, density)

@JvmName("imperialMolarVolumeTimesUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.times(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (density.unit.weight per unit.per).molarMass(this, density)

@JvmName("ukImperialMolarVolumeTimesImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (density.unit.weight per unit.per).molarMass(this, density)

@JvmName("ukImperialMolarVolumeTimesUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>.times(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (density.unit.weight per unit.per).molarMass(this, density)

@JvmName("usCustomaryMolarVolumeTimesImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>.times(
    density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>
) = (density.unit.weight per unit.per).molarMass(this, density)

@JvmName("usCustomaryMolarVolumeTimesUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>.times(
    density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>
) = (density.unit.weight per unit.per).molarMass(this, density)

@JvmName("molarVolumeTimesDensity")
infix operator fun <MolarVolumeUnit : MolarVolume, DensityUnit : Density> ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>
) = (Kilogram per Mole).molarMass(this, density)

@JvmName("molarVolumeDivMetricSpecificVolume")
infix operator fun <MolarVolumeUnit : MolarVolume> ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>
) = (specificVolume.unit.per per unit.per).molarMass(this, specificVolume)

@JvmName("molarVolumeDivImperialSpecificVolume")
infix operator fun <MolarVolumeUnit : MolarVolume> ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>
) = (specificVolume.unit.per per unit.per).molarMass(this, specificVolume)

@JvmName("molarVolumeDivUKImperialSpecificVolume")
infix operator fun <MolarVolumeUnit : MolarVolume> ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>
) = (specificVolume.unit.per per unit.per).molarMass(this, specificVolume)

@JvmName("molarVolumeDivUSCustomarySpecificVolume")
infix operator fun <MolarVolumeUnit : MolarVolume> ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>
) = (specificVolume.unit.per per unit.per).molarMass(this, specificVolume)

@JvmName("molarVolumeDivSpecificVolume")
infix operator fun <MolarVolumeUnit : MolarVolume, SpecificVolumeUnit : SpecificVolume> ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>
) = (Kilogram per unit.per).molarMass(this, specificVolume)
