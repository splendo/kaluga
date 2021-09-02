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

package com.splendo.kaluga.resources

data class ButtonStyle(
    val textStyle: TextStyle,
    val foregroundColor: Color = clearColor,
    val backgroundColor: Color,
    val cornerRadius: Double = 0.0,
    val strokeStyle: StrokeStyle = StrokeStyle.None
) {

    companion object {
        fun flat(textStyle: TextStyle, foregroundColor: Color = clearColor) = ButtonStyle(textStyle, foregroundColor, clearColor)
    }

    sealed class StrokeStyle {
        object None : StrokeStyle()
        data class Stroke(val width: Double, val color: Color) : StrokeStyle()
    }
}
