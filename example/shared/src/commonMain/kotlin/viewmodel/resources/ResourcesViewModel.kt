package com.splendo.kaluga.example.shared.viewmodel.resources

import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.resources.colorFrom
import com.splendo.kaluga.resources.defaultBoldFont
import com.splendo.kaluga.resources.stylable.BackgroundStyle
import com.splendo.kaluga.resources.stylable.ButtonStateStyle
import com.splendo.kaluga.resources.stylable.ButtonStyle
import com.splendo.kaluga.resources.stylable.GradientStyle
import com.splendo.kaluga.resources.stylable.colorPoints

class ResourcesViewModel : BaseViewModel() {

    val buttonStyle = ButtonStyle(
        defaultBoldFont,
        15.0f,
        false,
        ButtonStateStyle(
            colorFrom(1.0, 1.0, 1.0),
            BackgroundStyle(
                BackgroundStyle.FillStyle.Solid(colorFrom(1.0, 0.0, 0.0)),
                BackgroundStyle.StrokeStyle.Stroke(2.0f, colorFrom(1.0, 1.0, 1.0)),
                BackgroundStyle.Shape.Rectangle(5.0f)
            )
        ),
        ButtonStateStyle(
            colorFrom(0.8, 0.8, 0.8),
            BackgroundStyle(
                BackgroundStyle.FillStyle.Gradient(
                    GradientStyle.Linear(
                        listOf(
                            colorFrom(1.0, 0.0, 0.0),
                            colorFrom(1.0, 1.0, 1.0)
                        ).colorPoints,
                        GradientStyle.Linear.Orientation.RIGHT_LEFT
                    )
                ),
                BackgroundStyle.StrokeStyle.Stroke(4.0f, colorFrom(0.8, 0.8, 0.8)),
                BackgroundStyle.Shape.Rectangle(3.0f)
            )
        ),
        ButtonStateStyle(
            colorFrom(0.0, 0.0, 0.0),
            BackgroundStyle(
                BackgroundStyle.FillStyle.Gradient(
                    GradientStyle.Radial(
                        listOf(
                            colorFrom(0.3, 0.3, 0.3),
                            colorFrom(0.5, 0.5, 0.5)
                        ).colorPoints,
                        50f
                    )
                ),
                BackgroundStyle.StrokeStyle.Stroke(2.0f, colorFrom(0.8, 0.8, 0.8)),
                BackgroundStyle.Shape.Oval
            )
        )
    )
}