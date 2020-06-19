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

actual interface CollectionHeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView> : CellBinder<ItemType, V>

actual interface CollectionItemCellBinder<ItemType, V : CollectionItemCellView> : CellBinder<ItemType, V>

actual open class CollectionDataSource<
    Header,
    Item,
    Footer,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView>actual constructor(
        source: Observable<List<CollectionSection<Header, Item, Footer>>>,
        headerBinder: CollectionHeaderFooterCellBinder<Header, HeaderCell>?,
        itemBinder: CollectionItemCellBinder<Item, ItemCell>,
        footerBinder: CollectionHeaderFooterCellBinder<Footer, FooterCell>?
    ) : DataSource<Header, Item, Footer, HeaderCell, ItemCell, FooterCell, CollectionHeaderFooterCellBinder<Header, HeaderCell>, CollectionItemCellBinder<Item, ItemCell>, CollectionHeaderFooterCellBinder<Footer, FooterCell>>(source, headerBinder, itemBinder, footerBinder) {

    override fun notifyDataUpdated() {}
}

actual fun <
    Header,
    Item,
    Footer,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView> CollectionDataSource<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>.bindCollectionView(collectionView: CollectionView): DataSourceBindingResult {
    return object : DataSourceBindingResult {}
}
