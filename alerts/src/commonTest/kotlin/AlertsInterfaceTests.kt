package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.base.runBlocking
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class AlertsInterfaceTests {

    abstract val builder: AlertBuilder

    @Test
    fun testAlertBuilderExceptionNoActions() = runBlocking {
        assertFailsWith<IllegalArgumentException> {
            builder.buildAlert {
                setTitle("OK")
            }
        }
        Unit
    }

    @Test
    fun testAlertBuilderExceptionNoTitleOrMessage() = runBlocking {
        assertFailsWith<IllegalArgumentException> {
            builder.buildAlert {
                setPositiveButton("OK")
            }
        }
        Unit
    }

    @Test
    fun testAlertFlowCancel() = runBlocking {
        val coroutine = CoroutineScope(Dispatchers.Main).launch {
            val presenter = builder.buildAlert {
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

    @Test
    fun testActionSheetFlowCancel() = runBlocking {
        val coroutine = CoroutineScope(Dispatchers.Main).launch {
            val presenter = builder.buildActionSheet {
                setTitle("Choose")
                setPositiveButton("Option 1")
                setPositiveButton("Option 2")
                setPositiveButton("Option 3")
            }

            val result = coroutineContext.run { presenter.show() }
            assertNull(result)
        }
        // On cancel call, we expect the dialog to be dismissed
        coroutine.cancel()
    }
}
