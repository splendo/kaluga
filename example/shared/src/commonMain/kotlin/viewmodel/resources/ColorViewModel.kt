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

package com.splendo.kaluga.example.shared.viewmodel.resources

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.alerts.buildActionSheet
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.architecture.observable.InitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.resources.Color
import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.burn
import com.splendo.kaluga.resources.colorBlend
import com.splendo.kaluga.resources.colorFrom
import com.splendo.kaluga.resources.darken
import com.splendo.kaluga.resources.darkenBy
import com.splendo.kaluga.resources.defaultFont
import com.splendo.kaluga.resources.dodge
import com.splendo.kaluga.resources.exclude
import com.splendo.kaluga.resources.hardLight
import com.splendo.kaluga.resources.hexString
import com.splendo.kaluga.resources.hue
import com.splendo.kaluga.resources.lighten
import com.splendo.kaluga.resources.lightenBy
import com.splendo.kaluga.resources.luminate
import com.splendo.kaluga.resources.minus
import com.splendo.kaluga.resources.overlay
import com.splendo.kaluga.resources.plus
import com.splendo.kaluga.resources.saturate
import com.splendo.kaluga.resources.screen
import com.splendo.kaluga.resources.softLight
import com.splendo.kaluga.resources.stylable.BackgroundStyle
import com.splendo.kaluga.resources.stylable.ButtonStateStyle
import com.splendo.kaluga.resources.stylable.ButtonStyle
import com.splendo.kaluga.resources.times
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

enum class SelectableBlendMode {
    NORMAL,
    MULTIPLY,
    SCREEN,
    OVERLAY,
    DARKEN,
    LIGHTEN,
    HARD_LIGHT,
    SOFT_LIGHT,
    DODGE,
    BURN,
    DIFFERENCE,
    EXCLUSION,
    HUE,
    SATURATION,
    LUMINATE,
    COLOR_BLEND;
}

