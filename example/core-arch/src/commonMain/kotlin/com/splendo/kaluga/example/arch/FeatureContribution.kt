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

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

/**
 * One entry in the example app's feature list. Each example feature module ships a
 * `FeatureContribution` (or several) and registers it in its own Koin module; [AppRootScreen]
 * pulls every contribution from Koin via `getAll<FeatureContribution>()` so the host activities
 * don't need to know which features are linked into the framework. A feature is "available on
 * macOS" if and only if its module is part of the build for the macOS target — there is no
 * separate availability flag.
 *
 * Contributions can be either compose-native (the contribution registers its own
 * [NavGraphBuilder] destinations) or native-launched (the host platform pops up its own
 * activity / view controller, keyed by [id]). Mobile-only Kaluga features whose UI hasn't been
 * ported to Compose Multiplatform yet use the native-launch variant.
 */
interface FeatureContribution {
    /** Stable string key. Used as the primary nav route for compose features, or as the
     *  dispatch key the host platform inspects for native-launched features. */
    val id: String

    /** User-facing label shown in the feature list. */
    val label: String

    /** When `false`, [AppRootScreen] notifies the host via its `onNativeLaunch` callback instead
     *  of navigating in-graph. Default is `true`. */
    val isCompose: Boolean get() = true

    /** Add this feature's compose destination(s) to the root nav graph. Only called when
     *  [isCompose] is `true`. The implementation typically calls `builder.composable(id) { … }`
     *  for its primary screen and optionally additional sub-routes. */
    fun register(builder: NavGraphBuilder, navController: NavController) = Unit

    /** Return a [DeepLink] targeting this feature if [url] matches a pattern owned by the
     *  feature, otherwise `null`. [DeepLinkBus] iterates registered contributions and picks the
     *  first non-null match. Default: feature has no deep links. */
    fun parseDeepLink(url: String): DeepLink? = null
}
