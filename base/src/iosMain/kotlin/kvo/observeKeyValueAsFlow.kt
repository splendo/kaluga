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

import com.splendo.kaluga.base.flow.SharedFlowCollectionEvent
import com.splendo.kaluga.base.flow.onCollectionEvent
import kotlinx.cinterop.COpaquePointer
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSKeyValueObservingOptions
import platform.Foundation.addObserver
import platform.Foundation.removeObserver
import platform.Foundation.valueForKeyPath
import platform.darwin.NSObject
import kotlin.coroutines.CoroutineContext

@Suppress("UNCHECKED_CAST")
private class KVOObserver<T> : NSObject(), NSObjectObserverProtocol {

    val observedValue = MutableSharedFlow<T>(1)

    override fun observeValueForKeyPath(
        keyPath: String?,
        ofObject: Any?,
        change: Map<Any?, *>?,
        context: COpaquePointer?
    ) {
        val value = (ofObject as NSObject).valueForKeyPath(keyPath!!)
        observedValue.tryEmit(value as T)
    }
}

fun <T> NSObject.observeKeyValueAsFlow(
    keyPath: String,
    options: NSKeyValueObservingOptions = NSKeyValueObservingOptionNew,
    coroutineContext: CoroutineContext
): Flow<T> {
    val observer = KVOObserver<T>()
    CoroutineScope(coroutineContext + CoroutineName("Observing $keyPath")).launch {
        observer.observedValue.onCollectionEvent { event ->
            when (event) {
                SharedFlowCollectionEvent.NoMoreCollections -> removeObserver(observer, keyPath)
                SharedFlowCollectionEvent.FirstCollection -> addObserver(observer, keyPath, options, null)
                SharedFlowCollectionEvent.LaterCollections -> {}
            }
        }
    }
    return observer.observedValue.asSharedFlow()
}
