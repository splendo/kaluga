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

package com.splendo.kaluga.bytes.strings

import com.splendo.kaluga.bytes.ByteArrayBuilder
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.addEncoded

/**
 * Adds a [Char] to the array. The char will be encoded using [Encoding.copyCharIntoByteArray], taking [Encoding.byteSizeOf] bytes
 * @param char the Char to add
 * @param encoding the [Encoding] to encode the char with.
 * @param order the [ByteOrder] in which the Char will be encoded.
 */
fun ByteArrayBuilder.add(char: Char, encoding: Encoding = Encoding.UTF_8, order: ByteOrder = byteOrder) = addEncoded(encoding.byteSizeOf(char)) { array, offset ->
    encoding.copyCharIntoByteArray(char, array, offset, order)
}

/**
 * Adds a [String] to the array. The String will be encoded using [String.copyIntoArray]
 * @param string the String to add
 * @param settings the [StringEncodingSettings] to encode the String with.
 * @param order the [ByteOrder] in which the String will be encoded.
 */
fun ByteArrayBuilder.add(string: String, settings: StringEncodingSettings = StringEncodingSettings(), order: ByteOrder = byteOrder) = when (settings.endMarking) {
    // Sizing a stuffed string takes a full encode, so it is cheaper to stuff once and add the result.
    is StringEncodingSettings.ByteStuffed -> add(string.toByteArray(settings, order))

    else -> addEncoded(string.byteArraySize(settings)) { array, offset -> string.copyIntoArray(array, settings, offset, order) }
}
