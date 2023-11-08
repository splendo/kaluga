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

import com.splendo.kaluga.architecture.navigation.DefaultNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.ViewControllerNavigator
import platform.UIKit.UIViewController

fun ArchitectureNavigator(onDetails: (InputDetails) -> Unit, onBottomSheet: () -> Unit) = DefaultNavigator<ArchitectureNavigationAction<*>> { action ->
    when (action) {
        is ArchitectureNavigationAction.Details -> onDetails(action.value)
        is ArchitectureNavigationAction.BottomSheet -> onBottomSheet()
    }
}

fun ArchitectureViewControllerNavigator(parent: UIViewController, onDetails: (InputDetails) -> NavigationSpec, onBottomSheet: () -> NavigationSpec) =
    ViewControllerNavigator<ArchitectureNavigationAction<*>>(parent) { action ->
        when (action) {
            is ArchitectureNavigationAction.Details -> onDetails(action.value)
            is ArchitectureNavigationAction.BottomSheet -> onBottomSheet()
        }
    }
