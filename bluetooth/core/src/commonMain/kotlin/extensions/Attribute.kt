package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.bluetooth.Attribute
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.getOrNull
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmName

/**
 * Provides access to an [Attribute] by given string uuid
 * Flow throws [NoSuchElementException] if the attribute cannot be found.
 * @param uuidString string attribute uuid representation
 * @throws UUIDException.InvalidFormat
 * @return the [Flow] of [Attribute] of [T] with the given [UUID]. Flow throws [NoSuchElementException] if the attribute cannot be found.
 */
@JvmName("getAttribute")
operator fun <T : Attribute> Flow<List<T>>.get(uuidString: String): Flow<T> = get(uuidFrom(uuidString))

/**
 * Provides access to an [Attribute] by given string uuid, or `null` if it cannot be found
 * @param uuidString string attribute uuid representation
 * @throws UUIDException.InvalidFormat
 * @return the [Flow] of [Attribute] of [T] with the given [UUID] or `null` if the attribute cannot be found.
 */
@JvmName("getAttributeOrNull")
fun <T : Attribute> Flow<List<T>>.getOrNull(uuidString: String): Flow<T?> = getOrNull(uuidFrom(uuidString))
