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

package com.splendo.kaluga.bluetooth.example

import com.splendo.kaluga.bluetooth.annotations.Advertising
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.annotations.Encrypted
import com.splendo.kaluga.bluetooth.annotations.Indicatable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.annotations.WritableSigned
import com.splendo.kaluga.bluetooth.annotations.WritableWithoutResponse

/**
 * Coverage fixtures for the KSP processor: every property annotation and combination,
 * byte-array payloads, and an included service. Generating and compiling this exercises
 * each codegen path that the primary example does not.
 */
@Bluetooth
interface FixtureDevice {
    @Advertising
    val fixtureService: FixtureService
}

@BluetoothService("f000")
interface FixtureService {
    val indicateCharacteristic: IndicateFixture
    val multiWriteCharacteristic: MultiWriteFixture
    val signedCharacteristic: SignedFixture
    val encryptedCharacteristic: EncryptedFixture
    val byteArrayCharacteristic: ByteArrayFixture
    val includedService: IncludedFixtureService
}

@BluetoothCharacteristic("f001")
interface IndicateFixture {
    @Indicatable
    val indicateState: Short
}

@BluetoothCharacteristic("f002")
interface MultiWriteFixture {
    @Writable
    @WritableWithoutResponse
    val multiValue: Int
}

@BluetoothCharacteristic("f003")
interface SignedFixture {
    @WritableSigned
    val signedValue: Int
}

@BluetoothCharacteristic("f004")
interface EncryptedFixture {
    @Readable
    @Encrypted
    val secret: String

    @Writable
    @Encrypted
    val secretWrite: String
}

@BluetoothCharacteristic("f005")
interface ByteArrayFixture {
    @Readable
    val rawRead: ByteArray

    @Writable
    val rawWrite: ByteArray
}

@BluetoothService("f006")
interface IncludedFixtureService {
    val includedCharacteristic: IncludedFixtureCharacteristic
}

@BluetoothCharacteristic("f007")
interface IncludedFixtureCharacteristic {
    @Readable
    val includedValue: String
}
