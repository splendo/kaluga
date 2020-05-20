package com.splendo.kaluga.example.shared.viewmodel

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.observable.toSubject
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

sealed class ExampleTabNavigation : NavigationAction<Nothing>(null) {

    object FeatureList : ExampleTabNavigation()
    object Info : ExampleTabNavigation()

}

class ExampleViewModel(navigator: Navigator<ExampleTabNavigation>) : NavigatingViewModel<ExampleTabNavigation>(navigator) {

    sealed class Tab(val title: String) {
        object FeatureList : Tab("Features")
        object Info : Tab("About")
    }

    val tabs = observableOf(listOf(Tab.FeatureList, Tab.Info))

    private val _tab  = BaseFlowable<Tab>().apply { runBlocking{set(Tab.FeatureList)} }
    val tab = _tab.toSubject(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)
        scope.launch(MainQueueDispatcher) { _tab.flow().distinctUntilChanged().collect { currentTab ->
            navigator.navigate(when (currentTab) {
                is Tab.FeatureList -> ExampleTabNavigation.FeatureList
                is Tab.Info -> ExampleTabNavigation.Info
            })
        } }
    }
}