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

package com.splendo.kaluga.resources.compose

import android.graphics.Typeface
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.StringStyleAttribute
import com.splendo.kaluga.resources.StyledStringBuilder
import com.splendo.kaluga.resources.stylable.ButtonStateStyle
import com.splendo.kaluga.resources.stylable.KalugaBackgroundStyle
import com.splendo.kaluga.resources.stylable.KalugaButtonStyle
import com.splendo.kaluga.resources.styled
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.resources.view.KalugaLabel

/**
 * Gets a [Button] that looks and behaves according to a [KalugaButton]
 * @param modifier the [Modifier] to be applied to the button
 * @param elevation the [ButtonElevation] used to resolve the elevation for this button in different states.
 * This controls the size of the shadow below the button. Pass `null` here to disable elevation for this button.
 * See [ButtonDefaults.elevation].
 * @param contentPadding The spacing values to apply internally between the container and the content
 */
@Composable
fun KalugaButton.Composable(modifier: Modifier, elevation: ButtonElevation = ButtonDefaults.elevation(), contentPadding: PaddingValues = ButtonDefaults.ContentPadding) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val stateStyle = style.getStateStyle(isEnabled, pressed)
    val textStyle = style.getStateTextStyle(isEnabled, pressed)
    Button(
        modifier = modifier,
        enabled = isEnabled,
        elevation = elevation,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        shape = stateStyle.backgroundStyle.shape.shape,
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        onClick = action,
    ) {
        Box(
            modifier = Modifier
                .backgroundStyle(stateStyle.backgroundStyle)
                .padding(contentPadding)
                .then(modifier),
        ) {
            when (this@Composable) {
                is KalugaButton.Plain -> KalugaLabel.Plain(text, textStyle)
                is KalugaButton.Styled -> KalugaLabel.Styled(text)
                else -> error("unknown button type")
            }.Composable(modifier = modifier)
        }
    }
}

@Composable
@Preview
fun PreviewKalugaButton() {
    val buttonStyle = KalugaButtonStyle(
        font = Typeface.DEFAULT_BOLD,
        textSize = 14.0f,
        defaultStyle = ButtonStateStyle(
            DefaultColors.white,
            DefaultColors.red,
            shape = KalugaBackgroundStyle.Shape.Rectangle(5.0f),
        ),
        pressedStyle = ButtonStateStyle(
            DefaultColors.red,
            DefaultColors.white,
            shape = KalugaBackgroundStyle.Shape.Rectangle(5.0f),
        ),
        disabledStyle = ButtonStateStyle(
            DefaultColors.black,
            DefaultColors.white,
            shape = KalugaBackgroundStyle.Shape.Rectangle(5.0f),
        ),
    )
    Column(modifier = Modifier.size(100.dp)) {
        KalugaButton.Plain("Plain Text", buttonStyle, true) {}.Composable(
            modifier = Modifier.fillMaxWidth(),
        )
        KalugaButton.Styled(
            "Styled Text".styled(
                StyledStringBuilder.Provider(LocalContext.current),
                buttonStyle.getStateTextStyle(isEnabled = true, isPressed = false),
                {
                    StringStyleAttribute.CharacterStyleAttribute.ForegroundColor(
                        DefaultColors.darkBlue,
                    ) to IntRange(0, 5)
                },
            ),
            buttonStyle,
            true,
        ) {}.Composable(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
