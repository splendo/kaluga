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

package com.splendo.kaluga.media

import android.view.SurfaceHolder
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

/**
 * A [MediaSurfaceProvider] that provides a [SurfaceHolder] from a given [Activity]
 * @param Activity the type of [android.app.Activity] that provides the [SurfaceHolder]
 * @param activityClass the [KClass] of [Activity]
 * @param activitySurfaceHolder method for getting a [SurfaceHolder] from an instance of [Activity]
 * @param observer the [LifecycleManagerObserver] to observe the lifecycle
 */
class ActivityMediaSurfaceProvider<Activity : android.app.Activity>(
    private val activityClass: KClass<Activity>,
    private val activitySurfaceHolder: Activity.() -> SurfaceHolder?,
    private val observer: LifecycleManagerObserver = LifecycleManagerObserver(),
) : MediaSurfaceProvider, ActivityLifecycleSubscribable by observer {

    override val surface: Flow<MediaSurface?> = observer.managerState.flatMapLatest { manager ->
        activityClass.safeCast(manager?.activity)?.let { activity ->
            activity.activitySurfaceHolder()?.flowMediaSurface()
        } ?: flowOf(null)
    }
}

private fun SurfaceHolder.flowMediaSurface(): Flow<MediaSurface?> {
    val mutex = Mutex()
    var isAdded = false
    val state = MutableStateFlow(if (this.isCreating || !this.surface.isValid) null else MediaSurface(this))
    val callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            state.value = MediaSurface(holder)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            state.value = null
        }
    }

    return state.asStateFlow().onSubscription {
        mutex.withLock {
            if (state.subscriptionCount.value > 0 && !isAdded) {
                addCallback(callback)
                isAdded = true
            }
        }
    }.onCompletion {
        mutex.withLock {
            if (state.subscriptionCount.value == 0 && isAdded) {
                removeCallback(callback)
                isAdded = false
            }
        }
    }
}

/**
 * Creates an [ActivityMediaSurfaceProvider]
 * @param Activity the type of [android.app.Activity] that provides the [SurfaceHolder]
 * @param activitySurfaceHolder method for getting a [SurfaceHolder] from an instance of [Activity]
 */
inline fun <reified Activity : android.app.Activity> ActivityMediaSurfaceProvider(noinline activitySurfaceHolder: Activity.() -> SurfaceHolder?) =
    ActivityMediaSurfaceProvider(Activity::class, activitySurfaceHolder)
