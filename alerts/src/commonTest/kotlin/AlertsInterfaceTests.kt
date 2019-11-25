package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

abstract class AlertsInterfaceTests {

    abstract val builder: AlertBuilder

    @Test
    fun testAlertBuilderExceptionNoActions() = runBlocking {
        assertFailsWith<IllegalArgumentException> {
            builder.alert {
                setTitle("OK")
            }
        }
        Unit
    }

    @Test
    fun testAlertBuilderExceptionNoTitleOrMessage() = runBlocking {
        assertFailsWith<IllegalArgumentException> {
            builder.alert {
                setPositiveButton("OK")
            }
        }
        Unit
    }

    @Test
    fun testAlertFlowCancel() = runBlocking {
        val coroutine = CoroutineScope(Dispatchers.Main).launch {
            val presenter = builder.alert {
                setTitle("Hello")
                setPositiveButton("OK")
                setNegativeButton("Cancel")
            }

            val result = coroutineContext.run { presenter.show() }
            assertNull(result)
        }
        // On cancel call, we expect the dialog to be dismissed
        coroutine.cancel()
    }
}
