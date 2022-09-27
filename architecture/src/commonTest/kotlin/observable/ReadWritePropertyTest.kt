/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.test.Test

// We use the Unconfined dispatcher for observables to keep testing on the same thread.
// This indirectly also tests using an alternate dispatcher
class ReadWritePropertyTest : ObservableBaseTest() {

    val nullableReadWritePropertyValue = MutableStateFlow<String?>(null)

    private val nullableReadWriteProperty = object : ReadWriteProperty<Any?, String?> {
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            nullableReadWritePropertyValue.value = value
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): String? = nullableReadWritePropertyValue.value
    }

    val readWritePropertyValue = MutableStateFlow("initial")

    private val readWriteProperty = object : ReadWriteProperty<Any?, String> {
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            readWritePropertyValue.value = value
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): String = readWritePropertyValue.value
    }

    @Test
    fun testReadWritePropertyDefaultObservable() = testReadWritePropertyDefaultObservableWithInitialValue("something", true)
    @Test
    fun testReadWritePropertyDefaultObservableWithInitialNull() = testReadWritePropertyDefaultObservableWithInitialValue(null, false)

    private fun testReadWritePropertyDefaultObservableWithInitialValue(initialValue: String?, useSuspendableSetter: Boolean) = runBlocking {

        nullableReadWritePropertyValue.value = initialValue

        val subject = nullableReadWriteProperty.toDefaultSubject(
            defaultValue = "default",
            context = Dispatchers.Unconfined
        )

        testStringDefaultSubject(
            subject,
            initialExpected = initialValue ?: "default",
            shortDelayAfterUpdate = false,
            useSuspendableSetter = useSuspendableSetter,
            "new" to "new",
            null to "default",
            "newer" to "newer"
        )
    }

    @Test
    fun testReadWritePropertyObservable() = runBlocking {

        val subject = readWriteProperty.toInitializedSubject(context = Dispatchers.Unconfined)

        testStringSubject(
            subject,
            initialExpected = "initial",
            shortDelayAfterUpdate = false,
            useSuspendableSetter = false,
            "new" to "new",
            "other" to "other"
        )
    }

    @Test
    fun testReadWriteNullablePropertyObservableWithInitialValue() = runBlocking {

        nullableReadWritePropertyValue.value = "initial"

        testStringSubject(
            subject = nullableReadWriteProperty.toInitializedSubject(context = Dispatchers.Unconfined),
            initialExpected = "initial",
            shortDelayAfterUpdate = false,
            useSuspendableSetter = false,
            "new" to "new",
            null to null,
            "other" to "other"
        )
    }

    @Test
    fun testReadWriteNullablePropertyObservable() = runBlocking {
        testStringSubject(
            subject = nullableReadWriteProperty.toInitializedSubject(context = Dispatchers.Unconfined),
            initialExpected = null,
            shortDelayAfterUpdate = false,
            useSuspendableSetter = false,
            "new" to "new",
            null to null,
            "other" to "other"
        )
    }
}
