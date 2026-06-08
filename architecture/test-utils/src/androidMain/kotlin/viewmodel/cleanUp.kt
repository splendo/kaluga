/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.splendo.kaluga.architecture.viewmodel.LifecycleViewModel

/**
 * Ensure a [LifecycleViewModel] calls its [LifecycleViewModel.onCleared] method
 */
actual fun LifecycleViewModel.cleanUp() {
    // Android ViewModels dont support directly calling clear
    // However registering a ViewModel to a ViewModel Store and then cleaning the store does the same trick
    val viewModelStore = ViewModelStore()
    val viewModelProvider = ViewModelProvider(
        viewModelStore,
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = this@cleanUp as T
        },
    )

    viewModelProvider[this::class.java]

    viewModelStore.clear()
}
