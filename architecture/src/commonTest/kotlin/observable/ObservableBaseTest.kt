/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.architecture.observable.ObservableOptional.Nothing
import com.splendo.kaluga.architecture.observable.ObservableOptional.Value
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.BaseTest
import kotlinx.coroutines.delay
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class ObservableBaseTest:BaseTest() {

    companion object {
        const val DELAY_MS = 30L
    }

    fun <V:String?, O>((O) -> V).asUpdate() = { observable:O -> Value(this(observable))}
    fun <O>((O) -> String?).asNullableUpdate() = { observable:O -> Value(this(observable))}

    fun <O:InitializedObservable<String?>> testInitializedNullableStringObservable(
        observable:O,
        initialExpected:String?,
        shortDelayAfterUpdate:Boolean = false,
        vararg updates: (O) -> String?
    ) = testObservable(
        observable,
        "unused",
        Value(initialExpected),
        shortDelayAfterUpdate,
        *updates.map { it.asNullableUpdate() }.toTypedArray()
    )

    fun <V:String?, O:InitializedObservable<V>> testInitializedStringObservable(
        observable:O,
        initialExpected:V,
        shortDelayAfterUpdate:Boolean = false,
        vararg updates: (O) -> V
    ) = testObservable(
        observable,
        "unused" as V,
        Value(initialExpected),
        shortDelayAfterUpdate,
        *updates.map { it.asUpdate() }.toTypedArray()
    )

    fun <V:String?, OO:ObservableOptional<V>, S:BasicSubject<V, V, OO>> testStringSubject(
        subject: S,
        initialExpected:V,
        shortDelayAfterUpdate:Boolean = false,
        useSuspendableSetter: Boolean = false,
        vararg updates: Pair<V, V>

    ) = testStringObservable(
        observable = subject,
        initialExpected,
        shortDelayAfterUpdate,
        *updates.map { it.asUpdate<V, OO, S>(useSuspendableSetter) }.toTypedArray()
    )

    fun <V:String?, OO:ObservableOptional<V>, O:BasicObservable<V, V, OO>> testUninitializedStringObservable(
        observable:O,
        shortDelayAfterUpdate:Boolean = false,
        vararg updates: (O) -> ObservableOptional<V>
    ) = testObservable(
        observable,
        "unused" as V,
        Nothing<V>() as OO,
        shortDelayAfterUpdate,
        *updates
    )

    fun <O:DefaultObservable<String, String?>> testDefaultStringObservable(
        observable:O,
        initialExpected: String,
        shortDelayAfterUpdate:Boolean = false,
        vararg updates: (O) -> String
    ) = testObservable(
        observable,
        "unused",
        Value(initialExpected),
        shortDelayAfterUpdate,
        *updates.map { it.asUpdate() }.toTypedArray()
    )

    fun <V:String?, OO:ObservableOptional<V>, O:BasicObservable<V, V, OO>> testStringObservable(
        observable:O,
        initialExpected:V,
        shortDelayAfterUpdate:Boolean = false,
        vararg updates: (O) -> ObservableOptional<V>
    ) = testObservable(
        observable,
        "unused" as V,
        Value(initialExpected) as OO,
        shortDelayAfterUpdate,
        *updates
    )

    fun <O:DefaultObservable<String, String?>> Pair<String?, String>.asNullableUpdate(useSetter:Boolean):(O) -> String = {
        if (useSetter)
            (it as? SuspendableSetter<String?>)?.let { runBlocking { it.set(this@asNullableUpdate.first) } } ?: throw Exception("Could not set value")
        else
            (it as? Postable<String?>)?.post(this.first) ?: throw Exception("Could not post value")

        this.second
    }

    fun <V:String?, OO:ObservableOptional<V>,O:BasicObservable<V, V, OO>> Pair<V, V>.asUpdate(useSetter:Boolean):(O) -> Value<V> = {
        if (useSetter)
            (it as? SuspendableSetter<V>)?.let { runBlocking { it.set(this@asUpdate.first) } } ?: throw Exception("Could not set value")
        else
            (it as? Postable<V>)?.post(this.first) ?: throw Exception("Could not post value")

        Value(this.second)
    }

    fun <S:DefaultSubject<String, String?>> testStringDefaultSubject(
        subject: S,
        initialExpected:String,
        shortDelayAfterUpdate:Boolean = false,
        useSuspendableSetter: Boolean = false,
        vararg updates: Pair<String?, String>

    ) = testStringDefaultObservable(
        subject,
        initialExpected,
        shortDelayAfterUpdate,
        *updates.map { it.asNullableUpdate<S>(useSuspendableSetter) }.toTypedArray()
    )

    fun <O:DefaultObservable<String, String?>> testStringDefaultObservable(
        observable:O,
        initialExpected:String,
        shortDelayAfterUpdate:Boolean = false,
        vararg updates: (O) -> String
    ) = testObservable(
            observable,
            "unused",
            Value(initialExpected),
            shortDelayAfterUpdate,
            *updates.map { it.asUpdate() }.toTypedArray()
        )

    fun <R:T,T, OO:ObservableOptional<R>, O:BasicObservable<R, T, OO>>testObservable(
        observable:O,
        unusedValue: R,
        initialExpected: OO,
        shortDelayAfterUpdate:Boolean = false,
        vararg updates: (O) -> ObservableOptional<R>
    ) = runBlocking {

        val observableOptional by observable

        assertEquals(initialExpected.valueOrNull, observable.currentOrNull)

        if (observable is Initialized<*, *>) {
            assertTrue(initialExpected is Value<*>)
            observableOptional.let {
                println("--> $it")
                assertTrue(it is Value<*>)
                assertEquals(initialExpected.value, it.value)
            }
            assertEquals(initialExpected.value, observable.current)
        }

        if (observable is Uninitialized<*>) {
            assertTrue(observableOptional is Nothing<*>)
        }

        if (observable is DefaultObservable<*, *>) {
            val property by observable.propertyDelegate
            assertTrue(initialExpected is Value<*>)
            assertEquals(initialExpected.value, property)
        }

        val observedValue = AtomicReference<R?>(unusedValue)
        val disposable = observable.observe { observedValue.set(it) }

        var observedInitializedValue:AtomicReference<R>? = null
        var disposableInitialized: Disposable? = null
        if (observable is Initialized<*, *>) {
            observedInitializedValue = AtomicReference(unusedValue)
            disposableInitialized = (observable as Initialized<R, T>).observeInitialized { observedInitializedValue.set(it) }
        }


        when (initialExpected) {
            is Nothing<*> -> assertEquals(null, observedValue.get())
            is Value<*> -> assertEquals(initialExpected.value, observedValue.get())
        }

        if (observedInitializedValue != null) {
            assertTrue(initialExpected is Value<*>)
            assertEquals(initialExpected.value as R, observedInitializedValue.get())
        }

        updates.forEachIndexed { count, update ->

            val lastUpdate = count == updates.size -1

            val observedBefore = observedValue.get()

            if (lastUpdate) {
                disposable.dispose()
                disposableInitialized?.dispose()
            }

            val expected = update(observable)

            if (shortDelayAfterUpdate)
                delay(DELAY_MS)

            assertEquals(expected, observableOptional)
            assertEquals(expected.valueOrNull, observable.currentOrNull)

            if (observable is DefaultObservable<*, *>) {
                assertTrue ( expected is Value<*> )
                val property by observable.propertyDelegate
                assertEquals(expected.value, property)
            }

            if (lastUpdate || expected !is Value<*>) // TODO <-- sus
                assertEquals(observedBefore, observedValue.get())
            else
                assertEquals(expected.value, observedValue.get())

            if (!lastUpdate && observedInitializedValue != null) {
                assertTrue(expected is Value<*>)
                assertEquals(expected.value, observedInitializedValue.get())
            }
            else if (observedInitializedValue != null) {
                assertEquals(observedBefore, observedInitializedValue.get())
            }
        }
    }
}
