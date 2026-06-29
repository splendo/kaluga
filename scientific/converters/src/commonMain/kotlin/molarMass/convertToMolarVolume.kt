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

package com.splendo.kaluga.scientific.converter.molarMass

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarVolume.molarVolume
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("molarMassDivMetricDensity")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.div(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    (density.unit.per per unit.per).molarVolume(this, density)

@JvmName("molarMassDivImperialDensity")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>,
) = (density.unit.per per unit.per).molarVolume(this, density)

@JvmName("molarMassDivUKImperialDensity")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>,
) = (density.unit.per per unit.per).molarVolume(this, density)

@JvmName("molarMassDivUSCustomaryDensity")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>,
) = (density.unit.per per unit.per).molarVolume(this, density)

@JvmName("molarMassDivDensity")
infix operator fun <MolarMassUnit : MolarMass, DensityUnit : Density> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = (CubicMeter per unit.per).molarVolume(this, density)

@JvmName("molarMassTimesMetricSpecificVolume")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>,
) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)

@JvmName("molarMassTimesImperialSpecificVolume")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)

@JvmName("molarMassTimesUKImperialSpecificVolume")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)

@JvmName("molarMassTimesUSCustomarySpecificVolume")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)

@JvmName("molarMassTimesSpecificVolume")
infix operator fun <MolarMassUnit : MolarMass, SpecificVolumeUnit : SpecificVolume> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
) = (CubicMeter per unit.per).molarVolume(this, specificVolume)
