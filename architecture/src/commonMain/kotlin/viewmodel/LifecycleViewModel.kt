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

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Simple ViewModel class that is to be bound to a View lifecycle
 */
expect open class LifecycleViewModel internal constructor() {
    /**
     * [CoroutineScope] of the ViewModel.
     * This scope is active until the ViewModel lifecycle is cleared.
     */
    val coroutineScope: CoroutineScope

    /**
     * Called when the ViewModel lifecycle is cleared
     */
    protected open fun onCleared()
}

/**
 * Default [LifecycleViewModel] implementation respecting the Lifecycle of the presenting view.
 * @param lifecycleSubscribables The [LifecycleSubscribable] to be used by this viewModel.
 */
open class BaseLifecycleViewModel(vararg lifecycleSubscribables: LifecycleSubscribable) : LifecycleViewModel() {

    private val _activeLifecycleSubscribables = MutableStateFlow(lifecycleSubscribables.toSet())

    /**
     * A [StateFlow] of the set of [LifecycleSubscribable] that are currently in use.
     * Hook up your lifecycle to these [LifecycleSubscribable]s where required.
     */
    val activeLifecycleSubscribables = _activeLifecycleSubscribables.asStateFlow()
    private val resumedJobs = SupervisorJob()

    /**
     * Function to be called when the presenting views lifecycle begins
     */
    fun didResume() {
        onResume(CoroutineScope(coroutineScope.coroutineContext + resumedJobs))
    }

    /**
     * Custom handler when the presenting views lifecycle begins
     * @param scope A [CoroutineScope] that lives during the lifecycle of the presenting view
     */
    protected open fun onResume(scope: CoroutineScope) {}

    /**
     * Function to be called when the presenting views lifecycle ends
     */
    fun didPause() {
        onPause()
        resumedJobs.cancelChildren()
    }

    /**
     * Custom handler when the presenting views lifecycle ends
     */
    protected open fun onPause() {}

    /**
     * Adds a list of [LifecycleSubscribable] to [activeLifecycleSubscribables].
     */
    protected fun addLifecycleSubscribables(vararg markers: LifecycleSubscribable) {
        _activeLifecycleSubscribables.update { it.toMutableSet().apply { addAll(markers.toSet()) }.toSet() }
    }

    /**
     * Removes a list of [LifecycleSubscribable] from [activeLifecycleSubscribables] (if added).
     */
    protected fun removeLifecycleSubscribables(vararg markers: LifecycleSubscribable) {
        _activeLifecycleSubscribables.update { it.toMutableSet().apply { removeAll(markers.toSet()) }.toSet() }
    }
}

/**
 * Default [LifecycleViewModel] allowing navigation.
 * @param navigator The [Navigator] handling navigation.
 * @param lifecycleSubscribables The [LifecycleSubscribable] to be used by this viewModel.
 */
open class NavigatingViewModel<A : NavigationAction<*>>(
    protected val navigator: Navigator<A>,
    vararg lifecycleSubscribables: LifecycleSubscribable,
) : BaseLifecycleViewModel(*lifecycleSubscribables, navigator)
