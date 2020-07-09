/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.value
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.databinding.BluetoothDescriptorItemBinding
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothDescriptorViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothServiceViewModel
import kotlinx.android.synthetic.main.bluetooth_descriptor_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object DescriptorsBinding {

    @BindingAdapter("descriptors")
    @JvmStatic
    fun bindDescriptors(view: RecyclerView, descriptors: List<BluetoothDescriptorViewModel>?) {
        val descriptorAdapter = view.adapter as? BluetoothDescriptorAdapter
            ?: return
        descriptorAdapter.descriptors = descriptors ?: emptyList()
    }
}

class BluetoothDescriptorAdapter(private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<BluetoothDescriptorAdapter.BluetoothDescriptorItemViewHolder>() {

    class BluetoothDescriptorItemViewHolder(val descriptorItem: BluetoothDescriptorItemBinding) : RecyclerView.ViewHolder(descriptorItem.root)

    var descriptors = emptyList<BluetoothDescriptorViewModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BluetoothDescriptorItemViewHolder {
        val binding = BluetoothDescriptorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = lifecycleOwner
        return BluetoothDescriptorItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return descriptors.size
    }

    override fun onBindViewHolder(holder: BluetoothDescriptorItemViewHolder, position: Int) {
        holder.descriptorItem.viewModel = descriptors[position]
    }

    @ExperimentalStdlibApi
    override fun onViewAttachedToWindow(holder: BluetoothDescriptorItemViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.descriptorItem.viewModel?.didResume()
    }

    override fun onViewDetachedFromWindow(holder: BluetoothDescriptorItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.descriptorItem.viewModel?.didPause()
    }
}
