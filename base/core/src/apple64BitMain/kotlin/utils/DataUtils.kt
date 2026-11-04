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

package com.splendo.kaluga.base.utils

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.posix.memcpy

actual fun NSData.toByteArray(): ByteArray {
    val source = bytes ?: return byteArrayOf()
    return ByteArray(length.toInt()).apply {
        if (isNotEmpty()) usePinned { memcpy(it.addressOf(0), source, length) }
    }
}

actual fun ByteArray.toNSData(): NSData = memScoped {
    return NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}
