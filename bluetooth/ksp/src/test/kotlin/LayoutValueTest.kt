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

package com.splendo.kaluga.bluetooth.ksp

import com.splendo.kaluga.bytes.ksp.BytesSymbolProcessorProvider
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Bluetooth values typed by a `@BytesLayout`: read, written and notified through the layout's generated view when it
 * has one, and through its DATA serializer when it has only that.
 */
class LayoutValueTest {

    private fun definitions(layout: String = "@BytesLayout", serializable: String = "") = bluetoothSource(
        """
        $layout
        interface Measurement {
            @Size(Length.`8_BIT`)
            @Unsigned
            val bpm: Int
        }

        @BluetoothCharacteristic("2a37")
        interface MeasurementCharacteristic {
            @Readable
            @Writable
            @Notifiable
            val measurement: Measurement

            val label: LabelDescriptor
        }

        @BluetoothDescriptor("2901")
        interface LabelDescriptor {
            @Readable
            @Writable
            val label: Measurement
        }

        $serializable
        """,
    )

    @Test
    fun `the client reads, writes and notifies a layout through its view`() {
        val compilation = BluetoothCompilation(definitions())
        assertEquals(emptyList(), compilation.kspErrors)
        val remote = compilation.generatedFile("RemoteMeasurementCharacteristic.kt")

        assertContains(remote, "public val measurement: Flow<MeasurementView>")
        assertContains(remote, "public suspend fun readMeasurement(): MeasurementCharacteristicReadResponse")
        assertContains(remote, "public suspend fun writeMeasurement(measurement: MeasurementView): GattResponse.WriteResponse")
        assertContains(remote, "public val measurement: Flow<MeasurementView>\n        get() = measurementBytes.map { MeasurementView(it) }")
        assertContains(remote, "MeasurementCharacteristicReadResponse.Success(MeasurementView(readResult.value))")
        assertContains(remote, "characteristic.write(measurement.bytes, WithResponse)")
        // Nothing is serialized, so the client needs no BytesFormat.
        assertFalse("BytesFormat" in remote, remote)
        assertContains(remote, "suspend (measurement: MeasurementView) -> GattResponse.WriteResponse")
        assertContains(remote, "measurement: Flow<MeasurementView>,")
        assertContains(remote, "override val measurementBytes: MutableSharedFlow<ByteArray> = MutableSharedFlow()")

        val response = compilation.generatedFile("MeasurementCharacteristicReadResponse.kt")
        assertContains(response, "public val response: MeasurementView")
    }

    @Test
    fun `the client collects notifications of a view without boxing one per notification`() {
        val compilation = BluetoothCompilation(definitions())
        assertEquals(emptyList(), compilation.kspErrors)
        val remote = compilation.generatedFile("RemoteMeasurementCharacteristic.kt")

        assertContains(remote, "public val measurementBytes: Flow<ByteArray>\n\n")
        assertContains(remote, "override val measurementBytes: Flow<ByteArray>\n        get() = characteristic.`value`()")
        assertContains(
            remote,
            "public suspend inline fun RemoteMeasurementCharacteristic.collectMeasurement(crossinline action: suspend (MeasurementView) -> Unit) {\n" +
                "    measurementBytes.collect { action(MeasurementView(it)) }\n}",
        )
    }

    @Test
    fun `the bytes of a view are the one flow an implementation provides`() {
        val compilation = BluetoothCompilation(definitions())
        assertEquals(emptyList(), compilation.kspErrors)
        val remote = compilation.generatedFile("RemoteMeasurementCharacteristic.kt")

        // Only the API declares the flow of views, wrapping the bytes; no implementation overrides it.
        assertEquals(1, Regex("val measurement: Flow<MeasurementView>").findAll(remote).count(), remote)
        assertFalse("override val measurement:" in remote, remote)
        // The simulator is given views, and notes a collector on the bytes it derives from them.
        assertContains(remote, "measurement: Flow<MeasurementView>,")
        assertContains(
            remote,
            "override val measurementBytes: Flow<ByteArray>\n" +
                "        get() = _measurement.map { it.bytes }.onStart { _isNotifying.value = true }.onCompletion { _isNotifying.value = false }",
        )
        assertEquals(1, Regex("_isNotifying\\.value = true").findAll(remote).count(), remote)
    }

