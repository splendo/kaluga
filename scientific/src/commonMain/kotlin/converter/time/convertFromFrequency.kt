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

package com.splendo.kaluga.scientific.converter.time

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byInverting
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.convertValue
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.Hertz
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("timeFromInvertedFrequencyDefault")
fun <
    TimeUnit : Time,
    FrequencyUnit : Frequency,
    > TimeUnit.time(frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>) =
    time(frequency, ::DefaultScientificValue)

@JvmName("timeFromInvertedFrequency")
fun <
    TimeUnit : Time,
    FrequencyUnit : Frequency,
    Value : ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    > TimeUnit.time(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
    factory: (Decimal, TimeUnit) -> Value,
) = byInverting(frequency, factory)

fun <
    FrequencyUnit : Frequency,
    TimeUnit : Time,
    > TimeUnit.time(cycle: Decimal, at: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>) =
    time(cycle, at, ::DefaultScientificValue)

fun <
    FrequencyUnit : Frequency,
    TimeUnit : Time,
    Value : ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    > TimeUnit.time(
    cycle: Decimal,
    at: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
    factory: (Decimal, TimeUnit) -> Value,
) = (cycle / at.convertValue(Hertz))(Second).convert(this, factory)
