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

package com.splendo.kaluga.hud

import com.splendo.kaluga.base.runOnMain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

abstract class HUDTests {

    @Test
    fun builderInitializer() = runOnMain { 
        assertNotNull(
            builder.build()
        )
    }

    @Test
    fun builderSetStyleAndTitle() = runOnMain{ 
        assertNotNull(
            builder.build {
                setStyle(HUDStyle.CUSTOM)
                setTitle("Foo")
            }
        )
    }

    @Test
    fun testBuilder() = runOnMain {
        val hud1 = builder.build(mainScope)
        assertEquals(hud1.style, HUDStyle.SYSTEM)
        assertNull(hud1.title)
        val hud2 = builder.build(mainScope) {
            setStyle(HUDStyle.CUSTOM)
            setTitle("Title")
        }
        assertEquals(hud2.style, HUDStyle.CUSTOM)
        assertEquals(hud2.title, "Title")
    }

    protected abstract val builder: HUD.Builder
}
