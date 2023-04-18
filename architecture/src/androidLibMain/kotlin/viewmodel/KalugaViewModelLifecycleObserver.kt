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

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.subscribe
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch

/**
 * [LifecycleObserver] used to manage the lifecycle of a [BaseLifecycleViewModel]
 * @param viewModel The [BaseLifecycleViewModel] whose lifecycle is managed
 * @param activity The [Activity] managing the lifecycle
 * @param fragmentManager The [FragmentManager] for this lifecycle
 */
class KalugaViewModelLifecycleObserver<ViewModel : BaseLifecycleViewModel> internal constructor(
    private val viewModel: ViewModel,
    private val activity: Activity?,
    private val fragmentManager: FragmentManager,
    childFragmentManager: FragmentManager? = null,
) : DefaultLifecycleObserver {

    private val manager = LifecycleSubscribableManager(viewModel, activity, fragmentManager, childFragmentManager)

    override fun onCreate(owner: LifecycleOwner) {
        manager.onCreate(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        manager.onDestroy()
    }

    override fun onResume(owner: LifecycleOwner) {
        viewModel.didResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        viewModel.didPause()
    }
}

class LifecycleSubscribableManager<ViewModel : BaseLifecycleViewModel>(
    private val viewModel: ViewModel,
    private val activity: Activity?,
    private val fragmentManager: FragmentManager,
    private val childFragmentManager: FragmentManager? = null,
) {
    fun onCreate(owner: LifecycleOwner) {
        owner.lifecycle.coroutineScope.launch {
            viewModel.activeLifecycleSubscribables.runningFold(emptySet<LifecycleSubscribable>() to emptySet<LifecycleSubscribable>()) { (_, previous), next ->
                previous to next.toSet()
            }.flowWithLifecycle(owner.lifecycle, Lifecycle.State.CREATED).collect { (previous, next) ->
                val toDelete = previous - next
                val toAdd = next - previous
                toDelete.forEach { it.onUnsubscribe() }
                toAdd.forEach { it.onSubscribe(owner) }
            }
        }
    }

    fun onDestroy() {
        viewModel.activeLifecycleSubscribables.value.forEach {
            it.onUnsubscribe()
        }
    }

    private fun LifecycleSubscribable.onSubscribe(lifecycleOwner: LifecycleOwner) {
        when (this) {
            is ActivityLifecycleSubscribable -> if (childFragmentManager != null) {
                subscribe(activity, lifecycleOwner, fragmentManager, childFragmentManager)
            } else {
                subscribe(activity, lifecycleOwner, fragmentManager)
            }
            else -> {}
        }
    }

    private fun LifecycleSubscribable.onUnsubscribe() {
        when (this) {
            is ActivityLifecycleSubscribable -> unsubscribe()
            else -> {}
        }
    }
}
