package com.splendo.kaluga.bluetooth.extensions

import com.splendo.kaluga.bluetooth.RemoteCharacteristic
import com.splendo.kaluga.bluetooth.RemoteDescriptor
import com.splendo.kaluga.bluetooth.RemoteService
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.descriptors
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.discoveredServices
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.bluetooth.value
import com.splendo.kaluga.bluetooth.valueOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.DeserializationStrategy

/**
 * The [Flow] of the [RemoteService] for [serviceUUID] of this [ConnectableDevice]. Only emits after services have been discovered.
 * @param serviceUUID string service uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] is not valid
 * @return the [Flow] of the [RemoteService] associated with [serviceUUID]. Flow throws [NoSuchElementException] if the service cannot be found after discovery.
 */
fun ConnectableDevice.serviceFlow(serviceUUID: String) = discoveredServices()[serviceUUID]

/**
 * Provides access to a [RemoteService] [Flow] by service string uuids.
 * Only emits after services have been discovered.
 * @param serviceUUID string service uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] is not valid
 * @return the [Flow] of the [com.splendo.kaluga.bluetooth.RemoteService] associated with [serviceUUID]. Flow throws [NoSuchElementException] if the service cannot be found after discovery.
 */
fun Flow<ConnectableDevice?>.serviceFlow(serviceUUID: String) = discoveredServices()[serviceUUID]

/**
 * The [Flow] of the [RemoteService] for [serviceUUID] of this [ConnectableDevice], or `null` if not available.
 * @param serviceUUID string service uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] is not valid
 * @return the [Flow] of the [RemoteService] associated with [serviceUUID] or `null` if the service is not available.
 */
fun ConnectableDevice.serviceOrNullFlow(serviceUUID: String) = services().getOrNull(serviceUUID)

/**
 * Provides access to a [RemoteService] [Flow] by service string uuids.
 * Emits `null` if the service cannot be found.
 * @param serviceUUID string service uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] is not valid
 * @return the [Flow] of the [RemoteService] associated with [serviceUUID] or `null` if the service is not available.
 */
fun Flow<ConnectableDevice?>.serviceOrNullFlow(serviceUUID: String) = services().getOrNull(serviceUUID)

/**
 * The [RemoteService] for [serviceUUID] of this [ConnectableDevice], suspending until services have been discovered.
 * @param serviceUUID string service uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] is not valid
 * @throws NoSuchElementException if the service cannot be found after discovery.
 * @return the [RemoteService] associated with [serviceUUID]
 */
suspend fun ConnectableDevice.service(serviceUUID: String) = serviceFlow(serviceUUID).first()

/**
 * Provides access to [RemoteService] by service string uuids.
 * The method will suspend until services have been discovered.
 * @param serviceUUID string service uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] is not valid
 * @throws NoSuchElementException if the service cannot be found after discovery.
 * @return the [RemoteService] associated with [serviceUUID]
 */
suspend fun Flow<ConnectableDevice?>.service(serviceUUID: String) = serviceFlow(serviceUUID)
    .first()

/**
 * The [RemoteService] for [serviceUUID] of this [ConnectableDevice], or `null` if not available.
 * @param serviceUUID string service uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] is not valid
 * @return the [RemoteService] associated with [serviceUUID] or `null` if not available
 */
suspend fun ConnectableDevice.serviceOrNull(serviceUUID: String) = serviceOrNullFlow(serviceUUID).first()

/**
 * Provides access to [RemoteService] by service and characteristic string uuids or `null` if not available.
 * @param serviceUUID string service uuid representation
 * @throws UUIDException.InvalidFormat
 * @return the [RemoteService] associated with [serviceUUID] or `null` if not available
 */
suspend fun Flow<ConnectableDevice?>.serviceOrNull(serviceUUID: String) = serviceOrNullFlow(serviceUUID)
    .first()

/**
 * The [Flow] of the [RemoteCharacteristic] for [serviceUUID]/[characteristicUUID] of this [ConnectableDevice]. Only emits after services have been discovered.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Flow throws [NoSuchElementException] if the characteristic cannot be found after discovery.
 */
