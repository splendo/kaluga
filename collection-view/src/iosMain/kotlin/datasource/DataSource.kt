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

import com.splendo.kaluga.architecture.observable.Disposable
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.collectionview.CollectionCellView
import com.splendo.kaluga.collectionview.CollectionView
import platform.Foundation.NSIndexPath
import platform.UIKit.UICollectionView
import platform.UIKit.UICollectionViewCell
import platform.UIKit.UICollectionViewDataSourceProtocol
import platform.UIKit.UICollectionViewDelegateProtocol
import platform.UIKit.row
import platform.darwin.NSInteger
import platform.darwin.NSObject

actual open class DataSource<Item, Cell : CollectionCellView>(private val source: Observable<List<Item>>, private val identifier: (Item) -> String) {

    private var bindCell: (Item, Cell) -> Unit = { _, _ ->}
    protected var items: List<Item> = emptyList()

    @Suppress("CONFLICTING_OVERLOADS")
    private val collectionViewDelegate = object : NSObject(), UICollectionViewDelegateProtocol, UICollectionViewDataSourceProtocol {

        override fun collectionView(collectionView: UICollectionView, cellForItemAtIndexPath: NSIndexPath): UICollectionViewCell {
            val item = items[cellForItemAtIndexPath.row.toInt()]
            return (collectionView.dequeueReusableCellWithReuseIdentifier(identifier(item), cellForItemAtIndexPath) as Cell).apply {
                bindCell(item, this)
            }
        }

        override fun collectionView(collectionView: UICollectionView, numberOfItemsInSection: NSInteger): NSInteger = cellsInSection(numberOfItemsInSection) as NSInteger

        override fun numberOfSectionsInCollectionView(collectionView: UICollectionView): NSInteger = numberOfSections() as NSInteger

        override fun collectionView(collectionView: UICollectionView, willDisplayCell: UICollectionViewCell, forItemAtIndexPath: NSIndexPath) {
            val item = items[forItemAtIndexPath.row.toInt()]
            startDisplayingItem(item)
        }

        override fun collectionView(collectionView: UICollectionView, didEndDisplayingCell: UICollectionViewCell, forItemAtIndexPath: NSIndexPath) {
            val item = items[forItemAtIndexPath.row.toInt()]
            stopDisplayingItem(item)
        }

    }

    actual fun bindTo(collectionView: CollectionView, bindCell: (Item, Cell) -> Unit): DataSourceBindingResult {
        this.bindCell = bindCell
        collectionView.dataSource = collectionViewDelegate
        return source.observe {
            items = it
            collectionView.reloadData()
        }
    }

    open fun numberOfSections(): Long = 1

    open fun cellsInSection(section: Long): Long {
        return if (section == 0L) {
            items.size.toLong()
        } else {
            0
        }
    }

    open fun startDisplayingItem(item: Item) {}

    open fun stopDisplayingItem(item: Item) {}
}

actual typealias DataSourceBindingResult = Disposable

actual class DataSourceBuilder<Item, Cell : CollectionCellView>(private val identifier: (Item) -> String) : BaseDataSourceBuilder<Item, Cell, DataSource<Item, Cell>> {
    override fun create(items: Observable<List<Item>>): DataSource<Item, Cell> = DataSource(items, identifier)
}
