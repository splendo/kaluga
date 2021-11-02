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

package com.splendo.kaluga.scientific.converter.molarEnergy

import com.splendo.kaluga.scientific.ImperialMolality
import com.splendo.kaluga.scientific.ImperialMolarEnergy
import com.splendo.kaluga.scientific.ImperialMolarMass
import com.splendo.kaluga.scientific.Joule
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAndImperialMolarEnergy
import com.splendo.kaluga.scientific.MetricMolality
import com.splendo.kaluga.scientific.MetricMolarEnergy
import com.splendo.kaluga.scientific.MetricMolarMass
import com.splendo.kaluga.scientific.Molality
import com.splendo.kaluga.scientific.MolarEnergy
import com.splendo.kaluga.scientific.MolarMass
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialMolality
import com.splendo.kaluga.scientific.UKImperialMolarMass
import com.splendo.kaluga.scientific.USCustomaryMolality
import com.splendo.kaluga.scientific.USCustomaryMolarMass
import com.splendo.kaluga.scientific.converter.specificEnergy.specificEnergy
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialMolarEnergyDivMetricMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MetricMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("metricAndImperialMolarEnergyDivImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("metricAndImperialMolarEnergyDivUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("metricAndImperialMolarEnergyDivUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("metricMolarEnergyDivMetricMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MetricMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("imperialMolarEnergyDivImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("imperialMolarEnergyDivUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("imperialMolarEnergyDivUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("molarEnergyDivMolarMass")
infix operator fun <MolarEnergyUnit : MolarEnergy, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (Joule per Kilogram).specificEnergy(this, molarMass)

@JvmName("metricAndImperialMolarEnergyTimesMetricMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, MetricMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("metricAndImperialMolarEnergyTimesImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("metricAndImperialMolarEnergyTimesUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("metricAndImperialMolarEnergyTimesUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("metricMolarEnergyTimesMetricMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, MetricMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("imperialMolarEnergyTimesImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("imperialMolarEnergyTimesUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("imperialMolarEnergyTimesUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("molarEnergyTimesMolality")
infix operator fun <MolarEnergyUnit : MolarEnergy, MolalityUnit : Molality> ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (Joule per Kilogram).specificEnergy(this, molality)