fun ConnectableDevice.characteristicFlow(serviceUUID: String, characteristicUUID: String) = serviceFlow(serviceUUID).characteristics()[characteristicUUID]

/**
 * Provides access to [RemoteCharacteristic]'s flow by service and characteristic string uuids.
 * Only emits after services have been discovered.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Flow throws [NoSuchElementException] if the characteristic cannot be found after discovery.
 */
fun Flow<ConnectableDevice?>.characteristicFlow(serviceUUID: String, characteristicUUID: String) = serviceFlow(serviceUUID)
    .characteristics()[characteristicUUID]

/**
 * The [Flow] of the [RemoteCharacteristic] for [serviceUUID]/[characteristicUUID] of this [ConnectableDevice], or `null` if not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID] or `null` if the characteristic is not available.
 */
fun ConnectableDevice.characteristicOrNullFlow(serviceUUID: String, characteristicUUID: String) = serviceOrNullFlow(serviceUUID).characteristics().getOrNull(characteristicUUID)

/**
 * Provides access to [RemoteCharacteristic]'s flow by service and characteristic string uuids.
 * Emits `null` if the characteristic cannot be found.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID] or `null` if the characteristic is not available.
 */
fun Flow<ConnectableDevice?>.characteristicOrNullFlow(serviceUUID: String, characteristicUUID: String) = serviceOrNullFlow(serviceUUID)
    .characteristics().getOrNull(characteristicUUID)

/**
 * The [RemoteCharacteristic] for [serviceUUID]/[characteristicUUID] of this [ConnectableDevice], suspending until available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @throws NoSuchElementException if the characteristic cannot be found after discovery.
 * @return the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]
 */
suspend fun ConnectableDevice.characteristic(serviceUUID: String, characteristicUUID: String) = characteristicFlow(serviceUUID, characteristicUUID).first()

/**
 * Provides access to [RemoteCharacteristic] by service and characteristic string uuids.
 * The method will suspend if characteristic is not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @throws NoSuchElementException if the characteristic cannot be found after discovery.
 * @return the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]
 */
suspend fun Flow<ConnectableDevice?>.characteristic(serviceUUID: String, characteristicUUID: String) = characteristicFlow(serviceUUID, characteristicUUID)
    .first()

/**
 * The [RemoteCharacteristic] for [serviceUUID]/[characteristicUUID] of this [ConnectableDevice], or `null` if not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID] or `null` if not available
 */
suspend fun ConnectableDevice.characteristicOrNull(serviceUUID: String, characteristicUUID: String) = characteristicOrNullFlow(serviceUUID, characteristicUUID).first()

/**
 * Provides access to [RemoteCharacteristic] by service and characteristic string uuids or `null` if not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID] or `null` if not available
 */
suspend fun Flow<ConnectableDevice?>.characteristicOrNull(serviceUUID: String, characteristicUUID: String) = characteristicOrNullFlow(serviceUUID, characteristicUUID)
    .first()

/**
 * The [Flow] of the [RemoteDescriptor] for [serviceUUID]/[characteristicUUID]/[descriptorUUID] of this [ConnectableDevice]. Only emits after services have been discovered.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param descriptorUUID string descriptor uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID], [characteristicUUID], or [descriptorUUID] is not valid
 * @return the [Flow] of the [RemoteDescriptor] associated with [serviceUUID], [characteristicUUID], and [descriptorUUID]. Flow throws [NoSuchElementException] if the descriptor cannot be found after discovery.
 */
fun ConnectableDevice.descriptorFlow(serviceUUID: String, characteristicUUID: String, descriptorUUID: String) =
    characteristicFlow(serviceUUID, characteristicUUID).descriptors()[descriptorUUID]

/**
 * Provides access to [RemoteDescriptor]'s flow by service, characteristic, and descriptor string uuids.
 * Only emits after services have been discovered.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param descriptorUUID string descriptor uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID], [characteristicUUID], or [descriptorUUID] is not valid
 * @return the [Flow] of the [RemoteDescriptor] associated with [serviceUUID], [characteristicUUID], and [descriptorUUID]. Flow throws [NoSuchElementException] if the descriptor cannot be found after discovery.
 */
