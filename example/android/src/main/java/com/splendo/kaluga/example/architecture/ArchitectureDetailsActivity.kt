package com.splendo.kaluga.example.architecture

import android.os.Bundle
import com.splendo.kaluga.architecture.navigation.toNavigationBundle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityArchitectureDetailsBinding
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.DetailsSpec
import com.splendo.kaluga.example.shared.viewmodel.architecture.DetailsSpecRow
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ArchitectureDetailsActivity : KalugaViewModelActivity<ArchitectureDetailsViewModel>() {

    companion object {
        val resultCode = 1
    }

    override val viewModel: ArchitectureDetailsViewModel by viewModel {
        val detailsSpec = DetailsSpec()
        intent.extras?.toNavigationBundle(detailsSpec)?.let { bundle ->
            parametersOf(bundle.get(DetailsSpecRow.NameRow), bundle.get(DetailsSpecRow.NumberRow))
        } ?: parametersOf("", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityArchitectureDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onBackPressed() {
        viewModel.onClosePressed()
    }

}