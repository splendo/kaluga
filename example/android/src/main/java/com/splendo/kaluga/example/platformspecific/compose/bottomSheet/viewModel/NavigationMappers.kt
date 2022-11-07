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

package com.splendo.kaluga.example.platformspecific.compose.bottomSheet.viewModel

import com.splendo.kaluga.architecture.compose.navigation.BottomSheetRoute
import com.splendo.kaluga.architecture.compose.navigation.Route
import com.splendo.kaluga.architecture.compose.navigation.bottomSheetContent
import com.splendo.kaluga.architecture.compose.navigation.bottomSheetSheetContent
import com.splendo.kaluga.architecture.compose.navigation.next
import com.splendo.kaluga.example.shared.viewmodel.bottomsheet.BottomSheetNavigation
import com.splendo.kaluga.example.shared.viewmodel.bottomsheet.BottomSheetParentNavigation
import com.splendo.kaluga.example.shared.viewmodel.bottomsheet.BottomSheetParentSubPageNavigation
import com.splendo.kaluga.example.shared.viewmodel.bottomsheet.BottomSheetSubPageNavigation

/** Maps a navigation action to a route string. */
internal fun bottomSheetParentNavigationRouteMapper(action: BottomSheetParentNavigation): BottomSheetRoute {
    return when (action) {
        is BottomSheetParentNavigation.SubPage -> action.next.bottomSheetContent
        is BottomSheetParentNavigation.ShowSheet -> action.next.bottomSheetSheetContent
    }
}

/** Maps a navigation action to a route string. */
internal fun bottomSheetParentSubPageNavigationRouteMapper(action: BottomSheetParentSubPageNavigation): Route {
    return when (action) {
        is BottomSheetParentSubPageNavigation.Back -> Route.Back
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
        is BottomSheetSubPageNavigation.Back -> Route.Back.bottomSheetSheetContent
    }
}
