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

package com.splendo.kaluga.example.shared.viewmodel.ui

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.resources.localized

sealed class SwiftUIOrUIKitNavigationAction : SingleValueNavigationAction<Unit>(Unit, NavigationBundleSpecType.UnitType) {
    data object SwiftUI : SwiftUIOrUIKitNavigationAction()
    data object UIKit : SwiftUIOrUIKitNavigationAction()
}

sealed class UIType(val title: String) {
    data object SwiftUI : UIType("ios_swift_ui".localized())
    data object UIKit : UIType("ios_ui_kit".localized())
}

class SwiftUIOrUIKitSelectionViewModel(navigator: Navigator<SwiftUIOrUIKitNavigationAction>) : NavigatingViewModel<SwiftUIOrUIKitNavigationAction>(navigator) {

    val uiTypes = observableOf(
        listOf(
            UIType.SwiftUI,
            UIType.UIKit,
        ),
    )

    fun onUITypePressed(uiType: UIType) {
        navigator.navigate(
            when (uiType) {
                is UIType.SwiftUI -> SwiftUIOrUIKitNavigationAction.SwiftUI
                is UIType.UIKit -> SwiftUIOrUIKitNavigationAction.UIKit
            },
        )
    }
}
