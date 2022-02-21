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

/**
 * Binary Writer
 *
 * Writes data into [buffer]
 *
 * @see ByteArrayWriter
 */
internal class BinaryWriter(
    private val buffer: ByteArrayWriter,
    private val octetEndianness: Endianness
) {

    private fun write(byteArray: ByteArray) = buffer.append(byteArray)

    /** Appends given [Byte] into [buffer] */
    fun writeByte(value: Byte) = write(value.toByteArray())
    /**
     * Converts given [value] into [ByteArray] using [octetEndianness] order
     * then appends it into [buffer]
     */
    fun writeInt(value: Int) = write(value.toByteArray(octetEndianness))
    /**
     * Converts given [value] into [ByteArray] using [octetEndianness] order
     * then appends it into [buffer]
     */
    fun writeShort(value: Short) = write(value.toByteArray(octetEndianness))
    /** Returns current [buffer] as [ByteArray] */
    fun toByteArray() = buffer.toByteArray()
}
