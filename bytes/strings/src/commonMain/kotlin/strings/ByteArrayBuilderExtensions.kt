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

/**
 * Adds a [Char] to the array. The char will be encoded using [Encoding.encodeChar]
 * @param char the Char to add
 * @param encoding the [Encoding] to encode the char with.
 * @param order the [ByteOrder] in which the Char will be encoded.
 */
fun ByteArrayBuilder.add(char: Char, encoding: Encoding = Encoding.UTF_8, order: ByteOrder = byteOrder) = add(encoding.encodeChar(char, order))

/**
 * Adds a [String] to the array. The String will be encoded using [Encoding.encodeString]
 * @param string the String to add
 * @param settings the [StringEncodingSettings] to encode the String with.
 * @param order the [ByteOrder] in which the String will be encoded.
 */
fun ByteArrayBuilder.add(string: String, settings: StringEncodingSettings = StringEncodingSettings(), order: ByteOrder = byteOrder) = add(string.toByteArray(settings, order))
