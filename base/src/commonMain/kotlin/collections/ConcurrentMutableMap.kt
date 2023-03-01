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
 * A [MutableMap] that ensures all calls to is happen in a concurrent way.
 * @param K the type of map keys. The map is invariant in its key type.
 * @param V the type of map values. The mutable map is invariant in its value type.
 */
class ConcurrentMutableMap<K, V> internal constructor(private val internal: MutableMap<K, V> = mutableMapOf()) : MutableMap<K, V> {
    private val lock = reentrantLock()

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> get() = synchronized { entries }
    override val keys: MutableSet<K> get() = synchronized { keys }
    override val size: Int get() = synchronized { size }
    override val values: MutableCollection<V> get() = synchronized { values }
    override fun clear() = synchronized { clear() }
    override fun containsKey(key: K): Boolean = synchronized { containsKey(key) }
    override fun containsValue(value: V): Boolean = synchronized { containsValue(value) }
    override fun get(key: K): V? = synchronized { get(key) }
    override fun isEmpty(): Boolean = synchronized { isEmpty() }
    override fun put(key: K, value: V): V? = synchronized { put(key, value) }
    override fun putAll(from: Map<out K, V>) = synchronized { putAll(from) }
    override fun remove(key: K): V? = synchronized { remove(key) }
    override fun equals(other: Any?): Boolean = when (other) {
        is ConcurrentMutableMap<*, *> -> synchronized {
            val inner = this
            other.synchronized {
                inner == this
            }
        }
        is Map<*, *> -> synchronized {
            this == other
        }
        else -> false
    }

    override fun hashCode(): Int = internal.hashCode()
    override fun toString(): String = internal.toString()

    /**
     * Synchronizes an action block on the [MutableMap]
     * @param action The action block to execute concurrently
     * @return The result of the [action] block
     */
    fun <T> synchronized(action: MutableMap<K, V>.() -> T): T = lock.withLock { internal.action() }
}

/**
 * Creates an empty [ConcurrentMutableMap]
 */
fun <K, V> concurrentMutableMapOf() = ConcurrentMutableMap<K, V>(mutableMapOf())

/**
 * Creates a [ConcurrentMutableMap] containing [entries].
 * @param entries The key-value pairs to add to the [ConcurrentMutableMap]
 */
fun <K, V> concurrentMutableMapOf(vararg entries: Pair<K, V>) = ConcurrentMutableMap(mutableMapOf(*entries))
