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

package com.splendo.kaluga.architecture.compose.viewModel

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel

/** A [CompositionLocal] containing the current activity. */
val LocalAppCompatActivity = staticCompositionLocalOf<AppCompatActivity?> { null }

/**
 * An implementation of [AppCompatActivity] which creates a [ViewModel] and renders it using [ViewModelComposable].
 * Also provides a reference to this [AppCompatActivity] using [LocalAppCompatActivity].
 */
abstract class KalugaViewModelComposeActivity<ViewModel : BaseLifecycleViewModel> : AppCompatActivity() {

    @SuppressLint("MissingSuperCall") // Lint bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalAppCompatActivity provides this
            ) {
                RootView {
                    ViewModelComposable(viewModel = createViewModel()) {
                        Layout()
                    }
                }
            }
        }
    }

    /**
     * Wrapper view to contain the ViewModel.
     */
    @Composable
    protected open fun RootView(content: @Composable () -> Unit) {
        content()
    }

    /**
     * Creates the [ViewModel]. Vms should be stores so they can be retained, e.g using [androidx.lifecycle.viewmodel.compose.viewModel]
     */
    @Composable
    protected abstract fun createViewModel(): ViewModel

    /**
     * Creates the layout associated with a [ViewModel]
     */
    @Composable
    protected abstract fun ViewModel.Layout()
}
