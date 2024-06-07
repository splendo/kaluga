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

package com.splendo.kaluga.hud

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Style of the Loading Indicator
 */
enum class HUDStyle {
    /** System appearance */
    SYSTEM,

    /** Custom appearance */
    CUSTOM,
}

/**
 * Class showing a loading indicator HUD.
 * @param coroutineScope The [CoroutineScope] managing the HUD lifecycle
 */
abstract class BaseHUD(coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {

    /**
     * Builder class for creating a [BaseHUD]
     */
    abstract class Builder : LifecycleSubscribable {

        /** */
        /**
         * Builds a [BaseHUD] based on some [HudConfig].
         *
         * @param hudConfig The [HudConfig] used for configuring the HUD style.
         * @param coroutineScope The [CoroutineScope] managing the HUD lifecycle.
         * @return The [BaseHUD] to display.
         */
        abstract fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope): BaseHUD
    }

    /**
     * The [HudConfig] of the HUD being presented.
     */
    abstract val hudConfig: HudConfig

    /**
     * Returns true if indicator is visible
     */
    abstract val isVisible: Boolean

    /**
     * Presents as indicator
     *
     * @param animated Pass `true` to animate the presentation
     * @return The [BaseHUD] being presented.
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

        /**
         * Creates a [HUD] based on [hudConfig].
         * @param hudConfig The [HudConfig] to apply to the [HUD].
         * @param coroutineScope The [CoroutineScope] managing the lifecycle of the HUD.
         * @return the created [HUD]
         */
        override fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope): HUD
    }

    override val isVisible: Boolean
    override val hudConfig: HudConfig

    override suspend fun present(animated: Boolean): HUD
    override suspend fun dismiss(animated: Boolean)
}

/**
 * The title of the HUD.
 */
val BaseHUD.title: String? get() = hudConfig.title

/**
 * The [HUDStyle] of the HUD.
 */
val BaseHUD.style: HUDStyle get() = hudConfig.style

/**
 * Dismisses the indicator after [duration]
 * @param duration The [Duration] to wait
 * @param animated Pass `true` to animate the transition
 */
fun BaseHUD.dismissAfter(duration: Duration, animated: Boolean = true): BaseHUD = apply {
    launch(Dispatchers.Main) {
        delay(duration)
        dismiss(animated)
    }
}

/**
 * Dismisses the indicator after [timeMillis] milliseconds
 * @param timeMillis The number of milliseconds to wait
 * @param animated Pass `true` to animate the transition
 */
fun BaseHUD.dismissAfter(timeMillis: Long, animated: Boolean = true): BaseHUD = dismissAfter(timeMillis.milliseconds, animated)

/**
 * Presents and keep presenting the indicator during block execution,
 * hides view after block finished.
 * @param animated Pass `true` to animate the transition
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
fun BaseHUD.Builder.build(coroutineScope: CoroutineScope, initialize: HudConfig.Builder.() -> Unit = { }): BaseHUD = create(
    HudConfig.Builder().apply {
        initialize()
    }.build(),
    coroutineScope,
)
