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
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class DataBindingCellBinder<ItemType, Binding : ViewDataBinding>(
    override val supportedViewTypes: Set<Int>,
    private val viewTypeByItem: (ItemType) -> Int,
    private val bindingLayout: (Int) -> Int,
    private val bind: (ItemType, Binding) -> Unit,
    private val onAppeared: ((Binding) -> Unit)? = null,
    private val onDisappeared: ((Binding) -> Unit)? = null
) : AndroidCellBinder<ItemType, View> {

    override fun viewType(item: ItemType): Int = viewTypeByItem(item)

    override fun createView(parent: ViewGroup, viewType: Int): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DataBindingUtil.inflate<Binding>(layoutInflater, bindingLayout(viewType), parent, false).root
    }

    override fun bindCell(item: ItemType, cell: View) {
        DataBindingUtil.getBinding<Binding>(cell)?.let {
            bind(item, it)
        }
    }

    override fun notifyAppeared(cell: View) {
        DataBindingUtil.getBinding<Binding>(cell)?.let {
            onAppeared?.invoke(it)
        }
    }

    override fun notifyDisappeared(cell: View) {
        DataBindingUtil.getBinding<Binding>(cell)?.let {
            onDisappeared?.invoke(it)
        }
    }
}

open class DataBindingHeaderFooterCellBinder<ItemType, Binding : ViewDataBinding> private constructor(
    supportedViewTypes: Set<Int>,
    viewTypeByItem: (ItemType) -> Int,
    bindingLayout: (Int) -> Int,
    bind: (ItemType, Binding) -> Unit,
    onAppeared: ((Binding) -> Unit)? = null,
    onDisappeared: ((Binding) -> Unit)? = null
) : CollectionHeaderFooterCellBinder<ItemType, View>, AndroidCellBinder<ItemType, View> by DataBindingCellBinder(supportedViewTypes, viewTypeByItem, bindingLayout, bind, onAppeared, onDisappeared) {

    companion object {
        fun <ItemType, Binding : ViewDataBinding> create(
            supportedViewTypes: Set<Int>,
            viewTypeByItem: (ItemType) -> Int,
            layout: (Int) -> Int,
            bind: (ItemType, Binding) -> Unit,
            onAppeared: ((Binding) -> Unit)? = null,
            onDisappeared: ((Binding) -> Unit)? = null
        ): DataBindingHeaderFooterCellBinder<ItemType, Binding> {
            return DataBindingHeaderFooterCellBinder(supportedViewTypes, viewTypeByItem, layout, bind, onAppeared, onDisappeared)
        }

        fun <ItemType, Binding : ViewDataBinding> create(
            isHeader: Boolean,
            layout: Int,
            bind: (ItemType, Binding) -> Unit,
            onAppeared: ((Binding) -> Unit)? = null,
            onDisappeared: ((Binding) -> Unit)? = null
        ): DataBindingHeaderFooterCellBinder<ItemType, Binding> {
            val viewType = if (isHeader) AndroidCellBinder.defaultHeaderViewType else AndroidCellBinder.defaultFooterViewType
            return DataBindingHeaderFooterCellBinder(setOf(viewType), { viewType }, { layout }, bind, onAppeared, onDisappeared)
        }
    }
}

open class DataBindingItemCellBinder<ItemType, Binding : ViewDataBinding>(
    supportedViewTypes: Set<Int>,
    viewTypeByItem: (ItemType) -> Int,
    bindingLayout: (Int) -> Int,
    bind: (ItemType, Binding) -> Unit,
    onAppeared: ((Binding) -> Unit)? = null,
    onDisappeared: ((Binding) -> Unit)? = null
) : CollectionItemCellBinder<ItemType, View>, AndroidCellBinder<ItemType, View> by DataBindingCellBinder(supportedViewTypes, viewTypeByItem, bindingLayout, bind, onAppeared, onDisappeared) {

    companion object {
        fun <ItemType, Binding : ViewDataBinding> create(
            supportedViewTypes: Set<Int>,
            viewTypeByItem: (ItemType) -> Int,
            layout: (Int) -> Int,
            bind: (ItemType, Binding) -> Unit,
            onAppeared: ((Binding) -> Unit)? = null,
            onDisappeared: ((Binding) -> Unit)? = null
        ): DataBindingItemCellBinder<ItemType, Binding> {
            return DataBindingItemCellBinder(supportedViewTypes, viewTypeByItem, layout, bind, onAppeared, onDisappeared)
        }

        fun <ItemType, Binding : ViewDataBinding> create(
            layout: Int,
            bind: (ItemType, Binding) -> Unit,
            onAppeared: ((Binding) -> Unit)? = null,
            onDisappeared: ((Binding) -> Unit)? = null
        ): DataBindingItemCellBinder<ItemType, Binding> {
            return DataBindingItemCellBinder(setOf(AndroidCellBinder.defaultItemViewType), { AndroidCellBinder.defaultItemViewType }, { layout }, bind, onAppeared, onDisappeared)
        }
    }
}
