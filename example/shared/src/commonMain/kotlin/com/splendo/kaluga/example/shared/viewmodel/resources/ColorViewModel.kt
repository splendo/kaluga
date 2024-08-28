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

package com.splendo.kaluga.example.shared.viewmodel.resources

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.alerts.buildActionSheet
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.architecture.observable.BaseInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.KalugaColor
import com.splendo.kaluga.resources.burn
import com.splendo.kaluga.resources.colorBlend
import com.splendo.kaluga.resources.colorFrom
import com.splendo.kaluga.resources.darken
import com.splendo.kaluga.resources.darkenBy
import com.splendo.kaluga.resources.difference
import com.splendo.kaluga.resources.dodge
import com.splendo.kaluga.resources.exclude
import com.splendo.kaluga.resources.hardLight
import com.splendo.kaluga.resources.hexString
import com.splendo.kaluga.resources.hue
import com.splendo.kaluga.resources.lighten
import com.splendo.kaluga.resources.lightenBy
import com.splendo.kaluga.resources.luminate
import com.splendo.kaluga.resources.multiply
import com.splendo.kaluga.resources.normal
import com.splendo.kaluga.resources.overlay
import com.splendo.kaluga.resources.saturate
import com.splendo.kaluga.resources.screen
import com.splendo.kaluga.resources.softLight
import com.splendo.kaluga.resources.stylable.KalugaBackgroundStyle
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
    COLOR_BLEND,
}

class ColorViewModel(private val alertPresenterBuilder: BaseAlertPresenter.Builder) : BaseLifecycleViewModel(alertPresenterBuilder) {

    private val backdropColor = MutableStateFlow(DefaultColors.mediumPurple)
    private val sourceColor = MutableStateFlow(DefaultColors.darkCyan)
    private val blendMode = MutableStateFlow(SelectableBlendMode.NORMAL)
    private val blendedColor = combine(backdropColor, sourceColor, blendMode) { backdrop, source, blendMode ->
        when (blendMode) {
            SelectableBlendMode.NORMAL -> backdrop normal source
            SelectableBlendMode.MULTIPLY -> backdrop multiply source
            SelectableBlendMode.SCREEN -> backdrop screen source
            SelectableBlendMode.OVERLAY -> backdrop overlay source
            SelectableBlendMode.DARKEN -> backdrop darken source
            SelectableBlendMode.LIGHTEN -> backdrop lighten source
            SelectableBlendMode.HARD_LIGHT -> backdrop hardLight source
            SelectableBlendMode.SOFT_LIGHT -> backdrop softLight source
            SelectableBlendMode.DODGE -> backdrop dodge source
            SelectableBlendMode.BURN -> backdrop burn source
            SelectableBlendMode.DIFFERENCE -> backdrop difference source
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

    val lightenBackdrops = backdropColor.lightenList()
    val darkenBackdrops = backdropColor.darkenList()
    val lightenSource = sourceColor.lightenList()
    val darkenSource = sourceColor.darkenList()
    val lightenBlended = blendedColor.lightenList()
    val darkenBlended = blendedColor.darkenList()

    private val steps = (1..10).map { it.toDouble() / 10.0 }
    private fun Flow<KalugaColor>.lightenList() = map { color ->
        steps.map {
            KalugaBackgroundStyle(
                KalugaBackgroundStyle.FillStyle.Solid(color.lightenBy(it)),
            )
        }
    }.toInitializedObservable(emptyList(), coroutineScope)
    private fun Flow<KalugaColor>.darkenList() = map { color ->
        steps.map {
            KalugaBackgroundStyle(
                KalugaBackgroundStyle.FillStyle.Solid(color.darkenBy(it)),
            )
        }
    }.toInitializedObservable(emptyList(), coroutineScope)

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
            ButtonStyles.redButton,
        ) {
            alertPresenterBuilder.buildActionSheet(coroutineScope) {
                addActions(
                    SelectableBlendMode.values().map { selectableBlendMode ->
                        Alert.Action(selectableBlendMode.name) {
                            blendMode.value = selectableBlendMode
                        }
                    },
                )
            }.showAsync()
        }
    }.toInitializedObservable<KalugaButton>(KalugaButton.Plain("", ButtonStyles.redButton, false) {}, coroutineScope)

    val flipButton: KalugaButton = KalugaButton.Plain(
        "Flip",
        ButtonStyles.textButton,
    ) {
        val sourceColor = sourceColor.value
        this.sourceColor.value = backdropColor.value
        backdropColor.value = sourceColor
    }

    private fun submitColorText(colorText: String, onColorParsed: (KalugaColor) -> Unit) {
        colorFrom(colorText)?.let { onColorParsed(it) } ?: run {
            coroutineScope.launch {
                alertPresenterBuilder.buildAlert(coroutineScope) {
                    setTitle("Invalid color")
                    setMessage("The text submitted is not a color")
                    setNeutralButton("OK")
                }.show()
            }
        }
    }

    private val Flow<KalugaColor>.backgroundStyleObservable: BaseInitializedObservable<KalugaBackgroundStyle> get() = map {
        KalugaBackgroundStyle(
            KalugaBackgroundStyle.FillStyle.Solid(it),
        )
    }.toInitializedObservable(KalugaBackgroundStyle(KalugaBackgroundStyle.FillStyle.Solid(DefaultColors.clear)), coroutineScope)
}
