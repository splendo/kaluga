package com.splendo.kaluga.hud

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

actual class UiContextTrackingBuilder: CoroutineScope by MainScope() {

    data class UiContextData(
        val lifecycleOwner: LifecycleOwner,
        val fragmentManager: FragmentManager
    )

    internal var onUiContextDataChanged: ((newValue: UiContextData?, oldValue: UiContextData?) -> Unit)? = null

    var uiContextData: UiContextData? = null
        set(value) {
            when (value) {
                null -> {
                    checkNotNull(uiContextData) { "Can't reset null value" }
                }
                else -> {
                    check(this.uiContextData == null) { "Can't reset non-null value" }
                }
            }
            onUiContextDataChanged?.invoke(value, uiContextData)
            field = value
        }
}
