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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.StringStyleAttribute
import com.splendo.kaluga.resources.StyledStringBuilder
import com.splendo.kaluga.resources.stylable.ButtonImage
import com.splendo.kaluga.resources.stylable.ButtonStateStyle
import com.splendo.kaluga.resources.stylable.ImageGravity
import com.splendo.kaluga.resources.stylable.ImageSize
import com.splendo.kaluga.resources.stylable.KalugaBackgroundStyle
import com.splendo.kaluga.resources.stylable.KalugaButtonStyle
import com.splendo.kaluga.resources.stylable.KalugaTextStyle
import com.splendo.kaluga.resources.styled
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.resources.view.KalugaLabel

/**
 * Gets a [Button] that looks and behaves according to a [KalugaButton]
 * @param modifier the [Modifier] to be applied to the button
 * @param elevation the [ButtonElevation] used to resolve the elevation for this button in different states.
 * This controls the size of the shadow below the button. Pass `null` here to disable elevation for this button.
 * See [ButtonDefaults.elevation].
 */
@Composable
fun KalugaButton.Composable(modifier: Modifier, elevation: ButtonElevation = ButtonDefaults.elevation()) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val stateStyle = style.getStateStyle(isEnabled, pressed)
    val elevationState by elevation.elevation(enabled = isEnabled, interactionSource = interactionSource)

    // Default Button has some issues with transparency & sizes below a minimum size.
    // Using custom implementation instead
    ShadowBox(
        elevation = elevationState,
        modifier = modifier,
        shape = stateStyle.backgroundStyle.shape.shape,
    ) {
        Box(
            modifier = Modifier
                .semantics { role = Role.Button }
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    enabled = isEnabled,
                    onClick = action,
                )
                .backgroundStyle(stateStyle.backgroundStyle)
                .padding(with(style.padding) { PaddingValues(start = start.dp, top = top.dp, end = end.dp, bottom = bottom.dp) })
                .then(modifier),
        ) {
            when (this@Composable) {
                is KalugaButton.WithoutText -> when (val style = style) {
                    is KalugaButtonStyle.WithoutContent -> Spacer(modifier = modifier)
                    is KalugaButtonStyle.ImageOnly -> Box(modifier = modifier) {
                        style.Composable(isEnabled = isEnabled, isPressed = pressed)
                    }
                }

                is KalugaButton.WithText -> {
                    val createLabel: (KalugaTextStyle) -> KalugaLabel = { textStyle ->
                        when (this@Composable) {
                            is KalugaButton.Plain -> KalugaLabel.Plain(text, textStyle)
                            is KalugaButton.Styled -> KalugaLabel.Styled(text, textStyle)
                        }
                    }
                    when (val style = style) {
                        is KalugaButtonStyle.TextOnly -> style.Composable(modifier = modifier, isEnabled = isEnabled, isPressed = pressed, createLabel)
                        is KalugaButtonStyle.WithImageAndText -> style.Composable(modifier = modifier, isEnabled = isEnabled, isPressed = pressed, createLabel)
                    }
                }
            }
        }
    }
}

@Composable
private fun KalugaButtonStyle.WithImageAndText.Composable(modifier: Modifier, isEnabled: Boolean, isPressed: Boolean, createLabel: (KalugaTextStyle) -> KalugaLabel) {
    val elements: @Composable (Modifier) -> List<@Composable () -> Unit> = { weight ->
        listOf(
            { (this as KalugaButtonStyle.WithImage<*>).Composable(isEnabled = isEnabled, isPressed = isPressed) },
            { (this as KalugaButtonStyle.WithText<*>).Composable(modifier = weight, isEnabled = isEnabled, isPressed = isPressed, createLabel) },
        )
    }

    val isRightToLeft = LocalLayoutDirection.current == LayoutDirection.Rtl

    when (imageGravity) {
        ImageGravity.START -> Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(spacing.dp), verticalAlignment = Alignment.CenterVertically) {
            elements(Modifier.weight(1.0f)).let { if (isRightToLeft) it.reversed() else it }.forEach { it() }
        }
        ImageGravity.LEFT -> Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(spacing.dp), verticalAlignment = Alignment.CenterVertically) {
            elements(Modifier.weight(1.0f)).forEach { it() }
        }
        ImageGravity.END -> Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(spacing.dp), verticalAlignment = Alignment.CenterVertically) {
            elements(Modifier.weight(1.0f)).let { if (isRightToLeft) it else it.reversed() }.forEach { it() }
        }
        ImageGravity.RIGHT -> Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(spacing.dp), verticalAlignment = Alignment.CenterVertically) {
            elements(Modifier.weight(1.0f)).reversed().forEach { it() }
        }
        ImageGravity.TOP -> Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(spacing.dp)) {
            elements(Modifier.fillMaxWidth()).forEach { it() }
        }
        ImageGravity.BOTTOM -> Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(spacing.dp)) {
            elements(Modifier.fillMaxWidth()).reversed().forEach { it() }
        }
    }
}

