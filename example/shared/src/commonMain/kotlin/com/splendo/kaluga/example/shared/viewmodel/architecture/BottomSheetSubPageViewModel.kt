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
import com.splendo.kaluga.resources.localized

sealed class BottomSheetSubPageNavigation : NavigationAction<Nothing>(null) {
    data object Back : BottomSheetSubPageNavigation()
    data object Close : BottomSheetSubPageNavigation()
}

class BottomSheetSubPageViewModel(navigator: Navigator<BottomSheetSubPageNavigation>) : NavigatingViewModel<BottomSheetSubPageNavigation>(navigator) {

    val text = "bottom_sheet_sheet_sub_page_title".localized()

    fun onBackPressed() {
        navigator.navigate(BottomSheetSubPageNavigation.Back)
    }

    fun onClosePressed() {
        navigator.navigate(BottomSheetSubPageNavigation.Close)
    }
}
