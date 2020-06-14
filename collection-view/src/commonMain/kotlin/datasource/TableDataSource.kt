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
import com.splendo.kaluga.collectionview.TableHeaderFooterCellView
import com.splendo.kaluga.collectionview.TableItemCellView
import com.splendo.kaluga.collectionview.TableView
import com.splendo.kaluga.collectionview.item.CollectionSection
import com.splendo.kaluga.collectionview.item.ItemsOnlyCollectionSection

expect interface TableHeaderFooterCellBinder<ItemType, V : TableHeaderFooterCellView> : CellBinder<ItemType, V>

expect interface TableItemCellBinder<ItemType, V : TableItemCellView> : CellBinder<ItemType, V>

expect open class TableDataSource<
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : TableHeaderFooterCellView,
    ItemCell : TableItemCellView,
    FooterCell : TableHeaderFooterCellView>(
        source: Observable<List<Section>>,
        headerBinder: TableHeaderFooterCellBinder<Header, HeaderCell>? = null,
        itemBinder: TableItemCellBinder<Item, ItemCell>,
        footerBinder: TableHeaderFooterCellBinder<Footer, FooterCell>? = null
    ) : DataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell, TableHeaderFooterCellBinder<Header, HeaderCell>, TableItemCellBinder<Item, ItemCell>, TableHeaderFooterCellBinder<Footer, FooterCell>>

expect fun <
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : TableHeaderFooterCellView,
    ItemCell : TableItemCellView,
    FooterCell : TableHeaderFooterCellView> TableDataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell>.bindTableView(tableView: TableView): DataSourceBindingResult

class SimpleTableDataSource<Item, ItemCell : TableItemCellView>(
    source: Observable<List<Item>>,
    itemBinder: TableItemCellBinder<Item, ItemCell>
) : TableDataSource<Nothing, Item, Nothing, ItemsOnlyCollectionSection<Item>, Nothing, ItemCell, Nothing>(source.map { items -> listOf(ItemsOnlyCollectionSection(items)) }, null, itemBinder, null)
