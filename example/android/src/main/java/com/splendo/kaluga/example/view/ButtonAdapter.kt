/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.example.databinding.ViewListButtonBinding
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.resources.view.bindButton

object ButtonsBinding {
    @BindingAdapter("buttons")
    @JvmStatic
    fun bindButtons(view: RecyclerView, buttons: List<KalugaButton>?) {
        val adapter = (view.adapter as? ButtonAdapter) ?: return
        adapter.buttons = buttons.orEmpty()
    }
}

class ButtonAdapter : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    class ButtonViewHolder(val binding: ViewListButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        val button = binding.button
    }

    var buttons: List<KalugaButton> = emptyList()
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
