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

package com.splendo.kaluga.scientific.converter.decimal

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.frequency.frequency
import com.splendo.kaluga.scientific.unit.BeatsPerMinute
import com.splendo.kaluga.scientific.unit.Hertz
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("numberDivMinute")
infix operator fun Number.div(minute: ScientificValue<MeasurementType.Time, Minute>): ScientificValue<MeasurementType.Frequency, BeatsPerMinute> =
    toDecimal() / minute

@JvmName("numberDivTime")
infix operator fun <TimeUnit : Time> Number.div(time: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Frequency, Hertz> =
    toDecimal() / time

@JvmName("decimalDivMinute")
infix operator fun Decimal.div(minute: ScientificValue<MeasurementType.Time, Minute>): ScientificValue<MeasurementType.Frequency, BeatsPerMinute> =
    BeatsPerMinute.frequency(this, minute)

@JvmName("decimalDivTime")
infix operator fun <TimeUnit : Time> Decimal.div(time: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Frequency, Hertz> =
    Hertz.frequency(this, time)
