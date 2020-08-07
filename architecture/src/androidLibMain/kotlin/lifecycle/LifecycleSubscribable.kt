/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.lifecycle

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelLifecycleObserver
import com.splendo.kaluga.architecture.viewmodel.ViewModel

/**
 * Implementing this interface as a public property of a [ViewModel] allows for automatic binding to a [KalugaViewModelLifecycleObserver].
 */
interface LifecycleSubscribable {
    /**
     * Subscribes this subscribable to a [Activity] and its [FragmentManager].
     * Called on [Lifecycle.Event.ON_CREATE]
     * @param activity The [Activity] managing the lifecycle.
     * @param lifecycleOwner The [LifecycleOwner] managing the lifecycle.
     * @param fragmentManager The [FragmentManager] for this lifecycle.
     */
    fun subscribe(activity: Activity?, lifecycleOwner: LifecycleOwner, fragmentManager: FragmentManager)

    /**
     * Unsubscribes this subscribable from its current subscribable.
     * Called on [Lifecycle.Event.ON_DESTROY]
     */
    fun unsubscribe()
}
