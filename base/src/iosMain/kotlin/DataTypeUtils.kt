package com.splendo.kaluga.base

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSData

fun NSData.toByteArray() : ByteArray? {
    val bytes = bytes?.let { it } ?: return null
    val ktBytes: CPointer<ByteVar> = bytes.reinterpret()
    return ByteArray(length.toInt()) { index -> ktBytes[index] }
}
