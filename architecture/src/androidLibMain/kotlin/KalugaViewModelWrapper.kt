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

package com.splendo.kaluga.architecture

import android.app.Activity
import androidx.fragment.app.FragmentManager
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel

class KalugaViewModelWrapper<VM : BaseViewModel>(val viewModel: VM) {

    fun onResume(activity: Activity? = null, fragmentManager: FragmentManager? = null) {
        if (viewModel is NavigatingViewModel<*>) {
            val navigator: Navigator<*> = viewModel.navigator
            navigator.subscribe(activity, fragmentManager)
        }
        viewModel.didResume()
    }

    fun onPause() {
        if (viewModel is NavigatingViewModel<*>) {
            val navigator: Navigator<*> = viewModel.navigator
            navigator.unsubscribe()
        }
        viewModel.didPause()
    }

}