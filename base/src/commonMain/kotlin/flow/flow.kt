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

package com.splendo.kaluga.base.flow

import com.splendo.kaluga.base.flow.SharedFlowCollectionEvent.NoMoreCollections
import com.splendo.kaluga.base.flow.SharedFlowCollectionEvent.FirstCollection
import com.splendo.kaluga.base.flow.SharedFlowCollectionEvent.LaterCollections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.flow.withIndex

enum class SharedFlowCollectionEvent {
    NoMoreCollections, FirstCollection, LaterCollections
}

/**
 * Calls back with events in the following order:
 *
 * [FirstCollection] -> [NoMoreCollections] -> [LaterCollections] -> back to [NoMoreCollections] when all flows are terminated
 *
 * If a flow already exists on this [MutableSharedFlow] the [FirstCollection] event is immediately raised.
 * If no subscriptions exist
 *
 * Note that is this [MutableSharedFlow] was subscribed and unsubscribed before this method was called,
 * a new subscription will still raise the [FirstCollection] event
 */
suspend fun <T> MutableSharedFlow<T>.onCollectionEvent(onEvent: suspend MutableSharedFlow<T>.(SharedFlowCollectionEvent) -> Unit): Unit =
    subscriptionCount
        .map { it > 0 } // simplify to zero or more-than-one
        .dropWhile { !it } // do not report until the first collection
        .distinctUntilChanged() // report as state not an event
        .withIndex() // add an index so we distinguish the first event
        .collect { subscriptionState ->
            onEvent(
                when {
                    subscriptionState.value && subscriptionState.index == 0 -> FirstCollection
                    subscriptionState.value && subscriptionState.index != 0 -> LaterCollections
                    else -> NoMoreCollections
                }
            )
        }

/**
 * These interfaces can be used to mark object you emit to a Flow as a special value.
 *
 * Extension methods to Flow can be used to filter and/or terminate on these values.
 */
sealed class SpecialFlowValue { // TODO: this can be a sealed interface one day

    /**
     * Indicates this value is the last in a series.
     * This is useful in particular to terminimate collection from endless flows such as SharedFlow and StateFlow.
     */
    interface Last

    /**
     * Indicates the value is less meaningful (e.g. an initial state with no value yet) that can usually be skipped
     */
    interface NotImportant
}


fun <T> Flow<T>.takeUntilLast(includeLast: Boolean = true): Flow<T> = transformWhile {
    val notLast = it !is SpecialFlowValue.Last
    if (notLast || includeLast)
        emit(it)
    notLast
}

@Suppress("NOTHING_TO_INLINE") // copies from filter()
inline fun <T> Flow<T>.filterOnlyImportant(): Flow<T> =
    filterNot { it is SpecialFlowValue.NotImportant }

suspend inline fun <T> Flow<T>.collectUntilLast(
    includeLast: Boolean = true,
    crossinline action: suspend (value: T) -> Unit
) =
    takeUntilLast(includeLast).collect(object : FlowCollector<T> {
        override suspend fun emit(value: T) = action(value)
    }
    )

suspend inline fun <T> Flow<T>.collectImportant(crossinline action: suspend (value: T) -> Unit) =
    filterOnlyImportant().collect(object : FlowCollector<T> {
        override suspend fun emit(value: T) = action(value)
    }
    )

suspend inline fun <T> Flow<T>.collectImportantUntilLast(
    includeLast: Boolean = true,
    crossinline action: suspend (value: T) -> Unit
) =
    takeUntilLast(includeLast).filterOnlyImportant().collect(object : FlowCollector<T> {
        override suspend fun emit(value: T) = action(value)
    }
    )
