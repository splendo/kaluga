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
import com.splendo.kaluga.collectionview.CollectionHeaderFooterCellView
import com.splendo.kaluga.collectionview.CollectionItemCellView

class SimpleItemBinder<ItemType, V : View>(
    private val viewType: Int,
    private val layout: Int,
    private val bind: (ItemType, V) -> Unit,
    private val onAppeared: ((V) -> Unit)?,
    private val onDisappeared: ((V) -> Unit)?
) : AndroidCellBinder<ItemType, V> {

    override val supportedViewTypes: Set<Int> = setOf(viewType)

    override fun viewType(item: ItemType): Int = viewType

    override fun createView(parent: ViewGroup, viewType: Int): V = LayoutInflater
        .from(parent.context)
        .inflate(layout, parent, false) as V

    override fun bindCell(item: ItemType, cell: V) = bind(item, cell)

    override fun notifyAppeared(cell: V) {
        onAppeared?.invoke(cell)
    }

    override fun notifyDisappeared(cell: V) {
        onDisappeared?.invoke(cell)
    }
}

open class SimpleHeaderFooterCellBinder<ItemType>(
    isHeader: Boolean,
    layout: Int,
    bind: (ItemType, View) -> Unit,
    onAppeared: ((View) -> Unit)? = null,
    onDisappeared: ((View) -> Unit)? = null
) : CollectionHeaderFooterCellBinder<ItemType, View>, AndroidCellBinder<ItemType, View> by SimpleItemBinder(if (isHeader) AndroidCellBinder.defaultHeaderViewType else AndroidCellBinder.defaultFooterViewType, layout, bind, onAppeared, onDisappeared)

open class SimpleItemCellBinder<ItemType>(
    layout: Int,
    bind: (ItemType, View) -> Unit,
    onAppeared: ((View) -> Unit)? = null,
    onDisappeared: ((View) -> Unit)? = null
) : CollectionItemCellBinder<ItemType, View>, AndroidCellBinder<ItemType, View> by SimpleItemBinder(AndroidCellBinder.defaultItemViewType, layout, bind, onAppeared, onDisappeared)
