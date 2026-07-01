/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.bluetooth.annotations

/**
 * Marks a class as the definition of a Bluetooth device. The `com.splendo.kaluga.bluetooth.plugin` generates a typed
 * client and/or server (and their `Bluetooth` and `Simulated` implementations) from it, derived from the
 * [BluetoothService] properties the class exposes.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Bluetooth

/**
 * Overrides the simple name of the client type generated for a [Bluetooth] device. Defaults to `<DeviceName>Client`.
 * @property name the simple name to use for the generated client.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class BluetoothClientName(val name: String)

/**
 * Overrides the simple name of the server type generated for a [Bluetooth] device. Defaults to `<DeviceName>Server`.
 * @property name the simple name to use for the generated server.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class BluetoothServerName(val name: String)

/**
 * Marks a class as a GATT service of a [Bluetooth] device.
 * The [BluetoothCharacteristic] properties it exposes define the service's characteristics.
 * @property uuid the service UUID, either 16-bit shorthand (e.g. `"180a"`) or the full 128-bit form.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class BluetoothService(val uuid: String)

/**
 * Marks a class as a GATT characteristic of a [BluetoothService].
 * Its properties, annotated with access markers such as [Readable], [Writable], [Notifiable] or [Indicatable],
 * define the characteristic's value(s) and how they may be accessed.
 * @property uuid the characteristic UUID, either 16-bit shorthand or the full 128-bit form.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class BluetoothCharacteristic(val uuid: String)

/**
 * Marks a class as a GATT descriptor of a [BluetoothCharacteristic].
 * Like a characteristic, its properties define the descriptor's value(s) and access.
 *
 * NOTE: Descriptors for servers are not supported by Apple platforms.
 *
 * @property uuid the descriptor UUID, either 16-bit shorthand or the full 128-bit form.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class BluetoothDescriptor(val uuid: String)

/**
 * The local name a generated server advertises for a [Bluetooth] device.
 * @property name the advertised local name.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AdvertisingName(val name: String)

/**
 * Marks a [BluetoothService] property of a [Bluetooth] device so that its UUID is included in the service UUIDs a generated server advertises.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Advertising

/**
 * Marks a characteristic or descriptor property as readable, generating a GATT read for it.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Readable

/**
 * Marks a characteristic or descriptor property as writable with a response, generating a GATT write for it.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Writable

/**
 * Marks a characteristic or descriptor property as writable without a response, generating a GATT write-without-response for it.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class WritableWithoutResponse

/**
 * Marks a characteristic or descriptor property as writable with an authenticated signed write.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class WritableSigned

/**
 * Marks a characteristic property as notifiable: the server can push value changes to subscribed clients without
 * acknowledgement, and the client can observe them.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Notifiable

/**
 * Marks a characteristic property as indicatable: like [Notifiable], but each pushed value change is acknowledged by the client.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Indicatable

/**
 * Marks a characteristic or descriptor property as requiring an encrypted (bonded) connection to access.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Encrypted
