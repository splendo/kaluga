package com.splendo.kaluga.example.architecture

import android.os.Bundle
import com.splendo.kaluga.architecture.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityArchitectureInputBinding
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArchitectureInputActivity : KalugaViewModelActivity<ArchitectureInputViewModel>() {

    override val viewModel: ArchitectureInputViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityArchitectureInputBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

}