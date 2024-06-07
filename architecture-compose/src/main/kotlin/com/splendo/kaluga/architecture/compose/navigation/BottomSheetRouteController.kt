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
 * A controller that handles the [BottomSheetRoute] for a [BottomSheetScaffold]
 * @param contentRouteController The [RouteController] managing the content.
 * @param sheetContentRouteController The [RouteController] managing the sheet content.
 */
class BottomSheetRouteController(
    internal val contentRouteController: RouteController,
    internal val sheetContentRouteController: BottomSheetSheetContentRouteController,
) {

    /**
     * Navigates using a given [BottomSheetRoute]
     */
    fun navigate(route: BottomSheetRoute) = when (route) {
        is BottomSheetRoute.SheetContent -> sheetContentRouteController.navigate(route.route)
        is BottomSheetRoute.Content -> contentRouteController.navigate(route.route)
    }
}
