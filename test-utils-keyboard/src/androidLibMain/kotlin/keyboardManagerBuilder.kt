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

package com.splendo.kaluga.test.keyboard

import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.keyboard.ViewKeyboardManager
import com.splendo.kaluga.test.architecture.getOrPutAndRemoveOnDestroyFromCache
import com.splendo.kaluga.test.architecture.lifecycleManagerObserver

/**
 * @return A [KeyboardManager.Builder] which can be used to manipulate the soft keyboard while this Activity is active.
 * Will be created if need but only one instance will exist.
 *
 * Warning: Do not attempt to use this builder outside of the lifespan of the Activity.
 * Instead, for example use a [com.splendo.kaluga.architecture.viewmodel.LifecycleViewModel],
 * which can automatically track which Activity is active for it.
 *
 */
fun AppCompatActivity.keyboardManagerBuilder(): ViewKeyboardManager.Builder = getOrPutAndRemoveOnDestroyFromCache {
    ViewKeyboardManager.Builder(lifecycleManagerObserver())
}
