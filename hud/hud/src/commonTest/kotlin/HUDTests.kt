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

package com.splendo.kaluga.hud

import com.splendo.kaluga.hud.HUDTests.HUDTestContext
import com.splendo.kaluga.test.base.UIThreadTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

abstract class HUDTests<HTC : HUDTestContext> : UIThreadTest<HTC>() {

    @Test
    fun builderInitializer() = testOnUIThread {
        assertNotNull(
            builder.build(MainScope()),
        )
    }

    @Test
    fun builderSetStyleAndTitle() = testOnUIThread {
        assertNotNull(
            builder.build(MainScope()) {
                setStyle(HUDStyle.CUSTOM)
                setTitle("Foo")
            },
        )
    }

    @Test
    fun testBuilder() = testOnUIThread {
        val hud1 = builder.build(MainScope())
        assertEquals(hud1.style, HUDStyle.SYSTEM)
        assertNull(hud1.title)
        val hud2 = builder.build(MainScope()) {
            setStyle(HUDStyle.CUSTOM)
            setTitle("Title")
        }
        assertEquals(hud2.style, HUDStyle.CUSTOM)
        assertEquals(hud2.title, "Title")
    }

    abstract class HUDTestContext(coroutineScope: CoroutineScope) :
        TestContext,
        CoroutineScope by coroutineScope {
        abstract val builder: HUD.Builder
    }
}
