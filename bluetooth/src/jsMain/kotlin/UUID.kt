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

package com.splendo.kaluga.bluetooth

actual data class UUID (val uuidString: String) {

    companion object {
        val validationRegex = "/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i".toRegex()
    }

    val isValid: Boolean
        get() = validationRegex.matches(uuidString)
}

actual val UUID.uuidString: String
    get() = uuidString

actual fun uuidFrom(uuidString:String):UUID = UUID(uuidString = uuidString)

actual fun randomUUID():UUID = TODO()

