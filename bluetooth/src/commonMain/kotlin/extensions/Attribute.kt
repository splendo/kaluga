package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.bluetooth.Attribute
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmName

/**
 * Provides access to an [Attribute] by given string uuid
 * @param uuidString string attribute uuid representation
 * @throws UUIDException.InvalidFormat
 */
@JvmName("getAttribute")
operator fun <AttributeType, ReadAction, WriteAction> Flow<List<AttributeType>>.get(
    uuidString: String,
): Flow<AttributeType?>
where AttributeType : Attribute<ReadAction, WriteAction>, ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write = this[uuidFrom(uuidString)]
