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

import com.splendo.kaluga.resources.stylable.KalugaBackgroundStyle
import com.splendo.kaluga.resources.stylable.GradientStyle
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.CoreFoundation.CFRetain
import platform.CoreGraphics.CGColorRef
import platform.CoreGraphics.CGPathRef
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.CFBridgingRelease
import platform.QuartzCore.CAGradientLayer
import platform.QuartzCore.CALayer
import platform.QuartzCore.CAShapeLayer
import platform.QuartzCore.kCAGradientLayerAxial
import platform.QuartzCore.kCAGradientLayerConic
import platform.QuartzCore.kCAGradientLayerRadial
import platform.UIKit.UIBezierPath
import platform.UIKit.UIColor
import platform.UIKit.UIRectCornerBottomLeft
import platform.UIKit.UIRectCornerBottomRight
import platform.UIKit.UIRectCornerTopLeft
import platform.UIKit.UIRectCornerTopRight
import platform.UIKit.UIView

/**
 * Sets a [KalugaBackgroundStyle] to a [UIView.layer]
 * @param style the [KalugaBackgroundStyle] to apply
 */
fun UIView.applyBackgroundStyle(style: KalugaBackgroundStyle) = layer.applyBackgroundStyle(style, bounds)

/**
 * Sets a [KalugaBackgroundStyle] to a [CALayer]
 * @param style the [KalugaBackgroundStyle] to apply
 */
fun CALayer.applyBackgroundStyle(style: KalugaBackgroundStyle, bounds: CValue<CGRect>) = apply {
    val maskPath = pathForShape(style.shape, bounds)
    mask = CAShapeLayer(this).apply {
        frame = bounds
        path = maskPath
    }
    applyFillStyle(style.fillStyle, bounds)
    applyStroke(style.strokeStyle, maskPath, bounds)
}

private fun pathForShape(shape: KalugaBackgroundStyle.Shape, bounds: CValue<CGRect>): CGPathRef? = when (shape) {
    is KalugaBackgroundStyle.Shape.Rectangle -> UIBezierPath.bezierPathWithRoundedRect(
        bounds,
        shape.roundedCorners.fold(0U) { acc, corner ->
            acc or when (corner) {
                KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT -> UIRectCornerTopLeft
                KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_RIGHT -> UIRectCornerTopRight
                KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT -> UIRectCornerBottomLeft
                KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT -> UIRectCornerBottomRight
            }
        },
        CGSizeMake(
            shape.cornerRadiusX.toDouble(),
            shape.cornerRadiusY.toDouble(),
        ),
    )
    is KalugaBackgroundStyle.Shape.Oval -> UIBezierPath.bezierPathWithOvalInRect(bounds)
}.CGPath

private fun CALayer.applyFillStyle(fillStyle: KalugaBackgroundStyle.FillStyle, bounds: CValue<CGRect>) {
    addSublayer(
        CAGradientLayer(this).apply {
            frame = bounds
            when (fillStyle) {
                is KalugaBackgroundStyle.FillStyle.Solid -> {
                    type = kCAGradientLayerAxial
                    colors = listOfNotNull(
                        fillStyle.color.uiColor.CGColor,
                        fillStyle.color.uiColor.CGColor,
                    ).mapToCGColor()
                }
                is KalugaBackgroundStyle.FillStyle.Gradient -> {
                    val sortedColorPoints =
                        fillStyle.gradientStyle.colorPoints.sortedBy { it.offset }
                    colors =
                        sortedColorPoints.mapNotNull { it.color.uiColor.CGColor }.mapToCGColor()
                    locations = sortedColorPoints.map { it.offset }
                    applyGradientStyle(fillStyle.gradientStyle, bounds)
                }
            }
        },
    )
}

private fun List<CGColorRef>.mapToCGColor() = map {
    CFBridgingRelease(CFRetain(it))
}

private fun CAGradientLayer.applyGradientStyle(gradientStyle: GradientStyle, bounds: CValue<CGRect>) = when (gradientStyle) {
    is GradientStyle.Linear -> {
        type = kCAGradientLayerAxial
        val startAndEndPoint = when (gradientStyle.orientation) {
            GradientStyle.Linear.Orientation.TOP_LEFT_BOTTOM_RIGHT -> Pair(
                CGPointMake(0.0, 0.0),
                CGPointMake(1.0, 1.0),
            )
            GradientStyle.Linear.Orientation.TOP_RIGHT_BOTTOM_LEFT -> Pair(
                CGPointMake(1.0, 0.0),
                CGPointMake(0.0, 0.0),
            )
            GradientStyle.Linear.Orientation.TOP_BOTTOM -> Pair(
                CGPointMake(0.5, 0.0),
                CGPointMake(0.5, 1.0),
            )
            GradientStyle.Linear.Orientation.LEFT_RIGHT -> Pair(
                CGPointMake(0.0, 0.5),
                CGPointMake(1.0, 0.5),
            )
            GradientStyle.Linear.Orientation.BOTTOM_LEFT_TOP_RIGHT -> Pair(
                CGPointMake(0.0, 1.0),
                CGPointMake(1.0, 0.0),
            )
            GradientStyle.Linear.Orientation.BOTTOM_RIGHT_TOP_LEFT -> Pair(
                CGPointMake(1.0, 1.0),
                CGPointMake(0.0, 0.0),
            )
            GradientStyle.Linear.Orientation.BOTTOM_TOP -> Pair(
                CGPointMake(0.5, 1.0),
                CGPointMake(0.5, 0.0),
            )
            GradientStyle.Linear.Orientation.RIGHT_LEFT -> Pair(
                CGPointMake(1.0, 0.5),
                CGPointMake(0.0, 0.5),
            )
        }
        startPoint = startAndEndPoint.first
        endPoint = startAndEndPoint.second
    }
    is GradientStyle.Radial -> {
        type = kCAGradientLayerRadial
        startPoint = CGPointMake(
            gradientStyle.centerPoint.x.toDouble(),
            gradientStyle.centerPoint.y.toDouble(),
        )
        endPoint = CGPointMake(
            gradientStyle.centerPoint.x.toDouble() + (gradientStyle.radius / bounds.useContents { size.width }),
            gradientStyle.centerPoint.y.toDouble() + (gradientStyle.radius / bounds.useContents { size.height }),
        )
    }
    is GradientStyle.Angular -> {
        type = kCAGradientLayerConic
        startPoint = CGPointMake(gradientStyle.centerPoint.x.toDouble(), gradientStyle.centerPoint.y.toDouble())
        endPoint = CGPointMake(1.0, gradientStyle.centerPoint.y.toDouble())
    }
}

private fun CALayer.applyStroke(strokeStyle: KalugaBackgroundStyle.StrokeStyle, path: CGPathRef?, bounds: CValue<CGRect>) {
    addSublayer(
        CAShapeLayer(this).apply {
            frame = bounds
            this.path = path
            when (strokeStyle) {
                is KalugaBackgroundStyle.StrokeStyle.Stroke -> {
                    lineWidth = strokeStyle.width.toDouble()
                    strokeColor = strokeStyle.color.uiColor.CGColor
                }
                is KalugaBackgroundStyle.StrokeStyle.None -> {
                    lineWidth = 0.0
                    strokeColor = UIColor.clearColor.CGColor
                }
            }
            fillColor = null
        },
    )
}
