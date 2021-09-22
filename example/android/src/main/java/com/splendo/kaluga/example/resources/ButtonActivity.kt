/*
 * Copyright 2021 Splendo Consulting B.V. The Netherlands
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
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityResourcesBinding
import com.splendo.kaluga.example.databinding.ViewListButtonBinding
import com.splendo.kaluga.example.shared.viewmodel.resources.ButtonViewModel
import com.splendo.kaluga.example.view.HorizontalSpaceItemDecoration
import com.splendo.kaluga.example.view.VerticalSpaceItemDecoration
import com.splendo.kaluga.resources.dpToPixel
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.resources.view.bindButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class ButtonActivity : KalugaViewModelActivity<ButtonViewModel>() {

    override val viewModel: ButtonViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityResourcesBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        val adapter = ButtonAdapter().apply {

            binding.resourcesList.adapter = this
        }
        viewModel.buttons.observeInitialized { adapter.buttons = it }
        binding.resourcesList.layoutManager = GridLayoutManager(this, 2)
        binding.resourcesList.addItemDecoration(VerticalSpaceItemDecoration(10.0f.dpToPixel(this).toInt()))
        binding.resourcesList.addItemDecoration(HorizontalSpaceItemDecoration(10.0f.dpToPixel(this).toInt()))
    }
}

class ButtonAdapter : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    class ButtonViewHolder(val binding: ViewListButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        val button = binding.button
    }

    var buttons: List<KalugaButton<*>> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val binding = ViewListButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ButtonViewHolder(binding)
    }

    override fun getItemCount(): Int = buttons.size

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        buttons.getOrNull(position)?.let { button ->
            holder.button.bindButton(button)
        } ?: run {
            holder.button.text = null
            holder.button.background = null
            holder.button.setOnClickListener(null)
        }
    }
}
