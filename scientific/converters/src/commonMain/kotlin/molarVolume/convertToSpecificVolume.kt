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

package com.splendo.kaluga.scientific.converter.molarVolume

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificVolume.specificVolume
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialMolality
import com.splendo.kaluga.scientific.unit.ImperialMolarMass
import com.splendo.kaluga.scientific.unit.ImperialMolarVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricMolality
import com.splendo.kaluga.scientific.unit.MetricMolarMass
import com.splendo.kaluga.scientific.unit.MetricMolarVolume
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.UKImperialMolality
import com.splendo.kaluga.scientific.unit.UKImperialMolarMass
import com.splendo.kaluga.scientific.unit.UKImperialMolarVolume
import com.splendo.kaluga.scientific.unit.USCustomaryMolality
import com.splendo.kaluga.scientific.unit.USCustomaryMolarMass
import com.splendo.kaluga.scientific.unit.USCustomaryMolarVolume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMolarVolumeTimesMetricMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, MetricMolarVolume>.times(molality: ScientificValue<PhysicalQuantity.Molality, MetricMolality>) =
    (unit.volume per molality.unit.per).specificVolume(this, molality)

@JvmName("imperialMolarVolumeTimesImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.times(molality: ScientificValue<PhysicalQuantity.Molality, ImperialMolality>) =
    (unit.volume per molality.unit.per).specificVolume(this, molality)

@JvmName("imperialMolarVolumeTimesUKImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.times(molality: ScientificValue<PhysicalQuantity.Molality, UKImperialMolality>) =
    (unit.volume per molality.unit.per).specificVolume(this, molality)

@JvmName("imperialMolarVolumeTimesUSCustomaryMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.times(molality: ScientificValue<PhysicalQuantity.Molality, USCustomaryMolality>) =
    (unit.volume per molality.unit.per).specificVolume(this, molality)

@JvmName("ukImperialMolarVolumeTimesImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>.times(molality: ScientificValue<PhysicalQuantity.Molality, ImperialMolality>) =
    (unit.volume per molality.unit.per).specificVolume(this, molality)

@JvmName("ukImperialMolarVolumeTimesUKImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>.times(molality: ScientificValue<PhysicalQuantity.Molality, UKImperialMolality>) =
    (unit.volume per molality.unit.per).specificVolume(this, molality)

@JvmName("usCustomaryMolarVolumeTimesImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>.times(molality: ScientificValue<PhysicalQuantity.Molality, ImperialMolality>) =
    (unit.volume per molality.unit.per).specificVolume(this, molality)

@JvmName("usCustomaryMolarVolumeTimesUSCustomaryMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>.times(molality: ScientificValue<PhysicalQuantity.Molality, USCustomaryMolality>) =
    (unit.volume per molality.unit.per).specificVolume(this, molality)

@JvmName("molarVolumeTimesMolality")
infix operator fun <MolarVolumeUnit : MolarVolume, MolalityUnit : Molality> ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>.times(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = (CubicMeter per Kilogram).specificVolume(this, molality)

@JvmName("metricMolarVolumeDivMetricMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, MetricMolarVolume>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, MetricMolarMass>) =
    (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)

@JvmName("imperialMolarVolumeDivImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>) =
    (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)

@JvmName("imperialMolarVolumeDivUKImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>) =
    (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)

@JvmName("imperialMolarVolumeDivUSCustomaryMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, ImperialMolarVolume>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>) =
    (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)

@JvmName("ukImperialMolarVolumeDivImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>) =
    (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)

@JvmName("ukImperialMolarVolumeDivUKImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, UKImperialMolarVolume>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>) =
    (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)

@JvmName("usCustomaryMolarVolumeDivImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>) =
    (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)

@JvmName("usCustomaryMolarVolumeDivUSCustomaryMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarVolume, USCustomaryMolarVolume>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>) =
    (unit.volume per molarMass.unit.weight).specificVolume(this, molarMass)

@JvmName("molarVolumeDivMolarMass")
infix operator fun <MolarVolumeUnit : MolarVolume, MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarVolume, MolarVolumeUnit>.div(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = (CubicMeter per Kilogram).specificVolume(this, molarMass)
