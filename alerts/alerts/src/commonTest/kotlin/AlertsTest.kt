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

package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.Alert
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AlertsTest {

    @Test
    fun testAlertBaseInitialization() {
        var handlerWasCalled = false
        val handler = { handlerWasCalled = true }
        val action = Alert.Action("action", Alert.Action.Style.DEFAULT, handler)
        val alert = Alert("title", "message", listOf(action))

        assertEquals(alert.title, "title")
        assertEquals(alert.message, "message")
        assertEquals(alert.actions.count(), 1)
        assertEquals(alert.actions.first().title, "action")
        assertEquals(alert.actions.first().style, Alert.Action.Style.DEFAULT)
        assertEquals(alert.actions.first().handler, handler)

        assertFalse(handlerWasCalled)
        action.handler()
        assertTrue(handlerWasCalled)
    }
}
