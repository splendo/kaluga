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

            val connectedDevice = className("ConnectedDevice")

            val localService = className("LocalService")
            val localServiceDSL = className("LocalService", "DSL")
            val localCharacteristic = className("LocalCharacteristic")
            val localCharacteristicDSL = className("LocalCharacteristic", "DSL")
            val localCharacteristicNotifiable = className("LocalCharacteristic", "Notifiable")
            val localDescriptor = className("LocalDescriptor")
            val localDescriptorDSL = className("LocalDescriptor", "DSL")
        }
        override val packageName = "com.splendo.kaluga.bluetooth"
        val bluetoothService = className("BluetoothService")
        val remoteService = className("RemoteService")
        val remoteCharacteristic = className("RemoteCharacteristic")
        val remoteDescriptor = className("RemoteDescriptor")
        val writeResponse = className("GattResponse", "WriteResponse")
        val readSuccess = className("GattResponse", "ReadSuccess")
        val readError = className("GattResponse", "ReadError")
        val requestNotSupported = className("GattResponse", "RequestNotSupported")
        val writeRequestRejected = className("GattResponse", "WriteRequestRejected")

        val writeProperty = className("CharacteristicProperty", "Write")
        val writeWithoutResponseProperty = className("CharacteristicProperty", "WriteWithoutResponse")
        val signedWriteProperty = className("CharacteristicProperty", "SignedWrite")
        val notifyProperty = className("CharacteristicProperty", "Notify")
        val indicatableProperty = className("CharacteristicProperty", "Indicatable")

        val get = memberName("get")
        val uuidFrom = memberName("uuidFrom")
        val value = memberName("value")
        val discoveredServices = memberName("discoveredServices")
    }

    object Kotlin : ClassNameProvider {
        override val packageName: String = "kotlin"

        val exception = className("Exception")
        val pair = className("Pair")
        val triple = className("Triple")

    }

    object KotlinX : ClassNameProvider {
        override val packageName: String = "kotlinx"

        object Coroutines : ClassNameProvider {
            override val packageName: String = "${KotlinX.packageName}.coroutines"

            object Flow : ClassNameProvider {
                override val packageName: String = "${Coroutines.packageName}.flow"
                val flow = className("Flow")
                val first = memberName("first")
                val map = memberName("map")
            }
        }
    }

}