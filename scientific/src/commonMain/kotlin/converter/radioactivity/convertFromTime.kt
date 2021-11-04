/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.scientific.Becquerel
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Radioactivity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Second
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.byInverting
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.convertValue
import com.splendo.kaluga.scientific.invoke
import kotlin.jvm.JvmName

@JvmName("radioactivityFromInvertedTimeDefault")
fun <
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity
> RadioactivityUnit.radioactivity(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = radioactivity(time, ::DefaultScientificValue)

@JvmName("radioactivityFromInvertedTime")
fun <
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity,
    Value : ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit>
> RadioactivityUnit.radioactivity(
    time: ScientificValue<MeasurementType.Time, TimeUnit>,
    factory: (Decimal, RadioactivityUnit) -> Value
) = byInverting(time, factory)

fun <
    RadioactivityUnit : Radioactivity,
    TimeUnit : Time
    > RadioactivityUnit.radioactivity(decays: Decimal, per: ScientificValue<MeasurementType.Time, TimeUnit>) = radioactivity(decays, per, ::DefaultScientificValue)

fun <
    RadioactivityUnit : Radioactivity,
    TimeUnit : Time,
    Value : ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit>
    > RadioactivityUnit.radioactivity(
    decays: Decimal,
    per: ScientificValue<MeasurementType.Time, TimeUnit>,
    factory: (Decimal, RadioactivityUnit) -> Value
) = (decays / per.convertValue(Second))(Becquerel).convert(this, factory)
