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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose.times
import com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose.times
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.IonizingRadiationAbsorbedDose
import com.splendo.kaluga.scientific.unit.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("weightTimesRad")
infix operator fun <WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, Rad>) = absorbedDose * this
@JvmName("weightTimesRadMultiple")
infix operator fun <AbsorbedDoseUnit, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) where AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, AbsorbedDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> = absorbedDose * this
@JvmName("weightTimesAbsorbedDose")
infix operator fun <AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>) = absorbedDose * this

@JvmName("weightTimesRoentgenEquivalentMan")
infix operator fun <WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>) = equivalentDose * this
@JvmName("weightTimesRoentgenEquivalentManMultiple")
infix operator fun <EquivalentDoseUnit, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) where EquivalentDoseUnit : IonizingRadiationEquivalentDose, EquivalentDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> = equivalentDose * this
@JvmName("weightTimesEquivalentDose")
infix operator fun <EquivalentDoseUnit : IonizingRadiationEquivalentDose, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(equivalentDose: ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit>) = equivalentDose * this

@JvmName("metricWeightTimesMetricSpecificEnergy")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = specificEnergy * this
@JvmName("imperialWeightTimesImperialSpecificEnergy")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy * this
@JvmName("ukImperialWeightTimesImperialSpecificEnergy")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy * this
@JvmName("usCustomaryWeightTimesImperialSpecificEnergy")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy * this
@JvmName("imperialWeightTimesUKImperialSpecificEnergy")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy * this
@JvmName("ukImperialWeightTimesUKImperialSpecificEnergy")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy * this
@JvmName("imperialWeightTimesUSCustomarySpecificEnergy")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy * this
@JvmName("usCustomaryWeightTimesUSCustomarySpecificEnergy")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy * this
@JvmName("weightTimesSpecificEnergy")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>) = specificEnergy * this
