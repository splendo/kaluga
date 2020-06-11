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

import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.collectionview.CollectionCellView
import com.splendo.kaluga.collectionview.CollectionView
import com.splendo.kaluga.logging.debug

actual typealias DataSourceBindingResult = LifecycleObserver

actual open class DataSource<Item, Cell : CollectionCellView>(
    private val source: Observable<List<Item>>,
    private val viewType: (Item) -> Int,
    private val createCell: (ViewGroup, Int) -> Cell,
    private val bindCell: (Item, Cell) -> Unit
) {

    protected var items: List<Item> = emptyList()

    private val collectionViewAdapter = object : RecyclerView.Adapter<ViewHolder<Item, Cell>>() {

        override fun getItemCount(): Int = numberOfItems

        override fun getItemViewType(position: Int): Int = itemViewType(position)
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Item, Cell> = this@DataSource.createViewHolder(parent, viewType)

        override fun onBindViewHolder(holder: ViewHolder<Item, Cell>, position: Int) {
            bindCell(items[position], holder.cell)
        }

        override fun onViewAttachedToWindow(holder: ViewHolder<Item, Cell>) {
            holder.item?.let { startDisplayingItem(it) }
        }

        override fun onViewDetachedFromWindow(holder: ViewHolder<Item, Cell>) {
            holder.item?.let { stopDisplayingItem(it) }
        }
    }

    class ViewHolder<Item, Cell : CollectionCellView>(val cell: Cell, var item: Item? = null) : RecyclerView.ViewHolder(cell)
    
    protected open val numberOfItems: Int get() = items.size
    
    protected open fun itemViewType(position: Int): Int = viewType(items[position])
    
    protected open fun createViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Item, Cell> = ViewHolder(createCell(parent, viewType))

    open fun startDisplayingItem(item: Item) {}

    open fun stopDisplayingItem(item: Item) {}

    actual fun bindTo(
        collectionView: CollectionView
    ): DataSourceBindingResult {
        collectionView.adapter = collectionViewAdapter
        return LifeCycleObserver(source) {
            items = it
            collectionViewAdapter.notifyDataSetChanged()
        }
    }
}

actual class DataSourceBuilder<Item, Cell : CollectionCellView>(
    private val viewType: (Item) -> Int,
    private val createCell: (ViewGroup, Int) -> Cell
) : BaseDataSourceBuilder<Item, Cell, DataSource<Item, Cell>> {
    
    constructor(createSingleCell: () -> Cell) : this({1}, { _, _ -> createSingleCell()})
    
    override fun create(items: Observable<List<Item>>, bindCell: (Item, Cell) -> Unit): DataSource<Item, Cell> = DataSource(items, viewType, createCell, bindCell)
}

private class LifeCycleObserver<Item>(private val source: Observable<List<Item>>, private val onChanged: (List<Item>) -> Unit) : LifecycleObserver {

    val observer = Observer<List<Item>> {
        onChanged(it)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startObserving() {
        source.liveData.observeForever(observer)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopObserving() {
        source.liveData.removeObserver(observer)
    }
}
