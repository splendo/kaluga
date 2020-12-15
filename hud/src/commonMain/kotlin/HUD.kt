/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.hud

import co.touchlab.stately.concurrency.Lock
import co.touchlab.stately.concurrency.withLock
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Style of the Loading Indicator
 */
enum class HUDStyle {
    /** System appearance */
    SYSTEM,
    /** Custom appearance */
    CUSTOM
}

/**
 * Class showing a loading indicator HUD.
 */
abstract class BaseHUD(coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {

    /**
     * Builder class for creating a [BaseHUD]
     */
    abstract class Builder : LifecycleSubscribable {

        internal val lock = Lock()

        /** The style of the loading indicator */
        internal var style: HUDStyle = HUDStyle.SYSTEM

        /** Sets the style for the loading indicator */
        fun setStyle(style: HUDStyle) = apply { this.style = style }

        /** The title of the loading indicator */
        internal var title: String? = null

        /** Set the title for the loading indicator */
        fun setTitle(title: String?) = apply { this.title = title }

        /** Sets default style and empty title */
        internal fun clear() {
            setStyle(HUDStyle.SYSTEM)
            setTitle(null)
        }

        /** */
        /**
         * Builds a [BaseHUD] based on some [HudConfig].
         *
         * @param hudConfig The [HudConfig] used for configuring the HUD style.
         * @param coroutineScope The [CoroutineScope] managing the HUD lifecycle.
         * @return The [BaseHUD] to diplay.
         */
        abstract fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope): BaseHUD
    }

    abstract val hudConfig: HudConfig

    /**
     * Returns true if indicator is visible
     */
    abstract val isVisible: Boolean

    /**
     * Presents as indicator
     *
     * @param animated Pass `true` to animate the presentation
     */
    abstract suspend fun present(animated: Boolean = true): BaseHUD

    /**
     * Dismisses the indicator
     *
     * @param animated Pass `true` to animate the transition
     */
    abstract suspend fun dismiss(animated: Boolean = true)
}

/**
 * Default [BaseHUD] implementation.
 */
expect class HUD : BaseHUD {

    /**
     * Builder class for creating a [HUD]
     */
    class Builder : BaseHUD.Builder {
        override fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope): HUD
    }
}

val BaseHUD.title: String? get() = hudConfig.title
val BaseHUD.style: HUDStyle get() = hudConfig.style

/**
 * Dismisses the indicator after [timeMillis] milliseconds
 * @param timeMillis The number of milliseconds to wait
 */
fun BaseHUD.dismissAfter(timeMillis: Long, animated: Boolean = true): BaseHUD = apply {
    launch(Dispatchers.Main) {
        delay(timeMillis)
        dismiss(animated)
    }
}

/**
 * Presents and keep presenting the indicator during block execution,
 * hides view after block finished.
 * @param block The block to execute with hud visible
 */
suspend fun <T> BaseHUD.presentDuring(animated: Boolean = true, block: suspend BaseHUD.() -> T): T {
    present(animated)
    return block().also { dismiss(animated) }
}

/**
 * Builds a [HUD] to display a loading indicator
 *
 * @param coroutineScope The [CoroutineScope] managing the HUD lifecycle.
 * @param initialize Method for initializing the [HUD.Builder]
 */
fun BaseHUD.Builder.build(coroutineScope: CoroutineScope, initialize: BaseHUD.Builder.() -> Unit = { }): BaseHUD = lock.withLock {
    clear()
    initialize()
    return create(HudConfig(style, title), coroutineScope)
}
