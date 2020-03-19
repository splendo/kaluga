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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.splendo.kaluga.example.R
import com.splendo.kaluga.collectionView.CollectionItemRepository
import com.splendo.kaluga.collectionView.CollectionViewItem

class CollectionViewFragment : Fragment() {

    companion object {
        fun newInstance() = CollectionViewFragment()
    }

    class Repo : CollectionItemRepository<CollectionViewItem>() {
        override suspend fun getItems() = listOf(
            CollectionViewItem("One"),
            CollectionViewItem("Two"),
            CollectionViewItem("3")
        )
    }

    class RepoFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CollectionViewViewModel(Repo()) as T
        }
    }

    private val viewModel: CollectionViewViewModel by viewModels { RepoFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.collection_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
