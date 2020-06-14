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

interface AndroidCellBinder<ItemType, V : View> {
    val supportedViewTypes: Set<Int>
    fun viewType(item: ItemType): Int
    fun createView(parent: ViewGroup, viewType: Int): V
    fun bindCell(item: ItemType, cell: V)
    fun notifyAppeared(cell: V)
    fun notifyDisappeared(cell: V)
}

actual interface CollectionHeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView> : CellBinder<ItemType, V>, AndroidCellBinder<ItemType, V>
actual interface CollectionItemCellBinder<ItemType, V : CollectionItemCellView> : CellBinder<ItemType, V>, AndroidCellBinder<ItemType, V>

class SimpleItemBinder<ItemType, V : View>(
    override val supportedViewTypes: Set<Int>,
    private val identifier: (ItemType) -> Int,
    private val layout: (Int) -> Int,
    private val bind: (ItemType, V) -> Unit,
    private val onAppeared: ((V) -> Unit)? = null,
    private val onDisappeared: ((V) -> Unit)? = null
) : AndroidCellBinder<ItemType, V> {

    override fun viewType(item: ItemType): Int = identifier(item)

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

open class SimpleHeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView>(supportedViewTypes: Set<Int>, identifier: (ItemType) -> Int, layout: (Int) -> Int, bind: (ItemType, V) -> Unit) : CollectionHeaderFooterCellBinder<ItemType, V>, AndroidCellBinder<ItemType, V> by SimpleItemBinder(supportedViewTypes, identifier, layout, bind)

open class SimpleItemCellBinder<ItemType, V : CollectionItemCellView>(supportedViewTypes: Set<Int>, identifier: (ItemType) -> Int, layout: (Int) -> Int, bind: (ItemType, V) -> Unit) : CollectionItemCellBinder<ItemType, V>, AndroidCellBinder<ItemType, V> by SimpleItemBinder(supportedViewTypes, identifier, layout, bind)

actual open class CollectionDataSource<
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView>actual constructor(
        source: Observable<List<Section>>,
        headerBinder: CollectionHeaderFooterCellBinder<Header, HeaderCell>?,
        itemBinder: CollectionItemCellBinder<Item, ItemCell>,
        footerBinder: CollectionHeaderFooterCellBinder<Footer, FooterCell>?
    ) : DataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell, CollectionHeaderFooterCellBinder<Header, HeaderCell>, CollectionItemCellBinder<Item, ItemCell>, CollectionHeaderFooterCellBinder<Footer, FooterCell>>(source, headerBinder, itemBinder, footerBinder) {

    private var currentCollectionView: CollectionView? = null

    private val collectionViewAdapter = object : RecyclerView.Adapter<ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>>() {

        override fun getItemCount(): Int = totalNumberOfElements

        override fun getItemViewType(position: Int): Int = itemViewType(position)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell> = this@CollectionDataSource.createViewHolder(parent, viewType)

        override fun onBindViewHolder(holder: ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>, position: Int) {
            val sectionType = sectionTypeAtAbsolutePosition(position)
            when (holder) {
                is ViewHolder.HeaderViewHolder -> {
                    (sectionType as? SectionType.HeaderType)?.let { headerType ->
                        holder.header = headerType.header
                        headerBinder?.bindCell(headerType.header, holder.cell)
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
                    }
                }
            }
        }

        override fun onViewAttachedToWindow(holder: ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>) {
            when (holder) {
                is ViewHolder.HeaderViewHolder -> {
                    holder.header?.let { startDisplayingHeader(it, holder.cell) }
                }
                is ViewHolder.ItemViewHolder -> {
                    holder.item?.let { startDisplayingItem(it, holder.cell) }
                }
                is ViewHolder.FooterViewHolder -> {
                    holder.footer?.let { startDisplayingFooter(it, holder.cell) }
                }
            }
        }

        override fun onViewDetachedFromWindow(holder: ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>) {
            when (holder) {
                is ViewHolder.HeaderViewHolder -> {
                    holder.header?.let { stopDisplayingHeader(it, holder.cell) }
                }
                is ViewHolder.ItemViewHolder -> {
                    holder.item?.let { stopDisplayingItem(it, holder.cell) }
                }
                is ViewHolder.FooterViewHolder -> {
                    holder.footer?.let { stopDisplayingFooter(it, holder.cell) }
                }
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

        class HeaderViewHolder<
            Header,
            Item,
            Footer,
            HeaderCell : CollectionHeaderFooterCellView,
            ItemCell : CollectionItemCellView,
            FooterCell : CollectionHeaderFooterCellView>(var header: Header?, val cell: HeaderCell) : ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>(cell)

        class ItemViewHolder<
            Header,
            Item,
            Footer,
            HeaderCell : CollectionHeaderFooterCellView,
            ItemCell : CollectionItemCellView,
            FooterCell : CollectionHeaderFooterCellView>(var item: Item?, val cell: ItemCell) : ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>(cell)

        class FooterViewHolder<
            Header,
            Item,
            Footer,
            HeaderCell : CollectionHeaderFooterCellView,
            ItemCell : CollectionItemCellView,
            FooterCell : CollectionHeaderFooterCellView>(var footer: Footer?, val cell: FooterCell) : ViewHolder<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>(cell)
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

    open fun addOnClick(item: Item, rootView: ItemCell) {
        if (item is CollectionItemViewModel<*>) {
            rootView.setOnClickListener { item.onSelected() }
        }
    }

    internal fun bindTo(
        collectionView: CollectionView
    ): DataSourceBindingResult {
        currentCollectionView?.adapter = null
        currentCollectionView = collectionView
        return object : LifecycleObserver {
            val observer = Observer<List<Section>> {
                sections = it
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun startObserving() {
                currentCollectionView?.adapter = collectionViewAdapter
                source.liveData.observeForever(observer)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun stopObserving() {
                currentCollectionView?.adapter = null
                source.liveData.removeObserver(observer)
            }
        }
    }
}

actual fun <
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView> CollectionDataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell>.bindCollectionView(collectionView: CollectionView): DataSourceBindingResult {
    return bindTo(collectionView)
}
