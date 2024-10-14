/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.material3.BottomSheetScaffold

/**
 * The specification for navigating from a [BottomSheetScaffold]
 */
sealed class BottomSheetComposableNavSpec

/**
 * A [BottomSheetComposableNavSpec] for navigating using a [Route]
 */
sealed class BottomSheetRoute : BottomSheetComposableNavSpec() {

    /**
     * Navigates within the sheet content of the [BottomSheetScaffold]
     * @param route The [Route] to navigate
     */
    data class SheetContent(val route: Route) : BottomSheetRoute()

    /**
     * Navigates within the content of the [BottomSheetScaffold]
     * @param route The [Route] to navigate
     */
    data class Content(val route: Route) : BottomSheetRoute()
}

/**
 * A [BottomSheetComposableNavSpec] that executes a [ComposableNavSpec.LaunchedNavigation] when navigating.
 * @property launchedNavigation the [ComposableNavSpec.LaunchedNavigation] to launch when navigating.
 */
data class BottomSheetLaunchedNavigation(val launchedNavigation: ComposableNavSpec.LaunchedNavigation) : BottomSheetComposableNavSpec()

/**
 * Converts a [Route] to a [BottomSheetRoute.SheetContent] route
 */
val Route.bottomSheetSheetContent get() = BottomSheetRoute.SheetContent(this)

/**
 * Converts a [Route] to a [BottomSheetRoute.Content] route
 */
val Route.bottomSheetContent get() = BottomSheetRoute.Content(this)

/**
 * Converts a [ComposableNavSpec.LaunchedNavigation] to a [BottomSheetLaunchedNavigation]
 */
val ComposableNavSpec.LaunchedNavigation.bottomSheetComposable get() = BottomSheetLaunchedNavigation(this)
