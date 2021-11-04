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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.Gray
import com.splendo.kaluga.scientific.IonizingRadiationAbsorbedDose
import com.splendo.kaluga.scientific.Joule
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificEnergy
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("specificEnergyFromAbsorbedDoseDefault")
fun <
    AbsorbedDoseUnit : IonizingRadiationAbsorbedDose,
    SpecificEnergyUnit : SpecificEnergy
    > SpecificEnergyUnit.specificEnergy(
    absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>
) = specificEnergy(absorbedDose, ::DefaultScientificValue)

@JvmName("specificEnergyFromAbsorbedDose")
fun <
    AbsorbedDoseUnit : IonizingRadiationAbsorbedDose,
    SpecificEnergyUnit : SpecificEnergy,
    Value : ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>
    > SpecificEnergyUnit.specificEnergy(
    absorbedDose: ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>,
    factory: (Decimal, SpecificEnergyUnit) -> Value
) = DefaultScientificValue(absorbedDose.convert(Gray).value, Joule per Kilogram).convert(this, factory)