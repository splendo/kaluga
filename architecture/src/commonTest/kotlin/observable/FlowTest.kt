/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.observable

import com.splendo.kaluga.architecture.observable.ObservableOptional.Nothing
import com.splendo.kaluga.architecture.observable.ObservableOptional.Value
import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlin.test.Test

class FlowTest : ObservableBaseTest() {

    private fun <V : String?> flowOfWithDelays(vararg values: V) = flowOf(*values).onStart { waitForUpdate() }.onEach { waitForUpdate() }

    private suspend fun <V : String?> testUninitializedFlow(observable: UninitializedObservable<V>, vararg furtherUpdates: ObservableOptional<V>) =
        testUninitializedStringObservable(
            observable,
            true,
            *furtherUpdates.map { { _: UninitializedObservable<V> -> it } }.toTypedArray(),
        )

    private suspend fun testDefaultFlow(observable: DefaultObservable<String, String?>, initialExcepted: String, vararg furtherUpdates: String) = testDefaultStringObservable(
        observable,
        initialExcepted,
        true,
        *furtherUpdates.map { { _: DefaultObservable<String, String?> -> it } }.toTypedArray(),
    )

    private suspend fun <V : String?> testInitializedFlow(observable: InitializedObservable<V>, initialExcepted: V, vararg furtherUpdates: V) = testInitializedStringObservable(
        observable,
        initialExcepted,
        true,
        *furtherUpdates.map { { _: InitializedObservable<V> -> it } }.toTypedArray(),
    )

    @Test
    fun testFlow() = runBlocking {
        withContext(Dispatchers.Main.immediate) {
            val flow = flowOfWithDelays("1", "2", "3")
            val o = flow.toUninitializedObservable(this)

            testUninitializedFlow(
                o,
                Nothing(),
                Value("1"),
                Value("2"),
                Value("3"),
            )
        }
    }

    @Test
    fun testInitializedFlow() = runBlocking(Dispatchers.Default) {
        val flow = flowOfWithDelays("1", "2", "3")
        val o = flow.toInitializedObservable("initial", this)

        testInitializedFlow(
            o,
            "initial",
            "initial",
            "1",
            "2",
            "3",
        )
    }

    @Test
    fun testNullableFlow() = runBlocking {
        val flow = flowOfWithDelays("1", null, "3")
        val o = flow.toUninitializedObservable(this)

        testUninitializedFlow(
            o,
            Nothing(),
            Value("1"),
            Value(null),
            Value("3"),
        )
    }

    @Test
    fun testNullableDefaultFlow() = runBlocking {
        val flow = flowOfWithDelays("1", null, "3")
        val o = flow.toDefaultObservable("default", "initial", this)

        testDefaultFlow(
            o,
            "initial",
            "initial",
            "1",
            "default",
            "3",
        )
    }
}
