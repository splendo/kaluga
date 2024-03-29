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

/**
 * A [MutableSet] that ensures all calls to is happen in a concurrent way.
 * @param E the type of elements contained in the set. The mutable set is invariant in its element type.
 */
class ConcurrentMutableSet<E> internal constructor(private val internal: MutableSet<E> = mutableSetOf()) : MutableSet<E> {
    private val lock = reentrantLock()

    override val size: Int get() = synchronized { size }
    override fun add(element: E): Boolean = synchronized { add(element) }
    override fun addAll(elements: Collection<E>): Boolean = synchronized { addAll(elements) }
    override fun contains(element: E): Boolean = synchronized { contains(element) }
    override fun containsAll(elements: Collection<E>): Boolean = synchronized { containsAll(elements) }
    override fun iterator(): ConcurrentMutableIterator<E> = synchronized { ConcurrentMutableIterator(iterator()) }
    override fun remove(element: E): Boolean = synchronized { remove(element) }
    override fun removeAll(elements: Collection<E>): Boolean = synchronized { removeAll(elements.toSet()) }
    override fun retainAll(elements: Collection<E>): Boolean = synchronized { retainAll(elements.toSet()) }
    override fun clear() = synchronized { clear() }
    override fun isEmpty(): Boolean = synchronized { isEmpty() }
    override fun equals(other: Any?): Boolean = when (other) {
        is ConcurrentMutableSet<*> -> {
            synchronized {
                val inner = this
                other.synchronized {
                    inner == this
                }
            }
        }
        is Set<*> -> synchronized {
            this == other
        }
        else -> false
    }

    override fun hashCode(): Int = internal.hashCode()
    override fun toString(): String = internal.toString()

    /**
     * Synchronizes an action block on the [MutableSet]
     * @param action The action block to execute concurrently
     * @return The result of the [action] block
     */
    fun <T> synchronized(action: MutableSet<E>.() -> T): T = lock.withLock { internal.action() }
}

/**
 * Creates an empty [ConcurrentMutableSet]
 */
fun <E> concurrentMutableSetOf() = ConcurrentMutableSet<E>(mutableSetOf())

/**
 * Creates a [ConcurrentMutableSet] containing [elements].
 * @param elements The elements to add to the [ConcurrentMutableList]
 */
fun <E> concurrentMutableSetOf(vararg elements: E) = ConcurrentMutableSet(mutableSetOf(*elements))
