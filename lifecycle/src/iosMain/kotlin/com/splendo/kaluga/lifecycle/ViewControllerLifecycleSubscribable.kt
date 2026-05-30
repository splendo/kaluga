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

package com.splendo.kaluga.lifecycle

import platform.UIKit.UIViewController

/**
 * A [LifecycleSubscribable] bound to an iOS [UIViewController]. Implementations grab the
 * controller off [manager] to call presentation APIs (e.g. presenting media, modals, in-app
 * pickers) that need an originating view controller.
 */
interface ViewControllerLifecycleSubscribable : LifecycleSubscribable {

    data class LifecycleManager(val viewController: UIViewController)

    /** The currently-subscribed [LifecycleManager]. */
    val manager: LifecycleManager?

    fun subscribe(manager: LifecycleManager)
    fun unsubscribe()
}

/** Default implementation storing the current manager. */
open class DefaultViewControllerLifecycleSubscribable : ViewControllerLifecycleSubscribable {

    override var manager: ViewControllerLifecycleSubscribable.LifecycleManager? = null

    override fun subscribe(manager: ViewControllerLifecycleSubscribable.LifecycleManager) {
        this.manager = manager
    }

    override fun unsubscribe() {
        this.manager = null
    }
}

/** Subscribe with a raw [UIViewController]. */
fun ViewControllerLifecycleSubscribable.subscribe(viewController: UIViewController) =
    subscribe(ViewControllerLifecycleSubscribable.LifecycleManager(viewController))
