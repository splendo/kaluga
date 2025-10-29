package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.bluetooth.RemoteService
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmName

/**
 * Provides access to [RemoteService] by given string uuid
 * @param uuidString string service uuid representation
 * @throws UUIDException.InvalidFormat
 */
@JvmName("getService")
operator fun Flow<List<RemoteService>>.get(uuidString: String) = this[uuidFrom(uuidString)]
