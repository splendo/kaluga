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

import com.splendo.kaluga.architecture.observable.DefaultDisposable
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.collectionview.CollectionHeaderFooterCellView
import com.splendo.kaluga.collectionview.CollectionItemCellView
import com.splendo.kaluga.collectionview.CollectionView
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import com.splendo.kaluga.collectionview.item.CollectionSection
import com.splendo.kaluga.collectionview.item.itemAt
import com.splendo.kaluga.collectionview.item.sectionAt
import kotlin.native.ref.WeakReference
import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.CoreGraphics.CGSizeZero
import platform.Foundation.NSIndexPath
import platform.UIKit.UICollectionElementKindSectionFooter
import platform.UIKit.UICollectionElementKindSectionHeader
import platform.UIKit.UICollectionReusableView
import platform.UIKit.UICollectionView
import platform.UIKit.UICollectionViewCell
import platform.UIKit.UICollectionViewDataSourceProtocol
import platform.UIKit.UICollectionViewDelegateFlowLayoutProtocol
import platform.UIKit.UICollectionViewLayout
import platform.UIKit.UILayoutPriorityDefaultLow
import platform.UIKit.UILayoutPriorityRequired
import platform.UIKit.indexPathForRow
import platform.UIKit.layoutIfNeeded
import platform.UIKit.setNeedsLayout
import platform.UIKit.systemLayoutSizeFittingSize
import platform.darwin.NSInteger
import platform.darwin.NSObject

actual interface CollectionHeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView> : CellBinder<ItemType, V> {
    fun sizeForItem(collectionView: CollectionView, supplementaryKind: String, item: ItemType, at: NSIndexPath): CValue<CGSize>
    fun dequeueCell(collectionView: CollectionView, supplementaryKind: String, item: ItemType, at: NSIndexPath): V
}

open class SimpleCollectionHeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView>(
    private val identifier: String,
    private val bind: (ItemType, V) -> Unit,
    private val onAppear: ((V) -> Unit)? = null,
    private val onDisappear: ((V) -> Unit)? = null
) : CollectionHeaderFooterCellBinder<ItemType, V> {

    override fun sizeForItem(collectionView: CollectionView, supplementaryKind: String, item: ItemType, at: NSIndexPath): CValue<CGSize> {
        val cell = dequeueCell(collectionView, supplementaryKind, item, at)
        bindCell(item, cell)
        cell.setNeedsLayout()
        cell.layoutIfNeeded()
        return cell.systemLayoutSizeFittingSize(CGSizeMake(collectionView.bounds.useContents { size.width }, (Double.MAX_VALUE / 2.0) as CGFloat), UILayoutPriorityRequired, UILayoutPriorityDefaultLow)
    }

    override fun dequeueCell(collectionView: CollectionView, supplementaryKind: String, item: ItemType, at: NSIndexPath): V {
        return collectionView.dequeueReusableSupplementaryViewOfKind(supplementaryKind, identifier, at) as V
    }

    override fun bindCell(item: ItemType, cell: V) {
        bind(item, cell)
    }

    override fun notifyAppeared(cell: V) {
        onAppear?.invoke(cell)
    }

    override fun notifyDisappeared(cell: V) {
        onDisappear?.invoke(cell)
    }
}

actual interface CollectionItemCellBinder<ItemType, V : CollectionItemCellView> : CellBinder<ItemType, V> {
    fun dequeueCell(collectionView: CollectionView, item: ItemType, at: NSIndexPath): V
}

