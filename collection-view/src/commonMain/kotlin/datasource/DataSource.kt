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

import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.collectionview.CollectionHeaderFooterCellView
import com.splendo.kaluga.collectionview.CollectionItemCellView
import com.splendo.kaluga.collectionview.CollectionView
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import com.splendo.kaluga.collectionview.item.CollectionSection

expect interface DataSourceBindingResult

expect interface HeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView> {
    fun bindCell(item: ItemType, cell: V)
}

expect interface ItemCellBinder<ItemType, V : CollectionItemCellView> {
    fun bindCell(item: ItemType, cell: V)
}

abstract class BaseDataSource<
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView>(
    protected val source: Observable<List<Section>>,
    protected val headerBinder: HeaderFooterCellBinder<Header, HeaderCell>? = null,
    protected val itemBinder: ItemCellBinder<Item, ItemCell>,
    protected val footerBinder: HeaderFooterCellBinder<Footer, FooterCell>? = null
) {

    protected var sections: List<Section> = emptyList()
        set(value) {
            field = value
            notifyDataUpdated()
        }

    sealed class SectionType<Header, Item, Footer> {
        class HeaderType<Header, Item, Footer>(val header: Header): SectionType<Header, Item, Footer>()
        class ItemType<Header, Item, Footer>(val item: Item): SectionType<Header, Item, Footer>()
        class FooterType<Header, Item, Footer>(val footer: Footer): SectionType<Header, Item, Footer>()
    }

    abstract fun notifyDataUpdated()

    val totalNumberOfElements: Int get() = sections.fold(0, { acc, section -> acc + section.totalNumberOfElements })

    protected fun sectionTypeAtAbsolutePosition(position: Int): SectionType<Header, Item, Footer>? {
        var offset = 0
        var result: SectionType<Header, Item, Footer>? = null
        sections.forEach { section ->
            val relativePosition = position - offset
            val totalNumberOfElements = section.totalNumberOfElements
            if (relativePosition < totalNumberOfElements) {
                result = if (section.hasHeader && relativePosition == 0) {
                    section.header?.let {
                        SectionType.HeaderType(it)
                    }
                }
                else if (section.hasFooter && relativePosition == totalNumberOfElements - 1) {
                    section.footer?.let {
                        SectionType.FooterType(it)
                    }
                }
                else {
                    section.items.getOrNull(relativePosition - if (section.hasHeader) 1 else 0)?.let {
                        SectionType.ItemType(it)
                    }
                }
                return@forEach
            }
            offset += totalNumberOfElements
        }
        return result
    }

    open fun <I> startDisplayingItem(item: I) {
        if (item is CollectionItemViewModel<*>) {
            item.didResume()
        }
    }

    open fun <I> stopDisplayingItem(item: I) {
        if (item is CollectionItemViewModel<*>) {
            item.didPause()
        }
    }
}

expect open class DataSource<
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView>(
    source: Observable<List<Section>>,
    headerBinder: HeaderFooterCellBinder<Header, HeaderCell>? = null,
    itemBinder: ItemCellBinder<Item, ItemCell>,
    footerBinder: HeaderFooterCellBinder<Footer, FooterCell>? = null
) : BaseDataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell> {
    fun bindTo(collectionView: CollectionView): DataSourceBindingResult
}
