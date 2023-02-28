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

package com.splendo.kaluga.resources.databinding

import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.splendo.kaluga.resources.KalugaImage
import com.splendo.kaluga.resources.TintedImage
import com.splendo.kaluga.resources.drawable
import com.splendo.kaluga.resources.stylable.KalugaBackgroundStyle
import com.splendo.kaluga.resources.stylable.KalugaTextStyle
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.resources.view.KalugaLabel
import com.splendo.kaluga.resources.view.alignment
import com.splendo.kaluga.resources.view.applyBackgroundStyle
import com.splendo.kaluga.resources.view.applyTextStyle
import com.splendo.kaluga.resources.view.bindButton
import com.splendo.kaluga.resources.view.bindLabel
import com.splendo.kaluga.resources.view.gravity
import kotlin.math.max

object Binding {

    /**
     * Binds a [KalugaBackgroundStyle] to a [View].
     */
    @BindingAdapter("backgroundStyle")
    @JvmStatic
    fun bindBackground(view: View, backgroundStyle: KalugaBackgroundStyle?) {
        backgroundStyle?.let {
            view.applyBackgroundStyle(it)
        }
    }

    /**
     * Binds a [KalugaImage] to an [ImageView].
     */
    @BindingAdapter("image")
    @JvmStatic
    fun bindImage(view: ImageView, image: KalugaImage?) {
        view.setImageDrawable(image?.drawable)
    }

    /**
     * Binds a [TintedImage] to an [ImageView].
     */
    @BindingAdapter("tintedImage")
    @JvmStatic
    fun bindImage(view: ImageView, image: TintedImage?) {
        view.setImageDrawable(image?.drawable)
    }

    /**
     * Binds a [KalugaButton] to a [Button].
     */
    @BindingAdapter("kalugaButton")
    @JvmStatic
    fun bindKalugaButton(button: Button, kalugaButton: KalugaButton?) {
        kalugaButton?.let {
            button.bindButton(it)
        }
    }

    /**
     * Binds a [KalugaLabel] to a [TextView]
     */
    @BindingAdapter("kalugaLabel")
    @JvmStatic
    fun bindKalugaLabel(view: TextView, kalugaLabel: KalugaLabel?) {
        kalugaLabel?.let {
            view.bindLabel(it)
        }
    }

    /**
     * Binds a [KalugaTextStyle] to a [TextView].
     */
    @BindingAdapter("textStyle")
    @JvmStatic
    fun bindTextStyle(textView: TextView, textStyle: KalugaTextStyle?) {
        textStyle?.let {
            textView.applyTextStyle(textStyle)
        }
    }

    /**
     * Binds a [KalugaTextStyle] to a  [TextView] using [TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration]
     */
    @BindingAdapter("autoTextStyle", "autoTextMinScalingFactor")
    @JvmStatic
    fun bindAutoTextStyle(textView: TextView, autoTextStyle: KalugaTextStyle?, autoTextMinScalingFactor: Float) {
        autoTextStyle?.let {
            textView.applyTextStyle(it)
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView, max(1, (it.size * autoTextMinScalingFactor).toInt()), it.size.toInt(), 1, TypedValue.COMPLEX_UNIT_SP)
            textView.gravity = Gravity.CENTER_VERTICAL or it.alignment.alignment(textView.context).gravity
        }
    }

    /**
     * Binds a [KalugaTextStyle] to the [TextInputLayout.getEditText] of a [TextInputLayout]
     */
    @BindingAdapter("textStyle")
    @JvmStatic
    fun bindTextInputLayoutTextStyle(textView: TextInputLayout, textStyle: KalugaTextStyle?) {
        textStyle?.let {
            textView.editText?.applyTextStyle(it)
        }
    }

    /**
     * Binds a [KalugaTextStyle] to the [TextInputLayout.getPrefixTextView] of a [TextInputLayout]
     */
    @BindingAdapter("prefixTextStyle")
    @JvmStatic
    fun bindPrefixTextStyle(textView: TextInputLayout, textStyle: KalugaTextStyle?) {
        textStyle?.let {
            textView.prefixTextView.applyTextStyle(it)
        }
    }

    /**
     * Binds a [KalugaTextStyle] to the [TextInputLayout.getSuffixTextView] of a [TextInputLayout]
     */
    @BindingAdapter("suffixTextStyle")
    @JvmStatic
    fun bindSuffixTextStyle(textView: TextInputLayout, textStyle: KalugaTextStyle?) {
        textStyle?.let {
            textView.suffixTextView.applyTextStyle(it)
        }
    }
}
