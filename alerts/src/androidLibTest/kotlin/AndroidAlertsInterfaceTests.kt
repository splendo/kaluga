package com.splendo.kaluga.test

import android.content.Context
import com.splendo.kaluga.alerts.AlertBuilder
import org.junit.Before
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class AndroidAlertsInterfaceTests : AlertsInterfaceTests() {

    private lateinit var mockContext: Context

    @Before
    fun before() {
        mockContext = mock(Context::class.java)
        `when`(mockContext.applicationContext).thenReturn(mockContext)
    }

    override val builder get() = AlertBuilder(mockContext.applicationContext)
}
