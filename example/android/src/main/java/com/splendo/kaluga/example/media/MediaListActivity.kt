/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityMediaListBinding
import com.splendo.kaluga.example.databinding.ViewListButtonBinding
import com.splendo.kaluga.example.shared.viewmodel.media.Media
import com.splendo.kaluga.example.shared.viewmodel.media.MediaListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.media.MediaListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaListActivity : KalugaViewModelActivity<MediaListViewModel>() {
    override val viewModel: MediaListViewModel by viewModel {
        parametersOf(
            ActivityNavigator<MediaListNavigationAction> { action ->
                when (action) {
                    is MediaListNavigationAction.Media -> NavigationSpec.Activity<MediaActivity>()
                    is MediaListNavigationAction.Sound -> NavigationSpec.Activity<MediaSoundActivity>()
                }
            },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMediaListBinding.inflate(layoutInflater, null, false)
        binding.mediaList.adapter = MediaAdapter(viewModel)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}

object MediaBinding {
    @BindingAdapter("media")
    @JvmStatic
    fun bindMedia(view: RecyclerView, media: List<Media>?) {
        val adapter = (view.adapter as? MediaAdapter) ?: return
        adapter.media = media.orEmpty()
    }
}

class MediaAdapter(private val viewModel: MediaListViewModel) :
    RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    class MediaViewHolder(val binding: ViewListButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val button = binding.button
    }

    var media: List<Media> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ViewListButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun getItemCount(): Int = media.size

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        media.getOrNull(position)?.let { media ->
            holder.button.text = media.title
            holder.button.setOnClickListener { viewModel.onMediaSelected(media) }
        } ?: run {
            holder.button.text = null
            holder.button.setOnClickListener(null)
        }
    }
}
