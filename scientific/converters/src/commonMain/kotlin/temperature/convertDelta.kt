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

package com.splendo.kaluga.scientific.converter.temperature

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.convertDelta

fun <
    TemperatureUnit : Temperature,
    TargetTemperatureUnit : Temperature,
    > TargetTemperatureUnit.deltaValue(
    value: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
) = deltaValue(value, ::DefaultScientificValue)

fun <
    TemperatureUnit : Temperature,
    TargetTemperatureUnit : Temperature,
    Value : ScientificValue<PhysicalQuantity.Temperature, TargetTemperatureUnit>,
    > TargetTemperatureUnit.deltaValue(
    value: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
    factory: (Decimal, TargetTemperatureUnit) -> Value,
) = factory(value.unit.convertDelta(value.decimalValue, this), this)
