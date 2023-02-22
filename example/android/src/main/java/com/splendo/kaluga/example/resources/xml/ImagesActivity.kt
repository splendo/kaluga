/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.resources.xml

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityResourcesImagesBinding
import com.splendo.kaluga.example.databinding.ViewResourceListImageBinding
import com.splendo.kaluga.example.databinding.ViewResourceListTintedImageBinding
import com.splendo.kaluga.example.shared.viewmodel.resources.ImagesViewModel
import com.splendo.kaluga.resources.KalugaImage
import com.splendo.kaluga.resources.TintedImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagesActivity : KalugaViewModelActivity<ImagesViewModel>() {

    override val viewModel: ImagesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityResourcesImagesBinding.inflate(layoutInflater, null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.imagesList.adapter = ImageAdapter()
        binding.tintedImagesList.adapter = TintedImageAdapter()

        setContentView(binding.root)
    }
}

object ImagesBinding {
    @BindingAdapter("images")
    @JvmStatic
    fun bindImages(view: RecyclerView, images: List<KalugaImage>?) {
        val adapter = (view.adapter as? ImageAdapter) ?: return
        adapter.images = images.orEmpty()
    }
}

object TintedImagesBinding {

    @BindingAdapter("tintedImages")
    @JvmStatic
    fun bindImages(view: RecyclerView, tintedImages: List<TintedImage>?) {
        val adapter = (view.adapter as? TintedImageAdapter) ?: return
        adapter.tintedImages = tintedImages.orEmpty()
    }
}

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(val binding: ViewResourceListImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding.root
    }

    var images: List<KalugaImage> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ViewResourceListImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.image = images.getOrNull(position)
    }
}

class TintedImageAdapter : RecyclerView.Adapter<TintedImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(val binding: ViewResourceListTintedImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding.root
    }

    var tintedImages: List<TintedImage> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ViewResourceListTintedImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = tintedImages.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.image = tintedImages.getOrNull(position)
    }
}