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

package com.splendo.kaluga.example.shared.ui

/**
 * A feature exposed by the example app's CMP feature list.
 *
 * [availableOnMacOS] reflects whether the Kaluga module backing the feature has macOS targets. The
 * macOS host filters the list to only these entries; Android and iOS show every entry and route
 * macOS-only features through their existing native screens until Phase 5 migrates each one.
 */
enum class Feature(val title: String, val availableOnMacOS: Boolean) {
    Alerts(title = "Alerts", availableOnMacOS = false),
    Architecture(title = "Architecture", availableOnMacOS = false),
    Beacons(title = "Beacons", availableOnMacOS = false),
    Bluetooth(title = "Bluetooth", availableOnMacOS = true),
    DateTime(title = "Date / Time", availableOnMacOS = true),
    DateTimePicker(title = "Date / Time Picker", availableOnMacOS = false),
    Keyboard(title = "Keyboard", availableOnMacOS = false),
    Links(title = "Links", availableOnMacOS = true),
    LoadingIndicator(title = "Loading Indicator", availableOnMacOS = false),
    Location(title = "Location", availableOnMacOS = true),
    Media(title = "Media", availableOnMacOS = false),
    Permissions(title = "Permissions", availableOnMacOS = true),
    Resources(title = "Resources", availableOnMacOS = false),
    Scientific(title = "Scientific", availableOnMacOS = true),
    System(title = "System", availableOnMacOS = true),
}
