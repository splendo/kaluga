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

package com.splendo.kaluga.example.arch

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.mp.KoinPlatformTools

/**
 * Generic payload posted onto the [DeepLinkBus]. [targetId] identifies the [FeatureContribution]
 * that should be navigated to; [payload] is arbitrary string data the feature reads from its
 * destination's saved-state handle (e.g. the originating URL).
 */
data class DeepLink(val targetId: String, val payload: Map<String, String> = emptyMap())

/**
 * Cross-platform inbox for inbound deep links. Each platform host (Android `onNewIntent`, iOS
 * `application:openURL:`, macOS `application:openURLs:`) calls [postUrl] with the raw URL; the bus
 * delegates URL parsing to whichever [FeatureContribution] knows how to recognise it (via
 * [FeatureContribution.parseDeepLink]). `AppRootScreen` observes [state], navigates to the
 * matching contribution, and calls [consume] to clear the inbox.
 */
object DeepLinkBus {
    private val _state = MutableStateFlow<DeepLink?>(null)
    val state: StateFlow<DeepLink?> = _state

    fun post(link: DeepLink) {
        _state.value = link
    }

    fun postUrl(url: String) {
        val contributions = KoinPlatformTools.defaultContext().get().getAll<FeatureContribution>()
        contributions.firstNotNullOfOrNull { it.parseDeepLink(url) }?.let { _state.value = it }
    }

    fun consume() {
        _state.value = null
    }
}
