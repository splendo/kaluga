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

import com.splendo.kaluga.base.MultiplatformMainScope
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import kotlin.test.Test
import kotlinx.coroutines.launch

abstract class KeyboardManagerTests {

    abstract val builder: KeyboardManagerImpl.Builder
    abstract val view: KeyboardHostingView

    @Test
    fun testShow() = runBlocking {
        val result = EmptyCompletableDeferred()
        MultiplatformMainScope().launch {
            builder.create(this).show(view)
            verifyShow()
            result.complete()
        }
        result.await()
    }

    abstract suspend fun verifyShow()

    @Test
    fun testDismiss() = runBlocking {
        val result = EmptyCompletableDeferred()
        MultiplatformMainScope().launch {
            builder.create(this).hide()
            verifyDismiss()
            result.complete()
        }
        result.await()
    }

    abstract suspend fun verifyDismiss()
}
