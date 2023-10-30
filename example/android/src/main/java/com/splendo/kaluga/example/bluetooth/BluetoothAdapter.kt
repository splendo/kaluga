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
import com.splendo.kaluga.example.databinding.BluetoothItemBinding
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListDeviceViewModel

object DeviceBinding {
    @BindingAdapter("devices")
    @JvmStatic
    fun bindDevices(view: RecyclerView, devices: List<BluetoothListDeviceViewModel>?) {
        val bluetoothAdapter = view.adapter as? BluetoothAdapter
            ?: return
        bluetoothAdapter.bluetoothDevices = devices ?: emptyList()
    }
}

class BluetoothAdapter(private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<BluetoothAdapter.BluetoothItemViewHolder>() {

    class BluetoothItemViewHolder(val binding: BluetoothItemBinding) : RecyclerView.ViewHolder(binding.root)

    internal var bluetoothDevices: List<BluetoothListDeviceViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BluetoothItemBinding.inflate(inflater, parent, false)
        binding.lifecycleOwner = lifecycleOwner
        return BluetoothItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bluetoothDevices.size
    }

    override fun onBindViewHolder(holder: BluetoothItemViewHolder, position: Int) {
        val viewModel = bluetoothDevices[position]
        holder.binding.viewModel = viewModel
    }
}
