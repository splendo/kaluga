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

package com.splendo.kaluga.scientific.converter.molarEnergy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificEnergy.specificEnergy
import com.splendo.kaluga.scientific.unit.ImperialMolality
import com.splendo.kaluga.scientific.unit.ImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.ImperialMolarMass
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricAndImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricMolality
import com.splendo.kaluga.scientific.unit.MetricMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricMolarMass
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarEnergy
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.UKImperialMolality
import com.splendo.kaluga.scientific.unit.UKImperialMolarMass
import com.splendo.kaluga.scientific.unit.USCustomaryMolality
import com.splendo.kaluga.scientific.unit.USCustomaryMolarMass
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialMolarEnergyDivMetricMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, MetricMolarMass>) =
    (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)

@JvmName("metricAndImperialMolarEnergyDivImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>) =
    (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)

@JvmName("metricAndImperialMolarEnergyDivUKImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>) =
    (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)

@JvmName("metricAndImperialMolarEnergyDivUSCustomaryMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>) =
    (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)

@JvmName("metricMolarEnergyDivMetricMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricMolarEnergy>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, MetricMolarMass>) =
    (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)

@JvmName("imperialMolarEnergyDivImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, ImperialMolarEnergy>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>) =
    (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)

@JvmName("imperialMolarEnergyDivUKImperialMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, ImperialMolarEnergy>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>) =
    (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)

@JvmName("imperialMolarEnergyDivUSCustomaryMolarMass")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, ImperialMolarEnergy>.div(molarMass: ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>) =
    (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)

@JvmName("molarEnergyDivMolarMass")
infix operator fun <MolarEnergyUnit : MolarEnergy, MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>.div(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = (Joule per Kilogram).specificEnergy(this, molarMass)

@JvmName("metricAndImperialMolarEnergyTimesMetricMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<PhysicalQuantity.Molality, MetricMolality>) =
    (unit.energy per molality.unit.per).specificEnergy(this, molality)

@JvmName("metricAndImperialMolarEnergyTimesImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<PhysicalQuantity.Molality, ImperialMolality>) =
    (unit.energy per molality.unit.per).specificEnergy(this, molality)

@JvmName("metricAndImperialMolarEnergyTimesUKImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<PhysicalQuantity.Molality, UKImperialMolality>) =
    (unit.energy per molality.unit.per).specificEnergy(this, molality)

@JvmName("metricAndImperialMolarEnergyTimesUSCustomaryMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<PhysicalQuantity.Molality, USCustomaryMolality>) =
    (unit.energy per molality.unit.per).specificEnergy(this, molality)

@JvmName("metricMolarEnergyTimesMetricMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, MetricMolarEnergy>.times(molality: ScientificValue<PhysicalQuantity.Molality, MetricMolality>) =
    (unit.energy per molality.unit.per).specificEnergy(this, molality)

@JvmName("imperialMolarEnergyTimesImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, ImperialMolarEnergy>.times(molality: ScientificValue<PhysicalQuantity.Molality, ImperialMolality>) =
    (unit.energy per molality.unit.per).specificEnergy(this, molality)

@JvmName("imperialMolarEnergyTimesUKImperialMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, ImperialMolarEnergy>.times(molality: ScientificValue<PhysicalQuantity.Molality, UKImperialMolality>) =
    (unit.energy per molality.unit.per).specificEnergy(this, molality)

@JvmName("imperialMolarEnergyTimesUSCustomaryMolality")
infix operator fun ScientificValue<PhysicalQuantity.MolarEnergy, ImperialMolarEnergy>.times(molality: ScientificValue<PhysicalQuantity.Molality, USCustomaryMolality>) =
    (unit.energy per molality.unit.per).specificEnergy(this, molality)

@JvmName("molarEnergyTimesMolality")
infix operator fun <MolarEnergyUnit : MolarEnergy, MolalityUnit : Molality> ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>.times(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
) = (Joule per Kilogram).specificEnergy(this, molality)
