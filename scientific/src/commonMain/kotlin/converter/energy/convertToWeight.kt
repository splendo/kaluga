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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.weight
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.IonizingRadiationAbsorbedDose
import com.splendo.kaluga.scientific.unit.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.RadMultiple
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentManMultiple
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import kotlin.jvm.JvmName

@JvmName("ergDivRad")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad>) =
    Gram.weight(this, absorbedDose)

@JvmName("ergMultipleDivRad")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(
    absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad>,
) = Gram.weight(this, absorbedDose)

@JvmName("ergDivRadMultiple")
infix operator fun <RadUnit : RadMultiple> ScientificValue<PhysicalQuantity.Energy, Erg>.div(
    absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, RadUnit>,
) = Gram.weight(this, absorbedDose)

@JvmName("ergMultipleDivRadMultiple")
infix operator fun <ErgUnit : ErgMultiple, RadUnit : RadMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(
    absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, RadUnit>,
) = Gram.weight(this, absorbedDose)

@JvmName("energyDivAbsorbedDose")
infix operator fun <EnergyUnit : Energy, AbsorbedDoseUnit : IonizingRadiationAbsorbedDose> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>,
) = Kilogram.weight(this, absorbedDose)

@JvmName("ergDivRem")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>) =
    Gram.weight(this, equivalentDose)

@JvmName("ergMultipleDivRem")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(
    equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>,
) = Gram.weight(this, equivalentDose)

@JvmName("ergDivRemMultiple")
infix operator fun <RemUnit : RoentgenEquivalentManMultiple> ScientificValue<PhysicalQuantity.Energy, Erg>.div(
    equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, RemUnit>,
) = Gram.weight(this, equivalentDose)

@JvmName("ergMultipleDivRemMultiple")
infix operator fun <ErgUnit : ErgMultiple, RemUnit : RoentgenEquivalentManMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(
    equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, RemUnit>,
) = Gram.weight(this, equivalentDose)

@JvmName("energyDivEquivalentDose")
infix operator fun <EnergyUnit : Energy, EquivalentDoseUnit : IonizingRadiationEquivalentDose> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, EquivalentDoseUnit>,
) = Kilogram.weight(this, equivalentDose)

@JvmName("metricAndImperialEnergyDivMetricSpecificEnergy")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>,
) = specificEnergy.unit.per.weight(this, specificEnergy)

@JvmName("metricAndImperialEnergyDivImperialSpecificEnergy")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = specificEnergy.unit.per.weight(this, specificEnergy)

@JvmName("metricAndImperialEnergyDivUKImperialSpecificEnergy")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>,
) = specificEnergy.unit.per.weight(this, specificEnergy)

@JvmName("metricAndImperialEnergyDivUSCustomarySpecificEnergy")
infix operator fun <EnergyUnit : MetricAndImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>,
) = specificEnergy.unit.per.weight(this, specificEnergy)

@JvmName("metricEnergyDivMetricSpecificEnergy")
infix operator fun <EnergyUnit : MetricEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>,
) = specificEnergy.unit.per.weight(this, specificEnergy)

@JvmName("imperialEnergyDivImperialSpecificEnergy")
infix operator fun <EnergyUnit : ImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = specificEnergy.unit.per.weight(this, specificEnergy)

@JvmName("imperialEnergyDivUKImperialSpecificEnergy")
infix operator fun <EnergyUnit : ImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>,
) = specificEnergy.unit.per.weight(this, specificEnergy)

@JvmName("imperialEnergyDivUSCustomarySpecificEnergy")
infix operator fun <EnergyUnit : ImperialEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>,
) = specificEnergy.unit.per.weight(this, specificEnergy)

@JvmName("energyDivSpecificEnergy")
infix operator fun <EnergyUnit : Energy, SpecificEnergyUnit : SpecificEnergy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
) = specificEnergy.unit.per.weight(this, specificEnergy)