open class SimpleCollectionItemCellBinder<ItemType, V : CollectionItemCellView>(
    private val identifier: String,
    private val bind: (ItemType, V) -> Unit,
    private val onAppear: ((V) -> Unit)? = null,
    private val onDisappear: ((V) -> Unit)? = null
) : CollectionItemCellBinder<ItemType, V> {

    override fun dequeueCell(collectionView: CollectionView, item: ItemType, at: NSIndexPath): V {
        return collectionView.dequeueReusableCellWithReuseIdentifier(identifier, at) as V
    }

    override fun bindCell(item: ItemType, cell: V) {
        bind(item, cell)
    }

    override fun notifyAppeared(cell: V) {
        onAppear?.invoke(cell)
    }

    override fun notifyDisappeared(cell: V) {
        onDisappear?.invoke(cell)
    }
}

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

    private var boundCollectionView: WeakReference<CollectionView>? = null

    @Suppress("CONFLICTING_OVERLOADS")
    private val collectionViewDelegate = object : NSObject(), UICollectionViewDelegateFlowLayoutProtocol, UICollectionViewDataSourceProtocol {

        override fun collectionView(collectionView: UICollectionView, numberOfItemsInSection: NSInteger): NSInteger = (sections.getOrNull(numberOfItemsInSection.toInt())?.items?.size ?: 0).toLong() as NSInteger

        override fun numberOfSectionsInCollectionView(collectionView: UICollectionView): NSInteger = sections.size.toLong() as NSInteger

        override fun collectionView(collectionView: UICollectionView, layout: UICollectionViewLayout, referenceSizeForHeaderInSection: NSInteger): CValue<CGSize> {
            val headerBinder = headerBinder ?: return CGSizeZero.readValue()
            val header = sections.getOrNull(referenceSizeForHeaderInSection.toInt())?.header ?: return CGSizeZero.readValue()
            return headerBinder.sizeForItem(collectionView, UICollectionElementKindSectionHeader, header, NSIndexPath.indexPathForRow(0, referenceSizeForHeaderInSection))
        }

        override fun collectionView(collectionView: UICollectionView, layout: UICollectionViewLayout, referenceSizeForFooterInSection: NSInteger): CValue<CGSize> {
            val footerBinder = footerBinder ?: return CGSizeZero.readValue()
            val footer = sections.getOrNull(referenceSizeForFooterInSection.toInt())?.footer ?: return CGSizeZero.readValue()
            return footerBinder.sizeForItem(collectionView, UICollectionElementKindSectionFooter, footer, NSIndexPath.indexPathForRow(0, referenceSizeForFooterInSection))
        }

        override fun collectionView(collectionView: UICollectionView, cellForItemAtIndexPath: NSIndexPath): UICollectionViewCell {
            val item = sections.itemAt(cellForItemAtIndexPath)
            return itemBinder.dequeueCell(collectionView, item, cellForItemAtIndexPath).apply {
                itemBinder.bindCell(item, this)
            }
        }

        override fun collectionView(collectionView: UICollectionView, viewForSupplementaryElementOfKind: String, atIndexPath: NSIndexPath): UICollectionReusableView {
            val section = sections.sectionAt(atIndexPath)
            return when (viewForSupplementaryElementOfKind) {
                UICollectionElementKindSectionHeader -> {
                    val header = section.header ?: throw Exception("Missing Header")
                    headerBinder?.dequeueCell(collectionView, UICollectionElementKindSectionHeader, header, atIndexPath)?.apply {
                        headerBinder.bindCell(header, this)
                    } ?: throw Exception("Header Binder not Provided")
                }
                UICollectionElementKindSectionFooter -> {
                    val footer = section.footer ?: throw Exception("Missing Footer")
                    footerBinder?.dequeueCell(collectionView, UICollectionElementKindSectionFooter, footer, atIndexPath)?.apply {
                        footerBinder.bindCell(footer, this)
                    } ?: throw Exception("Footer Binder not Provided")
                }
                else -> throw Exception("Unknown SupplementaryElement $viewForSupplementaryElementOfKind")
            }
        }

        override fun collectionView(collectionView: UICollectionView, willDisplaySupplementaryView: UICollectionReusableView, forElementKind: String, atIndexPath: NSIndexPath) {
            val section = sections.sectionAt(atIndexPath)
            when (forElementKind) {
                UICollectionElementKindSectionHeader -> {
                    section.header?.let {
                        startDisplayingHeader(it, willDisplaySupplementaryView as HeaderCell)
                    }
                }
                UICollectionElementKindSectionFooter -> {
                    section.footer?.let {
                        startDisplayingFooter(it, willDisplaySupplementaryView as FooterCell)
                    }
                }
            }
        }

        override fun collectionView(collectionView: UICollectionView, didEndDisplayingSupplementaryView: UICollectionReusableView, forElementOfKind: String, atIndexPath: NSIndexPath) {
            val section = sections.sectionAt(atIndexPath)
            when (forElementOfKind) {
                UICollectionElementKindSectionHeader -> {
                    section.header?.let {
                        stopDisplayingHeader(it, didEndDisplayingSupplementaryView as HeaderCell)
                    }
                }
                UICollectionElementKindSectionFooter -> {
                    section.footer?.let {
                        stopDisplayingFooter(it, didEndDisplayingSupplementaryView as FooterCell)
                    }
                }
            }
        }

        override fun collectionView(collectionView: UICollectionView, willDisplayCell: UICollectionViewCell, forItemAtIndexPath: NSIndexPath) {
            val item = sections.itemAt(forItemAtIndexPath)
            startDisplayingItem(item, willDisplayCell as ItemCell)
        }

        override fun collectionView(collectionView: UICollectionView, didEndDisplayingCell: UICollectionViewCell, forItemAtIndexPath: NSIndexPath) {
            val item = sections.itemAt(forItemAtIndexPath)
            stopDisplayingItem(item, didEndDisplayingCell as ItemCell)
        }

        override fun collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath: NSIndexPath) {
            onClick(sections.itemAt(didSelectItemAtIndexPath), collectionView, didSelectItemAtIndexPath)
        }
    }

    override fun notifyDataUpdated() {
        boundCollectionView?.get()?.reloadData()
    }

    open fun onClick(item: Item, collectionView: CollectionView, indexPath: NSIndexPath) {
        if (item is CollectionItemViewModel<*>) {
            item.onSelected()
        }
        collectionView.deselectItemAtIndexPath(indexPath, true)
    }

    internal fun bindTo(collectionView: CollectionView): DataSourceBindingResult {
        boundCollectionView?.get()?.let {
            it.delegate = null
            it.dataSource = null
        }
        collectionView.delegate = collectionViewDelegate
        collectionView.dataSource = collectionViewDelegate
        boundCollectionView = WeakReference(collectionView)
        val disposable = source.observe {
            sections = it
        }
        return DefaultDisposable {
            disposable.dispose()
            boundCollectionView?.get()?.let {
                it.delegate = null
                it.dataSource = null
            }
            boundCollectionView = null
        }
    }
}

actual fun <
    Header,
    Item,
    Footer,
    HeaderCell : CollectionHeaderFooterCellView,
    ItemCell : CollectionItemCellView,
    FooterCell : CollectionHeaderFooterCellView> CollectionDataSource<Header, Item, Footer, HeaderCell, ItemCell, FooterCell>.bindCollectionView(collectionView: CollectionView): DataSourceBindingResult {
    return bindTo(collectionView)
}
