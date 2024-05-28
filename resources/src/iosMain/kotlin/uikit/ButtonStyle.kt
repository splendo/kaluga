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

package com.splendo.kaluga.resources.uikit

import com.splendo.kaluga.base.IOSVersion
import com.splendo.kaluga.resources.stylable.ButtonImage
import com.splendo.kaluga.resources.stylable.ButtonStateStyle
import com.splendo.kaluga.resources.stylable.ImageGravity
import com.splendo.kaluga.resources.stylable.ImageSize
import com.splendo.kaluga.resources.stylable.KalugaButtonStyle
import com.splendo.kaluga.resources.uiImage
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSMapTable
import platform.Foundation.NSPointerFunctionsStrongMemory
import platform.Foundation.NSPointerFunctionsWeakMemory
import platform.QuartzCore.CALayer
import platform.UIKit.NSDirectionalEdgeInsetsMake
import platform.UIKit.NSDirectionalRectEdgeBottom
import platform.UIKit.NSDirectionalRectEdgeLeading
import platform.UIKit.NSDirectionalRectEdgeTop
import platform.UIKit.NSDirectionalRectEdgeTrailing
import platform.UIKit.UIApplication
import platform.UIKit.UIButton
import platform.UIKit.UIButtonConfiguration
import platform.UIKit.UIButtonConfigurationCornerStyleFixed
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlState
import platform.UIKit.UIControlStateDisabled
import platform.UIKit.UIControlStateHighlighted
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIGraphicsBeginImageContext
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIUserInterfaceLayoutDirection
import platform.UIKit.setContentEdgeInsets
import platform.UIKit.setFont
import platform.darwin.NSObject
import platform.objc.sel_registerName

internal class UIControlClosure(private val action: () -> Unit) : NSObject() {

    object Registry {
        val registeredUIControl = NSMapTable(NSPointerFunctionsWeakMemory, NSPointerFunctionsStrongMemory, 0U)
    }

    @ObjCAction
    fun invoke() {
        action()
    }
}

/**
 * Makes a [UIButton] look and behave according to a [KalugaButton]
 * @param button the [KalugaButton] that specifies the look and behaviour of the [UIButton]
 */
fun UIButton.bindButton(button: KalugaButton) {
    applyStyle(button.style)
    when (button) {
        is KalugaButton.WithoutText -> setTitle("", UIControlStateNormal)
        is KalugaButton.Plain -> setTitle(button.text, UIControlStateNormal)
        is KalugaButton.Styled -> setAttributedTitle(button.text.attributeString, UIControlStateNormal)
    }
    setEnabled(button.isEnabled)
    val wrappedAction = UIControlClosure(button.action)
    UIControlClosure.Registry.registeredUIControl.setObject(wrappedAction, this)
    addTarget(wrappedAction, sel_registerName("invoke"), UIControlEventTouchUpInside)
}

/**
 * Makes a [UIButton] look as specified by a [KalugaButtonStyle]
 * @param buttonStyle the [KalugaButtonStyle] that specifies the look of the [UIButton]
 */
fun UIButton.applyStyle(buttonStyle: KalugaButtonStyle<*>) {
    if (buttonStyle is KalugaButtonStyle.WithText) {
        applyTextStyle(buttonStyle)
    } else {
        applyTextStyle(KalugaButtonStyle.hiddenTextStyle)
    }

    if (IOSVersion.systemVersion >= IOSVersion(15)) {
        configuration = UIButtonConfiguration.plainButtonConfiguration().apply {
            with(buttonStyle.padding) {
                contentInsets = NSDirectionalEdgeInsetsMake(
                    top.toDouble(), start.toDouble(), bottom.toDouble(), end.toDouble()
                )
            }
            cornerStyle = UIButtonConfigurationCornerStyleFixed
            background.cornerRadius = 0.0
        }
        configurationUpdateHandler = { button ->
            button?.configuration?.let { conf ->

                if (buttonStyle is KalugaButtonStyle.WithImage) {
                    conf.applyImageStyle(buttonStyle, button.state)
                } else {
                    conf.image = null
                }

                conf.applyButtonBackgroundStateStyle(buttonStyle, button)
                button.configuration = conf
            }
        }
    } else {
        with(buttonStyle.padding) {
            val isLeftToRight = UIApplication.sharedApplication.userInterfaceLayoutDirection == UIUserInterfaceLayoutDirection.UIUserInterfaceLayoutDirectionLeftToRight
            setContentEdgeInsets(
                UIEdgeInsetsMake(
                    top.toDouble(),
                    (if (isLeftToRight) start else end).toDouble(),
                    bottom.toDouble(),
                    (if (isLeftToRight) end else start).toDouble()
                )
            )
        }

        if (buttonStyle is KalugaButtonStyle.WithImage) {
            applyImageStyle(buttonStyle)
        } else {
            setImage(null, UIControlStateNormal)
            setImage(null, UIControlStateHighlighted)
            setImage(null, UIControlStateDisabled)
        }

        applyButtonBackgroundStateStyle(buttonStyle.defaultStyle, UIControlStateNormal)
        applyButtonBackgroundStateStyle(buttonStyle.pressedStyle, UIControlStateHighlighted)
        applyButtonBackgroundStateStyle(buttonStyle.disabledStyle, UIControlStateDisabled)
    }
}

