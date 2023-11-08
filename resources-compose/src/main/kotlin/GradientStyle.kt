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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.TileMode
import com.splendo.kaluga.resources.stylable.GradientStyle

/**
 * Gets a [Brush] from a [GradientStyle]
 */
val GradientStyle.brush: Brush
    get() = when (this) {
        is GradientStyle.Linear -> Brush.linearGradient(
            *colorPoints.colorStops.toTypedArray(),
            start = orientation.offset.first,
            end = orientation.offset.second,
        )
        is GradientStyle.Radial -> RelativeRadialGradient(
            centerPoint,
            radius = radius,
            colorStops = colorPoints.colorStops.toTypedArray(),
        )
        is GradientStyle.Angular -> RelativeSweepGradient(
            centerPoint,
            *colorPoints.colorStops.toTypedArray(),
        )
    }

private val List<GradientStyle.ColorPoint>.colorStops: List<Pair<Float, Color>>
    get() = map {
        Pair(
            it.offset,
            it.color.composable,
        )
    }

private val GradientStyle.Linear.Orientation.offset: Pair<Offset, Offset>
    get() = when (this) {
        GradientStyle.Linear.Orientation.BOTTOM_LEFT_TOP_RIGHT -> Pair(
            Offset(0.0f, Float.POSITIVE_INFINITY),
            Offset(Float.POSITIVE_INFINITY, 0.0f),
        )
        GradientStyle.Linear.Orientation.BOTTOM_TOP -> Pair(
            Offset(0.0f, Float.POSITIVE_INFINITY),
            Offset(0.0f, Float.POSITIVE_INFINITY),
        )
        GradientStyle.Linear.Orientation.BOTTOM_RIGHT_TOP_LEFT -> Pair(
            Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
            Offset(0.0f, 0.0f),
        )
        GradientStyle.Linear.Orientation.LEFT_RIGHT -> Pair(
            Offset(0.0f, 0.0f),
            Offset(Float.POSITIVE_INFINITY, 0.0f),
        )
        GradientStyle.Linear.Orientation.RIGHT_LEFT -> Pair(
            Offset(Float.POSITIVE_INFINITY, 0.0f),
            Offset(0.0f, 0.0f),
        )
        GradientStyle.Linear.Orientation.TOP_LEFT_BOTTOM_RIGHT -> Pair(
            Offset(0.0f, 0.0f),
            Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )
        GradientStyle.Linear.Orientation.TOP_BOTTOM -> Pair(
            Offset(0.0f, 0.0f),
            Offset(0.0f, Float.POSITIVE_INFINITY),
        )
        GradientStyle.Linear.Orientation.TOP_RIGHT_BOTTOM_LEFT -> Pair(
            Offset(Float.POSITIVE_INFINITY, 0.0f),
            Offset(0.0f, Float.POSITIVE_INFINITY),
        )
    }

@Immutable
internal class RelativeRadialGradient internal constructor(
    private val colors: List<Color>,
    private val stops: List<Float>? = null,
    private val center: GradientStyle.CenterPoint,
    private val radius: Float,
    private val tileMode: TileMode = TileMode.Clamp,
) : ShaderBrush() {

    constructor(
        center: GradientStyle.CenterPoint,
        radius: Float,
        tileMode: TileMode = TileMode.Clamp,
        vararg colorStops: Pair<Float, Color>,
    ) : this(
        colors = List<Color>(colorStops.size) { i -> colorStops[i].second },
        stops = List<Float>(colorStops.size) { i -> colorStops[i].first },
        center = center,
        radius = radius,
        tileMode = tileMode,
    )

    override fun createShader(size: Size): Shader {
        val centerX = center.x * size.width
        val centerY = center.y * size.height

        return RadialGradientShader(
            colors = colors,
            colorStops = stops,
            center = Offset(centerX, centerY),
            radius = if (radius == Float.POSITIVE_INFINITY) size.minDimension / 2 else radius,
            tileMode = tileMode,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RelativeRadialGradient) return false

        if (colors != other.colors) return false
        if (stops != other.stops) return false
        if (center != other.center) return false
        if (radius != other.radius) return false
        if (tileMode != other.tileMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = colors.hashCode()
        result = 31 * result + (stops?.hashCode() ?: 0)
        result = 31 * result + center.hashCode()
        result = 31 * result + radius.hashCode()
        result = 31 * result + tileMode.hashCode()
        return result
    }

    override fun toString(): String {
        val radiusValue = if (radius.isFinite()) "radius=$radius, " else ""
        return "RelativeRadialGradient(" +
            "colors=$colors, " +
            "stops=$stops, " +
            "center=$center, " +
            radiusValue +
            "tileMode=$tileMode)"
    }
}

/**
 * Brush implementation used to apply a sweep gradient on a given [Paint]
 */
@Immutable
internal class RelativeSweepGradient internal constructor(
    private val center: GradientStyle.CenterPoint,
    private val colors: List<Color>,
    private val stops: List<Float>? = null,
) : ShaderBrush() {

    constructor(center: GradientStyle.CenterPoint, vararg colorStops: Pair<Float, Color>) : this(
        colors = List<Color>(colorStops.size) { i -> colorStops[i].second },
        stops = List<Float>(colorStops.size) { i -> colorStops[i].first },
        center = center,
    )

    override fun createShader(size: Size): Shader = SweepGradientShader(
        Offset(
            center.x * size.width,
            center.y * size.height,
        ),
        colors,
        stops,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RelativeSweepGradient) return false

        if (center != other.center) return false
        if (colors != other.colors) return false
        if (stops != other.stops) return false

        return true
    }

    override fun hashCode(): Int {
        var result = center.hashCode()
        result = 31 * result + colors.hashCode()
        result = 31 * result + (stops?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "RelativeSweepGradient(" +
            "center=$center, " +
            "colors=$colors, stops=$stops)"
    }
}
