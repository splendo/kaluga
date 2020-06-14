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
import com.splendo.kaluga.collectionview.TableHeaderFooterCellView
import com.splendo.kaluga.collectionview.TableItemCellView
import com.splendo.kaluga.collectionview.TableView
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import com.splendo.kaluga.collectionview.item.CollectionSection
import com.splendo.kaluga.collectionview.item.itemAt
import com.splendo.kaluga.collectionview.item.sectionAt
import kotlin.native.ref.WeakReference
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSIndexPath
import platform.UIKit.UILayoutPriorityDefaultLow
import platform.UIKit.UILayoutPriorityRequired
import platform.UIKit.UITableView
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewDataSourceProtocol
import platform.UIKit.UITableViewDelegateProtocol
import platform.UIKit.UIView
import platform.UIKit.layoutIfNeeded
import platform.UIKit.setNeedsLayout
import platform.UIKit.systemLayoutSizeFittingSize
import platform.darwin.NSInteger
import platform.darwin.NSObject

actual interface TableHeaderFooterCellBinder<ItemType, V : TableHeaderFooterCellView> : CellBinder<ItemType, V> {
    fun sizeForItem(tableView: TableView, item: ItemType): CGFloat
    fun dequeueCell(tableView: TableView, item: ItemType): V
}

