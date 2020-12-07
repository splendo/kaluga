package com.splendo.kaluga.example.datetimepicker

import android.os.Bundle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityDateTimePickerBinding
import com.splendo.kaluga.example.shared.viewmodel.datetimepicker.DateTimePickerViewModel
import org.koin.android.ext.android.inject

class DateTimePickerActivity : KalugaViewModelActivity<DateTimePickerViewModel>() {

    override val viewModel: DateTimePickerViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDateTimePickerBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}