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

import com.splendo.kaluga.base.GCScheduler
import com.splendo.kaluga.base.collections.ConcurrentMutableList
import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.collections.concurrentMutableMapOf

actual class SimpleDisposable actual constructor(onDispose: DisposeHandler) : BaseSimpleDisposable(onDispose) {

    override fun afterDispose() {
        GCScheduler.schedule()
    }
}

actual fun <R : T, T, OO : ObservableOptional<R>> addObserver(observation: Observation<R, T, OO>, observer: (R) -> Unit) {
    val observers = observersForObservation.getOrPut(observation) { concurrentMutableListOf() }
    @Suppress("UNCHECKED_CAST")
    observers.add(observer as (Any?) -> Unit)
}

actual fun <R : T, T, OO : ObservableOptional<R>> removeObserver(observation: Observation<R, T, OO>, observer: (R) -> Unit) {
    observersForObservation.synchronized {
        val observers = this[observation] ?: return@synchronized
        observers.remove(observer)
        if (observers.isEmpty())
            remove(observation)
    }
}

actual fun <R : T, T, OO : ObservableOptional<R>> observers(observation: Observation<R, T, OO>): List<(R) -> Unit> {
    return observersForObservation[observation] as? List<(R) -> Unit> ?: emptyList()
}

private val observersForObservation = concurrentMutableMapOf<Observation<*, *, *>, ConcurrentMutableList<(Any?) -> Unit>>()
