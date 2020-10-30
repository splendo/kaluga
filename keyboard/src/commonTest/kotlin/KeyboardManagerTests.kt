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

package com.splendo.kaluga.keyboard

import com.splendo.kaluga.base.runOnMain
import kotlinx.coroutines.MainScope
import kotlin.test.Test

abstract class KeyboardManagerTests {

    abstract val builder: KeyboardManager.Builder
    abstract val view: KeyboardHostingView

    @Test
    fun testShow() = runOnMain {
        builder.create(MainScope()).show(view)
        verifyShow()
    }

    abstract fun verifyShow()

    @Test
    fun testDismiss() = runOnMain {
        builder.create(MainScope()).hide()
        verifyDismiss()
    }

    abstract fun verifyDismiss()
}
