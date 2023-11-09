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

package com.splendo.kaluga.scientific.converter.radioactivity

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

@JvmName("radioactivityFromInvertedTimeDefault")
fun <
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity,
    > RadioactivityUnit.radioactivity(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    radioactivity(time, ::DefaultScientificValue)

@JvmName("radioactivityFromInvertedTime")
fun <
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity,
    Value : ScientificValue<PhysicalQuantity.Radioactivity, RadioactivityUnit>,
    > RadioactivityUnit.radioactivity(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    factory: (Decimal, RadioactivityUnit) -> Value,
) = byInverting(time, factory)

fun <
    RadioactivityUnit : Radioactivity,
    TimeUnit : Time,
    > RadioactivityUnit.radioactivity(decays: Decimal, per: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    radioactivity(decays, per, ::DefaultScientificValue)

fun <
    RadioactivityUnit : Radioactivity,
    TimeUnit : Time,
    Value : ScientificValue<PhysicalQuantity.Radioactivity, RadioactivityUnit>,
    > RadioactivityUnit.radioactivity(
    decays: Decimal,
    per: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    factory: (Decimal, RadioactivityUnit) -> Value,
) = (decays / per.convertValue(Second))(Becquerel).convert(this, factory)
