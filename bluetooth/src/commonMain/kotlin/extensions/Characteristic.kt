package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmName

/**
 * Provides access to [Characteristic] by given string uuid
 * @param uuidString string characteristic uuid representation
 * @throws UUIDException.InvalidFormat
 */
@JvmName("getCharacteristic")
operator fun Flow<List<Characteristic>>.get(uuidString: String) = this[uuidFrom(uuidString)]
