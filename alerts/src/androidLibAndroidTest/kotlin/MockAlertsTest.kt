package com.splendo.kaluga.alerts

import android.content.Context
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import org.mockito.Mockito.mock
import java.lang.IllegalArgumentException

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

class MockAlertsTest {

    private lateinit var mockContext: Context

    @Before
    fun before() {
        mockContext = mock(Context::class.java)
    }

    @Test
    fun testAlertBuilderExceptionNoActions() {
        assertFailsWith<IllegalArgumentException> {
            AlertBuilder(mockContext)
                .setTitle("OK")
                .create()
        }
    }

    @Test
    fun testAlertBuilderExceptionNoTitleOrMessage() {
        assertFailsWith<IllegalArgumentException> {
            AlertBuilder(mockContext)
                .setPositiveButton("OK") { }
                .create()
        }
    }
}
