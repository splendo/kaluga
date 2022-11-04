/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSData
import platform.Foundation.create

/**
 * Converts a [NSData] to its corresponding [ByteArray]
 * @return The [ByteArray] equivalent to this [NSData]
 */
fun NSData.toByteArray(): ByteArray {
    val bytes = bytes?.let { it } ?: return byteArrayOf()
    val ktBytes: CPointer<ByteVar> = bytes.reinterpret()
    return ByteArray(length.toInt()) { index -> ktBytes[index] }
}

/**
 * Converts a [ByteArray] to its corresponding [NSData]
 * @return Th [NSData] corresponding to this [ByteArray]
 */
fun ByteArray.toNSData(): NSData = memScoped {
    return NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}

/**
 * Converts a list of unknown typing to a list of a given type.
 * This proves useful since generics are lost when converting ObjC/Swift to Kotlin
 * @return The list of all the elements in the given list that match the desired typing
 */
inline fun <reified T : Any> List<*>.typedList(): List<T> {
    return mapNotNull {
        when (it) {
            is T -> it
            else -> null
        }
    }
}

/**
 * Converts a map of unknown typing to a map of a given key-value type.
 * This proves useful since generics are lost when converting ObjC/Swift to Kotlin
 * @return The map of all the elements in the given map that match the desired typing
 */
inline fun <reified K : Any, reified V : Any> Map<*, *>.typedMap(): Map<K, V> {
    return this.mapNotNull { entry ->
        when (val key = entry.key) {
            is K -> {
                when (val value = entry.value) {
                    is V -> Pair(key, value)
                    else -> null
                }
            }
            else -> null
        }
    }.toMap()
}
