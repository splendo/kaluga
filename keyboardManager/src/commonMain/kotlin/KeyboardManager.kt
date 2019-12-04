package com.splendo.kaluga.keyboardmanager

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

expect class KeyboardView

/**
 * Interface that defines actions that can be applied to the alert.
 */
interface KeyboardActions {

    fun show(keyboardView: KeyboardView)

    /**
     * Dismisses the current keyboard
     *
     */
    fun dismiss()

}

/**
 * Base alert presenter class, which used to show and dismiss the keyboard
 * Abstract methods should be implemented on platform-specific side
 */
abstract class BaseKeyboardManager : KeyboardActions {

}

expect class KeyboardInterface : BaseKeyboardManager

/**
 * Base alert builder class, which used to create an alert, which can be shown and dismissed
 * later on using AlertInterface object
 *
 * @see KeyboardInterface
 */
abstract class BaseKeyboardManagerBuilder {

    /**
     * Creates KeyboardInterface object
     *
     * @return The KeyboardInterface object
     */
    abstract fun create(): KeyboardInterface
}

expect class KeyboardManagerBuilder : BaseKeyboardManagerBuilder
