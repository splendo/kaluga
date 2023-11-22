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

package com.splendo.kaluga.resources.stylable

import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.KalugaColor
import com.splendo.kaluga.resources.KalugaFont

/**
 * The style to apply to a button
 * @property font the [KalugaFont] of the button text
 * @property textSize the size of the button text in `points`
 * @property textAlignment the [KalugaTextAlignment] of the text of the button
 * @property defaultStyle the [ButtonStateStyle] when the button is not in a special state
 * @property pressedStyle the [ButtonStateStyle] when the button is pressed
 * @property disabledStyle the [ButtonStateStyle] when the button is disabled
 */
data class KalugaButtonStyle(
    val font: KalugaFont,
    val textSize: Float,
    val textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER,
    val defaultStyle: ButtonStateStyle,
    val pressedStyle: ButtonStateStyle = defaultStyle,
    val disabledStyle: ButtonStateStyle = defaultStyle,
) {

    /**
     * Constructor
     * @param textStyle the [KalugaTextStyle] of the button text
     * @param textAlignment the [KalugaTextAlignment] of the text of the button
     * @param backgroundColor the [KalugaColor] of the background of the button when not in a special state
     * @param pressedBackgroundColor the [KalugaColor] of the background of the button when pressed
     * @param disabledBackgroundColor the [KalugaColor] of the background of the button when disabled
     * @param shape the [KalugaBackgroundStyle.Shape] of the background of the button
     */
    constructor(
        textStyle: KalugaTextStyle,
        textAlignment: KalugaTextAlignment = KalugaTextAlignment.CENTER,
        backgroundColor: KalugaColor = DefaultColors.clear,
        pressedBackgroundColor: KalugaColor = backgroundColor,
        disabledBackgroundColor: KalugaColor = backgroundColor,
        shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle(),
    ) : this(
        textStyle.font,
        textStyle.size,
        textAlignment,
        ButtonStateStyle(
            textStyle.color,
            backgroundColor,
            shape,
        ),
        ButtonStateStyle(
            textStyle.color,
            pressedBackgroundColor,
            shape,
        ),
        ButtonStateStyle(
            textStyle.color,
            disabledBackgroundColor,
            shape,
        ),
    )

    /**
     * Gets the [KalugaTextStyle] of the button depending on the state
     * @param isEnabled if `true` the button is enabled
     * @param isPressed if `true` the button is pressed
     * @return the [KalugaTextStyle] to apply to the text of the button in the current state
     */
    fun getStateTextStyle(isEnabled: Boolean, isPressed: Boolean) = KalugaTextStyle(font, getStateStyle(isEnabled, isPressed).textColor, textSize, textAlignment)

    /**
     * Gets the [ButtonStateStyle] of the button depending on the state
     * @param isEnabled if `true` the button is enabled
     * @param isPressed if `true` the button is pressed
     * @return the [ButtonStateStyle] to apply to the text of the button in the current state
     */
    fun getStateStyle(isEnabled: Boolean, isPressed: Boolean): ButtonStateStyle {
        return if (!isEnabled) {
            disabledStyle
        } else if (isPressed) {
            pressedStyle
        } else {
            defaultStyle
        }
    }
}

/**
 * Style of a button for its current state
 * @property textColor the [KalugaColor] to apply to the text of the button in this state
 * @property backgroundStyle the [KalugaBackgroundStyle] to apply to the background of the button in this state
 */
data class ButtonStateStyle(
    val textColor: KalugaColor,
    val backgroundStyle: KalugaBackgroundStyle,
) {

    /**
     * Constructor
     * @param textColor the [KalugaColor] to apply to the text of the button in this state
     * @param backgroundColor the [KalugaColor] to apply to the background of the button in this state
     * @param shape the [KalugaBackgroundStyle.Shape] to apply to the background of the button in this state
     */
    constructor(textColor: KalugaColor, backgroundColor: KalugaColor = DefaultColors.clear, shape: KalugaBackgroundStyle.Shape = KalugaBackgroundStyle.Shape.Rectangle()) : this(
        textColor,
        KalugaBackgroundStyle(
            KalugaBackgroundStyle.FillStyle.Solid(backgroundColor),
            shape = shape,
        ),
    )
}
