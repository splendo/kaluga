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

package com.splendo.kaluga.example.feature.bluetooth.generation

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * The app-controlled source of truth a server exposes. The [DemoServerDelegate] reads from it, and the
 * server view edits it — shared identically by the real Bluetooth server and the simulator.
 */
class DemoServerState {
    val reading = MutableStateFlow(0)
    val name = MutableStateFlow("Kaluga Demo Sensor")
    val lastThresholdWritten = MutableStateFlow<Int?>(null)
}
