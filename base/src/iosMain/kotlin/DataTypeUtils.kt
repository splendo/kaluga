package com.splendo.kaluga.base

import kotlinx.cinterop.*
import platform.Foundation.*

fun NSData.toByteArray() : ByteArray? {
    val bytes = bytes?.let { it } ?: return null
    val ktBytes: CPointer<ByteVar> = bytes.reinterpret()
    return ByteArray(length.toInt()) { index -> ktBytes[index] }
}

fun ByteArray.toNSData() : NSData? {
    return NSString.create(string = this.toKString()).dataUsingEncoding(NSUTF8StringEncoding)
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