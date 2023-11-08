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

package com.splendo.kaluga.example.shared.viewmodel.architecture

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton

sealed class BottomSheetNavigation : NavigationAction<Nothing>(null) {
    data object Close : BottomSheetNavigation()
    data object SubPage : BottomSheetNavigation()
}

class BottomSheetViewModel(navigator: Navigator<BottomSheetNavigation>) : NavigatingViewModel<BottomSheetNavigation>(navigator) {

    val text = "bottom_sheet_sheet_content".localized()
    val button = KalugaButton.Plain("bottom_sheet_show_sub_page".localized(), ButtonStyles.default) {
        navigator.navigate(BottomSheetNavigation.SubPage)
    }

    fun onClosePressed() {
        navigator.navigate(BottomSheetNavigation.Close)
    }
}
