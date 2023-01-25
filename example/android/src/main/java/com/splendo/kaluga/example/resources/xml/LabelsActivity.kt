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

package com.splendo.kaluga.example.resources.xml

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityLabelBinding
import com.splendo.kaluga.example.databinding.ViewListTextViewBinding
import com.splendo.kaluga.example.shared.viewmodel.resources.LabelViewModel
import com.splendo.kaluga.example.view.VerticalSpaceItemDecoration
import com.splendo.kaluga.resources.dpToPixel
import com.splendo.kaluga.resources.view.KalugaLabel
import com.splendo.kaluga.resources.view.bindLabel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LabelActivity : KalugaViewModelActivity<LabelViewModel>() {

    override val viewModel: LabelViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLabelBinding.inflate(layoutInflater, null, false)
        binding.labelsList.adapter = LabelAdapter()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        binding.labelsList.addItemDecoration(VerticalSpaceItemDecoration(10.0f.dpToPixel(this).toInt()))
    }
}

object LabelsBinding {
    @BindingAdapter("labels")
    @JvmStatic
    fun bindLabels(view: RecyclerView, labels: List<KalugaLabel>?) {
        val adapter = (view.adapter as? LabelAdapter) ?: return
        adapter.labels = labels.orEmpty()
    }
}

class LabelAdapter : RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {

    class LabelViewHolder(val binding: ViewListTextViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val label = binding.label
    }

    var labels: List<KalugaLabel> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val binding = ViewListTextViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LabelViewHolder(binding)
    }

    override fun getItemCount(): Int = labels.size

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        labels.getOrNull(position)?.let { label ->
            holder.label.bindLabel(label)
        } ?: run {
            holder.label.text = null
        }
    }
}
