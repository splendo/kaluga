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

package com.splendo.kaluga.scientific.converter.molarMass

import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.Density
import com.splendo.kaluga.scientific.ImperialDensity
import com.splendo.kaluga.scientific.ImperialMolarMass
import com.splendo.kaluga.scientific.ImperialSpecificVolume
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricDensity
import com.splendo.kaluga.scientific.MetricMolarMass
import com.splendo.kaluga.scientific.MetricSpecificVolume
import com.splendo.kaluga.scientific.MolarMass
import com.splendo.kaluga.scientific.Mole
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificVolume
import com.splendo.kaluga.scientific.UKImperialDensity
import com.splendo.kaluga.scientific.UKImperialMolarMass
import com.splendo.kaluga.scientific.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.USCustomaryDensity
import com.splendo.kaluga.scientific.USCustomaryMolarMass
import com.splendo.kaluga.scientific.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.converter.molarVolume.molarVolume
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("molarMassDivMetricDensity")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.div(density: ScientificValue<MeasurementType.Density, MetricDensity>) = (density.unit.per per unit.per).molarVolume(this, density)
@JvmName("molarMassDivImperialDensity")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (density.unit.per per unit.per).molarVolume(this, density)
@JvmName("molarMassDivUKImperialDensity")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (density.unit.per per unit.per).molarVolume(this, density)
@JvmName("molarMassDivUSCustomaryDensity")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (density.unit.per per unit.per).molarVolume(this, density)
@JvmName("molarMassDivDensity")
infix operator fun <MolarMassUnit : MolarMass, DensityUnit : Density> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.div(density: ScientificValue<MeasurementType.Density, DensityUnit>) = (CubicMeter per unit.per).molarVolume(this, density)

@JvmName("metricMolarMassTimesMetricSpecificVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, MetricMolarMass>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)
@JvmName("imperialMolarMassTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)
@JvmName("imperialMolarMassTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)
@JvmName("imperialMolarMassTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)
@JvmName("ukImperialMolarMassTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)
@JvmName("ukImperialMolarMassTimesUKImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)
@JvmName("usCustomaryMolarMassTimesImperialSpecificVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)
@JvmName("usCustomaryMolarMassTimesUSCustomarySpecificVolume")
infix operator fun ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = (specificVolume.unit.volume per unit.per).molarVolume(this, specificVolume)
@JvmName("molarMassTimesSpecificVolume")
infix operator fun <MolarMassUnit : MolarMass, SpecificVolumeUnit : SpecificVolume> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = (CubicMeter per Mole).molarVolume(this, specificVolume)
