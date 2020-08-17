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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

/**
 * [LifecycleObserver] used to manage the lifecycle of a [BaseViewModel]
 * @param viewModel The [BaseViewModel] whose lifecycle is managed
 * @param activity The [Activity] managing the lifecycle
 * @param fragmentManager The [FragmentManager] for this lifecycle
 */
class KalugaViewModelLifecycleObserver<VM : BaseViewModel> internal constructor(private val viewModel: VM, private val activity: Activity?, private val lifecycleOwner: LifecycleOwner, private val fragmentManager: FragmentManager) : LifecycleObserver {

    private val publicVmProperties: List<KProperty1<VM, Any?>> by lazy {
        viewModel::class.memberProperties
            .mapNotNull { it as? KProperty1<VM, Any?> }.filter { it.getter.visibility == KVisibility.PUBLIC && it.getter.returnType.isSubtypeOf(LifecycleSubscribable::class.starProjectedType) }
    }
    private val lifecycleSubscribables: List<LifecycleSubscribable> get() = publicVmProperties.map { it.getter.call(viewModel) as LifecycleSubscribable }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        lifecycleSubscribables.forEach { it.subscribe(activity, lifecycleOwner, fragmentManager) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        lifecycleSubscribables.forEach { it.unsubscribe() }
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
