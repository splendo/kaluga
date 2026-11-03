/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bytes.example.parts

import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Unsigned

// Parts of a device that the layouts of another module hold, read there from this module's klibs and jars.

@BytesLayout
interface BatteryCharge {
    @Size(Length.`8_BIT`)
    @Unsigned
    val level: Int
}

interface Channelled {
    @Size(Length.`8_BIT`)
    @Unsigned
    val channel: Int
}

// Its properties are not in alphabetical order, and the repeated @Size picks the width of value by a flag bit.
@BytesLayout
interface SensorReading : Channelled {
    @Size(Length.`8_BIT`)
    @Size(Length.`16_BIT`)
    val value: Int
    val calibrated: Boolean
}

@BytesLayout
@Prefix([0x10])
sealed interface SensorEvent {
    @BytesLayout
    @SerializedByteValue(0x01)
    interface Tap : SensorEvent {
        val count: Byte
    }

    @BytesLayout
    @SerializedByteValue(0x02)
    interface Swipe : SensorEvent {
        val reading: SensorReading
    }
}
