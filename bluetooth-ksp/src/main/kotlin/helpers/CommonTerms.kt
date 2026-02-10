/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.ksp.helpers

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

// Kotlin keywords
const val AS = "as"
const val FALSE = "false"
const val IS = "is"
const val IT = "it"
const val NULL = "null"
const val RETURN = "return"
const val THIS = "this"
const val TO = "to"
const val TRUE = "true"
const val VAL = "val"
const val VALUE = "value"

// Kotlin control flow
const val IF = "if"
const val ELSE = "else"
const val WHEN = "when"
const val WITH = "with"
const val LET = "let"
const val ALSO = "also"
const val LAZY = "lazy"

// Kotlin collections & flows
const val EMPTY_LIST = "emptyList()"
const val SET_OF = "setOf"
const val MUTABLE_MAP_OF = "mutableMapOf"
const val MUTABLE_FLOW = "mutableFlow"
const val ALL = "all"
const val CONTAINS = "contains"
const val DROP = "drop"
const val EMIT = "emit"
const val FIND = "find"
const val FIRST = "first"
const val GET_OR_PUT = "getOrPut"
const val MAP = "map"
const val SUBSCRIPTION_COUNT = "subscriptionCount"

// Kotlin Coroutines
const val COROUTINE_SCOPE = "coroutineScope"
const val COROUTINE_CONTEXT = "coroutineContext"
const val ON_AWAIT = "onAwait"

// Common Terms
const val ACTION = "Action"
const val BUILDER = "builder"
const val CHANGED = "Changed"
const val CLOSE = "close"
const val CONFIGURE = "configure"
const val DELEGATE = "Delegate"
const val ERROR = "error"
const val EXCEPTION = "exception"
const val FAILURE = "Failure"
const val IS_CLOSED = "isClosed"
const val OFFSET = "offset"
const val OR_NULL = "OrNull"
const val RESPONSE = "response"
const val SUCCESS = "Success"

// Encoding
const val BLUETOOTH_FORMAT = "bluetoothFormat"
const val DECODE_FROM_BYTE_ARRAY = "decodeFromByteArray"
const val ENCODE_TO_BYTE_ARRAY = "encodeToByteArray"
const val FORMAT = "format"
const val DESERIALIZATION_STRATEGY = "deserializationStrategy"

// Bluetooth
const val BLUETOOTH = "bluetooth"
const val IDENTIFIER = "identifier"
const val SIMULATED = "simulated"
const val UUID = "UUID"

// Client
const val GENERATE_CLIENT = "generateClient"

// Server
const val SERVER = "server"
const val REMOTES = "remotes"
const val GENERATE_REMOTE = "generateRemote"

// Device
const val DEVICE = "device"
const val DEVICES = "devices"
const val ALL_DEVICES = "allDevices"
const val SUBSCRIBED_DEVICES = "subscribedDevices"

// Service
const val SERVICE = "service"
const val SERVICES = "services"
const val FROM_SERVICE = "fromService"
const val INCLUDED_SERVICE = "includedService"
const val INCLUDED_SERVICES = "includedServices"
const val DISCOVERED_SERVICES = "discoveredServices"
const val FROM_DISCOVERED_SERVICES = "fromDiscoveredServices"

// Characteristic
const val CHARACTERISTIC = "characteristic"
const val CHARACTERISTICS = "characteristics"
const val FROM_CHARACTERISTIC = "fromCharacteristic"

// Descriptor
const val DESCRIPTOR = "descriptor"
const val DESCRIPTORS = "descriptors"

// Reading
const val ENCRYPTED = "encrypted"
const val READ = "read"
const val READABLE = "readable"
const val ON_READ = "onRead"
const val PROPERTIES = "properties"

// Writing
const val WRITE = "write"
const val WRITABLE = "writable"
const val ON_WRITE = "onWrite"
const val ON_FAILED_TO_WRITE = "onFailedToWrite"

// Notifying
const val NOTIFY = "notify"
const val NOTIFY_ALL = "notifyAll"
const val NOTIFIABLE = "notifiable"
const val ON_SUBSCRIBE = "onSubscribe"
const val ON_SUBSCRIBE_TO = "onSubscribeTo"
const val ON_UNSUBSCRIBE = "onUnsubscribe"
const val ON_UNSUBSCRIBE_TO = "onUnsubscribeTo"
const val SUBSCRIBERS = "Subscribers"

val KSDeclaration.delegateParameterName: String get() = "${simpleName.asString().replaceFirstChar { it.lowercase() }}$DELEGATE"

val KSPropertyDeclaration.orNullIfNullable: String get() = if (isNullable) OR_NULL else ""

val KSPropertyDeclaration.onReadMethodName: String get() = "$ON_READ${simpleName.asString().replaceFirstChar { it.uppercase() }}"
val KSPropertyDeclaration.onWriteMethodName: String get() = "$ON_WRITE${simpleName.asString().replaceFirstChar { it.uppercase() }}"
val KSPropertyDeclaration.subscribeMethodName: String get() = "$ON_SUBSCRIBE_TO${simpleName.asString().replaceFirstChar { it.uppercase() }}"
val KSPropertyDeclaration.unsubscribeMethodName: String get() = "$ON_UNSUBSCRIBE_TO${simpleName.asString().replaceFirstChar { it.uppercase() }}"
