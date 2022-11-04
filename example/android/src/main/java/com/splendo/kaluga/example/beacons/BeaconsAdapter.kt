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

package com.splendo.kaluga.example.beacons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.example.databinding.BeaconItemBinding
import com.splendo.kaluga.example.shared.viewmodel.beacons.BeaconsListBeaconViewModel

class BeaconsAdapter(private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<BeaconsAdapter.BeaconItemViewHolder>() {

    class BeaconItemViewHolder(val binding: BeaconItemBinding) : RecyclerView.ViewHolder(binding.root)

    internal var beacons: List<BeaconsListBeaconViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BeaconItemBinding.inflate(inflater, parent, false)
        binding.lifecycleOwner = lifecycleOwner
        return BeaconItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beacons.size
    }

    override fun onBindViewHolder(holder: BeaconItemViewHolder, position: Int) {
        val viewModel = beacons[position]
        holder.binding.viewModel = viewModel
    }

    override fun onViewAttachedToWindow(holder: BeaconItemViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.binding.viewModel?.didResume()
    }

    override fun onViewDetachedFromWindow(holder: BeaconItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.binding.viewModel?.didPause()
    }
}
