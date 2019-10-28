package com.splendo.kaluga.loadingIndicator

import kotlin.test.Test
import kotlin.test.assertFailsWith

class LoadingIndicatorTests1 {

    @Test
    fun builderMissingViewException() {

        assertFailsWith<IllegalArgumentException> {
            IOSLoadingIndicator
                .Builder()
                .create()
        }
    }
}

