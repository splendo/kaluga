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

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.splendo.kaluga.example.arch.AppRootScreen
import com.splendo.kaluga.example.feature.datetime.datetimeFeatureModule
import com.splendo.kaluga.example.feature.localization.localizationFeatureModule
import com.splendo.kaluga.example.feature.scientific.scientificFeatureModule
import com.splendo.kaluga.example.koin.initKoin
import kotlinx.browser.document

/**
 * Web (Kotlin/Wasm) entry point. Only the features whose Kaluga modules support the `wasmJs`
 * target are wired in: localization, date/time and scientific units. Koin is bootstrapped with
 * just those feature modules; `AppRootScreen` then renders the same Compose UI used by the other
 * platform hosts, pulling the contributions from Koin.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(
        customModules = listOf(
            localizationFeatureModule,
            datetimeFeatureModule,
            scientificFeatureModule,
        ),
    )
    ComposeViewport(document.body!!) {
        AppRootScreen()
    }
}
