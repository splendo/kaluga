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
import com.splendo.kaluga.architecture.observable.Disposable
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.collectionview.CollectionHeaderFooterCellView
import com.splendo.kaluga.collectionview.CollectionItemCellView
import com.splendo.kaluga.collectionview.CollectionView
import com.splendo.kaluga.collectionview.item.CollectionSection
import kotlin.native.ref.WeakReference
import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeZero
import platform.Foundation.NSIndexPath
import platform.UIKit.UICollectionElementKindSectionFooter
import platform.UIKit.UICollectionElementKindSectionHeader
import platform.UIKit.UICollectionReusableView
import platform.UIKit.UICollectionView
import platform.UIKit.UICollectionViewCell
import platform.UIKit.UICollectionViewDataSourceProtocol
import platform.UIKit.UICollectionViewDelegateFlowLayoutProtocol
import platform.UIKit.UICollectionViewFlowLayoutAutomaticSize
import platform.UIKit.UICollectionViewLayout
import platform.UIKit.row
import platform.UIKit.section
import platform.darwin.NSInteger
import platform.darwin.NSObject

actual interface HeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView> {
    fun sizeForItem(item: ItemType): CGSize
    fun dequeueCell(collectionView: CollectionView, supplementaryKind: String, item: ItemType, at: NSIndexPath): V
    actual fun bindCell(item: ItemType, cell: V)
}

open class SimpleHeaderFooterCellBinder<ItemType, V : CollectionHeaderFooterCellView>(private val identifier: (ItemType) -> String, private val bind: (ItemType, V) -> Unit) : HeaderFooterCellBinder<ItemType, V> {

    override fun sizeForItem(item: ItemType): CGSize = UICollectionViewFlowLayoutAutomaticSize

    override fun dequeueCell(collectionView: CollectionView, supplementaryKind: String, item: ItemType, at: NSIndexPath): V {
        return collectionView.dequeueReusableSupplementaryViewOfKind(supplementaryKind, identifier(item), at) as V
    }

    override fun bindCell(item: ItemType, cell: V) {
        bind(item, cell)
    }
}

actual interface ItemCellBinder<ItemType, V : CollectionItemCellView> {
    fun sizeForItem(item: ItemType): CGSize
    fun dequeueCell(collectionView: CollectionView, item: ItemType, at: NSIndexPath): V
    actual fun bindCell(item: ItemType, cell: V)
}

open class SimpleItemCellBinder<ItemType, V : CollectionItemCellView>(private val identifier: (ItemType) -> String, private val bind: (ItemType, V) -> Unit) : ItemCellBinder<ItemType, V> {

    override fun sizeForItem(item: ItemType): CGSize = UICollectionViewFlowLayoutAutomaticSize

    override fun dequeueCell(collectionView: CollectionView, item: ItemType, at: NSIndexPath): V {
        return collectionView.dequeueReusableCellWithReuseIdentifier(identifier(item), at) as V
    }

    override fun bindCell(item: ItemType, cell: V) {
        bind(item, cell)
    }
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

    private val boundCollectionViews: MutableSet<WeakReference<CollectionView>> = mutableSetOf()

    @Suppress("CONFLICTING_OVERLOADS")
    private val collectionViewDelegate = object : NSObject(), UICollectionViewDelegateFlowLayoutProtocol, UICollectionViewDataSourceProtocol {

        override fun collectionView(collectionView: UICollectionView, numberOfItemsInSection: NSInteger): NSInteger = (sections.getOrNull(numberOfItemsInSection.toInt())?.items?.size ?: 0).toLong() as NSInteger

        override fun numberOfSectionsInCollectionView(collectionView: UICollectionView): NSInteger = sections.size.toLong() as NSInteger

        override fun collectionView(collectionView: UICollectionView, layout: UICollectionViewLayout, sizeForItemAtIndexPath: NSIndexPath): CValue<CGSize> {
            val item = itemAt(sizeForItemAtIndexPath)
            return itemBinder.sizeForItem(item).readValue()
        }

        override fun collectionView(collectionView: UICollectionView, layout: UICollectionViewLayout, referenceSizeForHeaderInSection: NSInteger): CValue<CGSize> {
            return (sections.getOrNull(referenceSizeForHeaderInSection.toInt())?.header?.let { headerBinder?.sizeForItem(it) } ?: CGSizeZero).readValue()
        }

        override fun collectionView(collectionView: UICollectionView, layout: UICollectionViewLayout, referenceSizeForFooterInSection: NSInteger): CValue<CGSize> {
            return (sections.getOrNull(referenceSizeForFooterInSection.toInt())?.footer?.let { footerBinder?.sizeForItem(it) } ?: CGSizeZero).readValue()
        }

        override fun collectionView(collectionView: UICollectionView, cellForItemAtIndexPath: NSIndexPath): UICollectionViewCell {
            val item = itemAt(cellForItemAtIndexPath)
            return itemBinder.dequeueCell(collectionView, item, cellForItemAtIndexPath).apply {
                itemBinder.bindCell(item, this)
            }
        }

        override fun collectionView(collectionView: UICollectionView, viewForSupplementaryElementOfKind: String, atIndexPath: NSIndexPath): UICollectionReusableView {
            val section = sectionAt(atIndexPath)
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
            val section = sectionAt(atIndexPath)
            when (forElementKind) {
                UICollectionElementKindSectionHeader -> section.header
                UICollectionElementKindSectionFooter -> section.footer
                else -> null
            }?.let { startDisplayingItem(it) }
        }

        override fun collectionView(collectionView: UICollectionView, didEndDisplayingSupplementaryView: UICollectionReusableView, forElementOfKind: String, atIndexPath: NSIndexPath) {
            val section = sectionAt(atIndexPath)
            when (forElementOfKind) {
                UICollectionElementKindSectionHeader -> section.header
                UICollectionElementKindSectionFooter -> section.footer
                else -> null
            }?.let { stopDisplayingItem(it) }
        }

        override fun collectionView(collectionView: UICollectionView, willDisplayCell: UICollectionViewCell, forItemAtIndexPath: NSIndexPath) {
            val item = itemAt(forItemAtIndexPath)
            startDisplayingItem(item)
        }

        override fun collectionView(collectionView: UICollectionView, didEndDisplayingCell: UICollectionViewCell, forItemAtIndexPath: NSIndexPath) {
            val item = itemAt(forItemAtIndexPath)
            stopDisplayingItem(item)
        }
    }

    fun sectionAt(indexPath: NSIndexPath): Section = sections[indexPath.section.toInt()]

    fun itemAt(indexPath: NSIndexPath): Item = sections[indexPath.section.toInt()].items[indexPath.row.toInt()]

    override fun notifyDataUpdated() {
        boundCollectionViews.forEach { it.get()?.reloadData() }
    }

    actual fun bindTo(collectionView: CollectionView): DataSourceBindingResult {
        collectionView.delegate = collectionViewDelegate
        collectionView.dataSource = collectionViewDelegate
        val reference = WeakReference(collectionView)
        boundCollectionViews.add(reference)
        val disposable = source.observe {
            sections = it
        }
        return DefaultDisposable {
            disposable.dispose()
            reference.get()?.let {
                it.delegate = null
                it.dataSource = null
            }
            boundCollectionViews.remove(reference)
        }
    }
}

actual typealias DataSourceBindingResult = Disposable
