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

package com.splendo.kaluga.datetime

import com.splendo.kaluga.base.i18n.KalugaLocale
import com.splendo.kaluga.base.i18n.enUsPosix
import com.splendo.kaluga.base.test.testRunBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

class DateFormatterConcurrencyTest {

    companion object {
        private const val WORKERS = 8
        private const val ITERATIONS = 10_000
        private val March181988 = DefaultKalugaDate.epoch(574695462750.milliseconds)
        private val Epoch = DefaultKalugaDate.epoch(0.milliseconds)
    }

    @Test
    fun testSharedFormatterFormatsAndParsesCorrectlyUnderContention() = testRunBlocking {
        val formatter = KalugaDateFormatter.patternFormat("yyyy-MM-dd HH:mm:ss", KalugaTimeZone.utc, KalugaLocale.enUsPosix)

        // Establish the single-threaded expectations first, so a failure below can only mean corruption.
        assertEquals("1988-03-18 13:37:42", formatter.format(March181988))
        assertEquals("1970-01-01 00:00:00", formatter.format(Epoch))
        assertEquals(1_000_000_000_000.milliseconds, formatter.parse("2001-09-09 01:46:40")?.durationSinceEpoch)
        assertEquals(574695462000.milliseconds, formatter.parse("1988-03-18 13:37:42")?.durationSinceEpoch)

        val failures = (0 until WORKERS).map { worker ->
            async(Dispatchers.Default) {
                val workerFailures = mutableListOf<String>()

                // A corrupted shared calendar can also make an operation throw (e.g. parse hitting
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
                        expect("format(March181988)", "1988-03-18 13:37:42") { formatter.format(March181988) }
                        expect("parse(\"2001-09-09 01:46:40\")", 1_000_000_000_000.milliseconds) { formatter.parse("2001-09-09 01:46:40")?.durationSinceEpoch }
                    } else {
                        expect("parse(\"1988-03-18 13:37:42\")", 574695462000.milliseconds) { formatter.parse("1988-03-18 13:37:42")?.durationSinceEpoch }
                        expect("format(Epoch)", "1970-01-01 00:00:00") { formatter.format(Epoch) }
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
