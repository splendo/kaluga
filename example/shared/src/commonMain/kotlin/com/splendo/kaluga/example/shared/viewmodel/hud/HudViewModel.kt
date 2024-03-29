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

package com.splendo.kaluga.example.shared.viewmodel.hud

import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.hud.BaseHUD
import com.splendo.kaluga.hud.HUDStyle
import com.splendo.kaluga.hud.build
import com.splendo.kaluga.hud.dismissAfter
import com.splendo.kaluga.hud.presentDuring
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HudViewModel(private val builder: BaseHUD.Builder) : BaseLifecycleViewModel(builder) {

    val showSystemButton = KalugaButton.Plain("show_loading_indicator_system".localized(), ButtonStyles.default) {
        // SYSTEM style by default
        // No title by default
        coroutineScope.launch {
            builder.build(this).present().dismissAfter(3_000)
        }
    }

    val showCustomButton = KalugaButton.Plain("show_loading_indicator_custom".localized(), ButtonStyles.default) {
        coroutineScope.launch {
            builder.build(this) {
                setStyle(HUDStyle.CUSTOM)
                setTitle("This is a custom title")
            }.presentDuring {
                // Simulate heavy task
                delay(3_000)
            }
        }
    }
}
