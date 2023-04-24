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
import com.splendo.kaluga.scientific.converter.angularAcceleration.times
import com.splendo.kaluga.scientific.unit.AngularAcceleration
import com.splendo.kaluga.scientific.unit.Time

val PhysicalQuantity.AngularAcceleration.converters get() = listOf<QuantityConverter<PhysicalQuantity.AngularAcceleration, *>>(
    QuantityConverterWithOperator(
        "Angular Velocity from Time",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Time,
    ) { (angularAccelerationValue, angularAccelerationUnit), (timeValue, timeUnit) ->
        when {
            angularAccelerationUnit is AngularAcceleration && timeUnit is Time -> {
                DefaultScientificValue(angularAccelerationValue, angularAccelerationUnit) * DefaultScientificValue(
                    timeValue,
                    timeUnit,
                )
            }
            else -> throw RuntimeException("Unexpected units: $angularAccelerationUnit $timeUnit")
        }
    },
)
