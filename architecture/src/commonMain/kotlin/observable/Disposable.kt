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

import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
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
    suspend fun dispose()

    /**
     * Adds this disposable to a [DisposeBag]
     */
    suspend fun addTo(disposeBag: DisposeBag)
}

expect class SimpleDisposable(onDispose: DisposeHandler) : BaseSimpleDisposable

/**
 * Plain [Disposable] to an object that should be disposed in time
 * @param onDispose Function to call when disposing the object
 */
abstract class BaseSimpleDisposable(onDispose: DisposeHandler) : Disposable {

    private val disposal = Semaphore(1)
    private var disposeHandler: DisposeHandler? = onDispose

    /**
     * Disposes the associated object
     */
    override suspend fun dispose() {
        disposal.withPermit {
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
    override suspend fun addTo(disposeBag: DisposeBag) {
        disposeBag.add(this)
    }
}

/**
 * Container for multiple [Disposable]. Allows nested [DisposeBag].
 */
class DisposeBag : Disposable {

    private val addingOperation = Semaphore(1)
    private val disposables: MutableList<Disposable> = mutableListOf()
    private val nestedBags: MutableList<DisposeBag> = mutableListOf()

    /**
     * Adds a nested [DisposeBag]
     */
    suspend fun add(disposeBag: DisposeBag) {
        addingOperation.withPermit {
            nestedBags.add(disposeBag)
        }
    }

    /**
     * Adds a [Disposable] to this [DisposeBag]
     */
    suspend fun add(disposable: Disposable) {
        addingOperation.withPermit {
            disposables.add(disposable)
        }
    }

    override suspend fun addTo(disposeBag: DisposeBag) {
        disposeBag.add(this)
    }

    /**
     * Disposes all [Disposable]s and nested [DisposeBag]s added to this [DisposeBag].
     * Added elements can only be disposed once
     */
    override suspend fun dispose() {
        disposables.forEach { it.dispose() }
        addingOperation.withPermit {
            disposables.clear()
        }
        nestedBags.forEach { it.dispose() }
        addingOperation.withPermit {
            nestedBags.clear()
        }
    }
}