class ColorViewModel(
    val alertPresenterBuilder: AlertPresenter.Builder
) : BaseViewModel() {

    private val backdropColor = MutableStateFlow(colorFrom(1.0, 0.0, 0.0))
    private val sourceColor = MutableStateFlow(colorFrom(1.0, 0.0, 0.0))
    private val blendMode = MutableStateFlow(SelectableBlendMode.NORMAL)
    val blendedColor = combine(backdropColor, sourceColor, blendMode) { backdrop, source, blendMode ->
        when (blendMode) {
            SelectableBlendMode.NORMAL -> backdrop + source
            SelectableBlendMode.MULTIPLY -> backdrop * source
            SelectableBlendMode.SCREEN -> backdrop screen source
            SelectableBlendMode.OVERLAY -> backdrop overlay source
            SelectableBlendMode.DARKEN -> backdrop darken source
            SelectableBlendMode.LIGHTEN -> backdrop lighten source
            SelectableBlendMode.HARD_LIGHT -> backdrop hardLight source
            SelectableBlendMode.SOFT_LIGHT -> backdrop softLight source
            SelectableBlendMode.DODGE -> backdrop dodge source
            SelectableBlendMode.BURN -> backdrop burn source
            SelectableBlendMode.DIFFERENCE -> backdrop - source
            SelectableBlendMode.EXCLUSION -> backdrop exclude source
            SelectableBlendMode.HUE -> backdrop hue source
            SelectableBlendMode.SATURATION -> backdrop saturate source
            SelectableBlendMode.LUMINATE -> backdrop luminate source
            SelectableBlendMode.COLOR_BLEND -> backdrop colorBlend source
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, DefaultColors.clear)

    val backdropColorBackground = backdropColor.backgroundStyleObservable
    val sourceColorBackground = sourceColor.backgroundStyleObservable
    val blendedColorBackground = blendedColor.backgroundStyleObservable

    val backdropText = backdropColor.map { it.hexString }.toInitializedObservable("", coroutineScope)
    val sourceText = sourceColor.map { it.hexString }.toInitializedObservable("", coroutineScope)
    
    // Lighten Backdrop
    val backdropLighten10 = backdropColor.map { it.lightenBy(0.1) }.backgroundStyleObservable
    val backdropLighten20 = backdropColor.map { it.lightenBy(0.2) }.backgroundStyleObservable
    val backdropLighten30 = backdropColor.map { it.lightenBy(0.3) }.backgroundStyleObservable
    val backdropLighten40 = backdropColor.map { it.lightenBy(0.4) }.backgroundStyleObservable
    val backdropLighten50 = backdropColor.map { it.lightenBy(0.5) }.backgroundStyleObservable
    val backdropLighten60 = backdropColor.map { it.lightenBy(0.6) }.backgroundStyleObservable
    val backdropLighten70 = backdropColor.map { it.lightenBy(0.7) }.backgroundStyleObservable
    val backdropLighten80 = backdropColor.map { it.lightenBy(0.8) }.backgroundStyleObservable
    val backdropLighten90 = backdropColor.map { it.lightenBy(0.9) }.backgroundStyleObservable
    val backdropLighten100 = backdropColor.map { it.lightenBy(1.0) }.backgroundStyleObservable

    // Darken Backdrop
    val backdropDarken10 = backdropColor.map { it.darkenBy(0.1) }.backgroundStyleObservable
    val backdropDarken20 = backdropColor.map { it.darkenBy(0.2) }.backgroundStyleObservable
    val backdropDarken30 = backdropColor.map { it.darkenBy(0.3) }.backgroundStyleObservable
    val backdropDarken40 = backdropColor.map { it.darkenBy(0.4) }.backgroundStyleObservable
    val backdropDarken50 = backdropColor.map { it.darkenBy(0.5) }.backgroundStyleObservable
    val backdropDarken60 = backdropColor.map { it.darkenBy(0.6) }.backgroundStyleObservable
    val backdropDarken70 = backdropColor.map { it.darkenBy(0.7) }.backgroundStyleObservable
    val backdropDarken80 = backdropColor.map { it.darkenBy(0.8) }.backgroundStyleObservable
    val backdropDarken90 = backdropColor.map { it.darkenBy(0.9) }.backgroundStyleObservable
    val backdropDarken100 = backdropColor.map { it.darkenBy(1.0) }.backgroundStyleObservable

    // Lighten Source
    val sourceLighten10 = sourceColor.map { it.lightenBy(0.1) }.backgroundStyleObservable
    val sourceLighten20 = sourceColor.map { it.lightenBy(0.2) }.backgroundStyleObservable
    val sourceLighten30 = sourceColor.map { it.lightenBy(0.3) }.backgroundStyleObservable
    val sourceLighten40 = sourceColor.map { it.lightenBy(0.4) }.backgroundStyleObservable
    val sourceLighten50 = sourceColor.map { it.lightenBy(0.5) }.backgroundStyleObservable
    val sourceLighten60 = sourceColor.map { it.lightenBy(0.6) }.backgroundStyleObservable
    val sourceLighten70 = sourceColor.map { it.lightenBy(0.7) }.backgroundStyleObservable
    val sourceLighten80 = sourceColor.map { it.lightenBy(0.8) }.backgroundStyleObservable
    val sourceLighten90 = sourceColor.map { it.lightenBy(0.9) }.backgroundStyleObservable
    val sourceLighten100 = sourceColor.map { it.lightenBy(1.0) }.backgroundStyleObservable

    // Darken Source
    val sourceDarken10 = sourceColor.map { it.darkenBy(0.1) }.backgroundStyleObservable
    val sourceDarken20 = sourceColor.map { it.darkenBy(0.2) }.backgroundStyleObservable
    val sourceDarken30 = sourceColor.map { it.darkenBy(0.3) }.backgroundStyleObservable
    val sourceDarken40 = sourceColor.map { it.darkenBy(0.4) }.backgroundStyleObservable
    val sourceDarken50 = sourceColor.map { it.darkenBy(0.5) }.backgroundStyleObservable
    val sourceDarken60 = sourceColor.map { it.darkenBy(0.6) }.backgroundStyleObservable
    val sourceDarken70 = sourceColor.map { it.darkenBy(0.7) }.backgroundStyleObservable
    val sourceDarken80 = sourceColor.map { it.darkenBy(0.8) }.backgroundStyleObservable
    val sourceDarken90 = sourceColor.map { it.darkenBy(0.9) }.backgroundStyleObservable
    val sourceDarken100 = sourceColor.map { it.darkenBy(1.0) }.backgroundStyleObservable

    // Lighten Blend
    val blendLighten10 = blendedColor.map { it.lightenBy(0.1) }.backgroundStyleObservable
    val blendLighten20 = blendedColor.map { it.lightenBy(0.2) }.backgroundStyleObservable
    val blendLighten30 = blendedColor.map { it.lightenBy(0.3) }.backgroundStyleObservable
    val blendLighten40 = blendedColor.map { it.lightenBy(0.4) }.backgroundStyleObservable
    val blendLighten50 = blendedColor.map { it.lightenBy(0.5) }.backgroundStyleObservable
    val blendLighten60 = blendedColor.map { it.lightenBy(0.6) }.backgroundStyleObservable
    val blendLighten70 = blendedColor.map { it.lightenBy(0.7) }.backgroundStyleObservable
    val blendLighten80 = blendedColor.map { it.lightenBy(0.8) }.backgroundStyleObservable
    val blendLighten90 = blendedColor.map { it.lightenBy(0.9) }.backgroundStyleObservable
    val blendLighten100 = blendedColor.map { it.lightenBy(1.0) }.backgroundStyleObservable

    // Darken Source
    val blendDarken10 = blendedColor.map { it.darkenBy(0.1) }.backgroundStyleObservable
    val blendDarken20 = blendedColor.map { it.darkenBy(0.2) }.backgroundStyleObservable
    val blendDarken30 = blendedColor.map { it.darkenBy(0.3) }.backgroundStyleObservable
    val blendDarken40 = blendedColor.map { it.darkenBy(0.4) }.backgroundStyleObservable
    val blendDarken50 = blendedColor.map { it.darkenBy(0.5) }.backgroundStyleObservable
    val blendDarken60 = blendedColor.map { it.darkenBy(0.6) }.backgroundStyleObservable
    val blendDarken70 = blendedColor.map { it.darkenBy(0.7) }.backgroundStyleObservable
    val blendDarken80 = blendedColor.map { it.darkenBy(0.8) }.backgroundStyleObservable
    val blendDarken90 = blendedColor.map { it.darkenBy(0.9) }.backgroundStyleObservable
    val blendDarken100 = blendedColor.map { it.darkenBy(1.0) }.backgroundStyleObservable

    fun submitBackdropText(backdropText: String) {
        submitColorText(backdropText) {
            backdropColor.value = it
        }
    }

    fun submitSourceText(sourceText: String) {
        submitColorText(sourceText) {
            sourceColor.value = it
        }
    }

    val blendModeButton = blendMode.map { currentlySelectedBlendMode ->
        KalugaButton.Plain(
            currentlySelectedBlendMode.name,
            ButtonStyle(
                defaultFont,
                12.0f,
                ButtonStateStyle(colorFrom(0.0, 0.0, 0.0))
            )
        ) {
            alertPresenterBuilder.buildActionSheet(coroutineScope) {
                addActions(
                    SelectableBlendMode.values().map { selectableBlendMode ->
                        Alert.Action(selectableBlendMode.name) {
                            blendMode.value = selectableBlendMode
                        }
                    }
                )
            }.showAsync()
        }
    }
    val flipButton = KalugaButton.Plain(
        "Flip",
        ButtonStyle(
            defaultFont,
            12.0f,
            ButtonStateStyle(colorFrom(0.0, 0.0, 0.0))
        )
    ) {
        val sourceColor = sourceColor.value
        this.sourceColor.value = backdropColor.value
        backdropColor.value = sourceColor
    }

    private fun submitColorText(colorText: String, onColorParsed: (Color) -> Unit) {
        colorFrom(colorText)?.let { onColorParsed(it) } ?: run {
            alertPresenterBuilder.buildAlert(coroutineScope) {
                setTitle("Invalid color")
                setMessage("The text submitted is not a color")
                setNeutralButton("OK")
            }
        }
    }

    private val Flow<Color>.backgroundStyleObservable: InitializedObservable<BackgroundStyle> get() = map {
        BackgroundStyle(
            BackgroundStyle.FillStyle.Solid(it)
        )
    }.toInitializedObservable(BackgroundStyle(BackgroundStyle.FillStyle.Solid(DefaultColors.clear)), coroutineScope)
}
