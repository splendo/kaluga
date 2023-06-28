/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.base
import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.time.Duration.Companion.milliseconds

class FlowExtensionsTest {
    private companion object {
        val TIMEOUT = 50.milliseconds
    }

    @Test
    fun captureForOnEmpty(): Unit = runBlocking {
        assertEquals(emptyList(), emptyFlow<String>().captureFor(TIMEOUT))
    }

    @Test
    fun captureForFiniteFlow(): Unit = runBlocking {
        assertEquals(listOf(1, 2, 3), flowOf(1, 2, 3).captureFor(TIMEOUT))
    }

    @Test
    fun captureForInfiniteFlow(): Unit = runBlocking {
        assertEquals(listOf(1), MutableStateFlow(1).captureFor(TIMEOUT))
    }

    @Test
    fun flowAwaitFirstFailsOnEmpty(): Unit = runBlocking {
        assertFails {
            emptyFlow<String>().awaitFirst()
        }

        assertFails {
            MutableSharedFlow<String>().awaitFirst(timeout = TIMEOUT)
        }
    }

    @Test
    fun flowAwaitFirstFailsOnNoMatch(): Unit = runBlocking {
        assertFails {
            flowOf("A", "B", "C")
                .awaitFirst { it == "D" }
        }

        assertFails {
            MutableStateFlow("A")
                .awaitFirst(timeout = TIMEOUT) { it == "D" }
        }
    }

    @Test
    fun flowAwaitFirstMatches(): Unit = runBlocking {
        val result = flowOf("A", "B", "C")
            .awaitFirst { it == "B" }
        assertEquals("B", result)
    }

    @Test
    fun flowAwaitFirstMatchesNull(): Unit = runBlocking {
        val result = flowOf("A", "B", "C", null)
            .awaitFirst { it == null }
        assertEquals(null, result)
    }

    @Test
    fun assertEmitsFailsOnEmpty(): Unit = runBlocking {
        assertFails {
            emptyFlow<String>().assertEmits { true }
        }

        assertFails {
            MutableSharedFlow<String>().assertEmits(timeout = TIMEOUT) { true }
        }
    }

    @Test
    fun assertEmitsFailsOnNoMatch(): Unit = runBlocking {
        assertFails {
            flowOf("A", "B", "C")
                .assertEmits { it == "D" }
        }

        assertFails {
            MutableStateFlow("A")
                .assertEmits(timeout = TIMEOUT) { it == "D" }
        }
    }

    @Test
    fun assertEmitsMatches(): Unit = runBlocking {
        flowOf("A", "B", "C")
            .assertEmits { it == "B" }

        MutableStateFlow("B").assertEmits { it == "B" }
    }

    @Test
    fun assertEmitsMatchesNull(): Unit = runBlocking {
        flowOf("A", "B", "C", null)
            .assertEmits { it == null }

        MutableStateFlow<String?>(null).assertEmits { it == null }
    }

    @Test
    fun assertEmitsValueFailsOnEmpty(): Unit = runBlocking {
        assertFails {
            emptyFlow<String>().assertEmits(expected = "")
        }

        assertFails {
            MutableSharedFlow<String>().assertEmits(expected = "", timeout = TIMEOUT)
        }
    }

    @Test
    fun assertEmitsValueFailsOnNoMatch(): Unit = runBlocking {
        assertFails {
            flowOf("A", "B", "C")
                .assertEmits(expected = "D")
        }

        assertFails {
            MutableStateFlow("A")
                .assertEmits(expected = "D", timeout = TIMEOUT)
        }
    }

    @Test
    fun assertEmitsValueFirstMatches(): Unit = runBlocking {
        flowOf("A", "B", "C")
            .assertEmits(expected = "B")

        MutableStateFlow("B").assertEmits("B")
    }

    @Test
    fun assertEmitsValueMatchesNull(): Unit = runBlocking {
        flowOf("A", "B", "C", null)
            .assertEmits(expected = null)

        MutableStateFlow<String?>(null).assertEmits(null)
    }
}
