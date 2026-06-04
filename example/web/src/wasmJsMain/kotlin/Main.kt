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
import com.splendo.kaluga.example.shared.di.initKoin
import kotlinx.browser.document

/**
 * Web (Kotlin/Wasm) entry point. Reuses `:shared`'s [initKoin] — whose `sharedFeaturesModule` resolves
 * to the feature modules whose Kaluga libraries support `wasmJs` — and renders the same [AppRootScreen]
 * Compose UI as the other platform hosts, pulling the feature contributions from Koin.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport(document.body!!) {
        AppRootScreen()
    }
}