open class SimpleTableHeaderFooterCellBinder<ItemType, V : TableHeaderFooterCellView>(
    private val identifier: (ItemType) -> String,
    private val bind: (ItemType, V) -> Unit,
    private val onAppear: ((V) -> Unit)? = null,
    private val onDisappear: ((V) -> Unit)? = null
) : TableHeaderFooterCellBinder<ItemType, V> {

    override fun sizeForItem(tableView: TableView, item: ItemType): CGFloat {
        val cell = dequeueCell(tableView, item)
        bindCell(item, cell)
        cell.setNeedsLayout()
        cell.layoutIfNeeded()
        return cell.systemLayoutSizeFittingSize(CGSizeMake(tableView.bounds.useContents { size.width }, (Double.MAX_VALUE / 2.0) as CGFloat), UILayoutPriorityRequired, UILayoutPriorityDefaultLow).useContents { width }
    }

    override fun dequeueCell(tableView: TableView, item: ItemType): V {
        return tableView.dequeueReusableHeaderFooterViewWithIdentifier(identifier(item)) as V
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

actual interface TableItemCellBinder<ItemType, V : TableItemCellView> : CellBinder<ItemType, V> {
    fun sizeForItem(tableView: TableView, item: ItemType, at: NSIndexPath): CGFloat
    fun dequeueCell(tableView: TableView, item: ItemType, at: NSIndexPath): V
}

open class SimpleTableItemCellBinder<ItemType, V : TableItemCellView>(
    private val identifier: (ItemType) -> String,
    private val bind: (ItemType, V) -> Unit,
    private val onAppear: ((V) -> Unit)? = null,
    private val onDisappear: ((V) -> Unit)? = null
) : TableItemCellBinder<ItemType, V> {

    override fun sizeForItem(tableView: TableView, item: ItemType, at: NSIndexPath): CGFloat {
        val cell = dequeueCell(tableView, item, at)
        bindCell(item, cell)
        cell.setNeedsLayout()
        cell.layoutIfNeeded()
        return cell.systemLayoutSizeFittingSize(CGSizeMake(tableView.bounds.useContents { size.width }, (Double.MAX_VALUE / 2.0) as CGFloat), UILayoutPriorityRequired, UILayoutPriorityDefaultLow).useContents { width }
    }

    override fun dequeueCell(tableView: TableView, item: ItemType, at: NSIndexPath): V {
        return tableView.dequeueReusableCellWithIdentifier(identifier(item), at) as V
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

actual open class TableDataSource<
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : TableHeaderFooterCellView,
    ItemCell : TableItemCellView,
    FooterCell : TableHeaderFooterCellView>actual constructor(
        source: Observable<List<Section>>,
        headerBinder: TableHeaderFooterCellBinder<Header, HeaderCell>?,
        itemBinder: TableItemCellBinder<Item, ItemCell>,
        footerBinder: TableHeaderFooterCellBinder<Footer, FooterCell>?
    ) : DataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell, TableHeaderFooterCellBinder<Header, HeaderCell>, TableItemCellBinder<Item, ItemCell>, TableHeaderFooterCellBinder<Footer, FooterCell>>(source, headerBinder, itemBinder, footerBinder) {

    private val boundTableViews: MutableSet<WeakReference<TableView>> = mutableSetOf()

    @Suppress("CONFLICTING_OVERLOADS")
    private val collectionViewDelegate = object : NSObject(), UITableViewDelegateProtocol, UITableViewDataSourceProtocol {

        override fun tableView(tableView: UITableView, numberOfRowsInSection: NSInteger): NSInteger = (sections.getOrNull(numberOfRowsInSection.toInt())?.items?.size ?: 0).toLong() as NSInteger

        override fun numberOfSectionsInTableView(tableView: TableView): NSInteger = sections.size.toLong() as NSInteger

        override fun tableView(tableView: UITableView, estimatedHeightForHeaderInSection: NSInteger): CGFloat {
            val headerBinder = headerBinder ?: return 0.0 as CGFloat
            val header = sections.getOrNull(estimatedHeightForHeaderInSection.toInt())?.header ?: return 0.0 as CGFloat
            return headerBinder.sizeForItem(tableView, header)
        }

        override fun tableView(tableView: UITableView, estimatedHeightForFooterInSection: NSInteger): CGFloat {
            val footerBinder = footerBinder ?: return 0.0 as CGFloat
            val footer = sections.getOrNull(estimatedHeightForFooterInSection.toInt())?.footer ?: return 0.0 as CGFloat
            return footerBinder.sizeForItem(tableView, footer)
        }

        override fun tableView(tableView: UITableView, estimatedHeightForRowAtIndexPath: NSIndexPath): CGFloat {
            val item = sections.sectionAt(estimatedHeightForRowAtIndexPath).itemAt(estimatedHeightForRowAtIndexPath)
            return itemBinder.sizeForItem(tableView, item, estimatedHeightForRowAtIndexPath)
        }

        override fun tableView(tableView: UITableView, cellForRowAtIndexPath: NSIndexPath): UITableViewCell {
            val item = sections.itemAt(cellForRowAtIndexPath)
            return itemBinder.dequeueCell(tableView, item, cellForRowAtIndexPath).apply {
                itemBinder.bindCell(item, this)
            }
        }

        override fun tableView(tableView: UITableView, viewForHeaderInSection: NSInteger): UIView? {
            val section = sections[viewForHeaderInSection.toInt()]
            val header = section.header ?: return null
            return headerBinder?.dequeueCell(tableView, header)?.apply {
                headerBinder.bindCell(header, this)
            }
        }

        override fun tableView(tableView: UITableView, viewForFooterInSection: NSInteger): UIView? {
            val section = sections[viewForFooterInSection.toInt()]
            val footer = section.footer ?: return null
            return footerBinder?.dequeueCell(tableView, footer)?.apply {
                footerBinder.bindCell(footer, this)
            }
        }

        override fun tableView(tableView: UITableView, willDisplayCell: UITableViewCell, forRowAtIndexPath: NSIndexPath) {
            val item = sections.itemAt(forRowAtIndexPath)
            startDisplayingItem(item, willDisplayCell as ItemCell)
        }

        override fun tableView(tableView: UITableView, didEndDisplayingCell: UITableViewCell, forRowAtIndexPath: NSIndexPath) {
            val item = sections.itemAt(forRowAtIndexPath)
            stopDisplayingItem(item, didEndDisplayingCell as ItemCell)
        }

        override fun tableView(tableView: UITableView, willDisplayHeaderView: UIView, forSection: NSInteger) {
            val header = sections[forSection.toInt()].header ?: return
            startDisplayingHeader(header, willDisplayHeaderView as HeaderCell)
        }

        override fun tableView(tableView: UITableView, willDisplayFooterView: UIView, forSection: NSInteger) {
            val footer = sections[forSection.toInt()].footer ?: return
            startDisplayingFooter(footer, willDisplayFooterView as FooterCell)
        }

        override fun tableView(tableView: UITableView, didEndDisplayingHeaderView: UIView, forSection: NSInteger) {
            val header = sections[forSection.toInt()].header ?: return
            stopDisplayingHeader(header, didEndDisplayingHeaderView as HeaderCell)
        }

        override fun tableView(tableView: UITableView, didEndDisplayingFooterView: UIView, forSection: NSInteger) {
            val footer = sections[forSection.toInt()].footer ?: return
            stopDisplayingFooter(footer, didEndDisplayingFooterView as FooterCell)
        }

        override fun tableView(tableView: UITableView, didSelectRowAtIndexPath: NSIndexPath) {
            onClick(sections.itemAt(didSelectRowAtIndexPath), tableView, didSelectRowAtIndexPath)
        }
    }

    override fun notifyDataUpdated() {
        boundTableViews.forEach { it.get()?.reloadData() }
    }

    open fun onClick(item: Item, tableView: TableView, indexPath: NSIndexPath) {
        if (item is CollectionItemViewModel<*>) {
            item.onSelected()
        }
        tableView.deselectRowAtIndexPath(indexPath, true)
    }

    internal fun bindTo(tableView: TableView): DataSourceBindingResult {
        tableView.delegate = collectionViewDelegate
        tableView.dataSource = collectionViewDelegate
        val reference = WeakReference(tableView)
        boundTableViews.add(reference)
        val disposable = source.observe {
            sections = it
        }
        return DefaultDisposable {
            disposable.dispose()
            reference.get()?.let {
                it.delegate = null
                it.dataSource = null
            }
            boundTableViews.remove(reference)
        }
    }
}

actual fun <
    Header,
    Item,
    Footer,
    Section : CollectionSection<Header, Item, Footer>,
    HeaderCell : TableHeaderFooterCellView,
    ItemCell : TableItemCellView,
    FooterCell : TableHeaderFooterCellView> TableDataSource<Header, Item, Footer, Section, HeaderCell, ItemCell, FooterCell>.bindTableView(tableView: TableView): DataSourceBindingResult {
    return bindTo(tableView)
}
