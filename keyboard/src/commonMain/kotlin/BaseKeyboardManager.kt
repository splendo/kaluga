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

package com.splendo.kaluga.keyboard


/**
 * Common interface which handles the focus on a view.
 */
interface FocusHandler {
    fun requestFocus()
}

/**
 * Interface that defines the actions available for the Keyboard Manager
 */
interface BaseKeyboardManager {

    /**
     * Base KeyboardManager builder class, which used to create an KeyboardManager
     *
     * @see KeyboardManager
     */
    interface Builder {

        /**
         * Creates KeyboardManager object
         *
         * @return The KeyboardManager object
         */
        fun create(): BaseKeyboardManager
    }

    /**
     * Shows the keyboard for a given [KeyboardHostingView]
     *
     * @param keyboardHostingView The view for which the keyboard will be shown
     */
    fun show(focusHandler: FocusHandler)

    /**
     * Dismisses the current keyboard
     */
    fun hide()
}

/**
 * Manager for Showing and Hiding the Keyboard.
 */
expect class KeyboardManager : BaseKeyboardManager {

    /**
     * Builder for creating a [KeyboardManager].
     */
    class Builder : BaseKeyboardManager.Builder {
        override fun create(): KeyboardManager
    }
}
