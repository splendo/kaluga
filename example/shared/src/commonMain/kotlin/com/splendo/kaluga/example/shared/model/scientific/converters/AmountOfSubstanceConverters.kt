/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.model.scientific.converters

import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.converter.amountOfSubstance.decaysWithHalfLife
import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.converter.amountOfSubstance.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.AmountOfSubstance.converters get() = listOf<QuantityConverter<PhysicalQuantity.AmountOfSubstance, *, *>>(
    QuantityConverter("Energy from Molar Energy", QuantityConverter.Type.Multiplication, PhysicalQuantity.MolarEnergy) { (amountOfSubstanceValue, amountOfSubstanceUnit), (molarEnergyValue, molarEnergyUnit) ->
        when {
            amountOfSubstanceUnit is AmountOfSubstance && molarEnergyUnit is MetricAndImperialMolarEnergy -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) * DefaultScientificValue(molarEnergyValue, molarEnergyUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molarEnergyUnit is MetricMolarEnergy -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) * DefaultScientificValue(molarEnergyValue, molarEnergyUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molarEnergyUnit is ImperialMolarEnergy -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) * DefaultScientificValue(molarEnergyValue, molarEnergyUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molarEnergyUnit is MolarEnergy -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) * DefaultScientificValue(molarEnergyValue, molarEnergyUnit)
            else -> throw RuntimeException("Unexpected units: $amountOfSubstanceUnit, $molarEnergyUnit")
        }
    },
    QuantityConverter("Molality from Weight", QuantityConverter.Type.Division, PhysicalQuantity.Weight) { (amountOfSubstanceValue, amountOfSubstanceUnit), (weightValue, weightUnit) ->
        when {
            amountOfSubstanceUnit is AmountOfSubstance && weightUnit is MetricWeight -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(weightValue, weightUnit)
            amountOfSubstanceUnit is AmountOfSubstance && weightUnit is ImperialWeight -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(weightValue, weightUnit)
            amountOfSubstanceUnit is AmountOfSubstance && weightUnit is UKImperialWeight -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(weightValue, weightUnit)
            amountOfSubstanceUnit is AmountOfSubstance && weightUnit is USCustomaryWeight -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(weightValue, weightUnit)
            amountOfSubstanceUnit is AmountOfSubstance && weightUnit is Weight -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(weightValue, weightUnit)
            else -> throw RuntimeException("Unexpected units: $amountOfSubstanceUnit, $weightUnit")
        }
    },
    QuantityConverter("Molarity from Volume", QuantityConverter.Type.Division, PhysicalQuantity.Volume) { (amountOfSubstanceValue, amountOfSubstanceUnit), (volumeValue, volumeUnit) ->
        when {
            amountOfSubstanceUnit is AmountOfSubstance && volumeUnit is MetricVolume -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(volumeValue, volumeUnit)
            amountOfSubstanceUnit is AmountOfSubstance && volumeUnit is ImperialVolume -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(volumeValue, volumeUnit)
            amountOfSubstanceUnit is AmountOfSubstance && volumeUnit is UKImperialVolume -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(volumeValue, volumeUnit)
            amountOfSubstanceUnit is AmountOfSubstance && volumeUnit is USCustomaryVolume -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(volumeValue, volumeUnit)
            amountOfSubstanceUnit is AmountOfSubstance && volumeUnit is Volume -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(volumeValue, volumeUnit)
            else -> throw RuntimeException("Unexpected units: $amountOfSubstanceUnit, $volumeUnit")
        }
    },
    QuantityConverter("Radioactivity from Time", QuantityConverter.Type.Custom("with λ"), PhysicalQuantity.Time) { (amountOfSubstanceValue, amountOfSubstanceUnit), (timeValue, timeUnit) ->
        when {
            amountOfSubstanceUnit is AmountOfSubstance && timeUnit is Time -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit).decaysWithHalfLife(DefaultScientificValue(timeValue, timeUnit))
            else -> throw RuntimeException("Unexpected units: $amountOfSubstanceUnit, $timeUnit")
        }
    },
    QuantityConverter("Time from Catalystic Activity", QuantityConverter.Type.Division, PhysicalQuantity.CatalysticActivity) { (amountOfSubstanceValue, amountOfSubstanceUnit), (catalysticActivityValue, catalysticActivityUnit) ->
        when {
            amountOfSubstanceUnit is AmountOfSubstance && catalysticActivityUnit is CatalysticActivity -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(catalysticActivityValue, catalysticActivityUnit)
            else -> throw RuntimeException("Unexpected units: $amountOfSubstanceUnit, $catalysticActivityUnit")
        }
    },
    QuantityConverter("Volume from Molarity", QuantityConverter.Type.Division, PhysicalQuantity.Molarity) { (amountOfSubstanceValue, amountOfSubstanceUnit), (molarityValue, molarityUnit) ->
        when {
            amountOfSubstanceUnit is AmountOfSubstance && molarityUnit is MetricMolarity -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molarityValue, molarityUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molarityUnit is ImperialMolarity -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molarityValue, molarityUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molarityUnit is UKImperialMolarity -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molarityValue, molarityUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molarityUnit is USCustomaryMolarity -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molarityValue, molarityUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molarityUnit is Molarity -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molarityValue, molarityUnit)
            else -> throw RuntimeException("Unexpected units: $amountOfSubstanceUnit, $molarityUnit")
        }
    },
    QuantityConverter("Weight from Molality", QuantityConverter.Type.Division, PhysicalQuantity.Molality) { (amountOfSubstanceValue, amountOfSubstanceUnit), (molalityValue, molalityUnit) ->
        when {
            amountOfSubstanceUnit is AmountOfSubstance && molalityUnit is MetricMolality -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molalityValue, molalityUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molalityUnit is ImperialMolality -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molalityValue, molalityUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molalityUnit is UKImperialMolality -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molalityValue, molalityUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molalityUnit is USCustomaryMolality -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molalityValue, molalityUnit)
            amountOfSubstanceUnit is AmountOfSubstance && molalityUnit is Molality -> DefaultScientificValue(amountOfSubstanceValue, amountOfSubstanceUnit) / DefaultScientificValue(molalityValue, molalityUnit)
            else -> throw RuntimeException("Unexpected units: $amountOfSubstanceUnit, $molalityUnit")
        }
    }
)
