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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.weight
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.IonizingRadiationAbsorbedDose
import com.splendo.kaluga.scientific.unit.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import kotlin.jvm.JvmName

@JvmName("ergDivRad")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, Rad>) = Gram.weight(this, absorbedDose)
@JvmName("ergMultipleDivRad")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, Rad>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Gram.weight(this, absorbedDose)
@JvmName("ergMultipleDivRadMultiple")
infix operator fun <AbsorbedDoseUnit> ScientificValue<MeasurementType.Energy, Erg>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) where
    AbsorbedDoseUnit : IonizingRadiationAbsorbedDose,
    AbsorbedDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad>
    = Gram.weight(this, absorbedDose)
@JvmName("ergMultipleDivAbsorbedDose")
infix operator fun <ErgUnit, AbsorbedDoseUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) where
    ErgUnit : Energy,
    ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg>,
    AbsorbedDoseUnit : IonizingRadiationAbsorbedDose,
    AbsorbedDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad>
    = Gram.weight(this, absorbedDose)
@JvmName("energyDivAbsorbedDose")
infix operator fun <EnergyUnit : Energy, AbsorbedDoseUnit : IonizingRadiationAbsorbedDose> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) = Kilogram.weight(this, absorbedDose)

@JvmName("ergDivRem")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>) = Gram.weight(this, equivalentDose)
@JvmName("ergMultipleDivRem")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Gram.weight(this, equivalentDose)
@JvmName("ergDivRemMultiple")
infix operator fun <EquivalentDoseUnit> ScientificValue<MeasurementType.Energy, Erg>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) where
    EquivalentDoseUnit : IonizingRadiationEquivalentDose,
    EquivalentDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>
    = Gram.weight(this, equivalentDose)
@JvmName("ergMultipleDivRemMultiple")
infix operator fun <ErgUnit, EquivalentDoseUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) where
    ErgUnit : Energy,
    ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg>,
    EquivalentDoseUnit : IonizingRadiationEquivalentDose,
    EquivalentDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>
    = Gram.weight(this, equivalentDose)
@JvmName("energyDivEquivalentDose")
infix operator fun <EnergyUnit : Energy, EquivalentDoseUnit : IonizingRadiationEquivalentDose> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) = Kilogram.weight(this, equivalentDose)

@JvmName("metricAndImperialEnergyDivMetricSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("metricAndImperialEnergyDivImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("metricAndImperialEnergyDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("metricAndImperialEnergyDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("metricEnergyDivMetricSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("imperialEnergyDivImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("imperialEnergyDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("imperialEnergyDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("energyDivSpecificEnergy")
infix operator fun <EnergyUnit : Energy, SpecificEnergyUnit : SpecificEnergy> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>) = specificEnergy.unit.per.weight(this, specificEnergy)
