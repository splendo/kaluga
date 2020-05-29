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

import kotlinx.cinterop.*
import platform.Foundation.*

fun NSData.toByteArray() : ByteArray? {
    val bytes = bytes?.let { it } ?: return null
    val ktBytes: CPointer<ByteVar> = bytes.reinterpret()
    return ByteArray(length.toInt()) { index -> ktBytes[index] }
}

fun ByteArray.toNSData() : NSData? = memScoped {
    return NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}

inline fun <reified T:Any> List<*>.typedList() : List<T> {
    return mapNotNull { when (it) {
        is T -> it
        else -> null
    } }
}

inline fun <reified K:Any, reified V:Any> Map<*, *>.typedMap() : Map<K, V> {
    return this.mapNotNull { entry ->
        when(val key = entry.key) {
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