fun Flow<ConnectableDevice?>.descriptorFlow(serviceUUID: String, characteristicUUID: String, descriptorUUID: String) = characteristicFlow(serviceUUID, characteristicUUID)
    .descriptors()[descriptorUUID]

/**
 * The [Flow] of the [RemoteDescriptor] for [serviceUUID]/[characteristicUUID]/[descriptorUUID] of this [ConnectableDevice], or `null` if not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param descriptorUUID string descriptor uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID], [characteristicUUID], or [descriptorUUID] is not valid
 * @return the [Flow] of the [RemoteDescriptor] associated with [serviceUUID], [characteristicUUID], and [descriptorUUID] or `null` if the descriptor is not available.
 */
fun ConnectableDevice.descriptorOrNullFlow(serviceUUID: String, characteristicUUID: String, descriptorUUID: String) =
    characteristicOrNullFlow(serviceUUID, characteristicUUID).descriptors().getOrNull(descriptorUUID)

/**
 * Provides access to [RemoteDescriptor]'s flow by service, characteristic, and descriptor string uuids.
 * Emits `null` if the descriptor cannot be found.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param descriptorUUID string descriptor uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID], [characteristicUUID], or [descriptorUUID] is not valid
 * @return the [Flow] of the [RemoteDescriptor] associated with [serviceUUID], [characteristicUUID], and [descriptorUUID] or `null` if the descriptor is not available.
 */
fun Flow<ConnectableDevice?>.descriptorOrNullFlow(serviceUUID: String, characteristicUUID: String, descriptorUUID: String) =
    characteristicOrNullFlow(serviceUUID, characteristicUUID)
        .descriptors().getOrNull(descriptorUUID)

/**
 * The [RemoteDescriptor] for [serviceUUID]/[characteristicUUID]/[descriptorUUID] of this [ConnectableDevice], suspending until available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param descriptorUUID string descriptor uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID], [characteristicUUID], or [descriptorUUID] is not valid
 * @throws NoSuchElementException if the descriptor cannot be found after discovery.
 * @return the [RemoteDescriptor] associated with [serviceUUID], [characteristicUUID], and [descriptorUUID]
 */
suspend fun ConnectableDevice.descriptor(serviceUUID: String, characteristicUUID: String, descriptorUUID: String) =
    descriptorFlow(serviceUUID, characteristicUUID, descriptorUUID).first()

/**
 * Provides access to [RemoteDescriptor] by service, characteristic and descriptor string uuids.
 * The method will suspend if descriptor is not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param descriptorUUID string descriptor uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID], [characteristicUUID], or [descriptorUUID] is not valid
 * @throws NoSuchElementException if the descriptor cannot be found after discovery.
 * @return the [RemoteDescriptor] associated with [serviceUUID], [characteristicUUID], and [descriptorUUID]
 */
suspend fun Flow<ConnectableDevice?>.descriptor(serviceUUID: String, characteristicUUID: String, descriptorUUID: String) =
    descriptorFlow(serviceUUID, characteristicUUID, descriptorUUID)
        .first()

/**
 * The [Flow] of the [ByteArray] value of the [RemoteCharacteristic] for [serviceUUID]/[characteristicUUID] of this [ConnectableDevice]. Only emits after services have been discovered.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [ByteArray] value of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Flow throws [NoSuchElementException] if the characteristic cannot be found after discovery.
 */
fun ConnectableDevice.dataFlow(serviceUUID: String, characteristicUUID: String) = characteristicFlow(serviceUUID, characteristicUUID).value()

/**
 * Provides access to device data flow by service and characteristic string uuids.
 * Only emits after services have been discovered.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [ByteArray] value of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Flow throws [NoSuchElementException] if the characteristic cannot be found after discovery.
 */
fun Flow<ConnectableDevice?>.dataFlow(serviceUUID: String, characteristicUUID: String) = characteristicFlow(serviceUUID, characteristicUUID).value()

