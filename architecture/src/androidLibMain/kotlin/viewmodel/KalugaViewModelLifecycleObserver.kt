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

package com.splendo.kaluga.architecture.viewmodel

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

/**
 * Implement this interface for public properties of a [ViewModel] that should automatically bind to a [KalugaViewModelLifecycleObserver].
 */
interface LifecycleSubscribable {
    /**
     * Subscribes this subscribable to a [Activity] and its [FragmentManager].
     * @param activity The [Activity] managing the lifecycle.
     * @param fragmentManager The [FragmentManager] for this lifecycle.
     */
    fun subscribe(activity: Activity, fragmentManager: FragmentManager)
    fun unsubscribe()
}

/**
 * [LifecycleObserver] used to manage the lifecycle of a [BaseViewModel]
 * @param viewModel The [BaseViewModel] whose lifecycle is managed
 * @param activity The [Activity] managing the lifecycle
 * @param fragmentManager The [FragmentManager] for this lifecycle
 */
class KalugaViewModelLifecycleObserver<VM : BaseViewModel> internal constructor(private val viewModel: VM, private val activity: Activity, private val fragmentManager: FragmentManager) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        viewModel::class.memberProperties.forEach { property ->
            (property as? KProperty1<VM, Any?>)?.let {
                if (it.getter.visibility == KVisibility.PUBLIC)
                    (it.getter.call(viewModel) as? LifecycleSubscribable)?.subscribe(activity, fragmentManager)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        viewModel::class.memberProperties.forEach { property ->
            (property as? KProperty1<VM, Any?>)?.let {
                if (it.getter.visibility == KVisibility.PUBLIC)
                    (it.getter.call(viewModel) as? LifecycleSubscribable)?.unsubscribe()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        viewModel.didResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        viewModel.didPause()
    }
}
