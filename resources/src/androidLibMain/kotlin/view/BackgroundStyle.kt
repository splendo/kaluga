/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import com.splendo.kaluga.resources.pixelValue
import com.splendo.kaluga.resources.stylable.BackgroundStyle
import com.splendo.kaluga.resources.stylable.GradientStyle

fun BackgroundStyle.createDrawable(context: Context): Drawable {
    return GradientDrawable().apply {
        applyShape(this@createDrawable.shape, context)
        applyFillStyle(fillStyle, context)
        applyStrokeStyle(strokeStyle, context)
    }
}

private fun GradientDrawable.applyShape(shape: BackgroundStyle.Shape, context: Context) {
    this.shape = when (shape) {
        is BackgroundStyle.Shape.Rectangle -> {
            val radiusX = shape.cornerRadiusX.pixelValue(context)
            val radiusY = shape.cornerRadiusY.pixelValue(context)
            cornerRadii = floatArrayOf(
                if (shape.roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT)) radiusX else 0.0f,
                if (shape.roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT)) radiusY else 0.0f,
                if (shape.roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.TOP_RIGHT)) radiusX else 0.0f,
                if (shape.roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.TOP_RIGHT)) radiusY else 0.0f,
                if (shape.roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT)) radiusX else 0.0f,
                if (shape.roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT)) radiusY else 0.0f,
                if (shape.roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT)) radiusX else 0.0f,
                if (shape.roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT)) radiusY else 0.0f,
            )
            GradientDrawable.RECTANGLE
        }
        is BackgroundStyle.Shape.Oval -> GradientDrawable.OVAL
    }
}

private fun GradientDrawable.applyFillStyle(fillStyle: BackgroundStyle.FillStyle, context: Context) {
    when (fillStyle) {
        is BackgroundStyle.FillStyle.Solid -> {
            color = ColorStateList(arrayOf(intArrayOf()), intArrayOf(fillStyle.color))
        }
        is BackgroundStyle.FillStyle.Gradient -> {
            val colors = fillStyle.gradientStyle.colorPoints.map { it.color }.toIntArray()
            val offsets = fillStyle.gradientStyle.colorPoints.map { it.offset }.toFloatArray()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setColors(
                    colors,
                    offsets
                )
            } else {
                setColors(colors)
            }
            applyGradientStyle(fillStyle.gradientStyle, context)
        }
    }
}

private fun GradientDrawable.applyGradientStyle(gradientStyle: GradientStyle, context: Context) {
    when (gradientStyle) {
        is GradientStyle.Linear -> {
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = when (gradientStyle.orientation) {
                GradientStyle.Linear.Orientation.TOP_BOTTOM -> GradientDrawable.Orientation.TOP_BOTTOM
                GradientStyle.Linear.Orientation.TOP_LEFT_BOTTOM_RIGHT -> GradientDrawable.Orientation.TL_BR
                GradientStyle.Linear.Orientation.TOP_RIGHT_BOTTOM_LEFT -> GradientDrawable.Orientation.TR_BL
                GradientStyle.Linear.Orientation.BOTTOM_TOP -> GradientDrawable.Orientation.BOTTOM_TOP
                GradientStyle.Linear.Orientation.BOTTOM_LEFT_TOP_RIGHT -> GradientDrawable.Orientation.BL_TR
                GradientStyle.Linear.Orientation.BOTTOM_RIGHT_TOP_LEFT -> GradientDrawable.Orientation.BR_TL
                GradientStyle.Linear.Orientation.LEFT_RIGHT -> GradientDrawable.Orientation.LEFT_RIGHT
                GradientStyle.Linear.Orientation.RIGHT_LEFT -> GradientDrawable.Orientation.RIGHT_LEFT
            }
        }
        is GradientStyle.Radial -> {
            gradientType = GradientDrawable.RADIAL_GRADIENT
            gradientRadius = gradientStyle.radius.pixelValue(context)
            setGradientCenter(gradientStyle.centerPoint.x, gradientStyle.centerPoint.y)
        }
        is GradientStyle.Angular -> {
            gradientType = GradientDrawable.SWEEP_GRADIENT
            setGradientCenter(gradientStyle.centerPoint.x, gradientStyle.centerPoint.y)
        }
    }
}

private fun GradientDrawable.applyStrokeStyle(strokeStyle: BackgroundStyle.StrokeStyle, context: Context) {
    when (strokeStyle) {
        is BackgroundStyle.StrokeStyle.Stroke -> {
            setStroke(strokeStyle.width.pixelValue(context).toInt(), ColorStateList(arrayOf(intArrayOf()), intArrayOf(strokeStyle.color)))
        }
        is BackgroundStyle.StrokeStyle.None -> {}
    }
}

fun View.applyBackgroundStyle(backgroundStyle: BackgroundStyle) {
    background = backgroundStyle.createDrawable(context)
}