    @Test
    fun `values that are not views have no undecoded notifications`() {
        val compilation = BluetoothCompilation(definitions(layout = "@BytesLayout(generate = [Generate.DATA])"))
        val remote = compilation.generatedFile("RemoteMeasurementCharacteristic.kt")
        assertFalse("measurementBytes" in remote, remote)
        assertFalse("collectMeasurement" in remote, remote)
    }

    @Test
    fun `the server reads, writes and notifies a layout through its view`() {
        val compilation = BluetoothCompilation(definitions())
        assertEquals(emptyList(), compilation.kspErrors)
        val local = compilation.generatedFile("LocalMeasurementCharacteristic.kt")

        assertContains(local, "suspend fun LocalMeasurementCharacteristic.onReadMeasurement(identifier: Identifier): MeasurementCharacteristicReadResponse")
        assertContains(local, "suspend fun LocalMeasurementCharacteristic.onWriteMeasurement(measurement: MeasurementView, identifier: Identifier): GattResponse.WriteResponse")
        assertContains(local, "suspend fun LocalMeasurementCharacteristic.onFailedToWriteMeasurement(exception: Exception, identifier: Identifier)")
        assertContains(local, "GattResponse.ReadSuccess(if (offset == 0) response.response.bytes else response.response.bytes.drop(offset).toByteArray())")
        assertContains(local, "decode = { MeasurementView.wrap(it) },")
        assertFalse("deserializationStrategy" in local, local)
        assertContains(local, "characteristic.notifyAll(measurement.bytes)")
        assertContains(local, "characteristic.notify(it, measurement.bytes)")
        assertContains(local, "public suspend fun notifyAllMeasurementChanged(measurement: MeasurementView): Boolean")
        assertContains(local, "MutableSharedFlow<MeasurementView>()")
        assertFalse("BytesFormat" in local, local)
    }

    @Test
    fun `descriptors read and write a layout through its view`() {
        val compilation = BluetoothCompilation(definitions())
        assertEquals(emptyList(), compilation.kspErrors)
        val remote = compilation.generatedFile("RemoteLabelDescriptor.kt")
        assertContains(remote, "public suspend fun writeLabel(label: MeasurementView): GattResponse.WriteResponse")
        assertContains(remote, "LabelDescriptorReadResponse.Success(MeasurementView(readResult.value))")
        assertContains(remote, "descriptor.write(label.bytes)")
        assertFalse("BytesFormat" in remote, remote)

        val local = compilation.generatedFile("LocalLabelDescriptor.kt")
        assertContains(local, "suspend fun LocalLabelDescriptor.onWriteLabel(label: MeasurementView, identifier: Identifier): GattResponse.WriteResponse")
        assertContains(local, "decode = { MeasurementView.wrap(it) },")
        assertContains(local, "GattResponse.ReadSuccess(if (offset == 0) response.response.bytes else response.response.bytes.drop(offset).toByteArray())")
        assertFalse("BytesFormat" in local, local)
    }

    @Test
    fun `the view the code names is the one the bytes processor generates`() {
        val compilation = BluetoothCompilation(definitions(), processors = listOf(BytesSymbolProcessorProvider()))
        assertEquals(emptyList(), compilation.kspErrors)
        assertContains(compilation.generatedFile("RemoteMeasurementCharacteristic.kt"), "Flow<MeasurementView>")
        assertContains(compilation.generatedSources, "public value class MeasurementView(")
        assertContains(compilation.generatedSources, "public fun wrap(bytes: ByteArray, validateChecksum: Boolean = true): MeasurementView")
    }

