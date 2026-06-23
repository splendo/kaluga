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

import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.testRunBlocking
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.randomIdentifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

// Beyond proving the generated types exist, this stands up the generated simulated SERVER and CLIENT for both devices
// and runs read/write round-trips through them: the client talks to the server's delegate over the in-memory simulated
// transport. A missing, renamed or wrongly-typed member (or delegate) fails to compile, and a misrouted call fails the
// assertions at runtime.
class GeneratedFullStructureTest : BaseTest() {

    // =================================================================================================================
    // Device: BluetoothTest
    // =================================================================================================================

    private class TestDescriptorDelegate(val name: String) : LocalTestCharacteristic.LocalTestDescriptor.Delegate {
        var lastWrittenAge: Int? = null
        override suspend fun LocalTestCharacteristic.LocalTestDescriptor.onReadName(identifier: Identifier): RemoteTestCharacteristic.TestDescriptorReadResponse =
            RemoteTestCharacteristic.TestDescriptorReadResponse.Success(name)
        override suspend fun LocalTestCharacteristic.LocalTestDescriptor.onWriteAge(age: Int, identifier: Identifier): GattResponse.WriteResponse {
            lastWrittenAge = age
            return GattResponse.WriteSuccess.Acknowledged
        }
        override suspend fun LocalTestCharacteristic.LocalTestDescriptor.onFailedToWriteAge(exception: Exception, identifier: Identifier): GattResponse.WriteResponse =
            GattResponse.WriteSuccess.Acknowledged
    }

    private class TestCharacteristicDelegate(val status: String, descriptorName: String) : LocalTestCharacteristic.Delegate {
        var lastWrittenShouldUpdate: Boolean? = null
        override val testDescriptorDelegate = TestDescriptorDelegate(descriptorName)
        override suspend fun LocalTestCharacteristic.onReadStatus(identifier: Identifier): TestCharacteristicReadResponse = TestCharacteristicReadResponse.Success(status)
        override suspend fun LocalTestCharacteristic.onWriteShouldUpdate(shouldUpdate: Boolean, identifier: Identifier): GattResponse.WriteResponse {
            lastWrittenShouldUpdate = shouldUpdate
            return GattResponse.WriteSuccess.Acknowledged
        }
        override suspend fun LocalTestCharacteristic.onFailedToWriteShouldUpdate(exception: Exception, identifier: Identifier): GattResponse.WriteResponse =
            GattResponse.WriteSuccess.Acknowledged
        override fun LocalTestCharacteristic.onSubscribeToState(identifier: Identifier) {}
        override fun LocalTestCharacteristic.onUnsubscribeToState(identifier: Identifier) {}
    }

    private class TestServerDelegate(val characteristicDelegate: TestCharacteristicDelegate) : BluetoothTestServer.Delegate {
        override val testServiceDelegate: LocalTestService.Delegate = object : LocalTestService.Delegate {
            override val testCharacteristicDelegate = characteristicDelegate
        }
    }

    @Test
    fun bluetoothTestRoundTrip() = testRunBlocking {
        val characteristicDelegate = TestCharacteristicDelegate(status = "online", descriptorName = "sensor")
        val server = BluetoothTestServer.simulated(TestServerDelegate(characteristicDelegate))
        try {
            val client = BluetoothTestClient.simulated(randomIdentifier(), server)

            // characteristic read
            val status = client.testService.testCharacteristic.readStatus()
            assertIs<TestCharacteristicReadResponse.Success>(status)
            assertEquals("online", status.response)

            // characteristic write
            assertIs<GattResponse.WriteSuccess>(client.testService.testCharacteristic.writeShouldUpdate(true))
            assertEquals(true, characteristicDelegate.lastWrittenShouldUpdate)

            // descriptor read
            val name = client.testService.testCharacteristic.testDescriptor.readName()
            assertIs<RemoteTestCharacteristic.TestDescriptorReadResponse.Success>(name)
            assertEquals("sensor", name.response)

            // descriptor write
            assertIs<GattResponse.WriteSuccess>(client.testService.testCharacteristic.testDescriptor.writeAge(33))
            assertEquals(33, characteristicDelegate.testDescriptorDelegate.lastWrittenAge)
        } finally {
            server.close()
        }
    }

