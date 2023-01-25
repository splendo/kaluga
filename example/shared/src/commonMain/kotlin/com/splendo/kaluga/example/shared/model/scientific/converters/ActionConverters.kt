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
import com.splendo.kaluga.scientific.converter.action.div
import com.splendo.kaluga.scientific.unit.Action
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.ImperialAction
import com.splendo.kaluga.scientific.unit.MetricAction
import com.splendo.kaluga.scientific.unit.MetricAndImperialAction
import com.splendo.kaluga.scientific.unit.Time

val PhysicalQuantity.Action.converters get() = listOf<QuantityConverter<PhysicalQuantity.Action, *>>(
    QuantityConverterWithOperator("Energy from Time", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Time) { (actionValue, actionUnit), (timeValue, timeUnit) ->
        when {
            actionUnit is MetricAndImperialAction && timeUnit is Time -> DefaultScientificValue(actionValue, actionUnit) / DefaultScientificValue(timeValue, timeUnit)
            actionUnit is MetricAction && timeUnit is Time -> DefaultScientificValue(actionValue, actionUnit) / DefaultScientificValue(timeValue, timeUnit)
            actionUnit is ImperialAction && timeUnit is Time -> DefaultScientificValue(actionValue, actionUnit) / DefaultScientificValue(timeValue, timeUnit)
            actionUnit is Action && timeUnit is Time -> DefaultScientificValue(actionValue, actionUnit) / DefaultScientificValue(timeValue, timeUnit)
            else -> throw RuntimeException("Unexpected units: $actionUnit, $timeUnit")
        }
    },
    QuantityConverterWithOperator("Time from Energy", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Energy) { (actionValue, actionUnit), (energyValue, energyUnit) ->
        when {
            actionUnit is Action && energyUnit is Energy -> DefaultScientificValue(actionValue, actionUnit) / DefaultScientificValue(energyValue, energyUnit)
            else -> throw RuntimeException("Unexpected units: $actionUnit, $energyUnit")
        }
    }
)
