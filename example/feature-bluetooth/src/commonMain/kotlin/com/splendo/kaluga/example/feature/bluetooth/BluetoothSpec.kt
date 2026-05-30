/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.bluetooth

import com.splendo.kaluga.bluetooth.serialization.FlagIndex
import com.splendo.kaluga.bluetooth.serialization.Length
import com.splendo.kaluga.bluetooth.serialization.NullIfEmpty
import com.splendo.kaluga.bluetooth.serialization.Prefix
import com.splendo.kaluga.bluetooth.serialization.Scalar
import com.splendo.kaluga.bluetooth.serialization.SerializedByteValue
import com.splendo.kaluga.bluetooth.serialization.Size
import com.splendo.kaluga.bluetooth.serialization.Unsigned
import com.splendo.kaluga.bluetooth.serialization.Unsized
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

object BluetoothSpec {

    object HeartRateService {
        val UUID = uuidFrom("180D")
        val HEART_RATE_MEASUREMENT_CHARACTERISTIC = uuidFrom("2A37")
        val SENSOR_LOCATION_CHARACTERISTIC = uuidFrom("2A38")
        val HEART_RATE_CONTROL_POINT_CHARACTERISTIC = uuidFrom("2A39")
    }

    object KalugaSensorService {
        val UUID = uuidFrom("b7f5bde4-65b2-4a26-b692-b6c1f90e6238")
        val DATA_STREAM_CHARACTERISTIC = uuidFrom("73b0e3f5-7f0a-4e31-a993-dbfbf490c1a4")
        val CONTROL_CHARACTERISTIC = uuidFrom("a3d1bc77-b3e2-4031-8e6b-21cbb2e5b1a9")
    }

    object KalugaSensorInfo {
        val UUID = uuidFrom("5a3f0d09-5cf9-4df1-b624-bfe93a2a68ad")
        val STATUS_CHARACTERISTIC = uuidFrom("94cc7369-2b5f-4b2e-9a12-ae7ad4fd90c8")
        val INFO_CHARACTERISTIC = uuidFrom("e85a71f7-3d54-4a43-ba9b-89ff9e3428e0")
    }

    @Serializable
    @JvmInline
    value class RRInterval private constructor(
        @Size(Length.`16_BIT`)
        @Scalar(binaryExponent = 10)
        val seconds: Double,
    ) {
        constructor(duration: Duration) : this(duration.toDouble(DurationUnit.SECONDS))

        val duration: Duration get() = seconds.seconds
    }

    @Serializable
    data class HeartRate(
        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        @Unsigned
        val heartRate: Int,
        @FlagIndex(1)
        val contactSupported: Boolean,
        @FlagIndex(2)
        val contactDetected: Boolean = !contactSupported,
        @Unsigned
        @Size(Length.`16_BIT`)
        val energyExpended: Int? = null,
        @NullIfEmpty
        @Unsized
        val rrIntervals: List<RRInterval> = emptyList(),
    )

    @Serializable
    enum class SensorLocation {

        @SerializedByteValue(0x00)
        OTHER,

        @SerializedByteValue(0x01)
        CHEST,

        @SerializedByteValue(0x02)
        WRIST,

        @SerializedByteValue(0x03)
        FINGER,

        @SerializedByteValue(0x04)
        HAND,

        @SerializedByteValue(0x05)
        EAR_LOBE,

        @SerializedByteValue(0x06)
        FOOT,
    }

    @Serializable
    @Prefix([0x01])
    data object ResetEnergyCommand
}
