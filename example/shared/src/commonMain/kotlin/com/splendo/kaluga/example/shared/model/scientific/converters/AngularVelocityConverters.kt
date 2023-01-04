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
import com.splendo.kaluga.scientific.converter.angularVelocity.div
import com.splendo.kaluga.scientific.converter.angularVelocity.times
import com.splendo.kaluga.scientific.unit.AngularAcceleration
import com.splendo.kaluga.scientific.unit.AngularAccelerationUnits
import com.splendo.kaluga.scientific.unit.AngularVelocity
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.TimeUnits

val PhysicalQuantity.AngularVelocity.converters get() = listOf<QuantityConverter<PhysicalQuantity.AngularVelocity, *, *>>(
    QuantityConverter("Angle from Time", QuantityConverter.Type.Multiplication, TimeUnits) { (angularVelocityValue, angularVelocityUnit), (timeValue, timeUnit) ->
        when {
            angularVelocityUnit is AngularVelocity && timeUnit is Time -> DefaultScientificValue(angularVelocityValue, angularVelocityUnit) * DefaultScientificValue(timeValue, timeUnit)
            else -> throw RuntimeException("Unexpected units: $angularVelocityUnit $timeUnit")
        }
    },
    QuantityConverter("Angular Acceleration from Time", QuantityConverter.Type.Division, TimeUnits) { (angularVelocityValue, angularVelocityUnit), (timeValue, timeUnit) ->
        when {
            angularVelocityUnit is AngularVelocity && timeUnit is Time -> DefaultScientificValue(angularVelocityValue, angularVelocityUnit) / DefaultScientificValue(timeValue, timeUnit)
            else -> throw RuntimeException("Unexpected units: $angularVelocityUnit $timeUnit")
        }
    },
    QuantityConverter("Time from Angular Acceleration", QuantityConverter.Type.Division, AngularAccelerationUnits) { (angularVelocityValue, angularVelocityUnit), (angularAccelerationValue, angularAccelerationUnit) ->
        when {
            angularVelocityUnit is AngularVelocity && angularAccelerationUnit is AngularAcceleration -> DefaultScientificValue(angularVelocityValue, angularVelocityUnit) / DefaultScientificValue(angularAccelerationValue, angularAccelerationUnit)
            else -> throw RuntimeException("Unexpected units: $angularVelocityUnit $angularAccelerationUnit")
        }
    }
)
