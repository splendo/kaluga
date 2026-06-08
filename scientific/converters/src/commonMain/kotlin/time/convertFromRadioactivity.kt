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
import com.splendo.kaluga.scientific.unit.Becquerel
import com.splendo.kaluga.scientific.unit.Radioactivity
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("timeFromInvertedRadioactivityDefault")
fun <
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity,
    > TimeUnit.time(radioactivity: ScientificValue<PhysicalQuantity.Radioactivity, RadioactivityUnit>) =
    time(radioactivity, ::DefaultScientificValue)

@JvmName("timeFromInvertedRadioactivity")
fun <
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity,
    Value : ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    > TimeUnit.time(
    radioactivity: ScientificValue<PhysicalQuantity.Radioactivity, RadioactivityUnit>,
    factory: (Decimal, TimeUnit) -> Value,
) = byInverting(radioactivity, factory)

fun <
    RadioactivityUnit : Radioactivity,
    TimeUnit : Time,
    > TimeUnit.time(decay: Decimal, at: ScientificValue<PhysicalQuantity.Radioactivity, RadioactivityUnit>) =
    time(decay, at, ::DefaultScientificValue)

fun <
    RadioactivityUnit : Radioactivity,
    TimeUnit : Time,
    Value : ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    > TimeUnit.time(
    decay: Decimal,
    at: ScientificValue<PhysicalQuantity.Radioactivity, RadioactivityUnit>,
    factory: (Decimal, TimeUnit) -> Value,
) = (decay / at.convertValue(Becquerel))(Second).convert(this, factory)
