/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

@file:JvmName("CommonUUID")
package com.splendo.kaluga.bluetooth

import kotlin.jvm.JvmName

expect class UUID

expect val UUID.uuidString: String

expect fun uuidFrom(uuidString:String):UUID

expect fun randomUUID():UUID

/**
 * The UUID format is the same for all services and characteristics and defined in bluetooth specification.
 *
 * @see <a href="https://www.bluetooth.com/specifications/gatt/characteristics/">GATT Characteristics specification</a>
 * @see <a href="https://btprodspecificationrefs.blob.core.windows.net/assigned-numbers/Assigned%20Number%20Types/Service%20Discovery.pdf">Service discovery</a>
 */
fun uuidFromShort(uuidString: String): UUID = uuidFrom("0000$uuidString-0000-1000-8000-00805f9b34fb")
internal fun String.isShortUUID() = length == 4

