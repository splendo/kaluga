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
        is Map<*, *> -> entries == other.entries
        else -> false
    }

    override fun hashCode(): Int = internal.hashCode()
    override fun toString(): String = internal.toString()

    fun <T> synchronized(action: MutableMap<K, V>.() -> T): T = lock.withLock { internal.action() }
}

fun <K, V> concurrentMutableMapOf() = ConcurrentMutableMap<K, V>(mutableMapOf())
fun <K, V> concurrentMutableMapOf(vararg entries: Pair<K, V>) = ConcurrentMutableMap(mutableMapOf(*entries))
