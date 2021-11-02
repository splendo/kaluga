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

import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.ImperialMolality
import com.splendo.kaluga.scientific.ImperialMolarMass
import com.splendo.kaluga.scientific.ImperialMolarVolume
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMolality
import com.splendo.kaluga.scientific.MetricMolarMass
import com.splendo.kaluga.scientific.MetricMolarVolume
import com.splendo.kaluga.scientific.Molality
import com.splendo.kaluga.scientific.MolarMass
import com.splendo.kaluga.scientific.MolarVolume
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialMolality
import com.splendo.kaluga.scientific.UKImperialMolarMass
import com.splendo.kaluga.scientific.UKImperialMolarVolume
import com.splendo.kaluga.scientific.USCustomaryMolality
import com.splendo.kaluga.scientific.USCustomaryMolarMass
import com.splendo.kaluga.scientific.USCustomaryMolarVolume
import com.splendo.kaluga.scientific.converter.specificVolume.specificVolume
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricMolarVolumeTimesMetricMolality")
infix operator fun ScientificValue<MeasurementType.MolarVolume, MetricMolarVolume>.times(molality: ScientificValue<MeasurementType.Molality, MetricMolality>) = (unit.volume per molality.unit.per).specificVolume(this, molality)
@JvmName("imperialMolarVolumeTimesImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>.times(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (unit.volume per molality.unit.per).specificVolume(this, molality)
@JvmName("imperialMolarVolumeTimesUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>.times(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (unit.volume per molality.unit.per).specificVolume(this, molality)
@JvmName("imperialMolarVolumeTimesUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>.times(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (unit.volume per molality.unit.per).specificVolume(this, molality)
@JvmName("ukImperialMolarVolumeTimesImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarVolume, UKImperialMolarVolume>.times(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (unit.volume per molality.unit.per).specificVolume(this, molality)
@JvmName("ukImperialMolarVolumeTimesUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarVolume, UKImperialMolarVolume>.times(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (unit.volume per molality.unit.per).specificVolume(this, molality)
@JvmName("usCustomaryMolarVolumeTimesImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarVolume, USCustomaryMolarVolume>.times(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (unit.volume per molality.unit.per).specificVolume(this, molality)
@JvmName("usCustomaryMolarVolumeTimesUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.MolarVolume, USCustomaryMolarVolume>.times(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (unit.volume per molality.unit.per).specificVolume(this, molality)
@JvmName("molarVolumeTimesMolality")
infix operator fun <MolarVolumeUnit : MolarVolume, MolalityUnit : Molality> ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (CubicMeter per Kilogram).specificVolume(this, molality)

@JvmName("metricMolarVolumeDivMetricMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarVolume, MetricMolarVolume>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MetricMolarMass>) = (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)
@JvmName("imperialMolarVolumeDivImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>.div(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)
@JvmName("imperialMolarVolumeDivUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>.div(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)
@JvmName("imperialMolarVolumeDivUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarVolume, ImperialMolarVolume>.div(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)
@JvmName("ukImperialMolarVolumeDivImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarVolume, UKImperialMolarVolume>.div(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)
@JvmName("ukImperialMolarVolumeDivUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarVolume, UKImperialMolarVolume>.div(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)
@JvmName("usCustomaryMolarVolumeDivImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarVolume, USCustomaryMolarVolume>.div(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)
@JvmName("usCustomaryMolarVolumeDivUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarVolume, USCustomaryMolarVolume>.div(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)
@JvmName("molarVolumeDivMolarMass")
infix operator fun <MolarVolumeUnit : MolarVolume, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (CubicMeter per Kilogram).specificVolume(this, molarMass)

