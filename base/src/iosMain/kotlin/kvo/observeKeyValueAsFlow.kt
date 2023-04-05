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

package com.splendo.kaluga.base.kvo

import kotlinx.cinterop.COpaquePointer
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSKeyValueObservingOptions
import platform.Foundation.addObserver
import platform.Foundation.removeObserver
import platform.Foundation.valueForKeyPath
import platform.darwin.NSObject

@Suppress("UNCHECKED_CAST")
private class KVOObserver<T>(nsObject: NSObject, keyPath: String, options: NSKeyValueObservingOptions = NSKeyValueObservingOptionNew) : NSObject(), NSObjectObserverProtocol {

    private var isAdded: Boolean = false
    private val mutex = Mutex()
    private val _observedValue = MutableSharedFlow<T>(1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val observedValue = _observedValue.asSharedFlow().onSubscription {
        mutex.withLock {
            if (_observedValue.subscriptionCount.value > 0 && !isAdded) {
                nsObject.addObserver(this@KVOObserver, keyPath, options, null)
                isAdded = true
            }
        }
    }.onCompletion {
        mutex.withLock {
            if (_observedValue.subscriptionCount.value == 0 && isAdded) {
                nsObject.removeObserver(this@KVOObserver, keyPath)
                isAdded = false
            }
        }
    }

    override fun observeValueForKeyPath(
        keyPath: String?,
        ofObject: Any?,
        change: Map<Any?, *>?,
        context: COpaquePointer?
    ) {
        val value = (ofObject as NSObject).valueForKeyPath(keyPath!!)
        _observedValue.tryEmit(value as T)
    }
}

fun <T> NSObject.observeKeyValueAsFlow(
    keyPath: String,
    options: NSKeyValueObservingOptions = NSKeyValueObservingOptionNew
): Flow<T> {
    val observer = KVOObserver<T>(this, keyPath, options)
    return observer.observedValue
}
