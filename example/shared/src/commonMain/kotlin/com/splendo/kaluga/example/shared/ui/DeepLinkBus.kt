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

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Singleton channel for inbound deep links. Hosts (Android `ExampleActivity`, iOS `AppDelegate`,
 * macOS app event handlers) call [postUrl] from their platform-specific URL hooks; the CMP
 * navigation root observes [state] via `collectAsState` and routes accordingly.
 *
 * Using a shared bus rather than a constructor parameter on `AppRootScreen` is necessary on Apple
 * platforms because the SwiftUI/UIKit host has no way to recompose the existing
 * `ComposeUIViewController` with a new argument — it has to push state into the running tree.
 */
object DeepLinkBus {

    private val _state = MutableStateFlow<DeepLink?>(null)
    val state: StateFlow<DeepLink?> = _state

    /** Map a raw URL to a [DeepLink] and post it for the CMP root to consume. */
    fun postUrl(url: String) {
        _state.value = parse(url)
    }

    /** Clear the bus once the CMP root has navigated to the destination. */
    fun consume() {
        _state.value = null
    }

    private fun parse(url: String): DeepLink? = when {
        url.startsWith("https://kaluga-links.web.app") -> DeepLink.Links(url)
        else -> null
    }
}
