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

package com.splendo.kaluga.example.shared

/**
 * String IDs the mobile feature modules register their [FeatureContribution]s with. Host
 * dispatchers (Android `ExampleActivity`, iOS `ExampleViewController`) inspect the id received
 * via `onNativeLaunch` and route to the corresponding native screen. The constants are mirrored
 * inside each `:feature-<mobile>` module as the contribution's `id`; this object is the
 * Swift-friendly central reference (`MobileFeatureIds.shared.ALERTS`).
 */
@Suppress("MemberVisibilityCanBePrivate")
object MobileFeatureIds {
    const val ALERTS = "alerts"
    const val ARCHITECTURE = "architecture"
    const val BEACONS = "beacons"
    const val DATE_TIME_PICKER = "datetimepicker"
    const val HUD = "hud"
    const val KEYBOARD = "keyboard"
    const val MEDIA = "media"
    const val RESOURCES = "resources"
}
