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

package com.splendo.kaluga.scientific.converter.decimal

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.time.time
import com.splendo.kaluga.scientific.unit.BeatsPerMinute
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.Radioactivity
import com.splendo.kaluga.scientific.unit.Second
import kotlin.jvm.JvmName

@JvmName("numberDivBPM")
infix operator fun Number.div(frequency: ScientificValue<PhysicalQuantity.Frequency, BeatsPerMinute>): ScientificValue<PhysicalQuantity.Time, Hour> = toDecimal() / frequency

@JvmName("numberDivFrequency")
infix operator fun <FrequencyUnit : Frequency> Number.div(frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>): ScientificValue<PhysicalQuantity.Time, Second> =
    toDecimal() / frequency

@JvmName("decimalDivBPM")
infix operator fun Decimal.div(frequency: ScientificValue<PhysicalQuantity.Frequency, BeatsPerMinute>): ScientificValue<PhysicalQuantity.Time, Hour> = Hour.time(this, frequency)

@JvmName("decimalDivFrequency")
infix operator fun <FrequencyUnit : Frequency> Decimal.div(frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>): ScientificValue<PhysicalQuantity.Time, Second> =
    Second.time(this, frequency)

@JvmName("numberDivRadioactivity")
infix operator fun <RadioactivityUnit : Radioactivity> Number.div(
    radioactivity: ScientificValue<PhysicalQuantity.Radioactivity, RadioactivityUnit>,
): ScientificValue<PhysicalQuantity.Time, Second> = toDecimal() / radioactivity

@JvmName("decimalDivRadioactivity")
infix operator fun <RadioactivityUnit : Radioactivity> Decimal.div(
    radioactivity: ScientificValue<PhysicalQuantity.Radioactivity, RadioactivityUnit>,
): ScientificValue<PhysicalQuantity.Time, Second> = Second.time(this, radioactivity)
