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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

interface ClassNameProvider {
    val packageName: String

    fun className(vararg names: String) = ClassName(packageName, names.toList())
    fun memberName(simpleName: String) = MemberName(packageName, simpleName)
}

object References {
    object Base : ClassNameProvider {
        override val packageName: String = "com.splendo.kaluga.base"

        val singleThreadDispatcher = memberName("singleThreadDispatcher")

        object Utils : ClassNameProvider {
            override val packageName: String = "${Base.packageName}.utils"

            val complete = memberName("complete")
        }

        object Test : ClassNameProvider {
            override val packageName: String = "${Base.packageName}.test.mock"

            object Parameters : ClassNameProvider {
                override val packageName: String = "${Test.packageName}.parameters"
                val mock = memberName("mock")
            }

            val call = memberName("call")

            /** The `[Suspend]{Void,Single,Pair,...}ParametersMock` type alias for a method of [parameterCount] parameters. */
            fun methodMock(parameterCount: Int, suspended: Boolean): ClassName {
                val arity = when (parameterCount) {
                    0 -> "Void"
                    1 -> "Single"
                    2 -> "Pair"
                    3 -> "Triple"
                    4 -> "Quadruple"
                    5 -> "Quintuple"
                    else -> error("No mock type for $parameterCount parameters")
                }
                return className("${if (suspended) "Suspend" else ""}${arity}ParametersMock")
            }
        }
    }
    object Bluetooth : ClassNameProvider {
        object Device : ClassNameProvider {
            override val packageName = "${Bluetooth.packageName}.device"

            val identifier = className("Identifier")
        }
        object Serialization : ClassNameProvider {
            override val packageName = "${Bluetooth.packageName}.serialization"

            val bluetoothFormat = className("BluetoothFormat")
        }
        object Server : ClassNameProvider {
            override val packageName: String = "${Bluetooth.packageName}.server"

            val bluetoothServer = className("BluetoothServer")
            val baseBluetoothServerBuilder = className("BaseBluetoothServerBuilder")
            val serverSettings = className("ServerSettings")
            val bluetoothServerDSL = className("BluetoothServerDSL")
            val connectedDevice = className("ConnectedDevice")

            val localService = className("LocalService")
            val localServiceDSL = className("LocalService", "DSL")
            val localCharacteristic = className("LocalCharacteristic")
            val localCharacteristicDSL = className("LocalCharacteristic", "DSL")
            val localCharacteristicNotifiable = className("LocalCharacteristic", "Notifiable")
            val localDescriptor = className("LocalDescriptor")
        }
        override val packageName = "com.splendo.kaluga.bluetooth"
        val bluetoothClient = className("BluetoothClient")
        val remoteService = className("RemoteService")
        val remoteCharacteristic = className("RemoteCharacteristic")
        val remoteDescriptor = className("RemoteDescriptor")
        val writeResponse = className("GattResponse", "WriteResponse")
        val readResponse = className("GattResponse", "ReadResponse")
        val readSuccess = className("GattResponse", "ReadSuccess")
        val readError = className("GattResponse", "ReadError")

        val deviceUnavailable = className("GattResponse", "DeviceUnavailable")

        val writeProperty = className("CharacteristicProperty", "Write")
        val writeWithoutResponseProperty = className("CharacteristicProperty", "WriteWithoutResponse")
        val signedWriteProperty = className("CharacteristicProperty", "SignedWrite")
        val notifyProperty = className("CharacteristicProperty", "Notify")
        val indicatableProperty = className("CharacteristicProperty", "Indicate")
        val uuid = className("UUID")

        val writeTypeWithResponse = MemberName(className("WriteType"), "WithResponse")
        val writeTypeWithoutResponse = MemberName(className("WriteType"), "WithoutResponse")

        val get = memberName("get")
        val getOrNull = memberName("getOrNull")
        val uuidFrom = memberName("uuidFrom")
        val value = memberName("value")
        val discoveredServices = memberName("discoveredServices")
    }

    object Permissions : ClassNameProvider {
        override val packageName: String = "com.splendo.kaluga.permissions.base"

        val permissions = className("Permissions")
    }

    object Kotlin : ClassNameProvider {
        override val packageName: String = "kotlin"

        object Coroutines : ClassNameProvider {
            override val packageName: String = "${Kotlin.packageName}.coroutines"

            val coroutineContext = className("CoroutineContext")
        }

        val exception = className("Exception")
        val pair = className("Pair")

        val autoCloseable = className("AutoCloseable")
    }

    object KotlinX : ClassNameProvider {
        override val packageName: String = "kotlinx"

        object Coroutines : ClassNameProvider {
            override val packageName: String = "${KotlinX.packageName}.coroutines"
            val coroutineScope = className("CoroutineScope")
            val deferred = className("Deferred")
            val completableDeferred = className("CompletableDeferred")

            val async = memberName("async")
            val launch = memberName("launch")
            val cancel = memberName("cancel")
            val cancelChildren = memberName("cancelChildren")
            val coroutineScopeMethod = memberName("coroutineScope")

            object Flow : ClassNameProvider {
                override val packageName: String = "${Coroutines.packageName}.flow"
                val flow = className("Flow")
                val mutableStateFlow = className("MutableStateFlow")
                val mutableSharedFlow = className("MutableSharedFlow")
                val first = memberName("first")
                val map = memberName("map")
                val asStateFlow = memberName("asStateFlow")
                val asSharedFlow = memberName("asSharedFlow")
                val distinctUntilChanged = memberName("distinctUntilChanged")
                val collect = memberName("collect")
                val update = memberName("update")
                val onCompletion = memberName("onCompletion")
                val emptyFlow = memberName("emptyFlow")
            }

            object Selects : ClassNameProvider {

                override val packageName: String = "${Coroutines.packageName}.selects"

                val select = memberName("select")
            }
        }
    }
}
