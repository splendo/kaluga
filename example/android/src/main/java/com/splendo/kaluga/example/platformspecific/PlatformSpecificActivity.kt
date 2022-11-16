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

package com.splendo.kaluga.example.platformspecific

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.KalugaViewModelComposeActivity
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.example.bottomSheet.BottomSheetActivity
import com.splendo.kaluga.example.contacts.ContactsActivity
import com.splendo.kaluga.example.contacts.ui.Padding
import com.splendo.kaluga.example.shared.viewmodel.featureList.PlatformFeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.PlatformSpecificFeaturesViewModel

class PlatformSpecificActivity : KalugaViewModelComposeActivity<PlatformSpecificFeaturesViewModel>() {
    override val viewModel = PlatformSpecificFeaturesViewModel(ActivityNavigator(::navigationMapper))

    @Composable
    override fun Layout(viewModel: PlatformSpecificFeaturesViewModel) {
        PlatformSpecificFeaturesLayout(viewModel)
    }
}

private fun navigationMapper(action: PlatformFeatureListNavigationAction): NavigationSpec =
    when (action) {
        is PlatformFeatureListNavigationAction.ComposeNavigation -> NavigationSpec.Activity(
            ContactsActivity::class.java)
        is PlatformFeatureListNavigationAction.ComposeBottomSheet -> NavigationSpec.Activity(
            BottomSheetActivity::class.java)
    }

@Composable
private fun PlatformSpecificFeaturesLayout(viewModel: PlatformSpecificFeaturesViewModel) {
    MdcTheme {
        ViewModelComposable(viewModel) {
            val features by feature.state()
            LazyColumn {
                items(features) { item ->
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Padding.default),
                        onClick = { onFeaturePressed(item) },
                    ) {
                        Text(
                            text = item.title.uppercase(),
                            style = MaterialTheme.typography.button,
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun PlatformSpecificFeaturesLayoutPreview() {
    PlatformSpecificFeaturesLayout(
        PlatformSpecificFeaturesViewModel(ActivityNavigator(::navigationMapper))
    )
}
