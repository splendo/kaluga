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

@file:JvmName("AndroidMobileSharedDependencyInjection")

package com.splendo.kaluga.example.mobileshared.di

import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.datetimepicker.DateTimePickerPresenter
import com.splendo.kaluga.example.mobileshared.viewmodel.alert.AlertViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.architecture.ArchitectureDetailsNavigationAction
import com.splendo.kaluga.example.mobileshared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.architecture.ArchitectureNavigationAction
import com.splendo.kaluga.example.mobileshared.viewmodel.architecture.ArchitectureViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.architecture.BottomSheetNavigation
import com.splendo.kaluga.example.mobileshared.viewmodel.architecture.BottomSheetSubPageNavigation
import com.splendo.kaluga.example.mobileshared.viewmodel.architecture.BottomSheetSubPageViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.architecture.BottomSheetViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.architecture.InputDetails
import com.splendo.kaluga.example.mobileshared.viewmodel.beacons.BeaconsListViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.compose.ComposeOrXMLNavigationAction
import com.splendo.kaluga.example.mobileshared.viewmodel.compose.ComposeOrXMLSelectionViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.datetimepicker.DateTimePickerViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.hud.HudViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.media.MediaListNavigationAction
import com.splendo.kaluga.example.mobileshared.viewmodel.media.MediaListViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.media.MediaNavigationAction
import com.splendo.kaluga.example.mobileshared.viewmodel.media.MediaSoundViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.media.MediaViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.permissions.NotificationPermissionViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.resources.ButtonViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.resources.ColorViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.resources.ImagesViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.resources.LabelViewModel
import com.splendo.kaluga.example.mobileshared.viewmodel.resources.ResourcesListNavigationAction
import com.splendo.kaluga.example.mobileshared.viewmodel.resources.ResourcesListViewModel
import com.splendo.kaluga.example.shared.di.initKoin as initSharedKoin
import com.splendo.kaluga.hud.HUD
import com.splendo.kaluga.media.DefaultMediaManager
import com.splendo.kaluga.media.MediaSurfaceProvider
import com.splendo.kaluga.resources.StyledStringBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal val mobileSharedAndroidModule = module {
    viewModel { (navigator: Navigator<ComposeOrXMLNavigationAction>) ->
        ComposeOrXMLSelectionViewModel(navigator)
    }

    viewModel { NotificationPermissionViewModel() }

    viewModel { (navigator: Navigator<ArchitectureNavigationAction<*>>) ->
        ArchitectureViewModel(navigator)
    }

    viewModel { (initialDetail: InputDetails, navigator: Navigator<ArchitectureDetailsNavigationAction<*>>) ->
        ArchitectureDetailsViewModel(
            initialDetail,
            navigator,
        )
    }

    viewModel { (navigator: Navigator<BottomSheetNavigation>) ->
        BottomSheetViewModel(navigator)
    }

    viewModel { (navigator: Navigator<BottomSheetSubPageNavigation>) ->
        BottomSheetSubPageViewModel(navigator)
    }

    viewModel {
        AlertViewModel(AlertPresenter.Builder())
    }

    viewModel {
        DateTimePickerViewModel(DateTimePickerPresenter.Builder())
    }

    viewModel {
        HudViewModel(HUD.Builder())
    }

    viewModel { (mediaSurfaceProvider: MediaSurfaceProvider, navigator: Navigator<MediaNavigationAction>) ->
        MediaViewModel(mediaSurfaceProvider, DefaultMediaManager.Builder(), AlertPresenter.Builder(), navigator)
    }

    viewModel { (navigator: Navigator<MediaListNavigationAction>) ->
        MediaListViewModel(navigator)
    }

    viewModel {
        MediaSoundViewModel()
    }

    viewModel {
        BeaconsListViewModel()
    }

    viewModel { (navigator: Navigator<ResourcesListNavigationAction>) ->
        ResourcesListViewModel(navigator)
    }

    viewModel {
        ColorViewModel(AlertPresenter.Builder())
    }

    viewModel {
        ImagesViewModel()
    }

    viewModel {
        LabelViewModel(StyledStringBuilder.Provider())
    }

    viewModel {
        ButtonViewModel(StyledStringBuilder.Provider(), AlertPresenter.Builder())
    }
}

fun initKoin(customModules: List<Module> = emptyList()) = initSharedKoin(
    customModules = listOf(mobileSharedAndroidModule) + customModules,
)
