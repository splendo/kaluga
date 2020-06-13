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

package com.splendo.kaluga.example.ui.collectionview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelFragment
import com.splendo.kaluga.collectionview.datasource.*
import com.splendo.kaluga.collectionview.item.CollectionSection
import com.splendo.kaluga.collectionview.item.DefaultCollectionItemViewModel
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.collectionview.CollectionFooter
import com.splendo.kaluga.example.shared.viewmodel.collectionview.CollectionHeader
import com.splendo.kaluga.example.shared.viewmodel.collectionview.CollectionItem
import com.splendo.kaluga.example.shared.viewmodel.collectionview.CollectionViewViewModel
import kotlinx.android.synthetic.main.collection_view_fragment.*
import kotlinx.android.synthetic.main.list_collection_footer.view.*
import kotlinx.android.synthetic.main.list_collection_header.view.*
import kotlinx.android.synthetic.main.list_collection_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionViewFragment : KalugaViewModelFragment<CollectionViewViewModel>() {

    companion object {
        fun newInstance() = CollectionViewFragment()
    }

    override val viewModel: CollectionViewViewModel by viewModel()
    private val dataSource = lazy {
        CollectionDataSource<CollectionHeader, DefaultCollectionItemViewModel<CollectionItem>, CollectionFooter, CollectionSection<CollectionHeader, DefaultCollectionItemViewModel<CollectionItem>, CollectionFooter>, View, View, View>(
            viewModel.items,
            SimpleHeaderFooterCellBinder(setOf(0), {0}, {R.layout.list_collection_header}) { header, cell -> cell.headerLabel.text = header.title },
            SimpleItemCellBinder(setOf(1), {1}, {R.layout.list_collection_item}) { item, cell -> cell.titleLabel.text = item.item.title },
            SimpleHeaderFooterCellBinder(setOf(2), {2}, {R.layout.list_collection_footer}) { footer, cell -> cell.footerCount.text = getString(R.string.list_total).format(footer.numberOfElements) }
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.collection_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.layoutManager = GridLayoutManager(this.context, 1)
        
        this.activity?.let { activity ->
            activity.lifecycle.addObserver(dataSource.value.bindCollectionView(recyclerView))
        }
    }
}
