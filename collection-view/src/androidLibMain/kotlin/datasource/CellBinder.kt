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

import android.view.View
import android.view.ViewGroup
import com.splendo.kaluga.collectionview.CollectionHeaderFooterCellView
import com.splendo.kaluga.collectionview.CollectionItemCellView

interface AndroidCellBinder<ItemType, V : View> {
    companion object {
        const val defaultHeaderViewType = 100
        const val defaultItemViewType = 200
        const val defaultFooterViewType = 300
    }
    val supportedViewTypes: Set<Int>
    fun viewType(item: ItemType): Int
    fun createView(parent: ViewGroup, viewType: Int): V
    fun bindCell(item: ItemType, cell: V)
    fun notifyAppeared(cell: V)
    fun notifyDisappeared(cell: V)
}

actual interface CollectionHeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView> : CellBinder<ItemType, V>, AndroidCellBinder<ItemType, V>
actual interface CollectionItemCellBinder<ItemType, V : CollectionItemCellView> : CellBinder<ItemType, V>, AndroidCellBinder<ItemType, V>
