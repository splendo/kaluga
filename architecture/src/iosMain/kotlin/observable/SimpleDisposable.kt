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

import com.splendo.kaluga.base.GCScheduler

actual class SimpleDisposable actual constructor(onDispose: DisposeHandler) : BaseSimpleDisposable(onDispose) {

    override fun afterDispose() {
        GCScheduler.schedule()
    }
}

actual fun <R:T, T, OO:ObservableOptional<R>>  addObserver(observation:Observation<R,T,OO>, observer:(R)->Unit) {
    val observers = observersForObservation.getOrPut(observation) { mutableListOf()}
    observers.add(observer as (Any?) -> Unit)
}

actual fun <R:T, T, OO:ObservableOptional<R>>  removeObserver(observation:Observation<R,T,OO>, observer:(R)->Unit) {
    val observers = observersForObservation[observation] ?: return
    observers.remove(observer)
    if (observers.isEmpty())
        observersForObservation.remove(observation)
}

actual fun <R:T, T, OO:ObservableOptional<R>> observers(observation:Observation<R,T,OO>): List<(R) -> Unit> {
    return observersForObservation[observation] as? List<(R) -> Unit> ?: emptyList()
}

// Use this to have a thread local reference on iOS that does not get frozen
// TODO: this can be further improved by using a WeakRef
@ThreadLocal
private val observersForObservation = mutableMapOf<Observation<*,*,*>, MutableList<(Any?)->Unit>>()
