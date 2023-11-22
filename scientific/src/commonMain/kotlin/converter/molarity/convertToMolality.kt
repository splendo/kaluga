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

package com.splendo.kaluga.scientific.converter.molarity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molality.molality
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("molarityDivMetricDensity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.div(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    (unit.amountOfSubstance per density.unit.weight).molality(this, density)

@JvmName("molarityDivImperialDensity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (unit.amountOfSubstance per density.unit.weight).molality(this, density)

@JvmName("molarityDivUKImperialDensity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (unit.amountOfSubstance per density.unit.weight).molality(this, density)

@JvmName("molarityDivUSCustomaryDensity")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (unit.amountOfSubstance per density.unit.weight).molality(this, density)

@JvmName("molarityDivDensity")
infix operator fun <MolarityUnit : Molarity, DensityUnit : Density> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = (unit.amountOfSubstance per Kilogram).molality(this, density)

@JvmName("molarityTimesMetricSpecificVolume")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>,
) = (unit.amountOfSubstance per specificVolume.unit.per).molality(this, specificVolume)

@JvmName("molarityTimesImperialSpecificVolume")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = (unit.amountOfSubstance per specificVolume.unit.per).molality(this, specificVolume)

@JvmName("molarityTimesUKImperialSpecificVolume")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = (unit.amountOfSubstance per specificVolume.unit.per).molality(this, specificVolume)

@JvmName("molarityTimesUSCustomarySpecificVolume")
infix operator fun <MolarityUnit : Molarity> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = (unit.amountOfSubstance per specificVolume.unit.per).molality(this, specificVolume)

@JvmName("molarityTimesSpecificVolume")
infix operator fun <MolarityUnit : Molarity, SpecificVolumeUnit : SpecificVolume> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.times(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
) = (unit.amountOfSubstance per Kilogram).molality(this, specificVolume)
