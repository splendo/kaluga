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

package com.splendo.kaluga.example.ui.collectionview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.collectionview.CollectionViewItem
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.list_collection_item.view.*

class CollectionViewAdapter : ListAdapter<CollectionViewItem, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    class ItemDiffCallback : DiffUtil.ItemCallback<CollectionViewItem>() {

        override fun areItemsTheSame(oldItem: CollectionViewItem, newItem: CollectionViewItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CollectionViewItem, newItem: CollectionViewItem): Boolean {
            return oldItem.title == newItem.title
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleLabel: TextView = view.titleLabel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_collection_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val viewHolder = holder as ItemViewHolder
        viewHolder.apply {
            titleLabel.text = item.title
        }
    }
}
