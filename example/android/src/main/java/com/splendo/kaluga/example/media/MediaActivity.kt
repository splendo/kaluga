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

package com.splendo.kaluga.example.media

import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.observable.observeOnLifecycle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.databinding.ActivityMediaBinding
import com.splendo.kaluga.example.shared.viewmodel.media.MediaNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.media.MediaViewModel
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.media.ActivityMediaSurfaceProvider
import com.splendo.kaluga.media.MediaSource
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaActivity : KalugaViewModelActivity<MediaViewModel>() {

    override val viewModel: MediaViewModel by viewModel {
        parametersOf(
            ActivityMediaSurfaceProvider<MediaActivity> {
                findViewById<SurfaceView>(R.id.video_surface).holder
            },
            ActivityNavigator<MediaNavigationAction> { action ->
                when (action) {
                    is MediaNavigationAction.SelectLocal -> NavigationSpec.Contract<MediaActivity, Array<String>>(
                        arrayOf("audio/*", "video/*")
                    ) { contract }
                }
            }
        )
    }

    private val contract = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        viewModel.didSelectFileAt(uri?.let { MediaSource.Content(uri = it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMediaBinding.inflate(layoutInflater, null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.playtimeProgress.addOnSliderTouchListener(object : OnSliderTouchListener {

            override fun onStartTrackingTouch(slider: Slider) {
                //
            }

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.seekTo(slider.value.toDouble())
            }
        })

        viewModel.aspectRatio.observeOnLifecycle(this) {
            val set = ConstraintSet()
            set.clone(binding.contraintLayout)
            set.setDimensionRatio(R.id.video_surface, it)
            set.applyTo(binding.contraintLayout)
        }

        setContentView(binding.root)
    }
}
