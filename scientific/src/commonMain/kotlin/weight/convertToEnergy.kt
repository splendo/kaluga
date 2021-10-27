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

package com.splendo.kaluga.scientific.weight

import com.splendo.kaluga.scientific.IonizingRadiationAbsorbedDose
import com.splendo.kaluga.scientific.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.Rad
import com.splendo.kaluga.scientific.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.ionizingRadiationAbsorbedDose.times
import com.splendo.kaluga.scientific.ionizingRadiationEquivalentDose.times
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
