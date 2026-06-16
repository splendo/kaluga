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

package com.splendo.kaluga.bluetooth.ksp

internal enum class GenerationType(val side: Side, val type: Type) {

    CLIENT_API(Side.CLIENT, Type.API),
    CLIENT_BLUETOOTH(Side.CLIENT, Type.BLUETOOTH),
    CLIENT_SIMULATOR(Side.CLIENT, Type.SIMULATOR),
    SERVER_API(Side.SERVER, Type.API),
    SERVER_BLUETOOTH(Side.SERVER, Type.BLUETOOTH),
    SERVER_SIMULATOR(Side.SERVER, Type.SIMULATOR),
    ;

    enum class Side {
        CLIENT,
        SERVER,
    }

    enum class Type {
        API,
        BLUETOOTH,
        SIMULATOR,
    }
}