fun UIButton.applyTextStyle(buttonStyle: KalugaButtonStyle.WithText<*>) {
    setFont(buttonStyle.font.fontWithSize(buttonStyle.textSize.toDouble()))
    setContentHorizontalAlignment(buttonStyle.textAlignment.contentHorizontalAlignment)

    setTitleColor(buttonStyle.defaultStyle.textColor.uiColor, UIControlStateNormal)
    setTitleColor(buttonStyle.pressedStyle.textColor.uiColor, UIControlStateHighlighted)
    setTitleColor(buttonStyle.disabledStyle.textColor.uiColor, UIControlStateDisabled)
}

fun UIButton.applyImageStyle(buttonStyle: KalugaButtonStyle.WithImage<*>) {
    setImage(buttonStyle.defaultStyle.image.image(buttonStyle.imageSize), UIControlStateNormal)
    setImage(buttonStyle.pressedStyle.image.image(buttonStyle.imageSize), UIControlStateHighlighted)
    setImage(buttonStyle.disabledStyle.image.image(buttonStyle.imageSize), UIControlStateDisabled)
}

private fun UIButton.applyButtonBackgroundStateStyle(style: ButtonStateStyle, state: UIControlState) {
    setBackgroundImage(backgroundImage(style), state)
}

private fun UIButtonConfiguration.applyButtonBackgroundStateStyle(style: KalugaButtonStyle<*>, button: UIButton) {
    val stateStyle =  when (button.state) {
        UIControlStateDisabled -> style.disabledStyle
        UIControlStateHighlighted -> style.pressedStyle
        else -> style.defaultStyle
    }

    background.image = button.backgroundImage(stateStyle)
}

private fun UIButton.backgroundImage(style: ButtonStateStyle): UIImage = try {
        val bounds = bounds
        UIGraphicsBeginImageContext(CGSizeMake(bounds.useContents { size.width }, bounds.useContents { size.height }))
        CALayer().apply {
            frame = bounds
            applyBackgroundStyle(style.backgroundStyle, bounds)
            renderInContext(UIGraphicsGetCurrentContext())
        }

        UIGraphicsGetImageFromCurrentImageContext()!!
    } finally {
        UIGraphicsEndImageContext()
    }

private fun UIButtonConfiguration.applyImageStyle(buttonStyle: KalugaButtonStyle.WithImage<*>, state: UIControlState) {
    when (buttonStyle) {
        is KalugaButtonStyle.ImageOnly -> applyImageGravity(ImageGravity.TOP, 0.0f)
        is KalugaButtonStyle.WithImageAndText -> applyImageGravity(buttonStyle.imageGravity, buttonStyle.spacing)
    }

    val stateStyle =  when (state) {
        UIControlStateDisabled -> buttonStyle.disabledStyle
        UIControlStateHighlighted -> buttonStyle.pressedStyle
        else -> buttonStyle.defaultStyle
    }

    image = stateStyle.image.image(buttonStyle.imageSize)
}
private fun UIButtonConfiguration.applyImageGravity(gravity: ImageGravity, spacing: Float) {
    imagePadding = spacing.toDouble()
    imagePlacement = when (gravity) {
        ImageGravity.START -> NSDirectionalRectEdgeLeading
        ImageGravity.LEFT -> if (UIApplication.sharedApplication.userInterfaceLayoutDirection == UIUserInterfaceLayoutDirection.UIUserInterfaceLayoutDirectionLeftToRight) {
            NSDirectionalRectEdgeLeading
        } else {
            NSDirectionalRectEdgeTrailing
        }
        ImageGravity.END -> NSDirectionalRectEdgeTrailing
        ImageGravity.RIGHT -> if (UIApplication.sharedApplication.userInterfaceLayoutDirection == UIUserInterfaceLayoutDirection.UIUserInterfaceLayoutDirectionLeftToRight) {
            NSDirectionalRectEdgeTrailing
        } else {
            NSDirectionalRectEdgeLeading
        }
        ImageGravity.TOP -> NSDirectionalRectEdgeTop
        ImageGravity.BOTTOM -> NSDirectionalRectEdgeBottom
    }
}

private fun ButtonImage.image(size: ImageSize): UIImage {
    val rawImage = when (this) {
        is ButtonImage.Hidden -> UIImage()
        is ButtonImage.Image -> image
        is ButtonImage.Tinted -> image.uiImage
    }
    return when (size) {
        is ImageSize.Intrinsic -> rawImage
        is ImageSize.Sized -> {
            try {
                UIGraphicsBeginImageContext(CGSizeMake(size.width.toDouble(), size.height.toDouble()))
                rawImage.drawInRect(CGRectMake(0.0, 0.0, size.width.toDouble(), size.height.toDouble()))
                UIGraphicsGetImageFromCurrentImageContext() ?: UIImage()
            } finally {
                UIGraphicsEndImageContext()
            }
        }
    }
}
