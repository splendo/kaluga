package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.resources.colorFrom
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AlertsTest {

    @Test
    fun testAlertBaseInitialization() {

        var handlerWasCalled = false
        val handler = { handlerWasCalled = true }
        val action = Alert.Action("action", Alert.Action.Style.DEFAULT, null, handler)
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

    @Test
    fun testAlertInitializationWithColor() {

        var handlerWasCalled = false
        val handler = { handlerWasCalled = true }
        val action = Alert.Action("action", Alert.Action.Style.DEFAULT, colorFrom(255, 255, 255), handler)
        val alert = Alert("title", "message", listOf(action))

        assertEquals(alert.title, "title")
        assertEquals(alert.message, "message")
        assertEquals(alert.actions.count(), 1)
        assertEquals(alert.actions.first().title, "action")
        assertEquals(alert.actions.first().style, Alert.Action.Style.DEFAULT)
        assertEquals(alert.actions.first().textColor, colorFrom(255, 255, 255))
        assertEquals(alert.actions.first().handler, handler)

        assertFalse(handlerWasCalled)
        action.handler()
        assertTrue(handlerWasCalled)
    }
}
