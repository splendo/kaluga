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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.collectionview.CollectionHeaderFooterCellView
import com.splendo.kaluga.collectionview.CollectionItemCellView
import com.splendo.kaluga.collectionview.CollectionView
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import com.splendo.kaluga.collectionview.item.CollectionSection

actual typealias DataSourceBindingResult = LifecycleObserver

interface CellBinder<ItemType, V : View> {
    val supportedViewTypes: Set<Int>
    fun viewType(item: ItemType): Int
    fun createView(parent: ViewGroup, viewType: Int): V
    fun bindCell(item: ItemType, cell: V)
}

actual interface HeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView> : CellBinder<ItemType, V> {
    actual override fun bindCell(item: ItemType, cell: V)
}
actual interface ItemCellBinder<ItemType, V : CollectionItemCellView> : CellBinder<ItemType, V> {
    actual override fun bindCell(item: ItemType, cell: V)
}

actual open class DataSource<
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView>actual constructor(
    source: Observable<List<Section>>,
    headerBinder: HeaderFooterCellBinder<Header, HeaderCell>?,
    itemBinder: ItemCellBinder<Item, ItemCell>,
    footerBinder: HeaderFooterCellBinder<Footer, FooterCell>?
) : BaseDataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell>(source, headerBinder, itemBinder, footerBinder) {

    private val collectionViewAdapter = object : RecyclerView.Adapter<ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>>() {

        override fun getItemCount(): Int = totalNumberOfElements

        override fun getItemViewType(position: Int): Int = itemViewType(position)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell> = this@DataSource.createViewHolder(parent, viewType)

        override fun onBindViewHolder(holder: ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>, position: Int) {
            val sectionType = sectionTypeAtAbsolutePosition(position)
            when (holder) {
                is ViewHolder.HeaderViewHolder -> {
                    (sectionType as? SectionType.HeaderType)?.let { headerType ->
                        holder.header = headerType.header
                        headerBinder?.bindCell(headerType.header, holder.cell)
                        addOnClick(headerType.header, holder.cell)
                    }
                }
                is ViewHolder.ItemViewHolder -> {
                    (sectionType as? SectionType.ItemType)?.let { itemType ->
                        holder.item = itemType.item
                        itemBinder.bindCell(itemType.item, holder.cell)
                        addOnClick(itemType.item, holder.cell)
                    }
                }
                is ViewHolder.FooterViewHolder -> {
                    (sectionType as? SectionType.FooterType)?.let { footerType ->
                        holder.footer = footerType.footer
                        footerBinder?.bindCell(footerType.footer, holder.cell)
                        addOnClick(footerType.footer, holder.cell)
                    }
                }
            }
        }

        override fun onViewAttachedToWindow(holder: ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>) {
            holder.item?.let { startDisplayingItem(it) }
        }

        override fun onViewDetachedFromWindow(holder: ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>) {
            holder.item?.let { stopDisplayingItem(it) }
        }

        private fun <T> addOnClick(item: T, rootView: View) {
            if (item is CollectionItemViewModel<*>) {
                rootView.setOnClickListener { item.onSelected() }
            }
        }

    }

    protected sealed class ViewHolder<
        Header,
        Item,
        Footer,
        HeaderCell : CollectionHeaderFooterCellView,
        ItemCell : CollectionItemCellView,
        FooterCell : CollectionHeaderFooterCellView>(cell: View) : RecyclerView.ViewHolder(cell) {

        abstract val item: Any?

        class HeaderViewHolder<
            Header,
            Item,
            Footer,
            HeaderCell : CollectionHeaderFooterCellView,
            ItemCell : CollectionItemCellView,
            FooterCell : CollectionHeaderFooterCellView>(var header: Header?, val cell: HeaderCell) : ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>(cell) {
            override val item: Any?
                get() = header
        }

        class ItemViewHolder<
            Header,
            Item,
            Footer,
            HeaderCell : CollectionHeaderFooterCellView,
            ItemCell : CollectionItemCellView,
            FooterCell : CollectionHeaderFooterCellView>(override var item: Item?, val cell: ItemCell) : ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>(cell)

        class FooterViewHolder<
            Header,
            Item,
            Footer,
            HeaderCell : CollectionHeaderFooterCellView,
            ItemCell : CollectionItemCellView,
            FooterCell : CollectionHeaderFooterCellView>(var footer: Footer?, val cell: FooterCell) : ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>(cell) {
            override val item: Any?
                get() = footer
        }
    }

    protected open fun itemViewType(position: Int): Int {
        return when (val sectionType = sectionTypeAtAbsolutePosition(position)) {
            is SectionType.HeaderType -> headerBinder?.viewType(sectionType.header)
            is SectionType.ItemType -> itemBinder.viewType(sectionType.item)
            is SectionType.FooterType -> footerBinder?.viewType(sectionType.footer)
            else -> null
        } ?: 0
    }

    protected open fun createViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell> {
        return when {
            headerBinder != null && headerBinder.supportedViewTypes.contains(viewType) -> {
                ViewHolder.HeaderViewHolder(null, headerBinder.createView(parent, viewType))
            }
            footerBinder != null && footerBinder.supportedViewTypes.contains(viewType) -> {
                ViewHolder.FooterViewHolder(null, footerBinder.createView(parent, viewType))
            }
            else -> ViewHolder.ItemViewHolder(null, itemBinder.createView(parent, viewType))
        }
    }

    override fun notifyDataUpdated() = collectionViewAdapter.notifyDataSetChanged()

    actual fun bindTo(
        collectionView: CollectionView
    ): DataSourceBindingResult {
        collectionView.adapter = collectionViewAdapter
        return object : LifecycleObserver {
            val observer = Observer<List<Section>> {
                sections = it
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
    }
}
