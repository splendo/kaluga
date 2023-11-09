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

package com.splendo.kaluga.example.shared.viewmodel

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.observable.toInitializedSubject
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class ExampleTabNavigation : NavigationAction<Nothing>(null) {
    data object FeatureList : ExampleTabNavigation()
    data object Info : ExampleTabNavigation()
}

class ExampleViewModel(navigator: Navigator<ExampleTabNavigation>) : NavigatingViewModel<ExampleTabNavigation>(navigator) {

    sealed class Tab(val title: String) {
        data object FeatureList : Tab("Features")
        data object Info : Tab("About")
    }

    val tabs = observableOf(listOf(Tab.FeatureList, Tab.Info))

    private val _tab = MutableStateFlow<Tab>(Tab.FeatureList)
    val tab = _tab.toInitializedSubject(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)
        scope.launch(Dispatchers.Main.immediate) {
            _tab.collect { currentTab ->
                navigator.navigate(
                    when (currentTab) {
                        is Tab.FeatureList -> ExampleTabNavigation.FeatureList
                        is Tab.Info -> ExampleTabNavigation.Info
                    },
                )
            }
        }
    }
}
