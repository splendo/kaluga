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

import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.Gram
import com.splendo.kaluga.scientific.Gray
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.Rad
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose.absorbedDose
import kotlin.jvm.JvmName

@JvmName("ergAbsorbedByGram")
infix fun ScientificValue<MeasurementType.Energy, Erg>.absorbedBy(gram: ScientificValue<MeasurementType.Weight, Gram>) = Rad.absorbedDose(this, gram)
@JvmName("ergMultipleAbsorbedByGram")
infix fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.absorbedBy(gram: ScientificValue<MeasurementType.Weight, Gram>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Rad.absorbedDose(this, gram)
@JvmName("energyAbsorbedByWeight")
infix fun <EnergyUnit : Energy, WeightUnit : Weight> ScientificValue<MeasurementType.Energy, EnergyUnit>.absorbedBy(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = Gray.absorbedDose(this, weight)