@Composable
private fun KalugaButtonStyle.WithText<*>.Composable(modifier: Modifier, isEnabled: Boolean, isPressed: Boolean, createLabel: (KalugaTextStyle) -> KalugaLabel) {
    createLabel(getStateTextStyle(isEnabled, isPressed)).Composable(modifier = modifier)
}

@Composable
private fun KalugaButtonStyle.WithImage<*>.Composable(isEnabled: Boolean, isPressed: Boolean) {
    val modifier = when (val size = imageSize) {
        is ImageSize.Sized -> Modifier.size(size.width.dp, size.height.dp)
        is ImageSize.Intrinsic -> Modifier
    }
    when (val image = getStateImage(isEnabled, isPressed)) {
        is ButtonImage.Hidden -> Spacer(modifier = modifier)
        is ButtonImage.Image -> image.image.Composable(contentDescription = null, modifier = modifier)
        is ButtonImage.Tinted -> image.image.Composable(contentDescription = null, modifier = modifier)
    }
}

@Composable
@Preview
fun PreviewKalugaButton() {
    val buttonStyle = KalugaButtonStyle.textOnly {
        font = Typeface.DEFAULT_BOLD
        textSize = 14.0f
        defaultStyle = ButtonStateStyle.textOnly {
            textColor = DefaultColors.white
            setBackgroundStyle(DefaultColors.red, KalugaBackgroundStyle.Shape.Rectangle(5.0f))
        }
        pressedStyle = ButtonStateStyle.textOnly {
            textColor = DefaultColors.red
            setBackgroundStyle(DefaultColors.white, KalugaBackgroundStyle.Shape.Rectangle(5.0f))
        }
        disabledStyle = ButtonStateStyle.textOnly {
            textColor = DefaultColors.black
            setBackgroundStyle(DefaultColors.white, KalugaBackgroundStyle.Shape.Rectangle(5.0f))
        }
    }
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

// Workaround for Elevation not working well with transparency
@Composable
private fun ShadowBox(elevation: Dp, modifier: Modifier = Modifier, shape: Shape = RectangleShape, wrappedTarget: @Composable () -> Unit) {
    Layout(
        {
            ClippedShadow(elevation, modifier, shape)
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentEnforcement provides false,
                wrappedTarget,
            )
        },
        modifier = modifier,
    ) { measurables, constraints ->
        require(measurables.size == 2)
        val shadow = measurables[0]
        val target = measurables[1]
        val targetPlaceable = target.measure(constraints)
        val width = targetPlaceable.width
        val height = targetPlaceable.height
        val shadowPlaceable = shadow.measure(Constraints.fixed(width, height))
        layout(width, height) {
            shadowPlaceable.place(0, 0)
            targetPlaceable.place(0, 0)
        }
    }
}

@Composable
private fun ClippedShadow(elevation: Dp, modifier: Modifier = Modifier, shape: Shape = RectangleShape) {
    Layout(
        modifier
            .drawWithCache {
                val outline = shape.createOutline(size, layoutDirection, this)
                val path = Path().apply { addOutline(outline) }
                onDrawWithContent {
                    clipPath(path, ClipOp.Difference) {
                        this@onDrawWithContent.drawContent()
                    }
                }
            }
            .shadow(elevation, shape, false),
    ) { _, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {}
    }
}
