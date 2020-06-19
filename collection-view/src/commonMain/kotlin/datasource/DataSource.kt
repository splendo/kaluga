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
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import com.splendo.kaluga.collectionview.item.CollectionSection

expect interface DataSourceBindingResult

interface CellBinder<ItemType, V> {
    fun bindCell(item: ItemType, cell: V)
    fun notifyAppeared(cell: V)
    fun notifyDisappeared(cell: V)
}

abstract class DataSource<
    Header,
    Item,
    Footer,
    HeaderCell,
    ItemCell,
    FooterCell,
    HeaderBinder : CellBinder<Header, HeaderCell>,
    ItemBinder : CellBinder<Item, ItemCell>,
    FooterBinder : CellBinder<Footer, FooterCell>>(
        protected val source: Observable<List<CollectionSection<Header, Item, Footer>>>,
        protected val headerBinder: HeaderBinder? = null,
        protected val itemBinder: ItemBinder,
        protected val footerBinder: FooterBinder? = null
    ) {

    protected var sections: List<CollectionSection<Header, Item, Footer>> = emptyList()
        set(value) {
            field = value
            notifyDataUpdated()
        }

    sealed class SectionType<Header, Item, Footer> {
        class HeaderType<Header, Item, Footer>(val header: Header) : SectionType<Header, Item, Footer>()
        class ItemType<Header, Item, Footer>(val item: Item) : SectionType<Header, Item, Footer>()
        class FooterType<Header, Item, Footer>(val footer: Footer) : SectionType<Header, Item, Footer>()
    }

    abstract fun notifyDataUpdated()

    val totalNumberOfElements: Int get() = sections.fold(0, { acc, section -> acc + section.totalNumberOfElements })

    protected fun sectionTypeAtAbsolutePosition(position: Int): SectionType<Header, Item, Footer>? {
        var offset = 0
        sections.forEach { section ->
            val relativePosition = position - offset
            val totalNumberOfElements = section.totalNumberOfElements
            if (relativePosition < totalNumberOfElements) {
                return if (section.hasHeader && relativePosition == 0) {
                    section.header?.let { header ->
                        SectionType.HeaderType<Header, Item, Footer>(header)
                    }
                } else if (section.hasFooter && relativePosition == totalNumberOfElements - 1) {
                    section.footer?.let { footer ->
                        SectionType.FooterType<Header, Item, Footer>(footer)
                    }
                } else {
                    section.items.getOrNull(relativePosition - if (section.hasHeader) 1 else 0)?.let { item ->
                        SectionType.ItemType<Header, Item, Footer>(item)
                    }
                }
            }
            offset += totalNumberOfElements
        }
        return null
    }

    protected fun startDisplayingHeader(header: Header, cell: HeaderCell) {
        headerBinder?.notifyAppeared(cell)
        startDisplayingItem(header)
    }

    protected fun startDisplayingItem(item: Item, cell: ItemCell) {
        itemBinder.notifyAppeared(cell)
        startDisplayingItem(item)
    }

    protected fun startDisplayingFooter(footer: Footer, cell: FooterCell) {
        footerBinder?.notifyAppeared(cell)
        startDisplayingItem(footer)
    }

    protected fun stopDisplayingHeader(header: Header, cell: HeaderCell) {
        headerBinder?.notifyDisappeared(cell)
        stopDisplayingItem(header)
    }

    protected fun stopDisplayingItem(item: Item, cell: ItemCell) {
        itemBinder.notifyDisappeared(cell)
        stopDisplayingItem(item)
    }

    protected fun stopDisplayingFooter(footer: Footer, cell: FooterCell) {
        footerBinder?.notifyDisappeared(cell)
        stopDisplayingItem(footer)
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
