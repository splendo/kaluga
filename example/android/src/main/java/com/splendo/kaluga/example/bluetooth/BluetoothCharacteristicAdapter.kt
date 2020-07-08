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
import androidx.lifecycle.Lifecycle
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
import kotlinx.android.synthetic.main.bluetooth_characteristic_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BluetoothCharacteristicAdapter(private val characteristicsFlow: Flow<List<Characteristic>>, private val lifecycle: Lifecycle) : RecyclerView.Adapter<BluetoothCharacteristicAdapter.BluetoothCharacteristicItemViewHolder>() {

    class BluetoothCharacteristicItemViewHolder(itemView: View, private val characteristicsFlow: Flow<List<Characteristic>>, private val lifecycle: Lifecycle) : RecyclerView.ViewHolder(itemView) {

        private val uuid = itemView.characteristic_uuid
        private val valueField = itemView.characteristic_value
        private val descriptors = itemView.descriptors
        private var descriptorsAdapter: BluetoothDescriptorAdapter? = null
            set(value) {
                descriptors.adapter = value
                field = value
            }

        private var characteristic: Characteristic? = null
        private var valueJob: Job? = null

        fun bindData(characteristic: Characteristic) {
            descriptors.addItemDecoration(DividerItemDecoration(itemView.context, LinearLayoutManager.VERTICAL))
            this.characteristic = characteristic
            uuid.text = characteristic.uuid.toString()
            descriptorsAdapter = BluetoothDescriptorAdapter(characteristicsFlow[characteristic.uuid].descriptors(), lifecycle)

        }

        @ExperimentalStdlibApi
        internal fun startUpdating() {
            valueJob?.cancel()
            valueJob = lifecycle.coroutineScope.launch {
                val characteristicUUID = characteristic?.uuid ?: return@launch
                characteristicsFlow[characteristicUUID].first()?.readValue()
                characteristicsFlow[characteristicUUID].value().asLiveData().observe({lifecycle}) {
                    valueField.text = it?.toHexString()
                }
            }
            descriptorsAdapter?.startMonitoring()
        }

        internal fun stopUpdating() {
            valueJob?.cancel()
            descriptorsAdapter?.stopMonitoring()
        }

    }

    private var characteristics = emptyList<Characteristic>()
    private var characteristicsJob: Job? = null

    fun startMonitoring() {
        characteristicsJob?.cancel()
        characteristicsJob = lifecycle.coroutineScope.launch {
            characteristicsFlow.collect{ newCharacteristics ->
                characteristics = newCharacteristics
                notifyDataSetChanged()
            }
        }
    }

    fun stopMonitoring() {
        characteristicsJob?.cancel()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BluetoothCharacteristicItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_characteristic_item, parent, false)
        return BluetoothCharacteristicItemViewHolder(view, characteristicsFlow, lifecycle)
    }

    override fun getItemCount(): Int {
        return characteristics.size
    }

    override fun onBindViewHolder(holder: BluetoothCharacteristicItemViewHolder, position: Int) {
        holder.bindData(characteristics[position])
    }

    @ExperimentalStdlibApi
    override fun onViewAttachedToWindow(holder: BluetoothCharacteristicItemViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.startUpdating()
    }

    override fun onViewDetachedFromWindow(holder: BluetoothCharacteristicItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.stopUpdating()
    }
}
