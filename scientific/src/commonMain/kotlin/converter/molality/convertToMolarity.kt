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

package com.splendo.kaluga.scientific.converter.molality

import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.Density
import com.splendo.kaluga.scientific.ImperialDensity
import com.splendo.kaluga.scientific.ImperialSpecificVolume
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricDensity
import com.splendo.kaluga.scientific.MetricSpecificVolume
import com.splendo.kaluga.scientific.Molality
import com.splendo.kaluga.scientific.Mole
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificVolume
import com.splendo.kaluga.scientific.UKImperialDensity
import com.splendo.kaluga.scientific.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.USCustomaryDensity
import com.splendo.kaluga.scientific.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.converter.molarity.molarity
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("molalityTimesMetricDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, MetricDensity>) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)
@JvmName("molalityTimesImperialDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)
@JvmName("molalityTimesUKImperialDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)
@JvmName("molalityTimesUSCustomaryDensity")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (unit.amountOfSubstance per density.unit.per).molarity(this, density)
@JvmName("molalityTimesDensity")
infix operator fun <MolalityUnit : Molality, DensityUnit : Density> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(density: ScientificValue<MeasurementType.Density, DensityUnit>) = (unit.amountOfSubstance per CubicMeter).molarity(this, density)

@JvmName("molalityDivMetricSpecificVolume")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = (unit.amountOfSubstance per specificVolume.unit.volume).molarity(this, specificVolume)
@JvmName("molalityDivImperialSpecificVolume")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = (unit.amountOfSubstance per specificVolume.unit.volume).molarity(this, specificVolume)
@JvmName("molalityDivUKImperialSpecificVolume")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = (unit.amountOfSubstance per specificVolume.unit.volume).molarity(this, specificVolume)
@JvmName("molarityDivUSCustomarySpecificVolume")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = (unit.amountOfSubstance per specificVolume.unit.volume).molarity(this, specificVolume)
@JvmName("molalityDivSpecificVolume")
infix operator fun <MolalityUnit : Molality, SpecificVolumeUnit : SpecificVolume> ScientificValue<MeasurementType.Molality, MolalityUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = (Mole per CubicMeter).molarity(this, specificVolume)
