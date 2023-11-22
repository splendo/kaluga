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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.InspectableValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

@Immutable
internal interface CornerRadiusSize {
    fun xToPx(shapeSize: Size, density: Density): Float
    fun yToPx(shapeSize: Size, density: Density): Float
}

internal fun CornerRadiusSize.isValid(shapeSize: Size, density: Density): Boolean {
    return xToPx(shapeSize, density) >= 0.0f && yToPx(shapeSize, density) >= 0.0f
}

@Stable
internal fun CornerRadiusSize(x: Dp, y: Dp): CornerRadiusSize = DpCornerRadiusSize(x, y)

private data class DpCornerRadiusSize(private val radiusSize: RadiusSize) :
    CornerRadiusSize,
    InspectableValue {
    constructor(x: Dp, y: Dp) : this (RadiusSize(x, y))
    data class RadiusSize(val x: Dp, val y: Dp)
    override fun xToPx(shapeSize: Size, density: Density) = with(density) { radiusSize.x.toPx() }
    override fun yToPx(shapeSize: Size, density: Density) = with(density) { radiusSize.y.toPx() }

    override fun toString(): String = "CornerRadiusSize(x = ${radiusSize.x.value}.dp," +
        "y = ${radiusSize.y.value}.dp)"

    override val valueOverride: RadiusSize
        get() = radiusSize
}

internal class RoundedCornerRadiusShape(
    private val topStart: CornerRadiusSize,
    private val topEnd: CornerRadiusSize,
    private val bottomEnd: CornerRadiusSize,
    private val bottomStart: CornerRadiusSize,
) : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        var topStartX = topStart.xToPx(size, density)
        var topStartY = topStart.yToPx(size, density)
        var topEndX = topEnd.xToPx(size, density)
        var topEndY = topEnd.yToPx(size, density)
        var bottomEndX = bottomEnd.xToPx(size, density)
        var bottomEndY = bottomEnd.yToPx(size, density)
        var bottomStartX = bottomStart.xToPx(size, density)
        var bottomStartY = bottomStart.yToPx(size, density)
        val minDimension = size.minDimension
        if (topStartX + bottomStartX > minDimension) {
            val scale = minDimension / (topStartX + bottomStartX)
            topStartX *= scale
            bottomStartX *= scale
        }
        if (topStartY + bottomStartY > minDimension) {
            val scale = minDimension / (topStartY + bottomStartY)
            topStartY *= scale
            bottomStartY *= scale
        }
        if (topEndX + bottomEndX > minDimension) {
            val scale = minDimension / (topEndX + bottomEndX)
            topEndX *= scale
            bottomEndX *= scale
        }
        if (topEndY + bottomEndY > minDimension) {
            val scale = minDimension / (topEndY + bottomEndY)
            topEndY *= scale
            bottomEndY *= scale
        }
        require(
            topStart.isValid(size, density) &&
                topEnd.isValid(size, density) &&
                bottomEnd.isValid(size, density) &&
                bottomStart.isValid(size, density),
        ) {
            "Corner size in Px can't be negative(topStart = $topStart, topEnd = $topEnd, " +
                "bottomEnd = $bottomEnd, bottomStart = $bottomStart)!"
        }
        return Outline.Rounded(
            if (layoutDirection == LayoutDirection.Ltr) {
                RoundRect(
                    rect = size.toRect(),
                    topLeft = CornerRadius(topStartX, topStartY),
                    topRight = CornerRadius(topEndX, topEndY),
                    bottomRight = CornerRadius(bottomEndX, bottomEndY),
                    bottomLeft = CornerRadius(bottomStartX, bottomStartY),
                )
            } else {
                RoundRect(
                    rect = size.toRect(),
                    topLeft = CornerRadius(topEndX, topEndY),
                    topRight = CornerRadius(topStartX, topStartY),
                    bottomRight = CornerRadius(bottomStartX, bottomStartY),
                    bottomLeft = CornerRadius(bottomEndX, bottomEndY),
                )
            },
        )
    }

    override fun toString(): String {
        return "RoundedCornerRadiusShape(topStart = $topStart, topEnd = $topEnd, bottomEnd = " +
            "$bottomEnd, bottomStart = $bottomStart)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RoundedCornerShape) return false

        if (topStart != other.topStart) return false
        if (topEnd != other.topEnd) return false
        if (bottomEnd != other.bottomEnd) return false
        if (bottomStart != other.bottomStart) return false

        return true
    }

    override fun hashCode(): Int {
        var result = topStart.hashCode()
        result = 31 * result + topEnd.hashCode()
        result = 31 * result + bottomEnd.hashCode()
        result = 31 * result + bottomStart.hashCode()
        return result
    }
}