/**
 * The [Flow] of the [T] value of the [RemoteCharacteristic] for [serviceUUID]/[characteristicUUID] of this [ConnectableDevice]. Only emits after services have been discovered.
 * @param T the type of the data to receive
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the [ByteArray] to [T]
 * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the [ByteArray] to [T]
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [T] value of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Flow throws [NoSuchElementException] if the characteristic cannot be found after discovery.
 */
inline fun <reified T> ConnectableDevice.dataFlow(
    serviceUUID: String,
    characteristicUUID: String,
    deserializationStrategy: DeserializationStrategy<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
) = characteristicFlow(serviceUUID, characteristicUUID).value(deserializationStrategy, bluetoothFormat)

/**
 * Provides access to device data flow [T] by service and characteristic string uuids.
 * Only emits after services have been discovered.
 * @param T the type of the data to receive
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the [ByteArray] to [T]
 * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the [ByteArray] to [T]
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [T] value of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Flow throws [NoSuchElementException] if the characteristic cannot be found after discovery.
 */
inline fun <reified T> Flow<ConnectableDevice?>.dataFlow(
    serviceUUID: String,
    characteristicUUID: String,
    deserializationStrategy: DeserializationStrategy<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
) = characteristicFlow(serviceUUID, characteristicUUID).value(deserializationStrategy, bluetoothFormat)

/**
 * The [Flow] of the [ByteArray] value of the [RemoteCharacteristic] for [serviceUUID]/[characteristicUUID] of this [ConnectableDevice]. Emits an empty [ByteArray] if not available.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [ByteArray] value of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Emits an empty [ByteArray] if the characteristic is not available.
 */
fun ConnectableDevice.dataOrEmptyFlow(serviceUUID: String, characteristicUUID: String) = characteristicOrNullFlow(serviceUUID, characteristicUUID).value()

/**
 * Provides access to device data flow by service and characteristic string uuids.
 * Emits and empty [ByteArray] if the service cannot be found.
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [ByteArray] value of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Emits an empty [ByteArray] if the characteristic is not available.
 */
fun Flow<ConnectableDevice?>.dataOrEmptyFlow(serviceUUID: String, characteristicUUID: String) = characteristicOrNullFlow(serviceUUID, characteristicUUID).value()

/**
 * The [Flow] of the [T] value of the [RemoteCharacteristic] for [serviceUUID]/[characteristicUUID] of this [ConnectableDevice]. Emits `null` if not available.
 * @param T the type of the data to receive
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the [ByteArray] to [T]
 * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the [ByteArray] to [T]
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [T] value of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Emits `null` if the descriptor cannot be found.
 */
inline fun <reified T> ConnectableDevice.dataOrNullFlow(
    serviceUUID: String,
    characteristicUUID: String,
    deserializationStrategy: DeserializationStrategy<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
) = characteristicOrNullFlow(serviceUUID, characteristicUUID).valueOrNull(deserializationStrategy, bluetoothFormat)

/**
 * Provides access to device data flow [T] by service and characteristic string uuids.
 * Emits `null` if the descriptor cannot be found.
 * @param T the type of the data to receive
 * @param serviceUUID string service uuid representation
 * @param characteristicUUID string characteristic uuid representation
 * @param deserializationStrategy the [DeserializationStrategy] to use to deserialize the [ByteArray] to [T]
 * @param bluetoothFormat the [BluetoothFormat] to use to deserialize the [ByteArray] to [T]
 * @throws UUIDException.InvalidFormat if [serviceUUID] or [characteristicUUID] is not valid
 * @return the [Flow] of the [T] value of the [RemoteCharacteristic] associated with [serviceUUID] and [characteristicUUID]. Emits `null` if the descriptor cannot be found.
 */
inline fun <reified T> Flow<ConnectableDevice?>.dataOrNullFlow(
    serviceUUID: String,
    characteristicUUID: String,
    deserializationStrategy: DeserializationStrategy<T>,
    bluetoothFormat: BluetoothFormat = BluetoothFormat,
) = characteristicOrNullFlow(serviceUUID, characteristicUUID).valueOrNull(deserializationStrategy, bluetoothFormat)
