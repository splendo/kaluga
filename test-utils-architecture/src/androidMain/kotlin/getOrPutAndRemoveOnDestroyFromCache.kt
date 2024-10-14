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

package com.splendo.kaluga.test.architecture

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import kotlin.reflect.KClass

/**
 * @suppress
 *
 * Do not use directly, but use through [AppCompatActivity.getOrPutAndRemoveOnDestroyFromCache]
 */
val lifecycleAwareActivityCache = concurrentMutableMapOf<Pair<AppCompatActivity, KClass<*>>, Any>()

/**
 * Lifecycle aware caching on the activity level
 */
@Suppress("UNCHECKED_CAST") // our kclass parameter guards access to the map
inline fun <reified T : Any> AppCompatActivity.getOrPutAndRemoveOnDestroyFromCache(
    crossinline onCreate: (T) -> Unit = {},
    crossinline onDestroy: (T) -> Unit = {},
    defaultValue: () -> T,
): T {
    val key = Pair(this, T::class)
    return lifecycleAwareActivityCache.getOrPut(key) {
        defaultValue().also {
            lifecycle.addObserver(
                object : DefaultLifecycleObserver {
                    override fun onCreate(owner: LifecycleOwner) {
                        onCreate(it)
                    }

                    override fun onDestroy(owner: LifecycleOwner) {
                        lifecycleAwareActivityCache.remove(key)
                        onDestroy(it)
                    }
                },
            )
        }
    } as T
}
