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
import com.splendo.kaluga.scientific.converter.acceleration.div
import com.splendo.kaluga.scientific.converter.acceleration.times
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.MetricAcceleration
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.Weight

val PhysicalQuantity.Acceleration.converters get() = listOf<QuantityConverter<PhysicalQuantity.Acceleration, *>>(
    QuantityConverterWithOperator("Force from Weight", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Weight) { (accelerationValue, accelerationUnit), (weightValue, weightUnit) ->
        when {
            accelerationUnit is MetricAcceleration && weightUnit is Gram -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is MetricAcceleration && weightUnit is MetricWeight -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is ImperialAcceleration && weightUnit is Pound -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is ImperialAcceleration && weightUnit is Ounce -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is ImperialAcceleration && weightUnit is Grain -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is ImperialAcceleration && weightUnit is UsTon -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is ImperialAcceleration && weightUnit is ImperialTon -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is ImperialAcceleration && weightUnit is ImperialWeight -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is ImperialAcceleration && weightUnit is UKImperialWeight -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is ImperialAcceleration && weightUnit is USCustomaryWeight -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            accelerationUnit is Acceleration && weightUnit is Weight -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(weightValue, weightUnit)
            else -> throw RuntimeException("Unexpected units: $accelerationUnit, $weightUnit")
        }
    },
    QuantityConverterWithOperator("Jolt from Time", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Time) { (accelerationValue, accelerationUnit), (timeValue, timeUnit) ->
        when {
            accelerationUnit is MetricAcceleration && timeUnit is Time -> DefaultScientificValue(accelerationValue, accelerationUnit) / DefaultScientificValue(timeValue, timeUnit)
            accelerationUnit is ImperialAcceleration && timeUnit is Time -> DefaultScientificValue(accelerationValue, accelerationUnit) / DefaultScientificValue(timeValue, timeUnit)
            accelerationUnit is Acceleration && timeUnit is Time -> DefaultScientificValue(accelerationValue, accelerationUnit) / DefaultScientificValue(timeValue, timeUnit)
            else -> throw RuntimeException("Unexpected units: $accelerationUnit, $timeUnit")
        }
    },
    QuantityConverterWithOperator("Speed from Time", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Time) { (accelerationValue, accelerationUnit), (timeValue, timeUnit) ->
        when {
            accelerationUnit is MetricAcceleration && timeUnit is Time -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(timeValue, timeUnit)
            accelerationUnit is ImperialAcceleration && timeUnit is Time -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(timeValue, timeUnit)
            accelerationUnit is Acceleration && timeUnit is Time -> DefaultScientificValue(accelerationValue, accelerationUnit) * DefaultScientificValue(timeValue, timeUnit)
            else -> throw RuntimeException("Unexpected units: $accelerationUnit, $timeUnit")
        }
    },
    QuantityConverterWithOperator("Time from Jolt", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Jolt) { (accelerationValue, accelerationUnit), (joltValue, joltUnit) ->
        when {
            accelerationUnit is Acceleration && joltUnit is Jolt -> DefaultScientificValue(accelerationValue, accelerationUnit) / DefaultScientificValue(joltValue, joltUnit)
            else -> throw RuntimeException("Unexpected units: $accelerationUnit, $joltUnit")
        }
    }
)