    // =================================================================================================================
    // Device: FixtureDevice
    // =================================================================================================================

    private class IndicateFixtureDelegate : LocalIndicateFixture.Delegate {
        override fun LocalIndicateFixture.onSubscribeToIndicateState(identifier: Identifier) {}
        override fun LocalIndicateFixture.onUnsubscribeToIndicateState(identifier: Identifier) {}
    }

    private class MultiWriteFixtureDelegate : LocalMultiWriteFixture.Delegate {
        var lastWrittenMultiValue: Int? = null
        override suspend fun LocalMultiWriteFixture.onWriteMultiValue(multiValue: Int, identifier: Identifier): GattResponse.WriteResponse {
            lastWrittenMultiValue = multiValue
            return GattResponse.WriteSuccess.Acknowledged
        }
        override suspend fun LocalMultiWriteFixture.onFailedToWriteMultiValue(exception: Exception, identifier: Identifier): GattResponse.WriteResponse =
            GattResponse.WriteSuccess.Acknowledged
    }

    private class SignedFixtureDelegate : LocalSignedFixture.Delegate {
        var lastWrittenSignedValue: Int? = null
        override suspend fun LocalSignedFixture.onWriteSignedValue(signedValue: Int, identifier: Identifier): GattResponse.WriteResponse {
            lastWrittenSignedValue = signedValue
            return GattResponse.WriteSuccess.Acknowledged
        }
        override suspend fun LocalSignedFixture.onFailedToWriteSignedValue(exception: Exception, identifier: Identifier): GattResponse.WriteResponse =
            GattResponse.WriteSuccess.Acknowledged
    }

    private class EncryptedFixtureDelegate(val secret: String) : LocalEncryptedFixture.Delegate {
        var lastWrittenSecret: String? = null
        override suspend fun LocalEncryptedFixture.onReadSecret(identifier: Identifier): EncryptedFixtureReadResponse = EncryptedFixtureReadResponse.Success(secret)
        override suspend fun LocalEncryptedFixture.onWriteSecretWrite(secretWrite: String, identifier: Identifier): GattResponse.WriteResponse {
            lastWrittenSecret = secretWrite
            return GattResponse.WriteSuccess.Acknowledged
        }
        override suspend fun LocalEncryptedFixture.onFailedToWriteSecretWrite(exception: Exception, identifier: Identifier): GattResponse.WriteResponse =
            GattResponse.WriteSuccess.Acknowledged
    }

    private class ByteArrayFixtureDelegate(val raw: ByteArray) : LocalByteArrayFixture.Delegate {
        var lastWrittenRaw: ByteArray? = null
        override suspend fun LocalByteArrayFixture.onReadRawRead(identifier: Identifier, offset: Int): GattResponse.ReadResponse = GattResponse.ReadSuccess(raw)
        override suspend fun LocalByteArrayFixture.onWriteRawWrite(rawWrite: ByteArray, offset: Int, identifier: Identifier): GattResponse.WriteResponse {
            lastWrittenRaw = rawWrite
            return GattResponse.WriteSuccess.Acknowledged
        }
    }

    private class IncludedFixtureCharacteristicDelegate(val included: String) : LocalIncludedFixtureCharacteristic.Delegate {
        override suspend fun LocalIncludedFixtureCharacteristic.onReadIncludedValue(identifier: Identifier): IncludedFixtureCharacteristicReadResponse =
            IncludedFixtureCharacteristicReadResponse.Success(included)
    }

