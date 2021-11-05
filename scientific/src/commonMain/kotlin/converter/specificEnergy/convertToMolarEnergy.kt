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

package com.splendo.kaluga.scientific.converter.specificEnergy

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarEnergy.molarEnergy
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificEnergyTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (unit.energy per molarMass.unit.per).molarEnergy(this, molarMass)
@JvmName("imperialSpecificEnergyTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (unit.energy per molarMass.unit.per).molarEnergy(this, molarMass)
@JvmName("ukImperialSpecificEnergyTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (unit.energy per molarMass.unit.per).molarEnergy(this, molarMass)
@JvmName("usCustomarySpecificEnergyTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (unit.energy per molarMass.unit.per).molarEnergy(this, molarMass)
@JvmName("specificEnergyTimesMolarMass")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (Joule per molarMass.unit.per).molarEnergy(this, molarMass)

@JvmName("metricSpecificEnergyDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.energy per molality.unit.amountOfSubstance).molarEnergy(this, molality)
@JvmName("imperialSpecificEnergyDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.energy per molality.unit.amountOfSubstance).molarEnergy(this, molality)
@JvmName("ukImperialSpecificEnergyDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.energy per molality.unit.amountOfSubstance).molarEnergy(this, molality)
@JvmName("usCustomarySpecificEnergyDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.energy per molality.unit.amountOfSubstance).molarEnergy(this, molality)
@JvmName("specificEnergyDivMolality")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (Joule per molality.unit.amountOfSubstance).molarEnergy(this, molality)
