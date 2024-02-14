package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.base.utils.bytesOf
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.services
import kotlinx.coroutines.flow.*

fun Flow<Device?>.dataFlow(serviceUUID: String, characteristicUUID: String) = characteristicsFlow(serviceUUID, characteristicUUID).flatMapConcat {
    it.enableNotification()
    it
}.map { it ?: bytesOf() }

fun Flow<Device?>.characteristicsFlow(serviceUUID: String, characteristicUUID: String) = services()[serviceUUID]
    .characteristics()[characteristicUUID]
    .filterNotNull()
