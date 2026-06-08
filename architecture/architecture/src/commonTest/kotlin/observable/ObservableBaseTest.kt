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
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

abstract class ObservableBaseTest : BaseTest() {

    companion object {
        val DELAY_MS = 100.milliseconds
    }

    fun <V : String?, O> ((O) -> V).asUpdate() = { observable: O -> Value(this(observable)) }
    fun <O> ((O) -> String?).asNullableUpdate() = { observable: O -> Value(this(observable)) }

    suspend fun <O : InitializedObservable<String?>> testInitializedNullableStringObservable(
        observable: O,
        initialExpected: String?,
        shortDelayAfterUpdate: Boolean = false,
        vararg updates: (O) -> String?,
    ) = testObservable(
        observable,
        "unused",
        Value(initialExpected),
        shortDelayAfterUpdate,
        *updates.map { it.asNullableUpdate() }.toTypedArray(),
    )

    @Suppress("UNCHECKED_CAST")
    suspend fun <V : String?, O : InitializedObservable<V>> testInitializedStringObservable(
        observable: O,
        initialExpected: V,
        shortDelayAfterUpdate: Boolean = false,
        vararg updates: (O) -> V,
    ) = testObservable(
        observable,
        "unused" as V,
        Value(initialExpected),
        shortDelayAfterUpdate,
        *updates.map { it.asUpdate() }.toTypedArray(),
    )

    suspend fun <V : String?, OO : ObservableOptional<V>, S : BasicSubject<V, V, OO>> testStringSubject(
        subject: S,
        initialExpected: V,
        shortDelayAfterUpdate: Boolean = false,
        useSuspendableSetter: Boolean = false,
        vararg updates: Pair<V, V>,
    ) = testStringObservable(
        observable = subject,
        initialExpected,
        shortDelayAfterUpdate,
        *updates.map { it.asUpdate<V, OO, S>(useSuspendableSetter) }.toTypedArray(),
    )

    @Suppress("UNCHECKED_CAST")
    suspend fun <V : String?, OO : ObservableOptional<V>, O : BasicObservable<V, V, OO>> testUninitializedStringObservable(
        observable: O,
        shortDelayAfterUpdate: Boolean = false,
        vararg updates: (O) -> ObservableOptional<V>,
    ) = testObservable(
        observable,
        "unused" as V,
        Nothing<V>() as OO,
        shortDelayAfterUpdate,
        *updates,
    )

    suspend fun <O : DefaultObservable<String, String?>> testDefaultStringObservable(
        observable: O,
        initialExpected: String,
        shortDelayAfterUpdate: Boolean = false,
        vararg updates: (O) -> String,
    ) = testObservable(
        observable,
        "unused",
        Value(initialExpected),
        shortDelayAfterUpdate,
        *updates.map { it.asUpdate() }.toTypedArray(),
    )

    @Suppress("UNCHECKED_CAST")
    suspend fun <R : T, T : String?, OO : ObservableOptional<R>, O : BasicObservable<R, T, OO>> testStringObservable(
        observable: O,
        initialExpected: T,
        shortDelayAfterUpdate: Boolean = false,
        vararg updates: (O) -> ObservableOptional<R>,
    ) = testObservable(
        observable,
        "unused" as R,
        Value(initialExpected) as OO,
        shortDelayAfterUpdate,
        *updates,
    )

