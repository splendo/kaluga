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

package com.splendo.kaluga.architecture.compose.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlinx.coroutines.flow.map
import kotlin.coroutines.EmptyCoroutineContext

interface ComposableLifecycleSubscribable : LifecycleSubscribable {
    val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit
}

@Composable
internal fun <ViewModel : BaseLifecycleViewModel> ViewModel.composableLifecycleSubscribable(): State<List<ComposableLifecycleSubscribable>> = remember {
    activeLifecycleSubscribables.map { list ->
        list.mapNotNull { it as? ComposableLifecycleSubscribable }
    }
}.collectAsState(emptyList(), EmptyCoroutineContext)
