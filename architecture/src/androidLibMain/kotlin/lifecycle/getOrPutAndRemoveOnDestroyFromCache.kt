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

package com.splendo.kaluga.architecture.lifecycle

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.reflect.KClass

/**
 * @suppress
 *
 * Do not use directly, but use through [AppCompatActivity.getOrPutAndRemoveOnDestroyFromCache]
 */
val lifecycleAwareActivityCache = mutableMapOf<Pair<AppCompatActivity, KClass<*>>, Any>()

/**
 * Lifecycle aware caching on the activity level
 */
@Suppress("UNCHECKED_CAST") // our kclass parameter guards access to the map
inline fun <reified T : Any> AppCompatActivity.getOrPutAndRemoveOnDestroyFromCache(
    crossinline onCreate: (T) -> Unit = {},
    crossinline onDestroy: (T) -> Unit = {},
    defaultValue: () -> T
): T {
    val key = Pair(this, T::class)
    return lifecycleAwareActivityCache.getOrPut(key) {
        defaultValue().also {
            lifecycle.addObserver(
                object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                    fun onCreate() =
                        onCreate(it)

                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onDestroy() {
                        lifecycleAwareActivityCache.remove(key)
                        onDestroy(it)
                    }
                }
            )
        }
    } as T
}
