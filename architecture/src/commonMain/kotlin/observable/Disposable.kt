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

@file:JvmName("DisposableCommonKt")
package com.splendo.kaluga.architecture.observable

import co.touchlab.stately.collections.sharedMutableListOf
import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.isFrozen
import kotlin.jvm.JvmName

typealias DisposeHandler = () -> Unit

internal expect fun <R : T, T, OO : ObservableOptional<R>> addObserver(observation: Observation<R, T, OO>, observer: (R) -> Unit)

internal expect fun <R : T, T, OO : ObservableOptional<R>> removeObserver(observation: Observation<R, T, OO>, observer: (R) -> Unit)

internal expect fun <R : T, T, OO : ObservableOptional<R>> observers(observation: Observation<R, T, OO>): List<(R) -> Unit>

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

expect class SimpleDisposable(onDispose: DisposeHandler) : BaseSimpleDisposable

/**
 * Plain [Disposable] to an object that should be disposed in time
 * @param onDispose Function to call when disposing the object
 */
abstract class BaseSimpleDisposable(onDispose: DisposeHandler) : Disposable {

    private val _isDisposed = AtomicBoolean(false)
    val isDisposed
        get() = _isDisposed.value

    private var disposeHandler: DisposeHandler? = onDispose

    /**
     * Disposes the associated object
     */
    override fun dispose() {
        if (_isDisposed.compareAndSet(expected = false, new = true)) {
            disposeHandler?.invoke()
            if (!disposeHandler.isFrozen) disposeHandler = null
            afterDispose()
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
 * Container for multiple [Disposable]. Allows nested [DisposeBag].
 */
class DisposeBag(allowFreezing: Boolean = false) : Disposable {

    constructor() : this(false)

    private val disposables: MutableList<Disposable> = if (allowFreezing) sharedMutableListOf() else mutableListOf()
    private val nestedBags: MutableList<DisposeBag> = if (allowFreezing) sharedMutableListOf() else mutableListOf()

    init {
        if (!allowFreezing)
            ensureNeverFrozen() // if our DisposeBag gets frozen we cannot dispose properly so this is generally unwanted
    }

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
        disposables.forEach { it.dispose() }
        disposables.clear()
        nestedBags.forEach { it.dispose() }
        nestedBags.clear()
    }
}
