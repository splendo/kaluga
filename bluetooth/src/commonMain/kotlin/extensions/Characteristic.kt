package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmName

@JvmName("getCharacteristic")
operator fun Flow<List<Characteristic>>.get(uuidString: String) = this[uuidFrom(uuidString)]
