package com.splendo.kaluga.example.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.bluetooth_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BluetoothAdapter(private val bluetooth: Bluetooth, private val lifecycle: Lifecycle) : RecyclerView.Adapter<BluetoothAdapter.BluetoothItemViewHolder>() {

    class BluetoothItemViewHolder(itemView: View, private val bluetooth: Bluetooth, private val lifecycle: Lifecycle) : RecyclerView.ViewHolder(itemView) {

        internal var device: Device? = null
        private var deviceJob: Job? = null

        private val context = itemView.context

        private val nameField = itemView.name
        private val identifierField = itemView.identifier
        private val rssiField = itemView.rssi
        private val txPowerField = itemView.txPower
        
        private val statusField = itemView.status

        private val connectButtonContainer = itemView.button_container
        private val connectButton = itemView.connect_button
        private val disconnectButton = itemView.disconnect_button

        private val foldOutBlock = itemView.fold_out_block
        private val serviceUuids = itemView.service_uuids
        private val serviceData = itemView.service_data
        private val manufacturerId = itemView.manufacturer_id
        private val manufacturerData = itemView.manufacturer_data

        init {
            itemView.setOnClickListener {
                val identifier = device?.identifier ?: return@setOnClickListener
                if (foldedOut.contains(identifier)) {
                    foldOutBlock.visibility = View.GONE
                    foldedOut.remove(identifier)
                } else {
                    foldOutBlock.visibility = View.VISIBLE
                    foldedOut.add(identifier)
                }
            }
            connectButton.setOnClickListener {
                val identifier = device?.identifier ?: return@setOnClickListener
                lifecycle.coroutineScope.launch {
                    bluetooth.devices()[identifier].connect()
                }
            }
            disconnectButton.setOnClickListener {
                val identifier = device?.identifier ?: return@setOnClickListener
                lifecycle.coroutineScope.launch {
                    bluetooth.devices()[identifier].disconnect()
                }
            }
        }
        
        @ExperimentalStdlibApi
        internal fun startUpdating() {
            deviceJob?.cancel()
            deviceJob = lifecycle.coroutineScope.launch { 
                device?.flow()?.collect { deviceState ->
                    nameField.text = deviceState.advertisementData.name ?: context.getText(R.string.bluetooth_no_name)
                    identifierField.text = deviceState.identifier
                    rssiField.text = context.getString(R.string.rssi).format(deviceState.rssi)
                    if (deviceState.advertisementData.txPowerLevel != Int.MIN_VALUE) {
                        txPowerField.visibility = View.VISIBLE
                        txPowerField.text = context.getString(R.string.txPower)
                            .format(deviceState.advertisementData.txPowerLevel)
                    } else {
                        txPowerField.visibility = View.INVISIBLE
                        txPowerField.text = ""
                    }

                    connectButtonContainer.visibility = if (deviceState.deviceInfo.advertisementData.isConnectible) View.VISIBLE else View.GONE

                    connectButton.visibility = when(deviceState) {
                        is DeviceState.Disconnected, is DeviceState.Disconnecting -> View.VISIBLE
                        else -> View.GONE
                    }
                    disconnectButton.visibility = when(deviceState) {
                        is DeviceState.Disconnected, is DeviceState.Disconnecting -> View.GONE
                        else -> View.VISIBLE
                    }

                    statusField.text = context.getString(when(deviceState) {
                        is DeviceState.Disconnecting -> R.string.bluetooth_disconneting
                        is DeviceState.Disconnected -> R.string.bluetooth_disconnected
                        is DeviceState.Connected -> R.string.bluetooth_connected
                        is DeviceState.Connecting -> R.string.bluetooth_connecting
                        is DeviceState.Reconnecting -> R.string.bluetooth_reconnecting
                    })

                    foldOutBlock.visibility = if (foldedOut.contains(deviceState.identifier)) View.VISIBLE else View.GONE

                    setServiceUUIDs(deviceState.advertisementData.serviceUUIDs)
                    setServiceData(deviceState.advertisementData.serviceData)
                    manufacturerId.text = context.getString(R.string.bluetooth_manufacturer_id).format(deviceState.advertisementData.manufacturerId ?: -1)
                    manufacturerData.text = context.getString(R.string.bluetooth_manufacturer_data).format(deviceState.advertisementData.manufacturerData?.toHexString() ?: "")
                }
            }
            
        }
        
        internal fun stopUpdating() {
            deviceJob?.cancel()
        }

        private fun setServiceUUIDs(uuids: List<UUID>) {
            val uuidString = uuids.fold("") { result, next ->
                if (result.isEmpty())
                    next.toString()
                else
                    "$result, $next"
            }
            serviceUuids.text = context.getString(R.string.bluetooth_service_uuids).format(uuidString)
        }

        @ExperimentalStdlibApi
        private fun setServiceData(data: Map<UUID, ByteArray?>) {
            val dataString = data.entries.fold("") { result, next ->
                val nextString = "${next.key}: ${next.value?.toHexString() ?: ""}"
                if (result.isEmpty())
                    nextString
                else
                    "$result/n$nextString"
            }
            serviceData.text = context.getString(R.string.bluetooth_service_data).format(dataString)
        }

        companion object {
            private val foldedOut: MutableSet<Identifier> = mutableSetOf()
        }
        
    }

    internal var bluetoothDevices: List<Device> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_item, parent, false)
        return BluetoothItemViewHolder(view, bluetooth, lifecycle)
    }

    override fun getItemCount(): Int = bluetoothDevices.size

    override fun onBindViewHolder(holder: BluetoothItemViewHolder, position: Int) {
        holder.device = bluetoothDevices[position]
    }

    @ExperimentalStdlibApi
    override fun onViewAttachedToWindow(holder: BluetoothItemViewHolder) {
        super.onViewAttachedToWindow(holder)
        
        holder.startUpdating()
    }

    override fun onViewDetachedFromWindow(holder: BluetoothItemViewHolder) {
        super.onViewDetachedFromWindow(holder)
        
        holder.stopUpdating()
    }
}
