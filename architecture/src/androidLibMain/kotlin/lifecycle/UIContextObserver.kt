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
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class UIContextObserver : LifecycleSubscribable, CoroutineScope by MainScope() {

    data class UIContextData(
        val activity: Activity?,
        val lifecycleOwner: LifecycleOwner,
        val fragmentManager: FragmentManager
    )

    private val _uiContextData = ConflatedBroadcastChannel<UIContextData?>(null)
    val uiContextData: Flow<UIContextData?> get() = _uiContextData.asFlow().distinctUntilChanged()

    override fun subscribe(activity: Activity?, lifecycleOwner: LifecycleOwner, fragmentManager: FragmentManager) {
        launch {
            _uiContextData.send(UIContextData(activity, lifecycleOwner, fragmentManager))
        }
    }

    override fun unsubscribe() {
        launch {
            _uiContextData.send(null)
        }
    }
}
