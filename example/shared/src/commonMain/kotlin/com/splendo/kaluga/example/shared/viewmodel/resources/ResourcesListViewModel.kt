package com.splendo.kaluga.example.shared.viewmodel.resources

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.resources.localized

sealed class ResourcesListNavigationAction : NavigationAction<Nothing>(null) {

    data object Color : ResourcesListNavigationAction()
    data object Button : ResourcesListNavigationAction()
    data object Image : ResourcesListNavigationAction()
    data object Label : ResourcesListNavigationAction()
}

enum class Resource(private val titleKey: String) {
    COLOR("feature_resources_color"),
    BUTTON("feature_resources_button"),
    IMAGE("feature_resources_image"),
    LABEL("feature_resources_label"),
    ;

    val title: String get() = titleKey.localized()
}

class ResourcesListViewModel(navigator: Navigator<ResourcesListNavigationAction>) : NavigatingViewModel<ResourcesListNavigationAction>(navigator) {

    val resources = observableOf(Resource.values().toList())

    fun onResourceSelected(resource: Resource) {
        navigator.navigate(
            when (resource) {
                Resource.COLOR -> ResourcesListNavigationAction.Color
                Resource.BUTTON -> ResourcesListNavigationAction.Button
                Resource.IMAGE -> ResourcesListNavigationAction.Image
                Resource.LABEL -> ResourcesListNavigationAction.Label
            },
        )
    }
}
