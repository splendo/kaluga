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

package com.splendo.kaluga.scientific.converter.molarity

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialMolality
import com.splendo.kaluga.scientific.unit.ImperialMolarMass
import com.splendo.kaluga.scientific.unit.ImperialMolarity
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricMolality
import com.splendo.kaluga.scientific.unit.MetricMolarMass
import com.splendo.kaluga.scientific.unit.MetricMolarity
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.UKImperialMolality
import com.splendo.kaluga.scientific.unit.UKImperialMolarMass
import com.splendo.kaluga.scientific.unit.UKImperialMolarity
import com.splendo.kaluga.scientific.unit.USCustomaryMolality
import com.splendo.kaluga.scientific.unit.USCustomaryMolarMass
import com.splendo.kaluga.scientific.unit.USCustomaryMolarity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMolarityDivMetricMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, MetricMolarity>.div(molality: ScientificValue<MeasurementType.Molality, MetricMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("imperialMolarityDivImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("imperialMolarityDivUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("imperialMolarityDivUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("ukImperialMolarityDivImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("ukImperialMolarityDivUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.div(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("usCustomaryMolarityDivImperialMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.div(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("usCustomaryMolarityDivUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.div(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (molality.unit.per per unit.per).density(this, molality)
@JvmName("molarityDivMolality")
infix operator fun <MolarityUnit : Molarity, MolalityUnit : Molality> ScientificValue<MeasurementType.Molarity, MolarityUnit>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (Kilogram per CubicMeter).density(this, molality)

@JvmName("metricMolarityTimesMetricMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, MetricMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MetricMolarMass>) = molarMass * this
@JvmName("imperialMolarityTimesImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("imperialMolarityTimesUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = molarMass * this
@JvmName("imperialMolarityTimesUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, ImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = molarMass * this
@JvmName("ukImperialMolarityTimesImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("ukImperialMolarityTimesUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, UKImperialMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = molarMass * this
@JvmName("usCustomaryMolarityTimesImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = molarMass * this
@JvmName("usCustomaryMolarityTimesUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.Molarity, USCustomaryMolarity>.times(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = molarMass * this
@JvmName("molarityTimesMolarMass")
infix operator fun <MolarityUnit : Molarity, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.Molarity, MolarityUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = molarMass * this
