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
import com.splendo.kaluga.collectionview.item.CollectionSection

actual interface DataSourceBindingResult

actual interface HeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView> {
    actual fun bindCell(item: ItemType, cell: V)
}

actual interface ItemCellBinder<ItemType, V : CollectionItemCellView> {
    actual fun bindCell(item: ItemType, cell: V)
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
    actual fun bindTo(collectionView: CollectionView): DataSourceBindingResult {
        return object : DataSourceBindingResult {}
    }

    override fun notifyDataUpdated() {}
}
