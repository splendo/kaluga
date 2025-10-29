package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.descriptors
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.RemoteDescriptor
import com.splendo.kaluga.bluetooth.services
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

/**
 * Provides access to device data flow by service, characteristic and descriptor string uuids.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat
 */
fun Flow<ConnectableDevice?>.dataFlow(serviceUUID: String, characteristicUUID: String) = characteristicsFlow(serviceUUID, characteristicUUID).flatMapLatest { characteristic ->
    characteristic.map { it ?: byteArrayOf() }
}

/**
 * Provides access to device data flow by service, characteristic and descriptor string uuids.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat
 */
fun Flow<ConnectableDevice?>.dataFlow(serviceUUID: String, characteristicUUID: String, descriptorUUID: String) =
    descriptorsFlow(serviceUUID, characteristicUUID, descriptorUUID).flatMapLatest { descriptor ->
        descriptor.map { it ?: byteArrayOf() }
    }

/**
 * Provides access to characteristic's flow by service and characteristic string uuids.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat
 */
fun Flow<ConnectableDevice?>.characteristicsFlow(serviceUUID: String, characteristicUUID: String) = services()[serviceUUID]
    .characteristics()[characteristicUUID]
    .filterNotNull()

/**
 * Provides access to [Characteristic] by service and characteristic string uuids.
 * The method will suspend if characteristic is not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat
 */
suspend fun Flow<ConnectableDevice?>.characteristic(serviceUUID: String, characteristicUUID: String) = services()[serviceUUID]
    .characteristics()[characteristicUUID]
    .filterNotNull()
    .first()

/**
 * Provides access to descriptors's flow by service, characteristic and descriptor string uuids.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param descriptorUUID string descriptor uuid representation
 * @throws UUIDException.InvalidFormat
 */
fun Flow<ConnectableDevice?>.descriptorsFlow(serviceUUID: String, characteristicUUID: String, descriptorUUID: String) = services()[serviceUUID]
    .characteristics()[characteristicUUID]
    .descriptors()[descriptorUUID]
    .filterNotNull()

/**
 * Provides access to [RemoteDescriptor] by service, characteristic and descriptor string uuids.
 * The method will suspend if descriptor is not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param descriptorUUID string descriptor uuid representation
 * @throws UUIDException.InvalidFormat
 */
suspend fun Flow<ConnectableDevice?>.descriptor(serviceUUID: String, characteristicUUID: String, descriptorUUID: String) = services()[serviceUUID]
    .characteristics()[characteristicUUID]
    .descriptors()[descriptorUUID]
    .filterNotNull()
    .first()
