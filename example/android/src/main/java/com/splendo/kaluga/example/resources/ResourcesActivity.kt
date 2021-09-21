package com.splendo.kaluga.example.resources

import android.os.Bundle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityResourcesBinding
import com.splendo.kaluga.example.shared.viewmodel.resources.ButtonViewModel
import com.splendo.kaluga.resources.view.applyButtonStyle
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResourcesActivity : KalugaViewModelActivity<ButtonViewModel>() {
    override val viewModel: ButtonViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityResourcesBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        binding.button.applyButtonStyle(viewModel.buttonStyle)
    }
}