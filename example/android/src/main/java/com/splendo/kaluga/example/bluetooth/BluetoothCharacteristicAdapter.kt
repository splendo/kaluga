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
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.bluetooth_characteristic_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BluetoothCharacteristicAdapter(private val bluetooth: Bluetooth, private val identifier: Identifier, private val serviceUUID: UUID, private val lifecycle: Lifecycle) : RecyclerView.Adapter<BluetoothCharacteristicAdapter.BluetoothCharacteristicItemViewHolder>() {

    class BluetoothCharacteristicItemViewHolder(itemView: View, private val bluetooth: Bluetooth, private val identifier: Identifier, private val serviceUUID: UUID, private val lifecycle: Lifecycle) : RecyclerView.ViewHolder(itemView) {

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
            descriptorsAdapter = BluetoothDescriptorAdapter(bluetooth, identifier, serviceUUID, characteristic.uuid, lifecycle)

        }

        @ExperimentalStdlibApi
        internal fun startUpdating() {
            valueJob?.cancel()
            valueJob = lifecycle.coroutineScope.launch {
                val characteristicUUID = characteristic?.uuid ?: return@launch
                bluetooth.devices()[identifier].services()[serviceUUID].characteristics()[characteristicUUID].first()?.readValue()
                bluetooth.devices()[identifier].services()[serviceUUID].characteristics()[characteristicUUID].value().asLiveData().observe({lifecycle}) {
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
            bluetooth.devices()[identifier].services()[serviceUUID].characteristics().collect{ newCharacteristics ->
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
        return BluetoothCharacteristicItemViewHolder(view, bluetooth, identifier, serviceUUID, lifecycle)
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