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

package com.splendo.kaluga.collectionview.datasource

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SimpleItemBinder<ItemType, V : View> internal constructor(
    override val supportedViewTypes: Set<Int>,
    private val viewTypeByItem: (ItemType) -> Int,
    private val layout: (Int) -> Int,
    private val bind: (ItemType, V) -> Unit,
    private val onAppeared: ((V) -> Unit)?,
    private val onDisappeared: ((V) -> Unit)?
) : AndroidCellBinder<ItemType, V> {

    override fun viewType(item: ItemType): Int = viewTypeByItem(item)

    override fun createView(parent: ViewGroup, viewType: Int): V = LayoutInflater
        .from(parent.context)
        .inflate(layout(viewType), parent, false) as V

    override fun bindCell(item: ItemType, cell: V) = bind(item, cell)

    override fun notifyAppeared(cell: V) {
        onAppeared?.invoke(cell)
    }

    override fun notifyDisappeared(cell: V) {
        onDisappeared?.invoke(cell)
    }
}

class SimpleHeaderFooterCellBinder<ItemType> private constructor(
    supportedViewTypes: Set<Int>,
    viewTypeByItem: (ItemType) -> Int,
    layout: (Int) -> Int,
    bind: (ItemType, View) -> Unit,
    onAppeared: ((View) -> Unit)? = null,
    onDisappeared: ((View) -> Unit)? = null
) : CollectionHeaderFooterCellBinder<ItemType, View>, AndroidCellBinder<ItemType, View> by SimpleItemBinder(supportedViewTypes, viewTypeByItem, layout, bind, onAppeared, onDisappeared) {

    companion object {

        fun <ItemType> create(
            supportedViewTypes: Set<Int>,
            viewTypeByItem: (ItemType) -> Int,
            layout: (Int) -> Int,
            bind: (ItemType, View) -> Unit,
            onAppeared: ((View) -> Unit)? = null,
            onDisappeared: ((View) -> Unit)? = null
        ): SimpleHeaderFooterCellBinder<ItemType> {
            return SimpleHeaderFooterCellBinder(supportedViewTypes, viewTypeByItem, layout, bind, onAppeared, onDisappeared)
        }

        fun <ItemType> create(
            isHeader: Boolean,
            layout: Int,
            bind: (ItemType, View) -> Unit,
            onAppeared: ((View) -> Unit)? = null,
            onDisappeared: ((View) -> Unit)? = null
        ): SimpleHeaderFooterCellBinder<ItemType> {
            val viewType = if (isHeader) AndroidCellBinder.defaultHeaderViewType else AndroidCellBinder.defaultFooterViewType
            return SimpleHeaderFooterCellBinder(setOf(viewType), { viewType }, { layout }, bind, onAppeared, onDisappeared)
        }
    }
}

open class SimpleItemCellBinder<ItemType> private constructor(
    supportedViewTypes: Set<Int>,
    viewTypeByItem: (ItemType) -> Int,
    layout: (Int) -> Int,
    bind: (ItemType, View) -> Unit,
    onAppeared: ((View) -> Unit)? = null,
    onDisappeared: ((View) -> Unit)? = null
) : CollectionItemCellBinder<ItemType, View>, AndroidCellBinder<ItemType, View> by SimpleItemBinder(supportedViewTypes, viewTypeByItem, layout, bind, onAppeared, onDisappeared) {

    companion object {
        fun <ItemType> create(
            supportedViewTypes: Set<Int>,
            viewTypeByItem: (ItemType) -> Int,
            layout: (Int) -> Int,
            bind: (ItemType, View) -> Unit,
            onAppeared: ((View) -> Unit)? = null,
            onDisappeared: ((View) -> Unit)? = null
        ): SimpleItemCellBinder<ItemType> {
            return SimpleItemCellBinder(supportedViewTypes, viewTypeByItem, layout, bind, onAppeared, onDisappeared)
        }

        fun <ItemType> create(
            layout: Int,
            bind: (ItemType, View) -> Unit,
            onAppeared: ((View) -> Unit)? = null,
            onDisappeared: ((View) -> Unit)? = null
        ): SimpleItemCellBinder<ItemType> {
            return SimpleItemCellBinder(setOf(AndroidCellBinder.defaultItemViewType), { AndroidCellBinder.defaultItemViewType }, { layout }, bind, onAppeared, onDisappeared)
        }
    }
}
