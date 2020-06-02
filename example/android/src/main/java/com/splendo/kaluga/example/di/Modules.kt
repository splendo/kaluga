package com.splendo.kaluga.example.di

import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.example.*
import com.splendo.kaluga.example.alerts.AlertsActivity
import com.splendo.kaluga.example.architecture.ArchitectureDetailsActivity
import com.splendo.kaluga.example.architecture.ArchitectureInputActivity
import com.splendo.kaluga.example.loading.LoadingActivity
import com.splendo.kaluga.example.location.LocationActivity
import com.splendo.kaluga.example.permissions.PermissionsDemoListActivity
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import com.splendo.kaluga.example.shared.viewmodel.collectionview.CollectionViewViewModel
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.shared.viewmodel.info.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.net.URL

val viewModelModule = module {
    viewModel {
        ExampleViewModel(
            Navigator { action ->
                when(action) {
                    is ExampleTabNavigation.FeatureList -> NavigationSpec.Fragment(R.id.example_fragment, createFragment = {FeaturesListFragment()})
                    is ExampleTabNavigation.Info -> NavigationSpec.Fragment(R.id.example_fragment, createFragment = {InfoFragment()})
                }
            }
        )
    }
    viewModel {
        FeatureListViewModel(
            Navigator { action ->
                when (action) {
                    is FeatureListNavigationAction.Location -> NavigationSpec.Activity(LocationActivity::class.java)
                    is FeatureListNavigationAction.Permissions -> NavigationSpec.Activity(PermissionsDemoListActivity::class.java)
                    is FeatureListNavigationAction.Alerts -> NavigationSpec.Activity(AlertsActivity::class.java)
                    is FeatureListNavigationAction.LoadingIndicator -> NavigationSpec.Activity(LoadingActivity::class.java)
                    is FeatureListNavigationAction.Architecture -> NavigationSpec.Activity(ArchitectureInputActivity::class.java)
                    is FeatureListNavigationAction.CollectionView -> NavigationSpec.Activity(CollectionViewActivity::class.java)
                }
            })
    }
    viewModel {
        InfoViewModel(
            Navigator { action ->
                when (action) {
                    is InfoNavigation.Dialog -> {
                        val title = action.bundle?.get(DialogSpecRow.TitleRow) ?: ""
                        val message = action.bundle?.get(DialogSpecRow.MessageRow) ?: ""
                        NavigationSpec.Dialog(createDialog = {
                            InfoDialog(title, message)
                        })
                    }
                    is InfoNavigation.Link -> NavigationSpec.Browser(URL(action.bundle!!.get(LinkSpecRow.LinkRow)))
                    is InfoNavigation.Mail -> NavigationSpec.Email(NavigationSpec.Email.EmailSettings(to = action.bundle?.get(MailSpecRow.ToRow) ?: emptyList(), subject = action.bundle?.get(MailSpecRow.SubjectRow)))
                }
            })
    }
    viewModel {
        ArchitectureInputViewModel(
            Navigator {
                NavigationSpec.Activity(ArchitectureDetailsActivity::class.java, requestCode = ArchitectureInputActivity.requestCode)
            }
        )
    }
    viewModel { (name: String, number: Int) ->
        ArchitectureDetailsViewModel(name, number, Navigator {
            NavigationSpec.Close(ArchitectureDetailsActivity.resultCode)
        })
    }

    viewModel { CollectionViewViewModel.create() }
}