package com.splendo.kaluga.hud

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

/**
 * Interface that defines loading indicator class, which can be shown or dismissed
 */
interface HUD {

    /**
     * Style of the Loading Indicator
     */
    enum class Style(val value: Int) {
        /** System appearance */
        SYSTEM(0),
        /** Custom appearance */
        CUSTOM(1);

        companion object {
            fun valueOf(value: Int) = values().first { it.value == value }
        }
    }

    /**
     * Interface used to build loading indicator
     */
    interface Builder {

        /** The style of the loading indicator */
        var style: Style

        /** Sets the style for the loading indicator */
        fun setStyle(style: Style) = apply { this.style = style }

        /** The title of the loading indicator */
        var title: String?

        /** Set the title for the loading indicator */
        fun setTitle(title: String?) = apply { this.title = title }

        /** Returns built loading indicator */
        fun build(initialize: Builder.() -> Unit = { }): HUD {
            clear()
            initialize()
            return create()
        }

        /** Returns created loading indicator */
        fun create(): HUD

        private fun clear() {
            setStyle(HUD.Style.SYSTEM)
            setTitle(null)
        }
    }

    /**
     * Returns true if indicator is visible
     */
    val isVisible: Boolean

    /**
     * Presents as indicator
     *
     * @param animated Pass `true` to animate the presentation
     * @param completion The block to execute after the presentation finishes
     */
    fun present(animated: Boolean = true, completion: () -> Unit = {}): HUD

    /**
     * Dismisses the indicator
     *
     * @param animated Pass `true` to animate the transition
     * @param completion The block to execute after the presentation finishes
     */
    fun dismiss(animated: Boolean = true, completion: () -> Unit = {})

    /**
     * Dismisses the indicator after [timeMillis] milliseconds
     * @param timeMillis The number of milliseconds to wait
     */
    fun dismissAfter(timeMillis: Long, animated: Boolean = true)
}
