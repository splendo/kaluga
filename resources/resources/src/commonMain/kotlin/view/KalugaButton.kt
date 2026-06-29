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

package com.splendo.kaluga.resources.view

import com.splendo.kaluga.resources.StyledString
import com.splendo.kaluga.resources.stylable.KalugaButtonStyle

/**
 * A button a [KalugaButtonStyle] applied
 */
sealed class KalugaButton {

    /**
     * The [KalugaButtonStyle] of the button
     */
    abstract val style: KalugaButtonStyle<*>

    /**
     * If `true` the button can be interacted with
     */
    abstract val isEnabled: Boolean

    /**
     * Function to execute when the button is pressed
     */
    abstract val action: () -> Unit

    /**
     * A [KalugaButton] with a [KalugaButton.WithoutText] [style] that does not display any text
     * @property style the [KalugaButtonStyle.WithoutText] of the button
     * @param isEnabled if `true` the button can be interacted with
     * @param action function to execute when the button is pressed
     */
    data class WithoutText(override val style: KalugaButtonStyle.WithoutText<*>, override val isEnabled: Boolean = true, override val action: () -> Unit) : KalugaButton()

    /**
     * A [KalugaButton] with a [KalugaButton.WithText] [style] that displays text
     * @property style the [KalugaButtonStyle.WithText] of the button
     */
    sealed class WithText : KalugaButton() {
        abstract override val style: KalugaButtonStyle.WithText<*>
    }

    /**
     * A [KalugaButton.WithText] that displays a regular text
     * @property text the text to display on the button
     * @param style the [KalugaButtonStyle.WithText] of the button
     * @param isEnabled if `true` the button can be interacted with
     * @param action function to execute when the button is pressed
     */
    data class Plain(val text: String, override val style: KalugaButtonStyle.WithText<*>, override val isEnabled: Boolean = true, override val action: () -> Unit) : WithText()

    /**
     * A [KalugaButton.WithText] that displays a [StyledString]
     * @property text the [StyledString] to display on the button
     * @param style the [KalugaButtonStyle.WithText] of the button
     * @param isEnabled if `true` the button can be interacted with
     * @param action function to execute when the button is pressed
     */
    data class Styled(val text: StyledString, override val style: KalugaButtonStyle.WithText<*>, override val isEnabled: Boolean = true, override val action: () -> Unit) :
        WithText()
}
