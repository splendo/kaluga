/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.media

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.resources.localized

sealed class MediaListNavigationAction : NavigationAction<Nothing>(null) {

    data object Media : MediaListNavigationAction()
    data object Sound : MediaListNavigationAction()
}

enum class Media(private val titleKey: String) {
    MEDIA("feature_media"),
    SOUND("feature_media_sound"),
    ;

    val title: String get() = titleKey.localized()
}

class MediaListViewModel(navigator: Navigator<MediaListNavigationAction>) : NavigatingViewModel<MediaListNavigationAction>(navigator) {

    val media = observableOf(Media.values().toList())

    fun onMediaSelected(media: Media) {
        navigator.navigate(
            when (media) {
                Media.MEDIA -> MediaListNavigationAction.Media
                Media.SOUND -> MediaListNavigationAction.Sound
            },
        )
    }
}
