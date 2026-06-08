/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

@file:Suppress("DEPRECATION")

package com.splendo.kaluga.architecture.lifecycle

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.splendo.kaluga.lifecycle.subscribe as lifecycleSubscribe

/**
 * Moved to `:lifecycle`. The aliases here keep existing imports compiling; new code should
 * import directly from `com.splendo.kaluga.lifecycle`.
 */
@Deprecated(
    message = "Moved to :lifecycle. Import com.splendo.kaluga.lifecycle.ActivityLifecycleSubscribable instead.",
    replaceWith = ReplaceWith(
        "ActivityLifecycleSubscribable",
        "com.splendo.kaluga.lifecycle.ActivityLifecycleSubscribable",
    ),
)
typealias ActivityLifecycleSubscribable = com.splendo.kaluga.lifecycle.ActivityLifecycleSubscribable

@Deprecated(
    message = "Moved to :lifecycle. Import com.splendo.kaluga.lifecycle.DefaultActivityLifecycleSubscribable instead.",
    replaceWith = ReplaceWith(
        "DefaultActivityLifecycleSubscribable",
        "com.splendo.kaluga.lifecycle.DefaultActivityLifecycleSubscribable",
    ),
)
typealias DefaultActivityLifecycleSubscribable = com.splendo.kaluga.lifecycle.DefaultActivityLifecycleSubscribable

@Deprecated(
    message = "Moved to :lifecycle. Import com.splendo.kaluga.lifecycle.subscribe instead.",
    replaceWith = ReplaceWith(
        "subscribe(activity)",
        "com.splendo.kaluga.lifecycle.subscribe",
    ),
)
fun ActivityLifecycleSubscribable.subscribe(activity: AppCompatActivity) = lifecycleSubscribe(activity)

@Deprecated(
    message = "Moved to :lifecycle. Import com.splendo.kaluga.lifecycle.subscribe instead.",
    replaceWith = ReplaceWith(
        "subscribe(fragment)",
        "com.splendo.kaluga.lifecycle.subscribe",
    ),
)
fun ActivityLifecycleSubscribable.subscribe(fragment: Fragment) = lifecycleSubscribe(fragment)

@Deprecated(
    message = "Moved to :lifecycle. Import com.splendo.kaluga.lifecycle.subscribe instead.",
    replaceWith = ReplaceWith(
        "subscribe(activity, owner, fragmentManager)",
        "com.splendo.kaluga.lifecycle.subscribe",
    ),
)
fun ActivityLifecycleSubscribable.subscribe(activity: Activity?, owner: LifecycleOwner, fragmentManager: FragmentManager) = lifecycleSubscribe(activity, owner, fragmentManager)

@Deprecated(
    message = "Moved to :lifecycle. Import com.splendo.kaluga.lifecycle.subscribe instead.",
    replaceWith = ReplaceWith(
        "subscribe(activity, owner, parentFragmentManager, childFragmentManager)",
        "com.splendo.kaluga.lifecycle.subscribe",
    ),
)
fun ActivityLifecycleSubscribable.subscribe(activity: Activity?, owner: LifecycleOwner, parentFragmentManager: FragmentManager, childFragmentManager: FragmentManager) =
    lifecycleSubscribe(activity, owner, parentFragmentManager, childFragmentManager)
