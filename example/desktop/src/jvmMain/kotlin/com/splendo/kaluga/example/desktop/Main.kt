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

package com.splendo.kaluga.example.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.splendo.kaluga.example.arch.AppRootScreen
import com.splendo.kaluga.example.feature.datetime.datetimeFeatureModule
import com.splendo.kaluga.example.feature.localization.localizationFeatureModule
import com.splendo.kaluga.example.feature.scientific.scientificFeatureModule
import com.splendo.kaluga.example.koin.initKoin

/**
 * Desktop (Kotlin/JVM, Compose Desktop) entry point. Mirrors the web (`:web`) host: it bootstraps
 * Koin and renders the shared [AppRootScreen] Compose UI, pulling feature contributions from Koin.
 *
 * Unlike the web host it does not reuse `:shared` — that aggregator pulls in features whose Kaluga
 * libraries have no JVM target (bluetooth, location, media, system, permissions). The desktop host
 * therefore loads only the feature modules whose Kaluga libraries are JVM-capable today: date-time,
 * scientific and localization. As more Kaluga libraries gain a JVM target their feature modules can
 * be added to this list (or a JVM-capable shared aggregator introduced).
 */
fun main() {
    initKoin(
        customModules = listOf(
            datetimeFeatureModule,
            localizationFeatureModule,
            scientificFeatureModule,
        ),
    )
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Kaluga Example",
        ) {
            AppRootScreen()
        }
    }
}
