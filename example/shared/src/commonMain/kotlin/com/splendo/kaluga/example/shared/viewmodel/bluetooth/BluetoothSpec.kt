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

package com.splendo.kaluga.example.shared.viewmodel.bluetooth

import com.splendo.kaluga.bluetooth.uuidFrom

object BluetoothSpec {

    object HeartRateService {
        val UUID = uuidFrom("180D")
        val HEART_RATE_MEASUREMENT_CHARACTERISTIC = uuidFrom("2A37")
        val SENSOR_LOCATION_CHARACTERISTIC = uuidFrom("2A38")
        val HEART_RATE_CONTROL_POINT_CHARACTERISTIC = uuidFrom("2A39")
    }

    object KalugaSensorService {
        val UUID = uuidFrom("b7f5bde4-65b2-4a26-b692-b6c1f90e6238")
        val DATA_STREAM_CHARACTERISTIC = uuidFrom("73b0e3f5-7f0a-4e31-a993-dbfbf490c1a4")
        val CONTROL_CHARACTERISTIC = uuidFrom("a3d1bc77-b3e2-4031-8e6b-21cbb2e5b1a9")
    }

    object KalugaSensorInfo {
        val UUID = uuidFrom("5a3f0d09-5cf9-4df1-b624-bfe93a2a68ad")
        val STATUS_CHARACTERISTIC = uuidFrom("94cc7369-2b5f-4b2e-9a12-ae7ad4fd90c8")
        val INFO_CHARACTERISTIC = uuidFrom("e85a71f7-3d54-4a43-ba9b-89ff9e3428e0")
    }
}
