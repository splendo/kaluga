package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.Alert
import kotlin.test.*

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
