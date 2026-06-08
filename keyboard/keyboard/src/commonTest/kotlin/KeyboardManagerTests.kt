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

import com.splendo.kaluga.keyboard.KeyboardManagerTests.KeyboardTestContext
import com.splendo.kaluga.test.base.UIThreadTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.test.Test

abstract class KeyboardManagerTests<FH : FocusHandler, KTC : KeyboardTestContext<FH>> : UIThreadTest<KTC>() {

    abstract class KeyboardTestContext<FH : FocusHandler> :
        TestContext,
        CoroutineScope {
        abstract val builder: BaseKeyboardManager.Builder<FH>
        abstract val focusHandler: FH

        abstract fun verifyShow()
        abstract fun verifyDismiss()
    }

    @Test
    fun testShow() = testOnUIThread {
        launch {
            val manager = builder.create(this)
            yield()
            manager.show(focusHandler)
            cancel()
        }.join()
        verifyShow()
    }

    @Test
    fun testDismiss() = testOnUIThread {
        launch {
            val manager = builder.create(this)
            yield()
            manager.hide()
            cancel()
        }.join()
        verifyDismiss()
    }
}
