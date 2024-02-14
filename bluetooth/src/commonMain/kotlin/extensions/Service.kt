package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmName

/**
 * Provides access to [Service] by given string uuid
 * @param uuidString string service uuid representation
 * @throws UUIDException.InvalidFormat
 */
@JvmName("getService")
operator fun Flow<List<Service>>.get(uuidString: String) = this[uuidFrom(uuidString)]
