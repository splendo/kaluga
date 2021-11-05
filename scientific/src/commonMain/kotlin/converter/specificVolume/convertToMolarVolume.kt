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

package com.splendo.kaluga.scientific.converter.specificVolume

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.molarVolume.molarVolume
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialMolarMass
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricMolarMass
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialMolarMass
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryMolarMass
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificVolumeTimesMetricMolarMass")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MetricMolarMass>) = molarMass * this
@JvmName("imperialSpecificVolumeTimesImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("imperialSpecificVolumeTimesUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = molarMass * this
@JvmName("imperialSpecificVolumeTimesUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = molarMass * this
@JvmName("ukImperialSpecificVolumeTimesImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("ukImperialSpecificVolumeTimesUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = molarMass * this
@JvmName("usCustomarySpecificVolumeTimesImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("usCustomarySpecificVolumeTimesUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = molarMass * this
@JvmName("specificVolumeTimesMolarMass")
infix operator fun <SpecificVolumeUnit : SpecificVolume, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = molarMass * this

@JvmName("metricSpecificVolumeDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.volume per molality.unit.amountOfSubstance).molarVolume(this, molality)
@JvmName("imperialSpecificVolumeDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.volume per molality.unit.amountOfSubstance).molarVolume(this, molality)
@JvmName("ukImperialSpecificVolumeDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.volume per molality.unit.amountOfSubstance).molarVolume(this, molality)
@JvmName("usCustomarySpecificVolumeDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.volume per molality.unit.amountOfSubstance).molarVolume(this, molality)
@JvmName("specificVolumeDivMolality")
infix operator fun <SpecificVolumeUnit : SpecificVolume, MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (CubicMeter per molality.unit.amountOfSubstance).molarVolume(this, molality)

