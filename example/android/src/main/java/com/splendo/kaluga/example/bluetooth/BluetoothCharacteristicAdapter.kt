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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.descriptors
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.value
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.databinding.BluetoothCharacteristicItemBinding
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothCharacteristicViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothDescriptorViewModel
import kotlinx.android.synthetic.main.bluetooth_characteristic_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object CharacteristicsBinding {

    @BindingAdapter("characteristics")
    @JvmStatic
    fun bindCharacteristics(view: RecyclerView, characteristics: List<BluetoothCharacteristicViewModel>?) {
        val characteristicAdapter = view.adapter as? BluetoothCharacteristicAdapter
            ?: return
        characteristicAdapter.characteristics = characteristics ?: emptyList()
    }
}

class BluetoothCharacteristicAdapter(private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<BluetoothCharacteristicAdapter.BluetoothCharacteristicItemViewHolder>() {

    class BluetoothCharacteristicItemViewHolder(val characteristicItem: BluetoothCharacteristicItemBinding) : RecyclerView.ViewHolder(characteristicItem.root)

    internal var characteristics = emptyList<BluetoothCharacteristicViewModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BluetoothCharacteristicItemViewHolder {
        val binding = BluetoothCharacteristicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = lifecycleOwner
        binding.descriptors.adapter = BluetoothDescriptorAdapter(lifecycleOwner)
        return BluetoothCharacteristicItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return characteristics.size
    }

    override fun onBindViewHolder(holder: BluetoothCharacteristicItemViewHolder, position: Int) {
        holder.characteristicItem.viewModel = characteristics[position]
    }

    @ExperimentalStdlibApi
    override fun onViewAttachedToWindow(holder: BluetoothCharacteristicItemViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.characteristicItem.viewModel?.didResume()
    }

    override fun onViewDetachedFromWindow(holder: BluetoothCharacteristicItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.characteristicItem.viewModel?.didPause()
    }
}
