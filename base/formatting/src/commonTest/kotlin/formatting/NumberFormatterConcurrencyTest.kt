/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.formatting

import com.splendo.kaluga.base.i18n.KalugaLocale.Companion.createLocale
import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.testRunBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NumberFormatterConcurrencyTest : BaseTest() {

    companion object {
        private const val WORKERS = 8
        private const val ITERATIONS = 10_000
    }

    @Test
    fun testSharedFormatterFormatsAndParsesCorrectlyUnderContention() = testRunBlocking {
        val formatter = NumberFormatter(createLocale("en", "US"), NumberFormatStyle.Decimal(minIntegerDigits = 1U, maxFractionDigits = 2U))

        // Establish the single-threaded expectations first, so a failure below can only mean corruption.
        assertEquals("0.8", formatter.format(0.8))
        assertEquals("42", formatter.format(42))
        assertEquals(10.0, formatter.parse("10")?.toDouble())
        assertEquals(7.5, formatter.parse("7.5")?.toDouble())

        val failures = (0 until WORKERS).map { worker ->
            async(Dispatchers.Default) {
                val workerFailures = mutableListOf<String>()

                // A corrupted shared digit buffer can also make an operation throw (e.g. parse hitting
                // NumberFormatException instead of ParseException), so exceptions count as corruption too.
                fun expect(operation: String, expected: Any?, actual: () -> Any?) {
                    val result = try {
                        actual()
                    } catch (e: Throwable) {
                        "threw $e"
                    }
                    if (result != expected) workerFailures.add("$operation -> $result")
                }
                repeat(ITERATIONS) { iteration ->
                    // Alternate which operation goes first per worker so format and parse overlap.
                    if ((worker + iteration) % 2 == 0) {
                        expect("format(0.8)", "0.8") { formatter.format(0.8) }
                        expect("parse(\"10\")", 10.0) { formatter.parse("10")?.toDouble() }
                    } else {
                        expect("parse(\"7.5\")", 7.5) { formatter.parse("7.5")?.toDouble() }
                        expect("format(42)", "42") { formatter.format(42) }
                    }
                }
                workerFailures
            }
        }.awaitAll().flatten()

        assertTrue(
            failures.isEmpty(),
            "${failures.size} corrupted results out of ${WORKERS * ITERATIONS * 2} operations; first: ${failures.take(5)}",
        )
    }
}
