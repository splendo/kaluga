package com.splendo.kaluga.example.architecture

import android.content.Intent
import android.os.Bundle
import com.splendo.kaluga.architecture.navigation.toNavigationBundle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityArchitectureInputBinding
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.DetailsSpec
import com.splendo.kaluga.example.shared.viewmodel.architecture.DetailsSpecRow
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArchitectureInputActivity : KalugaViewModelActivity<ArchitectureInputViewModel>() {

    companion object {
        val requestCode = 2
    }

    override val viewModel: ArchitectureInputViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityArchitectureInputBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ArchitectureInputActivity.requestCode && resultCode == ArchitectureDetailsActivity.resultCode) {
            val spec = DetailsSpec()
            data?.extras?.toNavigationBundle(spec)?.let { bundle ->
                viewModel.nameInput.postValue(bundle.get(DetailsSpecRow.NameRow))
                viewModel.numberInput.postValue(bundle.get(DetailsSpecRow.NumberRow).toString())
            }
        }
    }

}