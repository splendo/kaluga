package com.splendo.kotlin.components.location.test

import com.splendo.kotlin.components.location.Location
import kotlin.test.Test
import kotlin.test.assertEquals

open class LocationTest {
    @Test
    fun testLocation() {
        assertEquals(1.0, Location(1.0, 2.0).latitude)
    }

}