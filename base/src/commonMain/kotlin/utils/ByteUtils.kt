package com.splendo.kaluga.base.utils

/**
 * Converts a [ByteArray] to a String representing the bytes as their hexadecimal value
 * @return The String representing the [ByteArray]s hexadecimal value
 */
fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }