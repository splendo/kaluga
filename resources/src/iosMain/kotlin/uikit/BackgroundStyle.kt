package com.splendo.kaluga.resources.uikit

import com.splendo.kaluga.resources.stylable.BackgroundStyle
import com.splendo.kaluga.resources.stylable.GradientStyle
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPathRef
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGSizeMake
import platform.QuartzCore.CAGradientLayer
import platform.QuartzCore.CALayer
import platform.QuartzCore.CAShapeLayer
import platform.UIKit.UIBezierPath
import platform.UIKit.UIColor
import platform.UIKit.UIRectCornerBottomLeft
import platform.UIKit.UIRectCornerBottomRight
import platform.UIKit.UIRectCornerTopLeft
import platform.UIKit.UIRectCornerTopRight
import platform.UIKit.UIView

fun UIView.applyBackgroundStyle(style: BackgroundStyle) = layer.applyBackgroundStyle(style, bounds)

fun CALayer.applyBackgroundStyle(style: BackgroundStyle, bounds: CValue<CGRect>) = apply {
    val maskPath = pathForShape(style.shape, bounds)
    mask = CAShapeLayer(this).apply {
        frame = bounds
        path = maskPath
    }
    applyFillStyle(style.fillStyle, bounds)
    applyStroke(style.strokeStyle, maskPath, bounds)
}

private fun pathForShape(shape: BackgroundStyle.Shape, bounds: CValue<CGRect>): CGPathRef? = when (shape) {
    is BackgroundStyle.Shape.Rectangle -> UIBezierPath.bezierPathWithRoundedRect(bounds, shape.roundedCorners.fold(0U) { acc, corner ->
        acc or when (corner) {
            BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT -> UIRectCornerTopLeft
            BackgroundStyle.Shape.Rectangle.Corner.TOP_RIGHT -> UIRectCornerTopRight
            BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT -> UIRectCornerBottomLeft
            BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT -> UIRectCornerBottomRight
        }
    },
        CGSizeMake(shape.cornerRadiusX.toDouble(), shape.cornerRadiusY.toDouble())
    )
    is BackgroundStyle.Shape.Oval -> UIBezierPath.bezierPathWithOvalInRect(bounds)
}.CGPath

private fun CALayer.applyFillStyle(fillStyle: BackgroundStyle.FillStyle, bounds: CValue<CGRect>) {
    addSublayer(CAGradientLayer(this).apply {
        frame = bounds
        when (fillStyle) {
            is BackgroundStyle.FillStyle.Solid -> {
                type = "axial"
                colors = listOf(fillStyle.color.uiColor.CGColor)
            }
            is BackgroundStyle.FillStyle.Gradient -> {
                val sortedColorPoints = fillStyle.gradientStyle.colorPoints.sortedBy { it.offset }
                colors = sortedColorPoints.map { it.color.uiColor.CGColor }
                locations = sortedColorPoints.map { it.offset }
                applyGradientStyle(fillStyle.gradientStyle, bounds)
            }
        }
    })
}

private fun CAGradientLayer.applyGradientStyle(gradientStyle: GradientStyle, bounds: CValue<CGRect>) = when (gradientStyle) {
    is GradientStyle.Linear -> {
        type = "axial"
        val startAndEndPoint = when (gradientStyle.orientation) {
            GradientStyle.Linear.Orientation.TOP_LEFT_BOTTOM_RIGHT -> Pair(CGPointMake(0.0, 0.0), CGPointMake(1.0, 1.0))
            GradientStyle.Linear.Orientation.TOP_RIGHT_BOTTOM_LEFT -> Pair(CGPointMake(1.0, 0.0), CGPointMake(0.0, 0.0))
            GradientStyle.Linear.Orientation.TOP_BOTTOM -> Pair(CGPointMake(0.5, 0.0), CGPointMake(0.5, 1.0))
            GradientStyle.Linear.Orientation.LEFT_RIGHT -> Pair(CGPointMake(0.0, 0.5), CGPointMake(1.0, 0.5))
            GradientStyle.Linear.Orientation.BOTTOM_LEFT_TOP_RIGHT -> Pair(CGPointMake(0.0, 1.0), CGPointMake(1.0, 0.0))
            GradientStyle.Linear.Orientation.BOTTOM_RIGHT_TOP_LEFT -> Pair(CGPointMake(1.0, 1.0), CGPointMake(0.0, 0.0))
            GradientStyle.Linear.Orientation.BOTTOM_TOP -> Pair(CGPointMake(0.5, 1.0), CGPointMake(0.5, 0.0))
            GradientStyle.Linear.Orientation.RIGHT_LEFT -> Pair(CGPointMake(1.0, 0.5), CGPointMake(0.0, 0.5))
        }
        startPoint = startAndEndPoint.first
        endPoint = startAndEndPoint.second
    }
    is GradientStyle.Radial -> {
        type = "radial"
        startPoint = CGPointMake(gradientStyle.centerPoint.x.toDouble(), gradientStyle.centerPoint.y.toDouble())
        endPoint = CGPointMake(
            gradientStyle.centerPoint.x.toDouble() + (gradientStyle.radius / bounds.useContents { size.width }),
            gradientStyle.centerPoint.y.toDouble() + (gradientStyle.radius / bounds.useContents { size.height })
        )
    }
    is GradientStyle.Angular -> {
        type = "conic"
        startPoint = CGPointMake(gradientStyle.centerPoint.x.toDouble(), gradientStyle.centerPoint.y.toDouble())
        endPoint = CGPointMake(1.0, gradientStyle.centerPoint.y.toDouble())
    }
}

private fun CALayer.applyStroke(strokeStyle: BackgroundStyle.StrokeStyle, path: CGPathRef?, bounds: CValue<CGRect>) {
    addSublayer( CAShapeLayer(this).apply {
        frame = bounds
        this.path = path
        when (strokeStyle) {
            is BackgroundStyle.StrokeStyle.Stroke -> {
                lineWidth = strokeStyle.width.toDouble()
                strokeColor = strokeStyle.color.uiColor.CGColor
            }
            is BackgroundStyle.StrokeStyle.None -> {
                lineWidth = 0.0
                strokeColor = UIColor.clearColor.CGColor
            }
        }
        fillColor = null
    })

}