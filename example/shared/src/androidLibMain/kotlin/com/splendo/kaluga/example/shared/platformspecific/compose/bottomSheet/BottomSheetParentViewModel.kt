/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.platformspecific.compose.bottomSheet

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.resources.localized

sealed class BottomSheetParentNavigation : NavigationAction<Nothing>(null) {
    object ShowSheet : BottomSheetParentNavigation()
    object SubPage : BottomSheetParentNavigation()
}

class BottomSheetParentViewModel(navigator: Navigator<BottomSheetParentNavigation>) : NavigatingViewModel<BottomSheetParentNavigation>(navigator) {

    val sheetText = "bottom_sheet_show_sheet".localized()
    val subPageText = "bottom_sheet_show_sub_page".localized()

    fun onShowSheetPressed() {
        navigator.navigate(BottomSheetParentNavigation.ShowSheet)
    }

    fun onSubPagePressed() {
        navigator.navigate(BottomSheetParentNavigation.SubPage)
    }
}
