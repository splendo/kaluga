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

package com.splendo.kaluga.scientific.converter.specificVolume

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.molarVolume.molarVolume
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificVolumeTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = molarMass * this

@JvmName("imperialSpecificVolumeTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = molarMass * this

@JvmName("ukImperialSpecificVolumeTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = molarMass * this

@JvmName("usCustomarySpecificVolumeTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = molarMass * this

@JvmName("specificVolumeTimesMolarMass")
infix operator fun <SpecificVolumeUnit : SpecificVolume, MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = molarMass * this

@JvmName("metricSpecificVolumeDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = (unit.volume per molality.unit.amountOfSubstance).molarVolume(this, molality)

@JvmName("imperialSpecificVolumeDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = (unit.volume per molality.unit.amountOfSubstance).molarVolume(this, molality)

@JvmName("ukImperialSpecificVolumeDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = (unit.volume per molality.unit.amountOfSubstance).molarVolume(this, molality)

@JvmName("usCustomarySpecificVolumeDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = (unit.volume per molality.unit.amountOfSubstance).molarVolume(this, molality)

@JvmName("specificVolumeDivMolality")
infix operator fun <SpecificVolumeUnit : SpecificVolume, MolalityUnit : Molality> ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = (CubicMeter per molality.unit.amountOfSubstance).molarVolume(this, molality)
