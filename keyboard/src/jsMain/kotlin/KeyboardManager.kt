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

import kotlinx.coroutines.CoroutineScope

/**
 * A [BaseKeyboardManager] that takes any [FocusHandler]
 */
class KeyboardManager : BaseKeyboardManager<FocusHandler> {

    /**
     * A [BaseKeyboardManager.Builder] to create a [KeyboardManager]
     */
    class Builder : BaseKeyboardManager.Builder<FocusHandler> {
        override fun create(coroutineScope: CoroutineScope) = KeyboardManager()
    }

    override fun show(focusHandler: FocusHandler) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun hide() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