    private class FixtureServerDelegate(
        val indicateDelegate: IndicateFixtureDelegate,
        val multiWriteDelegate: MultiWriteFixtureDelegate,
        val signedDelegate: SignedFixtureDelegate,
        val encryptedDelegate: EncryptedFixtureDelegate,
        val byteArrayDelegate: ByteArrayFixtureDelegate,
        val includedCharacteristicDelegate: IncludedFixtureCharacteristicDelegate,
    ) : FixtureDeviceServer.Delegate {
        override val fixtureServiceDelegate: LocalFixtureService.Delegate = object : LocalFixtureService.Delegate {
            override val indicateCharacteristicDelegate = indicateDelegate
            override val multiWriteCharacteristicDelegate = multiWriteDelegate
            override val signedCharacteristicDelegate = signedDelegate
            override val encryptedCharacteristicDelegate = encryptedDelegate
            override val byteArrayCharacteristicDelegate = byteArrayDelegate
            override val includedServiceDelegate = object : LocalIncludedFixtureService.Delegate {
                override val includedCharacteristicDelegate = this@FixtureServerDelegate.includedCharacteristicDelegate
            }
        }
    }

    @Test
    fun fixtureDeviceRoundTrip() = testRunBlocking {
        val indicateDelegate = IndicateFixtureDelegate()
        val multiWriteDelegate = MultiWriteFixtureDelegate()
        val signedDelegate = SignedFixtureDelegate()
        val encryptedDelegate = EncryptedFixtureDelegate(secret = "topsecret")
        val byteArrayDelegate = ByteArrayFixtureDelegate(raw = byteArrayOf(1, 2, 3))
        val includedCharacteristicDelegate = IncludedFixtureCharacteristicDelegate(included = "included-value")
        val serverDelegate = FixtureServerDelegate(
            indicateDelegate,
            multiWriteDelegate,
            signedDelegate,
            encryptedDelegate,
            byteArrayDelegate,
            includedCharacteristicDelegate,
        )
        val server = FixtureDeviceServer.simulated(serverDelegate)
        try {
            val client = FixtureDeviceClient.simulated(randomIdentifier(), server)
            val service = client.fixtureService

            // encrypted characteristic read + write
            val secret = service.encryptedCharacteristic.readSecret()
            assertIs<EncryptedFixtureReadResponse.Success>(secret)
            assertEquals("topsecret", secret.response)
            assertIs<GattResponse.WriteSuccess>(service.encryptedCharacteristic.writeSecretWrite("newsecret"))
            assertEquals("newsecret", encryptedDelegate.lastWrittenSecret)

            // byte array characteristic read + write
            val rawRead = service.byteArrayCharacteristic.readRawRead()
            assertIs<GattResponse.ReadSuccess>(rawRead)
            assertEquals(listOf<Byte>(1, 2, 3), rawRead.value.toList())
            assertIs<GattResponse.WriteSuccess>(service.byteArrayCharacteristic.writeRawWrite(byteArrayOf(9, 8)))
            assertEquals(listOf<Byte>(9, 8), byteArrayDelegate.lastWrittenRaw?.toList())

            // multi-write characteristic (with and without response)
            assertIs<GattResponse.WriteSuccess>(service.multiWriteCharacteristic.writeMultiValue(11))
            assertEquals(11, multiWriteDelegate.lastWrittenMultiValue)
            assertIs<GattResponse.WriteSuccess>(service.multiWriteCharacteristic.writeMultiValueWithoutResponse(22))
            assertEquals(22, multiWriteDelegate.lastWrittenMultiValue)

            // signed characteristic write
            assertIs<GattResponse.WriteSuccess>(service.signedCharacteristic.writeSignedValue(-5))
            assertEquals(-5, signedDelegate.lastWrittenSignedValue)

            // included service -> characteristic read
            val included = service.includedService.includedCharacteristic.readIncludedValue()
            assertIs<IncludedFixtureCharacteristicReadResponse.Success>(included)
            assertEquals("included-value", included.response)

            // indicate characteristic state flow exists
            assertNotNull(service.indicateCharacteristic.indicateState)
        } finally {
            server.close()
        }
    }
}
