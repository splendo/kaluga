package com.splendo.kaluga.example.alerts.xml

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityAlertsBinding
import com.splendo.kaluga.example.shared.viewmodel.alert.AlertViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("SetTextI18n")
class XMLAlertsActivity : KalugaViewModelActivity<AlertViewModel>() {

    override val viewModel: AlertViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAlertsBinding.inflate(LayoutInflater.from(this), null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}
