package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.base.utils.bytesOf
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.services
import kotlinx.coroutines.flow.*

/**
 * Provides access to device data flow by service and characteristic string uuids.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat
 */
fun Flow<Device?>.dataFlow(serviceUUID: String, characteristicUUID: String) = characteristicsFlow(serviceUUID, characteristicUUID).flatMapConcat {
    it.enableNotification()
    it
}.map { it ?: bytesOf() }

/**
 * Provides access to characteristic's flow by service and characteristic string uuids.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat
 */
fun Flow<Device?>.characteristicsFlow(serviceUUID: String, characteristicUUID: String) = services()[serviceUUID]
    .characteristics()[characteristicUUID]
    .filterNotNull()

/**
 * Provides access to [Characteristic] by service and characteristic string uuids.
 * The method will suspend if characteristic is not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat
 */
suspend fun Flow<Device?>.characteristic(serviceUUID: String, characteristicUUID: String) = services()[serviceUUID]
    .characteristics()[characteristicUUID]
    .filterNotNull()
    .first()
