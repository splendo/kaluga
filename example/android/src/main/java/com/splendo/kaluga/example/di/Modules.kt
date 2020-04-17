package com.splendo.kaluga.example.di

import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.example.alerts.AlertsActivity
import com.splendo.kaluga.example.loading.LoadingActivity
import com.splendo.kaluga.example.location.LocationActivity
import com.splendo.kaluga.example.permissions.PermissionsDemoListActivity
import com.splendo.kaluga.example.shared.viewmodel.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.FeatureListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        FeatureListViewModel(Navigator { action ->
            when (action) {
                is FeatureListNavigationAction.Location -> NavigationSpec.Activity(LocationActivity::class.java)
                is FeatureListNavigationAction.Permissions -> NavigationSpec.Activity(PermissionsDemoListActivity::class.java)
                is FeatureListNavigationAction.Alerts -> NavigationSpec.Activity(AlertsActivity::class.java)
                is FeatureListNavigationAction.LoadingIndicator -> NavigationSpec.Activity(LoadingActivity::class.java)
            }
        })
    }
}