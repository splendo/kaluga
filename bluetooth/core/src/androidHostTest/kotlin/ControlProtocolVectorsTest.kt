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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.server.TestCentral
import com.splendo.kaluga.bluetooth.server.TestCentral.Instruction
import com.splendo.kaluga.bluetooth.server.TestCentral.Message
import com.splendo.kaluga.bluetooth.server.TestCentral.Report
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import com.splendo.kaluga.bluetooth.TestPeripheral.Command as C

/**
 * The control protocols of the device scenarios' peers against the byte vectors in `bytes-layouts-vectors.json`, which
 * the python peers' schema reader is checked against as well: each value encodes to its vector's bytes, and the bytes
 * decode to a value of the same type that encodes to them again.
 */
class ControlProtocolVectorsTest {

    private val values: Map<String, Pair<KSerializer<out Any>, Any>> = mapOf(
        "Command.Reset" to command(C.Reset),
        "Command.HoldRead" to command(C.HoldRead(800u)),
        "Command.HoldCccdWrite" to command(C.HoldCccdWrite(0u)),
        "Command.DropWhenHeld" to command(C.DropWhenHeld(1000u)),
        "Command.Drop" to command(C.Drop(200u)),
        "Command.Notify" to command(C.Notify(300u, byteArrayOf(0x33))),
        "Command.AnswerWithError" to command(C.AnswerWithError(0x85)),
        "Command.Indicate" to command(C.Indicate(0u, byteArrayOf(0x11, 0x22))),
        "Command.NotifyBurst" to command(C.NotifyBurst(200)),
        "Command.SetLongValue" to command(C.SetLongValue(600)),
        "Command.NotifyTwin" to command(C.NotifyTwin(0u, 1u, byteArrayOf(0x71))),
        "Command.ChangeServices" to command(C.ChangeServices(200u)),
        "Command.StartBeacon" to command(C.StartBeacon),
        "Command.StopBeacon" to command(C.StopBeacon),
        "Command.NotifyLength" to command(C.NotifyLength(514)),
        "Command.SetLongValueTo" to command(C.SetLongValueTo(byteArrayOf(1, 2, 3))),
        "Command.CapMtu" to command(C.CapMtu(23)),
        "Message.Stage" to message(1, Instruction.Stage(byteArrayOf(1, 2, 3))),
        "Message.Clear" to message(2, Instruction.Clear),
        "Message.Write" to message(3, Instruction.Write(3)),
        "Message.Prepare" to message(4, Instruction.Prepare(3, offset = 22, start = 18, length = 18)),
        "Message.Execute" to message(5, Instruction.Execute(write = true)),
        "Message.Disconnect" to message(6, Instruction.Disconnect),
        "Message.Subscribe" to message(7, Instruction.Subscribe(5)),
        "Message.Unsubscribe" to message(8, Instruction.Unsubscribe(5)),
        "Message.RequestMtu" to message(9, Instruction.RequestMtu(517)),
        "Report.Forwarded" to report(Report.Forwarded(5u, byteArrayOf(0x21, 0x22))),
        "Report.Result" to report(Report.Result(9u, 0u, byteArrayOf(0x05, 0x02))),
        "Report.Result.Empty" to report(Report.Result(255u, TestCentral.ECHO_MISMATCH.toUByte(), byteArrayOf())),
    )

    private val vectors: List<JsonObject> = Json.parseToJsonElement(File("src/deviceScenarios/python/bytes-layouts-vectors.json").readText())
        .jsonObject.getValue("vectors").jsonArray.map { it.jsonObject }

    @Test
    fun everyValueHasAVector() {
        assertEquals(values.keys, vectors.map { it.getValue("name").jsonPrimitive.content }.toSet())
    }

    @Test
    fun encodesToTheVectors() {
        vectors.forEach { vector ->
            val name = vector.getValue("name").jsonPrimitive.content
            val (serializer, value) = values.getValue(name)
            assertEquals(vector.hex, encode(serializer, value).toHexString(" "), name)
        }
    }

    @Test
    fun decodesTheVectors() {
        vectors.forEach { vector ->
            val name = vector.getValue("name").jsonPrimitive.content
            val (serializer, value) = values.getValue(name)
            val bytes = vector.hex.split(" ").filter { it.isNotEmpty() }.map { it.toInt(16).toByte() }.toByteArray()
            val decoded = BytesFormat.decodeFromByteArray(serializer, bytes)
            assertEquals(value.typeName, decoded.typeName, name)
            assertEquals(vector.hex, encode(serializer, decoded).toHexString(" "), name)
        }
    }

    private val JsonObject.hex get() = getValue("hex").jsonPrimitive.content

    private val Any.typeName get() = (if (this is Message) instruction else this)::class.simpleName

    @Suppress("UNCHECKED_CAST")
    private fun encode(serializer: KSerializer<out Any>, value: Any) = BytesFormat.encodeToByteArray(serializer as KSerializer<Any>, value)

    private fun command(value: C) = C.serializer() to value
    private fun message(id: Int, instruction: Instruction) = Message.serializer() to Message(id.toUByte(), instruction)
    private fun report(value: Report) = Report.serializer() to value
}
