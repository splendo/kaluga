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
import com.splendo.kaluga.collectionview.item.ItemsOnlyCollectionSection

expect interface CollectionHeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView> : CellBinder<ItemType, V>

expect interface CollectionItemCellBinder<ItemType, V : CollectionItemCellView> : CellBinder<ItemType, V>

expect open class CollectionDataSource<
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView>(
        source: Observable<List<Section>>,
        headerBinder: CollectionHeaderFooterCellBinder<Header, HeaderCell>? = null,
        itemBinder: CollectionItemCellBinder<Item, ItemCell>,
        footerBinder: CollectionHeaderFooterCellBinder<Footer, FooterCell>? = null
    ) : DataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell, CollectionHeaderFooterCellBinder<Header, HeaderCell>, CollectionItemCellBinder<Item, ItemCell>, CollectionHeaderFooterCellBinder<Footer, FooterCell>>

expect fun <
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView> CollectionDataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell>.bindCollectionView(collectionView: CollectionView): DataSourceBindingResult

class SimpleCollectionDataSource<Item, ItemCell : CollectionItemCellView>(
    source: Observable<List<Item>>,
    itemBinder: CollectionItemCellBinder<Item, ItemCell>
) : CollectionDataSource<Nothing, Item, Nothing, ItemsOnlyCollectionSection<Item>, Nothing, ItemCell, Nothing>(source.map { items -> listOf(ItemsOnlyCollectionSection(items)) }, null, itemBinder, null)
