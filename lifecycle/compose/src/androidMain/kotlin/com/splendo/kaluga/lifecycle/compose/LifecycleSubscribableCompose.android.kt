/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.lifecycle.compose

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.splendo.kaluga.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.lifecycle.LifecycleSubscribable

@Composable
actual fun LifecycleSubscribable.AttachToCompose() {
    val subscribable = this as? ActivityLifecycleSubscribable ?: return
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context.findActivity()
    val fragmentManager = (activity as? FragmentActivity)?.supportFragmentManager
    DisposableEffect(subscribable, lifecycleOwner, fragmentManager) {
        if (fragmentManager != null) {
            subscribable.subscribe(
                ActivityLifecycleSubscribable.LifecycleManager(
                    activity = activity,
                    lifecycleOwner = lifecycleOwner,
                    fragmentManager = fragmentManager,
                ),
            )
        }
        onDispose { subscribable.unsubscribe() }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
