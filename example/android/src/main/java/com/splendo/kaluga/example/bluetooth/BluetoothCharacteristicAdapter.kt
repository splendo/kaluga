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

package com.splendo.kaluga.example.bluetooth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.example.databinding.BluetoothCharacteristicItemBinding
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothCharacteristicViewModel

object CharacteristicBinding {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothCharacteristicItemViewHolder {
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

    override fun onViewAttachedToWindow(holder: BluetoothCharacteristicItemViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.characteristicItem.viewModel?.onResume()
    }

    override fun onViewDetachedFromWindow(holder: BluetoothCharacteristicItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.characteristicItem.viewModel?.onPause()
    }
}
