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

import com.splendo.kaluga.collectionview.TableHeaderFooterCellView
import com.splendo.kaluga.collectionview.TableItemCellView
import com.splendo.kaluga.collectionview.TableView

actual typealias TableHeaderFooterCellBinder<ItemType, V> = CollectionHeaderFooterCellBinder<ItemType, V>

actual typealias TableItemCellBinder<ItemType, V> = CollectionItemCellBinder<ItemType, V>

actual typealias TableDataSource<
    Header,
    Item,
    Footer,
    HeaderCell,
    ItemCell,
    FooterCell> = CollectionDataSource<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>

actual fun <
    Header,
    Item,
    Footer,
    HeaderCell : TableHeaderFooterCellView,
    ItemCell : TableItemCellView,
    FooterCell : TableHeaderFooterCellView> TableDataSource<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>.bindTableView(tableView: TableView): DataSourceBindingResult {
    return bindTo(tableView)
}
