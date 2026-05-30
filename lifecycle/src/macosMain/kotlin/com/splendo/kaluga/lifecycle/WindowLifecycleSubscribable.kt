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

import platform.AppKit.NSWindow

/**
 * A [LifecycleSubscribable] bound to a macOS [NSWindow]. Implementations grab the window off
 * [manager] to find native AppKit views in its content view (e.g. an `AVPlayerView` host for the
 * native-AppKit media path) or to anchor window-modal sheets.
 */
interface WindowLifecycleSubscribable : LifecycleSubscribable {

    data class LifecycleManager(val window: NSWindow)

    /** The currently-subscribed [LifecycleManager]. */
    val manager: LifecycleManager?

    fun subscribe(manager: LifecycleManager)
    fun unsubscribe()
}

/** Default implementation storing the current manager. */
open class DefaultWindowLifecycleSubscribable : WindowLifecycleSubscribable {

    override var manager: WindowLifecycleSubscribable.LifecycleManager? = null

    override fun subscribe(manager: WindowLifecycleSubscribable.LifecycleManager) {
        this.manager = manager
    }

    override fun unsubscribe() {
        this.manager = null
    }
}

/** Subscribe with a raw [NSWindow]. */
fun WindowLifecycleSubscribable.subscribe(window: NSWindow) =
    subscribe(WindowLifecycleSubscribable.LifecycleManager(window))