    @Test
    fun `a layout generating only DATA is serialized through its interface serializer`() {
        val compilation = BluetoothCompilation(definitions(layout = "@BytesLayout(generate = [Generate.DATA])"))
        assertEquals(emptyList(), compilation.kspErrors)
        val remote = compilation.generatedFile("RemoteMeasurementCharacteristic.kt")
        assertContains(remote, "public val measurement: Flow<Measurement>")
        assertContains(remote, "characteristic.`value`(MeasurementData.InterfaceSerializer, format)")
        assertContains(remote, "format.decodeFromByteArray(MeasurementData.InterfaceSerializer, readResult.value)")
        assertContains(remote, "format.encodeToByteArray(MeasurementData.InterfaceSerializer, measurement)")

        val local = compilation.generatedFile("LocalMeasurementCharacteristic.kt")
        assertContains(local, "deserializationStrategy = MeasurementData.InterfaceSerializer,")
        assertContains(local, "characteristic.notifyAll(measurement, MeasurementData.InterfaceSerializer, format)")
    }

    @Test
    fun `a layout without its own generate follows the module default of the bytes plugin`() {
        val dataByDefault = BluetoothCompilation(definitions(), options = mapOf("generate" to "DATA"))
        assertEquals(emptyList(), dataByDefault.kspErrors)
        assertContains(dataByDefault.generatedFile("RemoteMeasurementCharacteristic.kt"), "Flow<Measurement>")

        val viewByDefault = BluetoothCompilation(definitions(), options = mapOf("generate" to "VIEW"))
        assertContains(viewByDefault.generatedFile("RemoteMeasurementCharacteristic.kt"), "Flow<MeasurementView>")

        // An explicit generate overrides the module default, even when it is what the annotation defaults to.
        val explicit = BluetoothCompilation(definitions(layout = "@BytesLayout(generate = [Generate.DATA, Generate.VIEW])"), options = mapOf("generate" to "DATA"))
        assertContains(explicit.generatedFile("RemoteMeasurementCharacteristic.kt"), "Flow<MeasurementView>")
    }

    @Test
    fun `a sealed layout is read through its frame`() {
        val compilation = BluetoothCompilation(
            bluetoothSource(
                """
                @BytesLayout
                sealed interface Reading {
                    @BytesLayout
                    @SerializedByteValue(1)
                    interface Pulse : Reading {
                        @Size(Length.`8_BIT`)
                        @Unsigned
                        val bpm: Int
                    }
                }

                @BluetoothCharacteristic("2a38")
                interface ReadingCharacteristic {
                    @Notifiable
                    val reading: Reading
                }
                """,
            ),
        )
        assertEquals(emptyList(), compilation.kspErrors)
        val remote = compilation.generatedFile("RemoteReadingCharacteristic.kt")
        assertContains(remote, "public val reading: Flow<ReadingFrame>")
        assertContains(remote, "get() = readingBytes.map { ReadingFrame(it) }")
        assertContains(remote, "override val readingBytes: Flow<ByteArray>\n        get() = characteristic.`value`()")
    }

    @Test
    fun `a serializable value keeps the serializer`() {
        val compilation = BluetoothCompilation(
            bluetoothSource(
                """
                @Serializable
                data class Level(val value: Int)

                @BluetoothCharacteristic("2a39")
                interface LevelCharacteristic {
                    @Readable
                    @Notifiable
                    val level: Level
                }
                """,
            ),
        )
        assertEquals(emptyList(), compilation.kspErrors)
        val remote = compilation.generatedFile("RemoteLevelCharacteristic.kt")
        assertContains(remote, "public val level: Flow<Level>")
        assertContains(remote, "characteristic.`value`(format.serializer<Level>(), format)")
        assertContains(remote, "format.decodeFromByteArray(format.serializer<Level>(), readResult.value)")
    }

    @Test
    fun `a byte stuffed layout cannot be viewed in place`() {
        val compilation = BluetoothCompilation(definitions(layout = "@BytesLayout\n@ByteStuffed"))
        assertTrue(compilation.kspErrors.any { "test.Measurement is @ByteStuffed" in it }, compilation.kspErrors.toString())
    }
}
