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

package com.splendo.kaluga.scientific.converter.molality

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarity.molarity
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("molalityTimesMetricDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.times(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    (unit.amountOfSubstance per density.unit.per).molarity(this, density)

@JvmName("molalityTimesImperialDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (unit.amountOfSubstance per density.unit.per).molarity(this, density)

@JvmName("molalityTimesUKImperialDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>,
) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)

@JvmName("molalityTimesUSCustomaryDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>,
) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)

@JvmName("molalityTimesDensity")
infix operator fun <MolalityUnit : Molality, DensityUnit : Density> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = (unit.amountOfSubstance per CubicMeter).molarity(this, density)

@JvmName("molalityDivMetricSpecificVolume")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>,
) = (unit.amountOfSubstance per specificVolume.unit.volume).molarity(this, specificVolume)

@JvmName("molalityDivImperialSpecificVolume")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = (unit.amountOfSubstance per specificVolume.unit.volume).molarity(this, specificVolume)

@JvmName("molalityDivUKImperialSpecificVolume")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = (unit.amountOfSubstance per specificVolume.unit.volume).molarity(this, specificVolume)

@JvmName("molarityDivUSCustomarySpecificVolume")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = (unit.amountOfSubstance per specificVolume.unit.volume).molarity(this, specificVolume)

@JvmName("molalityDivSpecificVolume")
infix operator fun <MolalityUnit : Molality, SpecificVolumeUnit : SpecificVolume> ScientificValue<PhysicalQuantity.Molality, MolalityUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
) = (unit.amountOfSubstance per CubicMeter).molarity(this, specificVolume)
