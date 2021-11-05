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

package com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.IonizingRadiationAbsorbedDose
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("radTimesWeight")
infix operator fun <WeightUnit : Weight> ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, Rad>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = Erg.energy(this, weight)
@JvmName("radMultipleTimesWeight")
infix operator fun <AbsorbedDoseUnit, WeightUnit : Weight> ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) where AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, AbsorbedDoseUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> = Erg.energy(this, weight)
@JvmName("absorbedDoseTimesWeight")
infix operator fun <AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, WeightUnit : Weight> ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = Joule.energy(this, weight)
