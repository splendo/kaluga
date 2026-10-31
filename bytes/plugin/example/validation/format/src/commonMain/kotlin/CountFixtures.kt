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

package com.splendo.kaluga.bytes.example.format

import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Count
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.SizePolymorphic

object FixedCountFixtures {
    /** A sensor record: raw register blocks of a fixed size before, between and after other fields. */
    @BytesLayout
    interface Record {
        @FlagIndex(0)
        val valid: Boolean

        @Count(2)
        val header: List<UShort>

        @Count(21)
        val coefficients: ByteArray
        val sequence: UByte

        @Count(3)
        val trims: List<Short>

        @Count(4)
        val tail: ByteArray
    }

    /** Calibration blocks told apart by their size alone. */
    @BytesLayout
    @SizePolymorphic
    sealed interface Calibration

    @BytesLayout
    interface CalibrationA : Calibration {
        @Count(88)
        val block: ByteArray
    }

    @BytesLayout
    interface CalibrationB : Calibration {
        @Count(36)
        val block: ByteArray
    }

    @BytesLayout
    interface CalibrationC : Calibration {
        @Count(14)
        val block: List<UShort>
    }

    @BytesLayout
    interface CalibrationD : Calibration {
        val index: UByte

        @Count(51)
        val block: ByteArray
    }

    @BytesLayout
    interface Sample {
        @Count(3)
        val raw: ByteArray
        val tag: Byte
    }

    /** Fixed-size samples in a counted list. */
    @BytesLayout
    interface Batch {
        val samples: List<Sample>
        val after: Byte
    }

    @BytesLayout
    interface Optional {
        @Count(2)
        val values: List<Byte>?
        val after: Byte
    }
}
