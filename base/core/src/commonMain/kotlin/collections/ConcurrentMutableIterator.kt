/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.collections

import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

internal open class InternalConcurrentMutableIterator<E, I : MutableIterator<E>> internal constructor(private val internal: I) : MutableIterator<E> {
    private val lock = reentrantLock()

    override fun hasNext(): Boolean = synchronized { hasNext() }
    override fun next(): E = synchronized { next() }
    override fun remove() = synchronized { remove() }

    fun <T> synchronized(action: I.() -> T): T = lock.withLock { internal.action() }
}

/**
 * A [MutableIterator] that ensures all calls to is happen in a concurrent way.
 * @see [MutableCollection.iterator]
 */
class ConcurrentMutableIterator<E> internal constructor(private val internal: InternalConcurrentMutableIterator<E, MutableIterator<E>>) : MutableIterator<E> by internal {
    internal constructor(internal: MutableIterator<E>) : this(InternalConcurrentMutableIterator(internal))

    /**
     * Synchronizes an action block on the [MutableIterator]
     * @param action The action block to execute concurrently
     * @return The result of the [action] block
     */
    fun <T> synchronized(action: MutableIterator<E>.() -> T): T = internal.synchronized(action)
}

/**
 * A [MutableListIterator] that ensures all calls to is happen in a concurrent way.
 */
class ConcurrentMutableListIterator<E> internal constructor(private val internal: InternalConcurrentMutableIterator<E, MutableListIterator<E>>) :
    MutableIterator<E> by internal,
    MutableListIterator<E> {

    internal constructor(internal: MutableListIterator<E>) : this(InternalConcurrentMutableIterator(internal))

    override fun add(element: E) = internal.synchronized { add(element) }
    override fun hasPrevious(): Boolean = internal.synchronized { hasPrevious() }
    override fun nextIndex(): Int = internal.synchronized { nextIndex() }
    override fun previous(): E = internal.synchronized { previous() }
    override fun previousIndex(): Int = internal.synchronized { previousIndex() }
    override fun set(element: E) = internal.synchronized { set(element) }

    /**
     * Synchronizes an action block on the [MutableListIterator]
     * @param action The action block to execute concurrently
     * @return The result of the [action] block
     */
    fun <T> synchronized(action: MutableListIterator<E>.() -> T): T = internal.synchronized(action)
}