    @Suppress("UNCHECKED_CAST")
    fun <O : BasicObservable<String, String?, Value<String>>> Pair<String?, String>.asNullableUpdate(useSetter: Boolean): (O) -> Value<String> = {
        if (useSetter) {
            (it as? SuspendableSetter<String?>)?.let { runBlocking { it.set(this@asNullableUpdate.first) } }
                ?: throw Exception("Could not set value")
        } else {
            (it as? Postable<String?>)?.post(this.first) ?: throw Exception("Could not post value")
        }
        Value(this.second)
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : String?, OO : ObservableOptional<V>, O : BasicObservable<V, V, OO>> Pair<V, V>.asUpdate(useSetter: Boolean): (O) -> Value<V> = {
        if (useSetter) {
            (it as? SuspendableSetter<V>)?.let {
                runBlocking { it.set(this@asUpdate.first) }
            } ?: throw Exception("Could not set value")
        } else {
            (it as? Postable<V>)?.post(this.first) ?: throw Exception("Could not post value")
        }

        Value(this.second)
    }

    suspend fun <S : DefaultSubject<String, String?>> testStringDefaultSubject(
        subject: S,
        initialExpected: String,
        shortDelayAfterUpdate: Boolean = false,
        useSuspendableSetter: Boolean = false,
        vararg updates: Pair<String?, String>,
    ) = testStringObservable(
        subject,
        initialExpected,
        shortDelayAfterUpdate,
        *updates.map { it.asNullableUpdate<S>(useSuspendableSetter) }.toTypedArray(),
    )

    suspend fun <O : DefaultObservable<String, String?>> testStringDefaultObservable(
        observable: O,
        initialExpected: String,
        shortDelayAfterUpdate: Boolean = false,
        vararg updates: (O) -> String,
    ) = testObservable(
        observable,
        "unused",
        Value(initialExpected),
        shortDelayAfterUpdate,
        *updates.map { it.asUpdate() }.toTypedArray(),
    )

    private var updateSemaphore: Semaphore? = null
    suspend fun waitForUpdate() {
        updateSemaphore?.acquire() ?: error("call testObservable to collect your flowOfWithDelays instead of doing this directly")
    }

    suspend fun <R : T, T, OO : ObservableOptional<R>, O : BasicObservable<R, T, OO>> testObservable(
        observable: O,
        unusedValue: R,
        initialExpected: OO,
        shortDelayAfterUpdate: Boolean = false,
        vararg updates: (O) -> ObservableOptional<R>,
    ) {
        val permits = updates.size + 1 // +1 for initial state
        val semaphore = Semaphore(permits)
        updateSemaphore = semaphore
        repeat(permits) { semaphore.acquire() }

        val observableOptional by observable

        assertEquals(initialExpected.valueOrNull, observable.currentOrNull)

        if (observable is Initialized<*, *>) {
            assertTrue(initialExpected is Value<*>)
            observableOptional.let {
                assertIs<Value<*>>(it)
                assertEquals(initialExpected.value, it.value)
            }
            assertEquals(initialExpected.value, observable.current)
            assertEquals(initialExpected.value, observable.stateFlow.value)
        }

        if (observable is Uninitialized<*>) {
            assertTrue(observableOptional is Nothing<*>)
        }

        if (observable is DefaultObservable<*, *>) {
            val property by observable.valueDelegate
            assertTrue(initialExpected is Value<*>)
            assertEquals(initialExpected.value, property)
        }

        var observedValue: R? = unusedValue
        val disposable = observable.observe { observedValue = it }

        var observedInitializedValue: R? = null
        var disposableInitialized: Disposable? = null
        if (observable is Initialized<*, *>) {
            observedInitializedValue = unusedValue
            @Suppress("UNCHECKED_CAST")
            disposableInitialized = (observable as Initialized<R, T>).observeInitialized { observedInitializedValue = it }
        }

        when (initialExpected) {
            is Nothing<*> -> assertEquals(null, observedValue)
            is Value<*> -> assertEquals(initialExpected.value, observedValue)
        }

        if (observedInitializedValue != null) {
            assertTrue(initialExpected is Value<*>)
            @Suppress("UNCHECKED_CAST")
            assertEquals(initialExpected.value as R, observedInitializedValue)
        }

        semaphore.release()
        if (shortDelayAfterUpdate) {
            delay(DELAY_MS)
        }

        updates.forEachIndexed { count, update ->

            val lastUpdate = count == updates.size - 1

            val observedBefore = observedValue

            if (lastUpdate) {
                disposable.dispose()
                disposableInitialized?.dispose()
            }

            val expected = update(observable)

            assertEquals(expected, observableOptional)
            assertEquals(expected.valueOrNull, observable.currentOrNull)
            @Suppress("UNCHECKED_CAST")
            assertEquals(expected.valueOrNull, (observable as? WithState<R>)?.stateFlow?.value)

            if (observable is DefaultObservable<*, *>) {
                assertTrue(expected is Value<*>)
                val property by observable.valueDelegate
                assertEquals(expected.value, property)
            }

            if (lastUpdate || expected !is Value<*>) {
                // TODO <-- sus
                assertEquals(observedBefore, observedValue)
            } else {
                assertEquals(expected.value, observedValue)
            }

            if (!lastUpdate && observedInitializedValue != null) {
                assertTrue(expected is Value<*>)
                assertEquals(expected.value, observedInitializedValue)
            } else if (observedInitializedValue != null) {
                assertEquals(observedBefore, observedInitializedValue)
            }

            semaphore.release()

            if (shortDelayAfterUpdate) {
                delay(DELAY_MS)
            }
        }
    }
}
