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
import com.splendo.kaluga.example.databinding.BluetoothServiceItemBinding
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothServiceViewModel

object ServiceBinding {
    @BindingAdapter("services")
    @JvmStatic
    fun bindServices(view: RecyclerView, services: List<BluetoothServiceViewModel>?) {
        val serviceAdapter = view.adapter as? BluetoothServiceAdapter
            ?: return
        serviceAdapter.services = services ?: emptyList()
    }
}

class BluetoothServiceAdapter(private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<BluetoothServiceAdapter.BluetoothServiceItemViewHolder>() {

    class BluetoothServiceItemViewHolder(val serviceItem: BluetoothServiceItemBinding) : RecyclerView.ViewHolder(serviceItem.root)

    internal var services: List<BluetoothServiceViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothServiceItemViewHolder {
        val binding = BluetoothServiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = lifecycleOwner
        binding.characteristicsList.adapter = BluetoothCharacteristicAdapter(lifecycleOwner)
        return BluetoothServiceItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    override fun onBindViewHolder(holder: BluetoothServiceItemViewHolder, position: Int) {
        holder.serviceItem.viewModel = services[position]
    }
}
