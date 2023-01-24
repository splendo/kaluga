
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

package com.splendo.kaluga.keyboard

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import kotlinx.coroutines.CoroutineScope

/**
 * Common interface which handles the focus on a view.
 */
interface FocusHandler

/**
 * Interface that defines the actions available for the Keyboard Manager
 */
interface BaseKeyboardManager<FH : FocusHandler> {

    /**
     * Base KeyboardManager builder class, which used to create a [BaseKeyboardManager]
     */
    interface Builder<FH : FocusHandler> : LifecycleSubscribable {

        /**
         * Creates KeyboardManager object
         *
         * @return The KeyboardManager object
         */
        fun create(coroutineScope: CoroutineScope): BaseKeyboardManager<FH>
    }

    /**
     * Shows the keyboard for a given [FocusHandler]
     *
     * @param focusHandler The [FocusHandler] for which the keyboard will be shown
     */
    fun show(focusHandler: FH)

    /**
     * Dismisses the current keyboard
     */
    fun hide()
}
