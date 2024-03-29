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

package com.splendo.kaluga.example.architecture.compose

import com.splendo.kaluga.architecture.compose.navigation.BottomSheetRoute
import com.splendo.kaluga.architecture.compose.navigation.Route
import com.splendo.kaluga.architecture.compose.navigation.back
import com.splendo.kaluga.architecture.compose.navigation.bottomSheetContent
import com.splendo.kaluga.architecture.compose.navigation.bottomSheetSheetContent
import com.splendo.kaluga.architecture.compose.navigation.next
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetNavigation
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetSubPageNavigation

/** Maps a navigation action to a route string. */
internal fun architectureNavigationRouteMapper(action: ArchitectureNavigationAction<*>): BottomSheetRoute {
    return when (action) {
        is ArchitectureNavigationAction.Details -> action.next.bottomSheetContent
        is ArchitectureNavigationAction.BottomSheet -> action.next.bottomSheetSheetContent
    }
}

/** Maps a navigation action to a route string. */
internal fun architectureDetailsNavigationRouteMapper(action: ArchitectureDetailsNavigationAction<*>): Route {
    return when (action) {
        is ArchitectureDetailsNavigationAction.FinishWithDetails -> action.back
        is ArchitectureDetailsNavigationAction.Close -> action.back
    }
}

/** Maps a navigation action to a route string. */
internal fun bottomSheetNavigationRouteMapper(action: BottomSheetNavigation): BottomSheetRoute {
    return when (action) {
        is BottomSheetNavigation.Close -> Route.Close.bottomSheetSheetContent
        is BottomSheetNavigation.SubPage -> action.next.bottomSheetSheetContent
    }
}

/** Maps a navigation action to a route string. */
internal fun bottomSheetSubPageNavigationRouteMapper(action: BottomSheetSubPageNavigation): BottomSheetRoute {
    return when (action) {
        is BottomSheetSubPageNavigation.Close -> Route.Close.bottomSheetSheetContent
        is BottomSheetSubPageNavigation.Back -> Route.Back().bottomSheetSheetContent
    }
}
