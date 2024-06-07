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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose.times
import com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose.times
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.IonizingRadiationAbsorbedDose
import com.splendo.kaluga.scientific.unit.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.RadMultiple
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentManMultiple
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("gramTimesRad")
infix operator fun ScientificValue<PhysicalQuantity.Weight, Gram>.times(absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad>) = absorbedDose * this

@JvmName("gramTimesRadMultiple")
infix operator fun <AbsorbedDoseUnit : RadMultiple> ScientificValue<PhysicalQuantity.Weight, Gram>.times(
    absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>,
) = absorbedDose * this

@JvmName("imperialWeightTimesAbsorbedDose")
infix operator fun <AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>,
) = absorbedDose * this

@JvmName("ukImperialWeightTimesAbsorbedDose")
infix operator fun <AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>,
) = absorbedDose * this

@JvmName("usCustomaryWeightTimesAbsorbedDose")
infix operator fun <AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>,
) = absorbedDose * this

@JvmName("weightTimesAbsorbedDose")
infix operator fun <AbsorbedDoseUnit : IonizingRadiationAbsorbedDose, WeightUnit : Weight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    absorbedDose: ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>,
) = absorbedDose * this

@JvmName("gramTimesRoentgenEquivalentMan")
infix operator fun ScientificValue<PhysicalQuantity.Weight, Gram>.times(equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>) =
    equivalentDose * this

@JvmName("gramTimesRoentgenEquivalentManMultiple")
infix operator fun <EquivalentDoseUnit : RoentgenEquivalentManMultiple> ScientificValue<PhysicalQuantity.Weight, Gram>.times(
    equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, EquivalentDoseUnit>,
) = equivalentDose * this

@JvmName("imperialWeightTimesEquivalentDose")
infix operator fun <EquivalentDoseUnit : IonizingRadiationEquivalentDose, WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, EquivalentDoseUnit>,
) = equivalentDose * this

@JvmName("ukImperialWeightTimesEquivalentDose")
infix operator fun <EquivalentDoseUnit : IonizingRadiationEquivalentDose, WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, EquivalentDoseUnit>,
) = equivalentDose * this

@JvmName("usCustomaryWeightTimesEquivalentDose")
infix operator fun <EquivalentDoseUnit : IonizingRadiationEquivalentDose, WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, EquivalentDoseUnit>,
) = equivalentDose * this

@JvmName("weightTimesEquivalentDose")
infix operator fun <EquivalentDoseUnit : IonizingRadiationEquivalentDose, WeightUnit : Weight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    equivalentDose: ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, EquivalentDoseUnit>,
) = equivalentDose * this

@JvmName("metricWeightTimesMetricSpecificEnergy")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>,
) = specificEnergy * this

@JvmName("imperialWeightTimesImperialSpecificEnergy")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = specificEnergy * this

@JvmName("ukImperialWeightTimesImperialSpecificEnergy")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = specificEnergy * this

@JvmName("usCustomaryWeightTimesImperialSpecificEnergy")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>,
) = specificEnergy * this

@JvmName("imperialWeightTimesUKImperialSpecificEnergy")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>,
) = specificEnergy * this

@JvmName("ukImperialWeightTimesUKImperialSpecificEnergy")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>,
) = specificEnergy * this

@JvmName("imperialWeightTimesUSCustomarySpecificEnergy")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>,
) = specificEnergy * this

@JvmName("usCustomaryWeightTimesUSCustomarySpecificEnergy")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>,
) = specificEnergy * this

@JvmName("weightTimesSpecificEnergy")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, WeightUnit : Weight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
) = specificEnergy * this
