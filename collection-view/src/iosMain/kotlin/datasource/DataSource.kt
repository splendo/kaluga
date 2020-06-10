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

open class DataSource<Item, Cell : CollectionCellView>(private val source: Observable<List<Item>>, private val identifier: (Item) -> String, private val bindCell: (Cell, Item) -> Unit) {

    @Suppress("CONFLICTING_OVERLOADS")
    private val collectionViewDelegate = object : NSObject(), UICollectionViewDelegateProtocol, UICollectionViewDataSourceProtocol {

        var items: List<Item> = emptyList()

        override fun collectionView(collectionView: UICollectionView, cellForItemAtIndexPath: NSIndexPath): UICollectionViewCell {
            val item = items[cellForItemAtIndexPath.row.toInt()]
            return (collectionView.dequeueReusableCellWithReuseIdentifier(identifier(item), cellForItemAtIndexPath) as Cell).apply {
                bindCell(this, item)
            }
        }

        override fun collectionView(collectionView: UICollectionView, numberOfItemsInSection: NSInteger): NSInteger = items.size.toLong() as NSInteger

        override fun numberOfSectionsInCollectionView(collectionView: UICollectionView): NSInteger = 1
    }

    fun bindTo(collectionView: CollectionView): Disposable {
        collectionView.dataSource = collectionViewDelegate
        return source.observe {
            collectionViewDelegate.items = it
            collectionView.reloadData()
        }
    }

}
