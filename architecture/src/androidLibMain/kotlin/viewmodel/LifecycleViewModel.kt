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

package com.splendo.kaluga.architecture.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope

actual open class LifecycleViewModel internal actual constructor() : androidx.lifecycle.ViewModel() {

    actual val coroutineScope = viewModelScope

    actual override fun onCleared() {
        super.onCleared()
    }
}

/**
 * Binds an [AppCompatActivity] to the [LifecycleViewModel] to manage the viewmodel lifecycle.
 * @param activity The [AppCompatActivity] to bind to.
 */
fun <VM : BaseLifecycleViewModel> VM.bind(activity: AppCompatActivity) {
    activity.lifecycle.addObserver(KalugaViewModelLifecycleObserver(this, activity, activity.supportFragmentManager))
}

/**
 * Binds a [Fragment] to the [LifecycleViewModel] to manage the viewmodel lifecycle
 * @param fragment The [Fragment] to bind to.
 */
fun <VM : BaseLifecycleViewModel> VM.bind(fragment: Fragment) =
    fragment.lifecycle.addObserver(
        KalugaViewModelLifecycleObserver(
            viewModel = this,
            activity = fragment.activity,
            fragmentManager = fragment.parentFragmentManager,
            childFragmentManager = fragment.childFragmentManager
        )
    )
