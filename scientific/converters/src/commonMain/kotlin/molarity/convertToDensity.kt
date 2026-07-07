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
infix operator fun ScientificValue<PhysicalQuantity.Molarity, MetricMolarity>.div(molality: ScientificValue<PhysicalQuantity.Molality, MetricMolality>) =
    (molality.unit.per per unit.per).density(this, molality)

@JvmName("imperialMolarityDivImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>.div(molality: ScientificValue<PhysicalQuantity.Molality, ImperialMolality>) =
    (molality.unit.per per unit.per).density(this, molality)

@JvmName("imperialMolarityDivUKImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>.div(molality: ScientificValue<PhysicalQuantity.Molality, UKImperialMolality>) =
    (molality.unit.per per unit.per).density(this, molality)

@JvmName("imperialMolarityDivUSCustomaryMolality")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>.div(molality: ScientificValue<PhysicalQuantity.Molality, USCustomaryMolality>) =
    (molality.unit.per per unit.per).density(this, molality)

@JvmName("ukImperialMolarityDivImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, UKImperialMolarity>.div(molality: ScientificValue<PhysicalQuantity.Molality, ImperialMolality>) =
    (molality.unit.per per unit.per).density(this, molality)

@JvmName("ukImperialMolarityDivUKImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, UKImperialMolarity>.div(molality: ScientificValue<PhysicalQuantity.Molality, UKImperialMolality>) =
    (molality.unit.per per unit.per).density(this, molality)

@JvmName("usCustomaryMolarityDivImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, USCustomaryMolarity>.div(molality: ScientificValue<PhysicalQuantity.Molality, ImperialMolality>) =
    (molality.unit.per per unit.per).density(this, molality)

@JvmName("usCustomaryMolarityDivUSCustomaryMolality")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, USCustomaryMolarity>.div(molality: ScientificValue<PhysicalQuantity.Molality, USCustomaryMolality>) =
    (molality.unit.per per unit.per).density(this, molality)

@JvmName("molarityDivMolality")
infix operator fun <MolarityUnit : Molarity, MolalityUnit : Molality> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = (Kilogram per CubicMeter).density(this, molality)

@JvmName("metricMolarityTimesMetricMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, MetricMolarity>.times(molarMass: ScientificValue<PhysicalQuantity.MolarMass, MetricMolarMass>) = molarMass * this

@JvmName("imperialMolarityTimesImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>.times(molarMass: ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>) = molarMass * this

@JvmName("imperialMolarityTimesUKImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>.times(molarMass: ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>) =
    molarMass * this

@JvmName("imperialMolarityTimesUSCustomaryMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, ImperialMolarity>.times(molarMass: ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>) =
    molarMass * this

@JvmName("ukImperialMolarityTimesImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, UKImperialMolarity>.times(molarMass: ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>) =
    molarMass * this

@JvmName("ukImperialMolarityTimesUKImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, UKImperialMolarity>.times(molarMass: ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>) =
    molarMass * this

@JvmName("usCustomaryMolarityTimesImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, USCustomaryMolarity>.times(molarMass: ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>) =
    molarMass * this

@JvmName("usCustomaryMolarityTimesUSCustomaryMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.Molarity, USCustomaryMolarity>.times(molarMass: ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>) =
    molarMass * this

@JvmName("molarityTimesMolarMass")
infix operator fun <MolarityUnit : Molarity, MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.Molarity, MolarityUnit>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = molarMass * this
