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
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import com.splendo.kaluga.architecture.lifecycle.subscribe

/**
 * @return The [LifecycleManagerObserver] that only reports the lifecycle of this [AppCompatActivity].
 * Will be created if need but only one instance will exist.
 *
 * For example can be used by code in the Activity itself.
 *
 * Warning: do not use this attempt to use this observer in cases where a dependent class such a Builder
 * exists over the lifespan of several Activities (e.g. when recreated due to rotation etc).
 * In particular this applies to places such as a ViewModel, which can outlive a single Activity.
 *
 */
fun AppCompatActivity.lifecycleManagerObserver(): LifecycleManagerObserver = getOrPutAndRemoveOnDestroyFromCache(
    onCreate = { it.subscribe(this@lifecycleManagerObserver) },
    onDestroy = { it.unsubscribe() },
    defaultValue = { LifecycleManagerObserver() },
)
