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

package com.splendo.kaluga.architecture.lifecycle

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * A [LifecycleSubscribable] bound to an [ActivityLifecycleSubscribable.LifecycleManager]
 */
interface ActivityLifecycleSubscribable : LifecycleSubscribable {

    /**
     * A reflection of the manager of a lifecycle.
     * @param activity The [Activity] managing the lifecycle if available.
     * @param lifecycleOwner The [LifecycleOwner] owning the lifecycle.
     * @param fragmentManager The [FragmentManager] attached to the lifecycle.
     * @param childFragmentManager Optional [FragmentManager] that hosts [androidx.fragment.app.Fragment] with the Fragment owning the lifecycle.
     */
    data class LifecycleManager(
        val activity: Activity?,
        val lifecycleOwner: LifecycleOwner,
        val fragmentManager: FragmentManager,
        val childFragmentManager: FragmentManager? = null,
    )

    /**
     * The current [LifecycleManager] subscribed to this [LifecycleSubscribable]
     */
    val manager: LifecycleManager?

    /**
     * Subscribes a [LifecycleManager] to this [LifecycleSubscribable].
     * Called on [Lifecycle.Event.ON_CREATE]
     * @param manager The [LifecycleManager] managing the lifecycle.
     */
    fun subscribe(manager: LifecycleManager)

    /**
     * Unsubscribes the current [LifecycleManager] from this [LifecycleSubscribable].
     * Called on [Lifecycle.Event.ON_DESTROY]
     */
    fun unsubscribe()
}

/**
 * Default implementation of [ActivityLifecycleSubscribable]
 */
open class DefaultActivityLifecycleSubscribable : ActivityLifecycleSubscribable {

    override var manager: ActivityLifecycleSubscribable.LifecycleManager? = null

    override fun subscribe(manager: ActivityLifecycleSubscribable.LifecycleManager) {
        this.manager = manager
    }

    override fun unsubscribe() {
        this.manager = null
    }
}

/**
 * Convenience method to subscribe an [AppCompatActivity] to this [ActivityLifecycleSubscribable] using its default [LifecycleOwner] and [FragmentManager].
 */
fun ActivityLifecycleSubscribable.subscribe(activity: AppCompatActivity) = subscribe(activity, activity, activity.supportFragmentManager)

/**
 * Convenience method to subscribe a [Fragment] to this [ActivityLifecycleSubscribable] using its default [LifecycleOwner] and [FragmentManager]s.
 */
fun ActivityLifecycleSubscribable.subscribe(fragment: Fragment) = subscribe(
    fragment.activity,
    fragment.viewLifecycleOwner,
    fragment.parentFragmentManager,
    fragment.childFragmentManager,
)

/**
 * Subscribes a [LifecycleOwner] to this [ActivityLifecycleSubscribable].
 * @param activity The [Activity] associated with the lifecycle or `null` if none exist.
 * @param owner The [LifecycleOwner] owning the lifecycle.
 * @param fragmentManager The [FragmentManager] attached to the lifecycle.
 */
fun ActivityLifecycleSubscribable.subscribe(activity: Activity?, owner: LifecycleOwner, fragmentManager: FragmentManager) =
    subscribe(ActivityLifecycleSubscribable.LifecycleManager(activity, owner, fragmentManager))

/**
 * Subscribes a [LifecycleOwner] to this [ActivityLifecycleSubscribable].
 * @param activity The [Activity] associated with the lifecycle or `null` if none exist.
 * @param owner The [LifecycleOwner] owning the lifecycle.
 * @param parentFragmentManager The [FragmentManager] attached to the lifecycle.
 * @param childFragmentManager Optional [FragmentManager] that hosts [androidx.fragment.app.Fragment] with the Fragment owning the lifecycle.
 */
fun ActivityLifecycleSubscribable.subscribe(activity: Activity?, owner: LifecycleOwner, parentFragmentManager: FragmentManager, childFragmentManager: FragmentManager) =
    subscribe(ActivityLifecycleSubscribable.LifecycleManager(activity, owner, parentFragmentManager, childFragmentManager))
