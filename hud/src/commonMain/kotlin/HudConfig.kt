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

data class HudConfig(
    /** Style of the HUD */
    val style: HUDStyle = HUDStyle.SYSTEM,
    /** Optional title of the HUD */
    val title: String? = null
) {

    class Builder {
        /** The style of the loading indicator */
        internal var style: HUDStyle = HUDStyle.SYSTEM

        /** Sets the style for the loading indicator */
        fun setStyle(style: HUDStyle) = apply { this.style = style }

        /** The title of the loading indicator */
        internal var title: String? = null

        /** Set the title for the loading indicator */
        fun setTitle(title: String?) = apply { this.title = title }

        /**
         * Creates a [HudConfig]
         *
         * @return The [HudConfig] created
         */
        fun build(): HudConfig = HudConfig(style, title)
    }
}
