/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.example.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityResourcesColorBinding
import com.splendo.kaluga.example.databinding.ViewResourceListBackgroundBinding
import com.splendo.kaluga.example.shared.viewmodel.resources.ColorViewModel
import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.stylable.BackgroundStyle
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.resources.view.KalugaLabel
import com.splendo.kaluga.resources.view.applyBackgroundStyle
import com.splendo.kaluga.resources.view.bindButton
import com.splendo.kaluga.resources.view.bindLabel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ColorActivity : KalugaViewModelActivity<ColorViewModel>() {

    override val viewModel: ColorViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityResourcesColorBinding.inflate(layoutInflater, null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.backdropEdit.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.submitBackdropText(v.text.toString())
                true
            } else {
                false
            }
        }

        binding.sourceEdit.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.submitSourceText(v.text.toString())
                true
            } else {
                false
            }
        }

        binding.backdropLighten.adapter = BackgroundAdapter()
        binding.backdropDarken.adapter = BackgroundAdapter()
        binding.blendedLighten.adapter = BackgroundAdapter()
        binding.blendedDarken.adapter = BackgroundAdapter()
        binding.sourceLighten.adapter = BackgroundAdapter()
        binding.sourceDarken.adapter = BackgroundAdapter()
    }
}

class BackgroundAdapter : RecyclerView.Adapter<BackgroundAdapter.BackgroundViewHolder>() {

    class BackgroundViewHolder(val binding: ViewResourceListBackgroundBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding.root
    }

    var backgrounds: List<BackgroundStyle> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundViewHolder {
        val binding = ViewResourceListBackgroundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BackgroundViewHolder(binding)
    }

    override fun getItemCount(): Int = backgrounds.size

    override fun onBindViewHolder(holder: BackgroundViewHolder, position: Int) {
        backgrounds.getOrNull(position)?.let { background ->
            holder.view.applyBackgroundStyle(background)
        } ?: run {
            holder.view.setBackgroundColor(DefaultColors.clear)
        }
    }
}

object ResourcesBinding {

    @BindingAdapter("button")
    @JvmStatic
    fun bindButton(view: Button, button: KalugaButton?) {
        button?.let {
            view.bindButton(it)
        }
    }

    @BindingAdapter("label")
    @JvmStatic
    fun bindLabel(view: TextView, label: KalugaLabel?) {
        label?.let {
            view.bindLabel(it)
        }
    }

    @BindingAdapter("backgroundStyle")
    @JvmStatic
    fun bindBackgroundStyle(view: View, backgroundStyle: BackgroundStyle?) {
        backgroundStyle?.let {
            view.applyBackgroundStyle(it)
        }
    }

    @BindingAdapter("backgrounds")
    @JvmStatic
    fun bindBackgrounds(recyclerView: RecyclerView, backgrounds: List<BackgroundStyle>?) {
        val adapter = recyclerView.adapter as? BackgroundAdapter ?: return
        adapter.backgrounds = backgrounds ?: emptyList()
    }
}
