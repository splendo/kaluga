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

package com.splendo.kaluga.bytes.view

import java.lang.management.ManagementFactory
import com.sun.management.ThreadMXBean

/**
 * Measures what code asks the heap for, per call, on the current thread. Run with `-XX:TieredStopAtLevel=1`: without
 * C2's escape analysis every allocation the code makes is counted, so the numbers do not depend on how warm the JIT is.
 */
object Allocations {

    private val threads = (ManagementFactory.getThreadMXBean() as ThreadMXBean).also {
        // Without it every measurement is zero, and every test would pass.
        check(it.isThreadAllocatedMemorySupported && it.isThreadAllocatedMemoryEnabled) { "This JVM does not count the memory a thread allocates" }
    }

    /** The bytes one call of [block] allocates, on average over [calls] calls, less what the loop itself allocates. */
    fun perCall(calls: Int = 20_000, block: () -> Unit): Double {
        repeat(calls) { block() }
        val baseline = measure(calls) { }
        return (measure(calls, block) - baseline).coerceAtLeast(0).toDouble() / calls
    }

    private inline fun measure(calls: Int, block: () -> Unit): Long {
        val before = threads.currentThreadAllocatedBytes
        for (call in 0 until calls) block()
        return threads.currentThreadAllocatedBytes - before
    }
}
