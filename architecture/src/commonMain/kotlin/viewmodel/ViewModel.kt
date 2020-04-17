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

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

expect open class ViewModel internal constructor() {
    val coroutineScope: CoroutineScope
    fun onClear()
}

open class BaseViewModel : ViewModel(){

    private val resumedJobs = SupervisorJob()

    fun didResume() {
        onResume.invoke(CoroutineScope(coroutineScope.coroutineContext + resumedJobs))
    }

    protected open val onResume: (CoroutineScope) -> Unit = {}

    fun didPause() {
        resumedJobs.cancelChildren()
    }

    protected open val onPause: () -> Unit = {}

}

open class NavigatingViewModel<A : NavigationAction<*>>(val navigator: Navigator<A>) : BaseViewModel() {

}

