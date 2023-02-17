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
 * A [MutableList] that ensures all calls to is happen in a concurrent way.
 * @param E the type of elements contained in the list. The mutable list is invariant in its element type.
 */
class ConcurrentMutableList<E> internal constructor(private val internal: MutableList<E> = mutableListOf()) : MutableList<E> {
    private val lock = reentrantLock()

    override val size: Int get() = synchronized { size }
    override fun add(element: E): Boolean = synchronized { add(element) }
    override fun add(index: Int, element: E) = synchronized { add(index, element) }
    override fun addAll(elements: Collection<E>): Boolean = synchronized { addAll(elements) }
    override fun addAll(index: Int, elements: Collection<E>): Boolean = synchronized { addAll(index, elements) }
    override fun get(index: Int): E = synchronized { get(index) }
    override fun set(index: Int, element: E): E = synchronized { set(index, element) }
    override fun indexOf(element: E): Int = synchronized { indexOf(element) }
    override fun lastIndexOf(element: E): Int = synchronized { lastIndexOf(element) }
    override fun contains(element: E): Boolean = synchronized { contains(element) }
    override fun containsAll(elements: Collection<E>): Boolean = synchronized { containsAll(elements) }
    override fun iterator(): ConcurrentMutableIterator<E> = synchronized { ConcurrentMutableIterator(iterator()) }
    override fun listIterator(): ConcurrentMutableListIterator<E> = synchronized { ConcurrentMutableListIterator(listIterator()) }
    override fun listIterator(index: Int): ConcurrentMutableListIterator<E> = synchronized { ConcurrentMutableListIterator(listIterator(index)) }
    override fun remove(element: E): Boolean = synchronized { remove(element) }
    override fun removeAt(index: Int): E = synchronized { removeAt(index) }
    override fun removeAll(elements: Collection<E>): Boolean = synchronized { removeAll(elements.toSet()) }
    override fun retainAll(elements: Collection<E>): Boolean = synchronized { retainAll(elements.toSet()) }
    override fun clear() = synchronized { clear() }
    override fun subList(fromIndex: Int, toIndex: Int): ConcurrentMutableList<E> = synchronized { ConcurrentMutableList(subList(fromIndex, toIndex)) }
    override fun isEmpty(): Boolean = synchronized { isEmpty() }
    override fun equals(other: Any?): Boolean = when (other) {
        is ConcurrentMutableList<*> -> {
            synchronized {
                val inner = this
                other.synchronized {
                    inner == this
                }
            }
        }
        is List<*> -> synchronized {
            this == other
        }
        else -> false
    }

    override fun hashCode(): Int = internal.hashCode()
    override fun toString(): String = internal.toString()

    /**
     * Synchronizes an action block on the [MutableList]
     * @param action The action block to execute concurrently
     * @return The result of the [action] block
     */
    fun <T> synchronized(action: MutableList<E>.() -> T): T = lock.withLock { internal.action() }
}

/**
 * Creates an empty [ConcurrentMutableList]
 */
fun <E> concurrentMutableListOf() = ConcurrentMutableList<E>(mutableListOf())

/**
 * Creates a [ConcurrentMutableList] containing [elements].
 * @param elements The elements to add to the [ConcurrentMutableList]
 */
fun <E> concurrentMutableListOf(vararg elements: E) = ConcurrentMutableList(mutableListOf(*elements))
