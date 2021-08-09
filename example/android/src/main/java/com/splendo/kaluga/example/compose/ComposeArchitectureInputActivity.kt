/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.splendo.kaluga.architecture.compose.mutableState
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.example.architecture.ArchitectureDetailsActivity
import com.splendo.kaluga.example.architecture.ArchitectureInputActivity
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel

val LocalAppCompatActivity =
    staticCompositionLocalOf<AppCompatActivity?> { null }

abstract class KalugaViewModelComposeActivity<VM : BaseViewModel> : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(LocalAppCompatActivity provides this) {
                Layout(viewModel())
            }
        }
    }

    @Composable
    protected abstract fun Layout(viewModel: VM)

    @Composable
    protected abstract fun viewModel(): VM
}

@Composable
fun LayoutP(viewModel: ArchitectureInputViewModel) {
    val nameHeader by viewModel.nameHeader.state()
    val (nameInput, nameInputDelegate) = viewModel.nameInput.mutableState()

    val numberHeader by viewModel.numberHeader.state()
    val (numberInput, numberInputDelegate) = viewModel.numberInput.mutableState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BasicText(nameHeader)
        BasicTextField(
            value = nameInput,
            onValueChange = nameInputDelegate,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true
        )

        BasicText(numberHeader)
        BasicTextField(
            value = numberInput,
            onValueChange = numberInputDelegate,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true
        )
    }
}

class ComposeArchitectureInputActivity : KalugaViewModelComposeActivity<ArchitectureInputViewModel>() {

    @Composable
    override fun viewModel(): ArchitectureInputViewModel = ArchitectureInputViewModel(
        ActivityNavigator {
            NavigationSpec.Activity(ArchitectureDetailsActivity::class.java, requestCode = ArchitectureInputActivity.requestCode)
        }
    )

    @Composable
    override fun Layout(viewModel: ArchitectureInputViewModel) = LayoutP(viewModel)
}

@Preview
@Composable
fun Preview() {
    LayoutP(
        ArchitectureInputViewModel(
            ActivityNavigator {
                NavigationSpec.Activity(ArchitectureDetailsActivity::class.java, requestCode = ArchitectureInputActivity.requestCode)
            }
        )
    )
}
