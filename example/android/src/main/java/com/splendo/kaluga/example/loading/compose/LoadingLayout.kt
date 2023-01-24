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

package com.splendo.kaluga.example.loading.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.google.android.material.composethemeadapter.MdcTheme
import com.splendo.kaluga.architecture.compose.viewModel.LocalAppCompatActivity
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.architecture.compose.viewModel.storeAndRemember
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.shared.viewmodel.hud.HudViewModel
import com.splendo.kaluga.hud.hudBuilder
import com.splendo.kaluga.resources.compose.Composable

class ComposeLoadingActivity : AppCompatActivity() {
    @SuppressLint("MissingSuperCall") // Lint bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalAppCompatActivity provides this
            ) {
                LoadingLayout()
            }
        }
    }
}

@Composable
fun LoadingLayout() {
    MdcTheme {
        val activity = LocalAppCompatActivity.current!!
        val viewModel = storeAndRemember {
            HudViewModel(activity.hudBuilder())
        }

        ViewModelComposable(viewModel) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Constants.Padding.default),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.Padding.default)
                    .verticalScroll(rememberScrollState())
            ) {
                showSystemButton.Composable(modifier = Modifier.fillMaxWidth())
                showCustomButton.Composable(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}