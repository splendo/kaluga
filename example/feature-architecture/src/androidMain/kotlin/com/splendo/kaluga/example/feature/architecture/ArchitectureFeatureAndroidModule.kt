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

package com.splendo.kaluga.example.feature.architecture

import com.splendo.kaluga.architecture.navigation.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val architectureFeatureAndroidModule: Module = module {
    viewModel { (navigator: Navigator<ComposeOrXMLNavigationAction>) ->
        ComposeOrXMLSelectionViewModel(navigator)
    }
    viewModel { (navigator: Navigator<ArchitectureNavigationAction<*>>) ->
        ArchitectureViewModel(navigator)
    }
    viewModel { (initialDetail: InputDetails, navigator: Navigator<ArchitectureDetailsNavigationAction<*>>) ->
        ArchitectureDetailsViewModel(initialDetail, navigator)
    }
    viewModel { (navigator: Navigator<BottomSheetNavigation>) ->
        BottomSheetViewModel(navigator)
    }
    viewModel { (navigator: Navigator<BottomSheetSubPageNavigation>) ->
        BottomSheetSubPageViewModel(navigator)
    }
}
