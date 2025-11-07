package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.bluetooth.Attribute
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.getOrNull
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmName

/**
 * Provides access to an [Attribute] by given string uuid
 * @param uuidString string attribute uuid representation
 * @throws UUIDException.InvalidFormat
 */
@JvmName("getAttribute")
operator fun <T : Attribute> Flow<List<T>>.get(uuidString: String): Flow<T> = get(uuidFrom(uuidString))

@JvmName("getAttributeOrNull")
fun <T : Attribute> Flow<List<T>>.getOrNull(uuidString: String): Flow<T?> = getOrNull(uuidFrom(uuidString))
