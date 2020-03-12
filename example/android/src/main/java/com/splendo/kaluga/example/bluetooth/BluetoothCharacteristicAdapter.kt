package com.splendo.kaluga.example.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.bluetooth_characteristic_item.view.*

class BluetoothCharacteristicAdapter(private val characteristics: List<Characteristic>) : RecyclerView.Adapter<BluetoothCharacteristicAdapter.BluetoothCharacteristicItemViewHolder>() {

    class BluetoothCharacteristicItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val uuid = itemView.characteristic_uuid
        private val descriptors = itemView.descriptors

        fun bindData(characteristic: Characteristic) {
            uuid.text = characteristic.uuid.toString()
            descriptors.text = characteristic.descriptors.fold("") { result, descriptor ->
                val descriptorString = descriptor.uuid.toString()
                if (result.isEmpty())
                    descriptorString
                else
                    "$result\n$descriptorString"
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BluetoothCharacteristicItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_characteristic_item, parent, false)
        return BluetoothCharacteristicItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return characteristics.size
    }

    override fun onBindViewHolder(holder: BluetoothCharacteristicItemViewHolder, position: Int) {
        holder.bindData(characteristics[position])
    }



}