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

@file:JvmName("DisposableCommonKt")

package com.splendo.kaluga.architecture.observable

import com.splendo.kaluga.base.collections.ConcurrentMutableList
import com.splendo.kaluga.base.collections.concurrentMutableListOf
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlin.jvm.JvmName

typealias DisposeHandler = () -> Unit

internal fun <R : T, T, OO : ObservableOptional<R>> addObserver(observation: Observation<R, T, OO>, observer: (R) -> Unit) {
    observation.observers.add(observer)
}

internal fun <R : T, T, OO : ObservableOptional<R>> removeObserver(observation: Observation<R, T, OO>, observer: (R) -> Unit) {
    observation.observers.remove(observer)
}

internal fun <R : T, T, OO : ObservableOptional<R>> observers(observation: Observation<R, T, OO>): List<(R) -> Unit> = observation.observers

/**
 * Reference to an object that should be disposed in time
 */
interface Disposable {
    /**
     * Disposes the associated object
     */
    fun dispose()

    /**
     * Adds this disposable to a [DisposeBag]
     */
    fun addTo(disposeBag: DisposeBag)
}

/**
 * Plain [Disposable] to an object that should be disposed in time
 * @param onDispose Function to call when disposing the object
 */
abstract class BaseSimpleDisposable(onDispose: DisposeHandler) : SynchronizedObject(), Disposable {

    private var disposeHandler: DisposeHandler? = onDispose

    /**
     * Disposes the associated object
     */
    override fun dispose() {
        synchronized(this) {
            disposeHandler?.let {
                it.invoke()
                disposeHandler = null
                afterDispose()
            }
        }
    }

    protected open fun afterDispose() {}

    /**
     * Adds this disposable to a [DisposeBag]
     */
    override fun addTo(disposeBag: DisposeBag) {
        disposeBag.add(this)
    }
}

/**
 * A [Disposable] that has a [DisposeHandler]
 * @param onDispose Function to call when disposing the object.
 */
expect class SimpleDisposable(onDispose: DisposeHandler) : BaseSimpleDisposable

/**
 * Container for multiple [Disposable]. Allows nested [DisposeBag].
 */
class DisposeBag : Disposable {

    private val disposables: ConcurrentMutableList<Disposable> = concurrentMutableListOf()
    private val nestedBags: ConcurrentMutableList<DisposeBag> = concurrentMutableListOf()

    /**
     * Adds a nested [DisposeBag]
     */
    fun add(disposeBag: DisposeBag) {
        nestedBags.add(disposeBag)
    }

    /**
     * Adds a [Disposable] to this [DisposeBag]
     */
    fun add(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun addTo(disposeBag: DisposeBag) {
        disposeBag.add(this)
    }

    /**
     * Disposes all [Disposable]s and nested [DisposeBag]s added to this [DisposeBag].
     * Added elements can only be disposed once
     */
    override fun dispose() {
        disposables.synchronized {
            forEach { it.dispose() }
            clear()
        }
        nestedBags.synchronized {
            forEach { it.dispose() }
            clear()
        }
    }
}
